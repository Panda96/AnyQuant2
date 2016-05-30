package bl.statisticbl;

import blservice.statisticsblservice.SingleStatisticBLService;
import blservice.statisticsblservice.SingleViewService;
import blservice.statisticsblservice.TechnicalAnalysisStrategy;
import blservice.stockviewblservice.StockViewService;
import tool.constant.R;
import tool.constant.StockAttribute;
import tool.enums.LinearChartType;
import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import tool.utility.DateCount;
import tool.utility.LinearRegression;
import tool.utility.MyHashItem;
import tool.utility.MySort;
import ui.chartui.linearchart.MyChartSeries;
import vo.analyse.MACDResult;
import vo.analyse.single.BasicSingleVO;
import vo.chart.common.StockVO;
import vo.chart.linearchart.LinearChartVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JiachenWang on 2016/3/23.
 */
public class SingleViewImpl implements SingleViewService {

    private StockVO stock;
    private int lastIndex;

    private SingleStatisticBLService stasticbl;

    private StockViewService stockViewService;

    private TechnicalAnalysisStrategy strategy;

    public SingleViewImpl(SingleStatisticBLService stasticbl, StockViewService stockViewService) throws NotFoundException {
        this.stasticbl = stasticbl;
        this.stockViewService = stockViewService;
        strategy = new TechnicalAnalysis();
    }

    @Override
    public BasicSingleVO getBasicSingleInfo(String stock_name, String stock_num,
                                            PeriodEnum period, String startDate, String endDate)
            throws BadInputException, NotFoundException {
        stock = this.stockViewService.getStock(stock_num, startDate, endDate, R.field.all, new ArrayList<>());
        lastIndex = stock.getAttribute().size() - 1;

//        BasicSingleVO basicSingleVO = new BasicSingleVO(stock_name, stock_num,
//                this.getPredict(stasticbl.getAllVarianceOfPrice(startDate, endDate)));
        BasicSingleVO basicSingleVO = new BasicSingleVO();
        basicSingleVO.setStock_name(stock_name);
        basicSingleVO.setStock_num(stock_num);
        StockVO stock_macd = this.stockViewService.getStock(stock_num, DateCount.count(endDate, -150), endDate, R.field.all, new ArrayList<>());
        basicSingleVO.setConclusion(getPredict(stasticbl.getAllVarianceOfPrice(startDate,endDate), stock_macd));


        //计算各种指标

        //心理指数
        int dayUp = 0;
        int days = this.stock.getNumberOfDays();
        for (int j = 0; j < days; j++) {
            double change = this.stock.getChangeAtDay(j);
            if (change > 0)
                dayUp += 1;
        }
        basicSingleVO.setPsychologicalValue(dayUp + 0.0 / days * 100);


        List<StockAttribute> stockAttributeList = stock.getAttribute();
        double max = -1;
        double min = 1000;
        double dayOne = stock.getPriceAtDay(0);
        double yesterdayPrice = dayOne;

        double sumOfVar = 0;
        double sumOfTurnover = 0;
        double sumOfPe_ttm = 0;
        double sumOfPb = 0;
        for (StockAttribute stockAttribute:stockAttributeList){
            double close = Double.parseDouble(stockAttribute.getAttribute("close"));
            //涨跌幅 = 所有天数涨跌幅的平均值
            double var = (close - yesterdayPrice)/yesterdayPrice * 100;
            sumOfVar += var;
            yesterdayPrice = close;

            //股票平均换手率
            double turnover = Double.parseDouble(stockAttribute.getAttribute("turnover"));
            sumOfTurnover += turnover;

            //股票平均市盈率
            double pe_ttm = Double.parseDouble(stockAttribute.getAttribute("pe_ttm"));
            sumOfPe_ttm += pe_ttm;

            double pb = Double.parseDouble(stockAttribute.getAttribute("pb"));
            sumOfPb += pb;

            //统计最大最小值
            if(close > max)
                max = close;
            if(close < min)
                min = close;
        }
        //振幅:以本周期的最高价与最低价的差，除以上一周期的收盘价，再以百分数表示的数值。
        double rise = max-min;
        basicSingleVO.setVariableRange(rise/dayOne * 100);

        //涨跌幅 = 所有天数涨跌幅的平均值
        double varEven = sumOfVar/days;
        basicSingleVO.setRiseAndFall(varEven);


        basicSingleVO.setAvgTurnover(sumOfTurnover/days);

        basicSingleVO.setAvgPe(sumOfPe_ttm/days);

        basicSingleVO.setAvgPb(sumOfPb/days);

        return basicSingleVO;
    }

    @Override
    public LinearChartVO getStockRSI(String stock_num, String startDate, String endDate) throws BadInputException, NotFoundException {
        stock = stockViewService.getStock(stock_num, startDate, endDate, R.field.all, new ArrayList<>());

        HashMap<String, Double> EMA6 = strategy.calculateRSI(stock, 6);
        HashMap<String, Double> EMA12 = strategy.calculateRSI(stock, 12);
        HashMap<String, Double> EMA50 = strategy.calculateRSI(stock, 25);

        MyChartSeries series1 = new MyChartSeries("6日指标", EMA6);
        MyChartSeries series2 = new MyChartSeries("12日指标", EMA12);
        MyChartSeries series3 = new MyChartSeries("24日指标", EMA50);

        List<MyChartSeries> myChartSeries = new ArrayList<>();
        myChartSeries.add(series1);
        myChartSeries.add(series2);
        myChartSeries.add(series3);

        LinearChartVO linearChartVO =  new LinearChartVO(myChartSeries, LinearChartType.RSI);

        return linearChartVO;
    }

    @Override
    public LinearChartVO getStockEMA(String stock_num, String startDate, String endDate) throws BadInputException, NotFoundException {
        stock = stockViewService.getStock(stock_num, startDate, endDate, R.field.all, new ArrayList<>());

        HashMap<String, Double> EMA6 = strategy.calculateEMA(stock, 6);
        HashMap<String, Double> EMA12 = strategy.calculateEMA(stock, 12);
        HashMap<String, Double> EMA50 = strategy.calculateEMA(stock, 35);

        MyChartSeries ema6s = new MyChartSeries("6日指数", EMA6);
        MyChartSeries ema12s = new MyChartSeries("12日指数", EMA12);
        MyChartSeries ema50s = new MyChartSeries("35日指数", EMA50);

        List<MyChartSeries> myChartSeries = new ArrayList<>();
        myChartSeries.add(ema6s);
        myChartSeries.add(ema12s);
        myChartSeries.add(ema50s);

        return new LinearChartVO(myChartSeries, LinearChartType.EMA);
    }

    @Override
    public LinearChartVO getStockMACD(String stock_num, String startDate, String endDate) throws BadInputException, NotFoundException {
        stock = stockViewService.getStock(stock_num, startDate, endDate, R.field.all, new ArrayList<>());

        MACDResult macdResult = strategy.calculateMACD(stock);

        MyChartSeries series1 = new MyChartSeries("MACD指数", macdResult.getMacd());
        MyChartSeries series2 = new MyChartSeries("DIF指数", macdResult.getDif());

        //TODO 柱状图折线图合并
        MyChartSeries series3 = new MyChartSeries("柱状图属性", macdResult.getBarValue());

        List<MyChartSeries> myChartSeries = new ArrayList<>();
        myChartSeries.add(series1);
        myChartSeries.add(series2);

        return new LinearChartVO(myChartSeries, LinearChartType.MACD);

    }

    private String getPredict(String variance_three,StockVO stock_macd) throws BadInputException {
        List<StockAttribute> attributes = stock.getAttribute();
        String result = "通过对" + stock_macd.getName() + "历史数据的分析，可以得到一些结论。";

        result += this.getMACDInfo(stock_macd);

        double variance = stasticbl.getVarianceOfPrice(stock);
        String[] list = variance_three.split("-");
        double v1 = Double.parseDouble(list[0]);
        double v2 = Double.parseDouble(list[1]);
        double v3 = Double.parseDouble(list[2]);
        result += "\n" +  "        " + "结合半个月的数据，" ;
        if (variance <= v1)
            result += "从稳定程度看，该股票特别稳定，稳定程度超过系统中75%的股票。";
        else if (variance > v1 && variance <= v2)
            result += "从稳定程度看，该股票具有一定的稳定性，稳定程度超过系统中50%的股票。";
        else if (variance > v2 && variance <= v3)
            result += "从稳定程度看，该股票不是特别稳定，稳定程度仅仅超过系统中25%的股票。";
        else if (variance > v3)
            result += "从稳定程度看，该股票波动很大，具有一定的风险性。";

        return result;
    }

    private String getMACDInfo(StockVO stock) throws BadInputException {
        String result = "";
        MACDResult macdResult = strategy.calculateMACD(stock);
        List<MyHashItem> list = MySort.sortHashmapByKey(macdResult.getMacd());
        double[] arg1 = new double[list.size()];
        double[] arg2 = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arg1[i] = i + 1;
            arg2[2] = (double) list.get(i).getValue();
        }
        double gradient = LinearRegression.calculateLR_b(arg1, arg2, list.size());

        double dif_avg = 0;
        for (double value : macdResult.getDif().values())
            dif_avg += value;
        dif_avg = dif_avg / macdResult.getDif().size();

        double macd_avg = 0;
        for (double value : macdResult.getMacd().values())
            macd_avg += value;
        macd_avg = macd_avg / macdResult.getMacd().size();

        if (macd_avg * dif_avg <= 0)
            return "";

//        System.out.println("fff" + gradient);
        result += "\n" +  "        " + "结合近半年的数据，从MACD指数来看，";
        if (macd_avg > 0 && gradient > 0)
            result += "该股票行情行情处于多头行情中，可以买入开仓或多头持仓。";
        else if (macd_avg < 0 && gradient < 0)
            result += "该股票行情处于空头行情中，可以卖出开仓或观望。";
        else if (macd_avg > 0 && gradient < 0)
            result += "该股票行情处于下跌阶段，可以卖出开仓和观望。";
        else if (macd_avg < 0 && gradient > 0)
            result += "该股票行情即将上涨，可以买入开仓或多头持仓。";
        else  if (gradient == 0)
            result += "该股票行情走势稳定。";

        return result;
    }

}

