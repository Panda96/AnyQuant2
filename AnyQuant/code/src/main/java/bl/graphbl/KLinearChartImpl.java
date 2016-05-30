package bl.graphbl;

import bl.blfactory.BLFactory;
import blservice.graphblservice.KLinearChartBLService;
import blservice.stockviewblservice.StockViewService;
import tool.constant.R;
import tool.constant.StockAttribute;
import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import tool.utility.DateCount;
import tool.utility.TimeConvert;
import vo.chart.common.StockVO;
import vo.chart.kchart.KChartVO;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Seven on 16/3/21.
 */
public class KLinearChartImpl implements KLinearChartBLService {
    StockViewService stockViewService;
    String fields;
    Double open, close, high, low, average;
    int day=1,threeday=3,fiveday=5,week=5;
    public KLinearChartImpl() throws NotFoundException, BadInputException, IOException {
        stockViewService = BLFactory.getInstance().getStockViewService();
        fields = "open+close+high+low";
    }

    /**
     * 按照时间段获得K线图数据
     *
     * @param stockNum
     * @param period
     * @return
     */
    @Override
    public List<KChartVO> getKChartVO(String stockNum, PeriodEnum period, String startDate, String endDate) throws NotFoundException, BadInputException {
        //判断这个日期是周几,获得最近的周一的日期
        if(period.equals(PeriodEnum.WEEK)) {
            int day = DateCount.getWeekOfDate(startDate);
            startDate = DateCount.count(startDate, day - 1);
        }
        StockVO stock = stockViewService.getStock(stockNum, startDate, endDate, fields, new ArrayList<>());
        List<StockAttribute> attributes = stock.getAttribute();
        List<KChartVO> list = new ArrayList<>();

        if (period.equals(PeriodEnum.DAY)) {
            list=getDataByPeriod(attributes,period,day);
        }else if (period.equals(PeriodEnum.THREEDAY)) {
            list=getDataByPeriod(attributes,period,threeday);
        }else if (period.equals(PeriodEnum.FIVEDAY)){
            list=getDataByPeriod(attributes,period,fiveday);
        }else if (period.equals(PeriodEnum.WEEK)){
          list=getDataByPeriod(attributes,period,week);
        }else if(period.equals(PeriodEnum.MONTH)){
            startDate=startDate.substring(0,8)+"01";
            endDate=endDate.substring(0,8)+"01";
            Calendar start= null;
            Calendar end=null;
            Calendar temp=null;
            try {
                start = TimeConvert.covertToCalendar(startDate);
                end = TimeConvert.covertToCalendar(endDate);
                temp=TimeConvert.covertToCalendar(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            temp.add(Calendar.MONTH,1);

            while (!temp.equals(end)){
                stock=stockViewService.getStock(stockNum,TimeConvert.getDisplayDate(start),TimeConvert.getDisplayDate(temp),fields,new ArrayList<>());
                attributes=stock.getAttribute();
                KChartVO kChart;
                if(attributes.size()!=0) {
                    open = Double.parseDouble(attributes.get(0).getAttribute(R.field.open));
                    close = Double.parseDouble(attributes.get(attributes.size() - 1).getAttribute(R.field.close));
                    low = getLow(attributes);
                    high = getHigh(attributes);
                    average = (open + close) / 2;
                    kChart = new KChartVO(period, attributes.get(0).getDate(), open, close, low, high, average);
                }else{
                    kChart=new KChartVO(period,TimeConvert.getDisplayDate(start),0,0,0,0,0);
                }
                list.add(kChart);
                temp.add(Calendar.MONTH,1);
                start.add(Calendar.MONTH,1);
            }
        }
        return list;
    }

    @Override
    /**
     * 获得最低价
     */
    public double getLowerBound(String stockNum, PeriodEnum period, String startDate, String endDate) throws BadInputException, NotFoundException {
        StockVO stock = stockViewService.getStock(stockNum, startDate, endDate, fields, new ArrayList<>());
        List<StockAttribute> attributes = stock.getAttribute();
        low=getLow(attributes);
        return low;
    }

    @Override
    /**
     * 获得最高价
     */
    public double getUpperBound(String stockNum, PeriodEnum period, String startDate, String endDate) throws BadInputException, NotFoundException {
        StockVO stock = stockViewService.getStock(stockNum, startDate, endDate, fields, new ArrayList<>());
        List<StockAttribute> attributes = stock.getAttribute();
        high=getHigh(attributes);
        return high;
    }

    private List<KChartVO> getDataByPeriod(List<StockAttribute> attributes,PeriodEnum period,int d){
        List<KChartVO> list=new ArrayList<>();
        for(int i=0;i<=attributes.size()-d;i=i+d){
            open=Double.parseDouble(attributes.get(i).getAttribute(R.field.open));
            close=Double.parseDouble(attributes.get(i+d-1).getAttribute(R.field.close));
            low=getLow(attributes.subList(i,i+d));
            high=getHigh(attributes.subList(i,i+d));
            average=(open+close)/2;
            // 判断是否有停盘的情况
            if(open==0&&high==0){
                close=0.0;
                low=0.0;
            }
            KChartVO kChart=new KChartVO(period,attributes.get(i).getDate(),open,close,low,high,average);
            list.add(kChart);
        }
        return list;
    }

    /**
     * 获得一段时间的股票的最低价
     *
     * @param attributes
     * @return
     */
    private double getLow(List<StockAttribute> attributes) {
        double low = Double.parseDouble(attributes.get(0).getAttribute(R.field.low));
        for (int i = 1; i < attributes.size(); i++) {
            double compare = Double.parseDouble(attributes.get(i).getAttribute(R.field.low));
            if (compare < low) {
                low = compare;
            }
        }
        return low;
    }

    /**
     * 获得一段时间的股票的最高价
     *
     * @param attributes
     * @return
     */
    private double getHigh(List<StockAttribute> attributes) {
          double high=Double.parseDouble(attributes.get(0).getAttribute(R.field.high));
        for (int i=1;i<attributes.size();i++){
            double compare=Double.parseDouble(attributes.get(i).getAttribute(R.field.high));
            if(compare>high){
                high=compare;
            }
        }
        return  high;
    }

}
