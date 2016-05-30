package ui.statisticsui;

import javafx.scene.chart.LineChart;
import tool.enums.IndustryPeriodEnum;
import tool.exception.*;
import ui.chartui.linearchart.LinearChartController;

import java.io.IOException;

/**
 * Created by kylin on 16/4/4.
 *
 * 显示单个行业价格折线图 与 行业大盘涨幅对比折线图
 */
public class IndustryLinearCharts {

    private LinearChartController linearChartController;

    public IndustryLinearCharts() {
        linearChartController = new LinearChartController();
    }

    //单个行业价格折线图
    public LineChart<String, Number> getIndustryLineChart(String industryName, IndustryPeriodEnum period) throws BadInputException, NotFoundException, IOException {
        return this.linearChartController.getIndustryLineChart(industryName,period);
    }

    //行业大盘涨幅对比折线图
    public LineChart<String, Number> getIndustryBenchmarkCompareLineChart(String industryName, IndustryPeriodEnum period) throws BadInputException, NotFoundException, IOException {
        return this.linearChartController.getIndustryBenchmarkCompareLineChart(industryName,period);
    }
}
