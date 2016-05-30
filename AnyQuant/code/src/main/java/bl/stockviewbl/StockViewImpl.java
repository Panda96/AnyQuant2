package bl.stockviewbl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import blservice.formatcheck.FormatCheck;
import blservice.stockviewblservice.StockViewService;
import data.factory.DataFactoryTxtImpl;
import dataservice.stockdataservice.CacheDataService;
import po.StockPO;
import tool.constant.ConditionSelect;
import tool.constant.StockAttribute;
import tool.exception.*;
import vo.chart.common.StockVO;

/**
 * Created by kylin on 16/3/4.
 */
public class StockViewImpl implements StockViewService {

    private CacheDataService CacheDataService;
    private DataFactoryTxtImpl dataFactory;
    private HashMap<String, StockPO> Cache;
    private List<HashMap<String, String>> numAndName;
    private List<String> exchangeNames;
    private HashMap<String, String> allNum;

    public StockViewImpl() throws NotFoundException {
        dataFactory = DataFactoryTxtImpl.getInstance();
        CacheDataService = dataFactory.getCacheDataService();
        Cache = new HashMap<String, StockPO>();
        numAndName = new ArrayList<HashMap<String, String>>();
        exchangeNames = new ArrayList<String>();
        allNum = new HashMap<String, String>();
        initial();
    }

    public void initial() throws NotFoundException {
        try {
            allNum = CacheDataService.getStockNumAndName();
        } catch (IOException e) {
            throw new NotFoundException("数据获取异常,请重试");
        }

        Iterator<Entry<String, String>> iter = allNum.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String number = entry.getKey();
            String exchangeName = number.substring(0, 2);
            int i = exchangeNames.indexOf(exchangeName);
            if (i < 0) {
                exchangeNames.add(exchangeName);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put(number, entry.getValue());
                numAndName.add(temp);
            } else {
                numAndName.get(i).put(number, entry.getValue());
            }
        }

    }

    public List<StockVO> getAllStock(String year, String exchange, String date) throws NotFoundException, BadInputException {

        List<StockVO> stocks = new ArrayList<StockVO>();
        // 编号
        String fields = null;
        try {
            fields = CacheDataService.getAllFields();
        } catch (IOException e) {
            throw new NotFoundException("数据获取异常,请重试");
        }
        int i = exchangeNames.indexOf(exchange);

        // Map迭代器
        Iterator<Entry<String, String>> iter = numAndName.get(i).entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            // 获得单只股票某一天的数据
            stocks.add(getStock(entry.getKey(), date, date, fields, new ArrayList<>()));
        }

        return stocks;

    }

    public StockVO getStock(String number, String start, String end, String fields, List<ConditionSelect> ranges) throws NotFoundException, BadInputException {
        FormatCheck.isDateBefore(start, end);
        // 先在缓存中找某只股票的PO,没有再从数据层获得
        try {
            StockPO po = Cache.get(number);
            if (po == null) {
                if(number.equals("hs300")){
                    po = CacheDataService.getBenchmark(number);
                    fields = CacheDataService.getBenchFields();
                }else{
                    po = CacheDataService.getStock(number);
                }

                Cache.put(number, po);
            }
            StockVO vo = StockVO.toStockVO(po, start, end, fields);
            String name = allNum.get(number);
            vo.setName(name);

            vo = filter(vo, ranges);
            return vo;
        } catch (IOException e) {
            throw new NotFoundException("数据获取异常，请重试！");
        }

    }

    /**
     * vo筛选器
     *
     * @param vo
     * @param ranges
     * @return
     */
    private StockVO filter(StockVO vo, List<ConditionSelect> ranges) throws BadInputException, NotFoundException {
        //若筛选条件为空直接返回vo

        if (ranges!=null&&!ranges.isEmpty()) {
            List<StockAttribute> atts = new ArrayList<StockAttribute>();
            List<StockAttribute> rawAtts = vo.getAttribute();

            //遍历每一天，选择有效的几天
            for (StockAttribute sAtt : rawAtts) {
                if (isValid(sAtt, ranges)) {
                    atts.add(sAtt);
                }
            }
            if(atts==null||atts.isEmpty()){
                throw new NotFoundException("亲，您选的数据域无数据^_^");
            }else{
                vo.setAttribute(atts);
            }

        }
        return vo;

    }

    /**
     * 判断某一天的数据是否满足条件
     *
     * @param att
     * @param ranges
     * @return
     */
    private boolean isValid(StockAttribute att, List<ConditionSelect> ranges) throws BadInputException, NotFoundException {

        for (ConditionSelect con : ranges) {
            FormatCheck.isNumber(con.getFromNum());
            Double bottom = new Double(con.getFromNum());

            FormatCheck.isNumber(con.getToNum());
            Double top = new Double(con.getToNum());

            if(top<bottom){
                throw new BadInputException("亲，请从小到大输入^_^");
            }

            String field = con.getField();

            String data = att.getAttribute(field);

            if(data==null){
                throw new NotFoundException("亲，该筛选条件无数据^_^");
            }else{
                FormatCheck.isNumber(att.getAttribute(field));
                Double bd = new Double(att.getAttribute(field));

                if (bd.compareTo(top) > 0 || bd.compareTo(bottom) < 0) {
                    return false;
                }
            }

        }
        return true;

    }

    public List<StockVO> getAllBenchmark(String date) throws NotFoundException, BadInputException {
        List<StockVO> vos = new ArrayList<StockVO>();
        String benchFields = null;
        try {
            benchFields = CacheDataService.getBenchFields();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> names = new ArrayList<String>();
        try {
            names = CacheDataService.getAllBenchmark();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(names.get(0));
        for (int i = 0; i < names.size(); i++) {
            StockVO vo = getBenchmark(names.get(i), date, date, benchFields, new ArrayList<>());
            vos.add(vo);
        }

        return vos;

    }

    public StockVO getBenchmark(String bench, String start, String end, String benchFields, List<ConditionSelect> ranges) throws NotFoundException,BadInputException {
        StockPO po = null;
        StockVO vo = null;
        FormatCheck.isDateBefore(start, end);
        po = Cache.get(bench);
        if (po == null) {

            try {
                po = CacheDataService.getBenchmark(bench);
            } catch (IOException e) {
                e.printStackTrace();
                throw new NotFoundException("数据获取异常，请重试!");
            }

            Cache.put(bench, po);
        }
//		System.out.println(Cache.size());
//		System.out.println(Cache.get("hs300").getNumber());
        vo = StockVO.toStockVO(po, start, end, benchFields);
        vo.setName(bench);
        vo = filter(vo, ranges);

        return vo;
    }

    public List<String> getAllFields() {
        String fieldstr = null;
        try {
            fieldstr = CacheDataService.getAllFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] fields = fieldstr.split("\\+");
        return toList(fields);

    }

    @Override
    public List<String> getBenchFields() {
        String benchfields = null;
        try {
            benchfields = CacheDataService.getBenchFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] fields = benchfields.split("\\+");
        return toList(fields);
    }

    public List<String> toList(String[] fields) {
        List<String> allFields = new ArrayList<String>();

        for (int i = 0; i < fields.length; i++) {
            allFields.add(fields[i]);
        }
        return allFields;
    }

    @Override
    public List<String> getAllBenches() {
        List<String> benches = new ArrayList<>();

        try {
            benches = CacheDataService.getAllBenchmark();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return benches;
    }
}
