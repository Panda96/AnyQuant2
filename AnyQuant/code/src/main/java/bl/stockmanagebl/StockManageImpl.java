package bl.stockmanagebl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import blservice.stockmanageblservice.StockManageService;
import data.factory.DataFactoryTxtImpl;
import dataservice.logindataservice.UserDataService;
import dataservice.stockdataservice.CacheDataService;
import po.StockPO;
import tool.constant.ResultMsg;
import tool.constant.UserInfo;
import tool.exception.NotFoundException;
import vo.chart.common.StockVO;

/**
 * Created by kylin on 16/3/4.
 */
public class StockManageImpl implements StockManageService {
    private UserDataService userDataService;
    private CacheDataService CacheDataService;
    private DataFactoryTxtImpl dataFactory;
    private HashMap<String,String> allNum;
    private HashMap<String,StockPO> Cache;

    public StockManageImpl() throws IOException {
        dataFactory = DataFactoryTxtImpl.getInstance();
        userDataService = dataFactory.getUserDataService();
        CacheDataService = dataFactory.getCacheDataService();
        allNum = CacheDataService.getStockNumAndName();
        Cache = new HashMap<String,StockPO>();
    }

    public List<String> getUserCollections(UserInfo info) {
        List<String> collections = new ArrayList<String>();
        collections = userDataService.getUserCollections(info.getUserName(), info.getPassword());
        return collections;
    }


    /**
     * 个人收藏的股票信息，默认是一个月内所有属性的数据
     *
     * @param info
     * @return List<StockVO> 收藏股票VO列表
     */

    public List<StockVO> getUserStock(UserInfo info,String date) throws NotFoundException {
        List<StockVO> stocks = new ArrayList<StockVO>();
        //获得用户收藏股票编号列表
        List<String> numbers = getUserCollections(info);
        if (numbers != null) {

            String fields = null;
            try {
                fields = CacheDataService.getAllFields();
            } catch (IOException e) {
                throw new NotFoundException("数据获取异常,请重试");
            }
            for (int i = 0; i < numbers.size(); i++) {
            	//先在缓存中找某只股票的PO,没有再从数据层获得
                String number = numbers.get(i);
                StockPO po = Cache.get(number);
                //这里必须这样写
                if(po==null) {
                    try {
                        po = CacheDataService.getStock(number);
                    } catch (IOException e) {
                        throw new NotFoundException("数据获取异常,请重试");
                    }
                    Cache.put(number,po);

                }
                StockVO vo = StockVO.toStockVO(po,date,date,fields);

                String name = allNum.get(number);
                vo.setName(name);
                stocks.add(vo);
            }
        }
        return stocks;

    }

    

    public ResultMsg addUserCollection(UserInfo info, String stockNum) {
        try {
            return userDataService.addUserCollection(info.getUserName(), info.getPassword(), stockNum);
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new ResultMsg(false,"未知错误^..^");
        }
    }

    public ResultMsg deleteUserCollection(UserInfo info, String stockNum) {
        try {
            return userDataService.deleteCollcetion(info.getUserName(), info.getPassword(), stockNum);
        } catch (ClassNotFoundException e) {
            return new ResultMsg(false, "未知错误^..^");
        }

    }
}
