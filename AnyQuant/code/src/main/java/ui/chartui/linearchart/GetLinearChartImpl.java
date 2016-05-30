package ui.chartui.linearchart;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tool.enums.LinearChartType;
import vo.chart.linearchart.LinearChartVO;

import java.util.*;

/**
 * Created by kylin on 16/4/12.
 */
public class GetLinearChartImpl implements GetLinearChartService {

    @Override
    public LineChart<String, Number> getStrAndNumLineChart(LinearChartVO chartVO) {

        //获取图表,设置图表属性
        LineChart<String, Number> lineChart;
        if (chartVO.getChartType().equals(LinearChartType.STOCK)
                || chartVO.getChartType().equals(LinearChartType.INDUSTRY)
                || chartVO.getChartType().equals(LinearChartType.INDUSTRY_COMPARE)) {
            lineChart = this.createChart(false,
                    chartVO.getTitle(), chartVO.getxName(), chartVO.getyName(),
                    chartVO.getLowerBound(), chartVO.getUpperBound(),
                    chartVO.getStep());
        } else {
            lineChart = this.createChart(true,
                    chartVO.getTitle(), chartVO.getxName(), chartVO.getyName(),
                    chartVO.getLowerBound(), chartVO.getUpperBound(),
                    chartVO.getStep());
        }

        //获取数据列
        List<MyChartSeries> chartSeries = chartVO.getChartSeries();

        //对于数据包中的每一个数据列
        for (MyChartSeries myChartSeries : chartSeries) {
            HashMap<String, Double> xyItems = myChartSeries.getXyItem();

            ArrayList<String> xList = new ArrayList<>();
            ArrayList<Double> yList = new ArrayList<>();

            //将map.entrySet()转换成list
            List<Map.Entry<String, Double>> list = new ArrayList<>(xyItems.entrySet());
            Collections.sort(list, (Map.Entry<String, Double> o1, Map.Entry<String, Double> o2)
                    -> o1.getKey().compareTo(o2.getKey()));

            //添加每一个数据
            for (Map.Entry<String, Double> mapping : list) {

                if (chartVO.getChartType().equals(LinearChartType.STOCK))
                    xList.add(mapping.getKey());
                else
                    xList.add(mapping.getKey().substring(5));

                yList.add(mapping.getValue());
            }

            //创建数据列并添加数据设置名称
            XYChart.Series<String, Number> xyChartSeries = this.getStrNumLineSeries(xList, yList);
            xyChartSeries.setName(myChartSeries.getName());
            lineChart.getData().add(xyChartSeries);
        }

        return lineChart;
    }

    /**
     * 从两列数据中获取一个图表Series
     *
     * @param strings X轴数据列
     * @param numbers Y周数据列
     * @return 图表Series
     */
    private XYChart.Series<String, Number> getStrNumLineSeries(List<String> strings, List<Double> numbers) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        assert strings.size() == numbers.size();

        for (int i = 0; i < strings.size(); i++) {
            series.getData().add(new XYChart.Data<>(strings.get(i), numbers.get(i)));
        }
        return series;
    }

    /**
     * 根据XY轴的名称Y轴的上下限创建图表
     *
     * @param xName X轴名称
     * @param yName Y轴名称
     * @param ylow  Y轴的下限
     * @param yhigh Y轴的上限
     * @return 折线图
     */
    private LineChart<String, Number> createChart(boolean tickLabelsVisible, String title, String xName, String yName,
                                                  double ylow, double yhigh, double step) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(xName);
        xAxis.setTickLabelsVisible(tickLabelsVisible);

        NumberAxis yAxis = new NumberAxis(yName, ylow, yhigh, step);

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(title);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(true);

        return lineChart;
    }

}
