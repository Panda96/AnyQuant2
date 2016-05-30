package bl.statisticbl;

import blservice.statisticsblservice.SingleStatisticBLService;
import blservice.statisticsblservice.TechnicalAnalysisStrategy;
import blservice.stockviewblservice.StockViewService;
import tool.constant.R;
import tool.constant.StockAttribute;
import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import tool.structure.Node;
import tool.utility.CorrelationCoefficient;
import tool.utility.TimeConvert;
import vo.analyse.MACDResult;
import vo.chart.common.StockVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JiachenWang on 2016/4/10.
 */
public class SingleStatisticBLImpl implements SingleStatisticBLService {

    private TechnicalAnalysisStrategy strategy;
    private StockViewService stockViewService;

    public SingleStatisticBLImpl(StockViewService stockViewService) {
        this.strategy = new TechnicalAnalysis();
        this.stockViewService = stockViewService;
    }

    @Override
    public double getVarianceOfPrice(StockVO stock) {
        double avg_price = this.getAvgPrice(stock);
        if(avg_price==-1)
            return -1;
        double variance = 0;
        for(StockAttribute tmp : stock.getAttribute()){
            double day_avg = Double.parseDouble(tmp.getAttribute(R.field.open)) + Double.parseDouble(tmp.getAttribute(R.field.close));
            day_avg = day_avg / 2;
            variance += Math.pow((day_avg - avg_price), 2);
        }
        variance = variance / stock.getAttribute().size();
        return variance;
    }

    @Override
    public double getAvgPrice(StockVO stock) {
        if(stock.getAttribute().size()==0)
            return -1;
        double total = 0;
        for(StockAttribute tmp : stock.getAttribute()){
            double day_avg = Double.parseDouble(tmp.getAttribute(R.field.open)) + Double.parseDouble(tmp.getAttribute(R.field.close));
            day_avg = day_avg / 2;
            total += day_avg;
        }

        return total / stock.getAttribute().size();
    }

    /**
     * 求某时间段所有股票日价格方差集合的1/4,1/2,3/4分位点
     * @return double + "-" + double + "-" + double
     */
    @Override
    public String getAllVarianceOfPrice(String startDate, String endDate) throws NotFoundException {
        String field = "open+close";

        HashMap<String,HashMap<String,String>> map;
        map = stockViewService.getNamesAndNumbers();

        double value = -1;
        Node<Double> head = new Node<Double>(value);
        Node<Double> point = head;

        //遍历获得链表
        for (String industry_name : map.keySet()) {

            HashMap<String,String> list = map.get(industry_name);
            for (String stock_name : list.keySet()) {
                String stock_num = list.get(stock_name);
                try {
                    StockVO stock = stockViewService.getStock(stock_num, startDate, endDate, field, new ArrayList<>());
                    value = this.getVarianceOfPrice(stock);
                    Node<Double> node = new Node<Double>(value);
                    point.link = node;
                    point = node;
                } catch (BadInputException e) {
                    throw new NotFoundException("传入参数有误");
                }
            }

        }

        List<Double> list = this.orderLinkedList(head);
        int count = list.size();
        double[] result = new double[3];//1 2 3 4 5 6 7
        result[1] = this.geiMiddle(list);
        result[0] = geiMiddle(getList(list, 0, count/2-1));
        result[2] = geiMiddle(getList(list, count/2, count-1));
        result[0] = geiMiddle(getList(list, 0, count/2-1));
        result[2] = geiMiddle(getList(list, count/2, count-1));

        return result[0] + "-" + result[1] + "-" + result[2];
    }

    /**
     * 计算近90天两只股票的相关系数
     *
     * @param stockname1
     * @param stockname2
     * @return
     * @throws NotFoundException
     * @throws BadInputException
     */
    @Override
    public String cal90CC(String stockname1, String stockname2) throws NotFoundException, BadInputException, IOException {

        //获得起止时间
        Calendar endDate=Calendar.getInstance();
        String end= TimeConvert.getDisplayDate(endDate);
        endDate.add(Calendar.MONTH,-3);
        String start=TimeConvert.getDisplayDate(endDate);

        StockVO stockVO1 = stockViewService.getStock(stockname1,start,end,R.field.close,new ArrayList<>());
        StockVO stockVO2=stockViewService.getStock(stockname2,start,end,R.field.close,new ArrayList<>());

        //存放两只股票每天的涨跌幅
        double[] profit1=new double[stockVO1.getAttribute().size()];
        double[] profit2=new double[stockVO1.getAttribute().size()];
        for(int i=1;i<stockVO1.getAttribute().size();i++){
            if(stockVO2.getAttribute().get(i)!=null){
                //计算第一只股票每天的涨跌幅
                double close11=Double.valueOf(stockVO1.getAttribute().get(i-1).getAttribute(R.field.close));
                double close12=Double.valueOf(stockVO1.getAttribute().get(i).getAttribute(R.field.close));
                profit1[i]=(close12-close11)/close11*100;

                //计算第二只股票每天的涨跌幅
                double close21=Double.valueOf(stockVO2.getAttribute().get(i-1).getAttribute(R.field.close));
                double close22=Double.valueOf(stockVO2.getAttribute().get(i).getAttribute(R.field.close));
                profit2[i]=(close21-close22)/close21*100;

            }
        }
        double p= CorrelationCoefficient.calculateCC(profit1,profit2);
        BigDecimal b=new BigDecimal(p);
        double f=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f);
        return String.valueOf(f);
    }

    @Override
    public MACDResult getMACDValue(String stock_name, String stock_num, PeriodEnum period, String startDate, String endDate) throws NotFoundException, BadInputException, IOException {
        StockVO stock = stockViewService.getStock(stock_num, startDate, endDate, R.field.all, new ArrayList<>());
        return this.strategy.calculateMACD(stock);
    }

    /**
     * 排序链表(冒泡排序)
     * @param head 头节点
     * @return 排序好的数组
     */
    private List<Double> orderLinkedList(Node<Double> head){
        List<Double> list = new ArrayList<Double>();
        Node<Double> point = head.link;
        while(point!=null){
            list.add(point.getValue());
            point = point.link;
        }
        for(int i=list.size()-1;i>=0;i--){
            for(int j=0;j<i;j++){
                if(list.get(j)>list.get(j+1)){
                    Double temp =list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                }
            }
        }

        return  list;
    }

    /**
     * 求中位数
     * @param list 数组
     * @return 中位数值
     */
    private double geiMiddle(List<Double> list){
        int count = list.size();
        if(count%2==1){
            return list.get(count/2).doubleValue();
        } else {
            return (list.get(count/2-1).doubleValue() + list.get(count/2).doubleValue()) / 2;
        }
    }

    /**
     * 求子数组
     * @param list 父数组
     * @param start 开始位置（包含）
     * @param end 结束位置（包含）
     * @return 子数组
     */
    private List<Double> getList(List<Double> list, int start, int end){
        List<Double> result = new ArrayList<Double>();
        while(start<=end){
            result.add(list.get(start));
            start++;
        }
        return result;
    }

}
