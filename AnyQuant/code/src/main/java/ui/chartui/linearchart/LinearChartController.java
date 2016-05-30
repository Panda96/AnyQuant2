package ui.chartui.linearchart;

import blservice.statisticsblservice.IndustryViewService;
import blservice.statisticsblservice.SingleViewService;
import javafx.scene.chart.LineChart;

import bl.blfactory.BLFactory;
import blservice.graphblservice.LinearChartBLService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import tool.enums.IndustryPeriodEnum;
import tool.enums.LinearChartType;
import tool.exception.*;
import tool.utility.DateCount;
import ui.UIController;
import vo.chart.linearchart.*;

import java.io.IOException;

/**
 * Created by Seven on 16/3/16.
 *
 * Modified by Kylin on 16/3/12.
 *
 */
public class LinearChartController extends UIController{

    private LinearChartBLService linearChartBLService;

    private GetLinearChartService getLinearChartService;

    private IndustryViewService industryViewService;

    private SingleViewService singleViewService;


    public static Parent launch() throws NotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader(LinearChartController.class.getResource("linearChart.fxml"));
        return loader.load();
    }

    /**
     * 返回一只股票date交易日的价格折线图
     *
     * @param stockNumber 股票代码
     * @param date 日期
     *
     * @return LineChart
     */
    public LineChart<String,Number> getLineChart(String stockNumber,String date) throws NotFoundException, BadInputException, IOException {
        this.linearChartBLService = BLFactory.getInstance().getLinearChartBLService();
        LinearChartVO twoSeriesLinearChartVO = linearChartBLService.getLineChartVO(stockNumber, date);
        return this.getLineChartFromVO(twoSeriesLinearChartVO);
    }

    /**
     * 返回一只股票最近一个交易日的价格折线图
     *
     * @param stockNumber 股票代码
     * @return LineChart
     * @throws IOException
     * @throws NotFoundException
     */
    public LineChart<String,Number> getLineChart(String stockNumber) throws NotFoundException, BadInputException, IOException {
        return this.getLineChart(stockNumber,DateCount.getRecentWorkDay());
    }


    /**
     * 行业均价图
     *
     * @param industryName
     * @param period
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     * @throws IOException
     */
    public LineChart<String,Number> getIndustryLineChart(String industryName, IndustryPeriodEnum period) throws BadInputException, NotFoundException, IOException {
        this.industryViewService = BLFactory.getInstance().getIndustryViewService();
        LinearChartVO oneSeriesLinearChartVO = industryViewService.getIndustryPrice(industryName,period);
        return this.getLineChartFromVO(oneSeriesLinearChartVO);
    }

    /**
     * 行业涨幅对比图
     *
     * @param industryName
     * @param period
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     * @throws IOException
     */
    public LineChart<String,Number> getIndustryBenchmarkCompareLineChart(String industryName,IndustryPeriodEnum period) throws BadInputException, NotFoundException, IOException {
        this.industryViewService = BLFactory.getInstance().getIndustryViewService();
        LinearChartVO twoSeriesLinearChartVO = this.industryViewService.getCompareLinearChartVO(industryName,period);
        return this.getLineChartFromVO(twoSeriesLinearChartVO);
    }


    /**
     * MACD
     *
     * @param stock
     * @param start
     * @param end
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     * @throws IOException
     */
    public LineChart<String,Number> getMACDLineChart(String stock,String start, String end) throws BadInputException, NotFoundException, IOException {
        this.singleViewService = BLFactory.getInstance().getSingleViewService();
        LinearChartVO chartVO = this.singleViewService.getStockMACD(stock,start,end);
        return this.getLineChartFromVO(chartVO);
    }

    /**
     * EMA
     *
     * @param stock
     * @param start
     * @param end
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     * @throws IOException
     */
    public LineChart<String,Number> getEMALineChart(String stock,String start, String end) throws BadInputException, NotFoundException, IOException {
        this.singleViewService = BLFactory.getInstance().getSingleViewService();
        LinearChartVO chartVO = this.singleViewService.getStockEMA(stock,start,end);
        return this.getLineChartFromVO(chartVO);
    }

    /**
     * RSI
     *
     * @param stock
     * @param start
     * @param end
     * @return
     * @throws BadInputException
     * @throws NotFoundException
     * @throws IOException
     */
    public LineChart<String,Number> getRSILineChart(String stock,String start, String end) throws BadInputException, NotFoundException, IOException {
        this.singleViewService = BLFactory.getInstance().getSingleViewService();
        LinearChartVO chartVO = this.singleViewService.getStockRSI(stock,start,end);
        return this.getLineChartFromVO(chartVO);
    }

    /**
     * 从LinearChartVO获得折线图图表
     *
     * @param linearChartVO 折线图数据VO
     * @return 折线图
     */
    private LineChart<String, Number> getLineChartFromVO(LinearChartVO linearChartVO) throws BadInputException, NotFoundException {
        //判断折线图的类型
        LinearChartType chartType = linearChartVO.getChartType();
        getLinearChartService = new GetLinearChartImpl();
        linearChartVO.setStep(0.1);

        //一个行业的折线图,时间与价格对应
        if(chartType.equals(LinearChartType.INDUSTRY)){
            linearChartVO.setTitle("行业均价趋势图");
            linearChartVO.setyName("均价 (单位:元)");

        //一只股票的折线图,始建于即时价格与累计均价对应
        }else if(chartType.equals(LinearChartType.STOCK)){
            linearChartVO.setTitle("股价分时图");
            linearChartVO.setyName("股价 (单位:元)");

        //行业与大盘涨幅对比折线图
        }else if(chartType.equals(LinearChartType.INDUSTRY_COMPARE)){
            linearChartVO.setTitle("行业-大盘涨幅对比图");
            linearChartVO.setyName("涨幅 (单位:百分比)");

        }else if(chartType.equals(LinearChartType.MACD)){
            linearChartVO.setTitle("MACD:指数平滑异同平均线");
            linearChartVO.setyName("指标值");

        }else if(chartType.equals(LinearChartType.RSI)){
            linearChartVO.setTitle("RSI:相对强弱指标");
            linearChartVO.setyName("指标值");

        }else if(chartType.equals(LinearChartType.EMA)){
            linearChartVO.setTitle("EMA:指数平滑移动平均");
            linearChartVO.setyName("指标值");
        }

        LineChart<String, Number> lineChart = getLinearChartService.getStrAndNumLineChart(linearChartVO);

        lineChart.getStylesheets().
                add(LinearChartController.class.getResource("LinearChart.css").toExternalForm());

        return lineChart;
    }

}