package bl.statisticbl;

import blservice.statisticsblservice.IndustryViewService;
import blservice.statisticsblservice.TechnicalAnalysisStrategy;
import blservice.stockviewblservice.StockViewService;
import tool.constant.R;
import tool.constant.StockAttribute;
import tool.enums.IndustryPeriodEnum;
import tool.enums.LinearChartType;
import tool.enums.PeriodEnum;
import tool.enums.TypeOfVolumn;
import tool.exception.*;
import tool.utility.DateCount;
import tool.utility.TimeConvert;
import ui.chartui.linearchart.MyChartSeries;
import vo.analyse.industry.IndustryBasicInfo;
import vo.analyse.industry.IndustryVO;
import vo.analyse.industry.TypicalStockInfo;
import vo.chart.barchart.VolumeChartVO;
import vo.chart.common.StockVO;
import vo.analyse.industry.IndustryVolumeVO;
import vo.chart.linearchart.LinearChartVO;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by kylin on 16/4/1.
 */
public class IndustryViewImpl implements IndustryViewService {

    private StockViewService stockViewService;

    private List<Industry> industryList;

    private DecimalFormat df = new DecimalFormat( "#.000");

    private TechnicalAnalysisStrategy strategy;

    public IndustryViewImpl(StockViewService stockViewService){
        this.strategy = new TechnicalAnalysis();
        this.stockViewService = stockViewService;
    }

    private void init(String startDate,String endDate) {
        //如果已经初始化过一次数据,则无需再次初始化

        HashMap<String,HashMap<String,String>> namesAndNumbers = this.stockViewService.getNamesAndNumbers();
        industryList = new ArrayList<>();
        Iterator<Map.Entry<String,HashMap<String,String>>> iter = namesAndNumbers.entrySet().iterator();
        //对于每一个行业
        while(iter.hasNext()){
            List<StockVO> stockVOsInIndustry = new ArrayList<>();
            Map.Entry entry = iter.next();
            //行业名称
            String industryName = (String) entry.getKey();
            HashMap<String,String> stocksInIndustry= (HashMap<String,String>)entry.getValue();
            //行业内部股票
            for (String stockName : stocksInIndustry.keySet()){
                String stockNumber = stocksInIndustry.get(stockName);
                //获取一只股票数据
                StockVO stockVO = null;
                try {
                    stockVO = this.stockViewService.getStock(stockNumber,startDate,endDate, R.field.all,null);

                } catch (NotFoundException | BadInputException e) {
                    e.printStackTrace();
                    stockVO = new StockVO();
                }
                stockVOsInIndustry.add(stockVO);
            }
            Industry industry = new Industry(industryName,stockVOsInIndustry);
            industryList.add(industry);
        }
    }

    @Override
    public IndustryVO getBasicIndustryInfo(String industryName, IndustryPeriodEnum period) throws NotFoundException {
        double averagePrice=0;
        double averageChange=0;
        double averagePb=0;
        double averageProfit=0;
        double  totalVolume=0;
        double totalAmount=0;
        int numberOfstocks=0;
        String startDate=IndustryPeriodEnum.getStartDate(period);
        this.init(startDate,getEndDate(startDate));

        for(int i=0;i<industryList.size();i++){
            if(industryList.get(i).getName().equals(industryName)){
                Industry industry=industryList.get(i);
                List<StockVO> stockVOs=industry.getStocks();
                String typicalName="";
                double typicalChange=0;
                double typicalPrice=0;
                //计算行业基本信息
                //获得行业内股票数目
                numberOfstocks = stockVOs.size();
                //获得所有股票的价格,市净率的平均值
                for (int j=0;j<numberOfstocks;j++){
                    double sumOfPrice=0;
                    double sumOfPb=0;
                    //获取每一支股票每一天的收盘价,市净率的总和
                    List<StockAttribute> attributes=stockVOs.get(j).getAttribute();
                    int day;
                    for (day=0;day<attributes.size();day++){
                        sumOfPrice=sumOfPrice+Double.valueOf(attributes.get(day).getAttribute(R.field.close));
                        String x = attributes.get(day).getAttribute(R.field.pb);
                        sumOfPb=sumOfPb+Double.valueOf(x);
                        //总成交量
                        totalVolume=totalVolume+Double.valueOf(attributes.get(day).getAttribute(R.field.volume));
                    }

                    //平均涨跌幅
                    double startClose=Double.valueOf(attributes.get(0).getAttribute().get(R.field.close));
                    double endClose=Double.valueOf(attributes.get(day-1).getAttribute().get(R.field.close));
                    averageChange=(averageChange*j+((endClose-startClose)/startClose)*100)/(j+1);
                    //平均价格
                    averagePrice=(averagePrice*j+sumOfPrice)/(j+1);
                    //平均市净率
                    averagePb=(averagePb*j+sumOfPb)/(j+1);
                    //平均每股收益
                    if(averagePb!=0) {
                        averageProfit = averagePrice / averagePb;
                    }else{
                        averageProfit=0;
                    }
                    //总成交金额
                    totalAmount=totalAmount+averagePrice*totalVolume;

                    //根据涨跌幅判断领头股
                    if (j==0){
                        typicalName = stockVOs.get(j).getName();
                        typicalChange = averageChange;
                        typicalPrice=Double.valueOf(attributes.get(day-1).getAttribute(R.field.close));
                    }else if (averageChange>=typicalChange) {
                        typicalName = stockVOs.get(j).getName();
                        typicalChange = averageChange;
                        typicalPrice=Double.valueOf(attributes.get(day-1).getAttribute(R.field.close));
                    }
                }

                //精确到小数点后三位
                averagePrice=Double.valueOf(df.format(averagePrice));
                averageChange=Double.valueOf(df.format(averageChange));
                averageProfit=Double.valueOf(df.format(averageProfit));
                totalVolume=Double.valueOf(df.format(totalVolume/10000));
                totalAmount=Double.valueOf(df.format(totalAmount/1000000));
                typicalPrice=Double.valueOf(df.format(typicalPrice));
                typicalChange=Double.valueOf(df.format(typicalChange));
                IndustryBasicInfo industryBasicInfo=new IndustryBasicInfo(numberOfstocks,averagePrice,averageChange,averageProfit,totalVolume,totalAmount);
                TypicalStockInfo typicalStockInfo=new TypicalStockInfo(typicalName,typicalPrice,typicalChange);
                IndustryVO industryVO=new IndustryVO(industryName,industryBasicInfo,typicalStockInfo);
                return industryVO;
            }
        }
        throw new NotFoundException("您输入的行业不存在!");
    }

    @Override
    public LinearChartVO getIndustryPrice(String industryName, IndustryPeriodEnum period) throws NotFoundException {
        String startDate=IndustryPeriodEnum.getStartDate(period);
        this.init(startDate,getEndDate(startDate));

        LinearChartVO linearChartVO = null;
        List<MyChartSeries> chartSerie = new ArrayList<>();

        for(int i=0;i<industryList.size();i++) {
            //get industry
            if (industryList.get(i).getName().equals(industryName)) {
                HashMap<String,Double> xyItem = new HashMap<>();
                Industry industry = industryList.get(i);
                List<StockVO> stockVOs = industry.getStocks();
                //以行业中的第一只股票所含天数作为标准
                StockVO stock=stockVOs.get(0);
                double upper=0;
                double lower=0;

                for (int day=0;day<stock.getAttribute().size();day++){
                    //每一天的行业价格=行业中所有股票当天收盘价的平均值
                    double sumOfClose=0;
                    for (int j=0;j<stockVOs.size();j++){
                        sumOfClose=sumOfClose+Double.valueOf(stockVOs.get(j).getAttribute().get(day).getAttribute(R.field.close));
                    }
                    double close=sumOfClose/stockVOs.size();
                    if(day==0){
                        upper=close;
                        lower=close;
                    }else {
                        if (upper < close) {
                            upper = close;
                        }
                        if (lower > close) {
                            lower = close;
                        }
                    }
                    xyItem.put(stock.getAttribute().get(day).getDate(),close);
                }

                MyChartSeries myChartSeries = new MyChartSeries("行业均价",xyItem);

                chartSerie.add(myChartSeries);
                linearChartVO=new LinearChartVO(LinearChartType.INDUSTRY,chartSerie,upper,lower);

            }

        }
        return linearChartVO;
    }

    @Override
    public LinearChartVO getCompareLinearChartVO(String industryName, IndustryPeriodEnum period) throws NotFoundException, BadInputException {
        String startDate=IndustryPeriodEnum.getStartDate(period);
        this.init(startDate,getEndDate(startDate));
        StockVO benchmark=this.stockViewService.getBenchmark("hs300",startDate,getEndDate(startDate),R.field.all,null);
        List<MyChartSeries> myChartSeries = new ArrayList<>();

        for(int i=0;i<industryList.size();i++) {
            if (industryList.get(i).getName().equals(industryName)) {

                HashMap<String,Double> xyItem1 = new HashMap<>();
                HashMap<String,Double> xyItem2 = new HashMap<>();

                Industry industry = industryList.get(i);
                List<StockVO> stockVOs = industry.getStocks();
                int minDay=benchmark.getAttribute().size();
                //以行业中的股票所含最少的天数作为标准
                for(StockVO stockVO:stockVOs){
                    if(stockVO.getAttribute().size()<minDay){
                        minDay=stockVO.getAttribute().size();
                    }
                }
                StockVO stock=stockVOs.get(0);
                double upper=0;
                double lower=0;
                for (int day=1;day<minDay;day++){
                    //行业每天的涨跌幅
                    double sumOfChange=0;
                    for (int j=0;j<stockVOs.size();j++){
                        double startClose=Double.valueOf(stockVOs.get(j).getAttribute().get(day-1).getAttribute().get(R.field.close));
                        double endClose=Double.valueOf(stockVOs.get(j).getAttribute().get(day).getAttribute().get(R.field.close));
                        sumOfChange=sumOfChange+(endClose-startClose)/startClose*100;
                    }
                    double industryChange=sumOfChange/stockVOs.size();
                    //大盘每天的涨跌幅
                    double benchmarkStart=Double.valueOf(benchmark.getAttribute().get(day-1).getAttribute().get(R.field.close));
                    double benchmarkEnd=Double.valueOf(benchmark.getAttribute().get(day).getAttribute().get(R.field.close));
                    double benchmarkChange=(benchmarkEnd-benchmarkStart)/benchmarkStart*100;

                    //判断上界和下界
                    double change;
                    if (benchmarkChange<industryChange){
                        change=industryChange;
                    }else{
                        change=benchmarkChange;
                    }
                    if(day==0){
                        upper=change;
                        lower=change;
                    }else{
                        if(upper<change)
                            upper=change;
                        if(lower>change)
                            lower=change;
                    }

                    //获得数据列
                    String dates = stock.getAttribute().get(day).getDate();
                    xyItem1.put(dates,industryChange);
                    xyItem2.put(dates,benchmarkChange);
                }

                MyChartSeries series1 = new MyChartSeries("行业",xyItem1);
                MyChartSeries series2 = new MyChartSeries("大盘",xyItem2);

                myChartSeries.add(series1);
                myChartSeries.add(series2);

                return new LinearChartVO(LinearChartType.INDUSTRY_COMPARE,myChartSeries,upper,lower);
            }
        }
        throw new NotFoundException("您输入的行业不存在!");
    }

    @Override
    public IndustryVolumeVO getIndustryVolume(String industryName, IndustryPeriodEnum period) throws NotFoundException {
        String startDate=IndustryPeriodEnum.getStartDate(period);
        this.init(startDate,getEndDate(startDate));
        ArrayList<VolumeChartVO> volumeChartVOs=new ArrayList<>();

        for(int i=0;i<industryList.size();i++) {
            if (industryList.get(i).getName().equals(industryName)) {
                Industry industry = industryList.get(i);
                List<StockVO> stockVOs = industry.getStocks();
                //以行业中的第一只股票所含天数作为标准
                StockVO stock=stockVOs.get(0);
                for (int day=0;day<stock.getAttribute().size();day++){
                    //每一天的行业成交量=行业中所有股票当天成交量的平均值
                    double sumOfVolume=0;
                    for (int j=0;j<stockVOs.size();j++){
                        sumOfVolume=sumOfVolume+Double.valueOf(stockVOs.get(j).getAttribute().get(day).getAttribute(R.field.volume))/10000;
                    }
                    sumOfVolume=sumOfVolume/stockVOs.size();
                    VolumeChartVO volumeChartVO=new VolumeChartVO(PeriodEnum.DAY,stock.getAttribute().get(day).getDate(),sumOfVolume);
                    System.out.println(stock.getAttribute().get(day).getDate()+"   "+sumOfVolume);
                    volumeChartVOs.add(volumeChartVO);
                }
                IndustryVolumeVO industryVolumeVO=new IndustryVolumeVO(volumeChartVOs, TypeOfVolumn.INDUSTRY);
                return industryVolumeVO;
            }
        }
        throw new NotFoundException("您输入的行业不存在!");
    }


    private String getEndDate(String startDate) {
        String endDate = "";
        try {
            Calendar end = TimeConvert.covertToCalendar(startDate);
            end.add(Calendar.MONTH,3);
            endDate = DateCount.count(TimeConvert.getDisplayDate(end),-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDate;
    }
}