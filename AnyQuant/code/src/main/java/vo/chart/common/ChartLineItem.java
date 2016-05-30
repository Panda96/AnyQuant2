package vo.chart.common;

/**
 * 一个二维图表的一个数据点
 */
public class ChartLineItem {

    /**
     * X轴值
     */
    String xValue;

    /**
     * Y轴数值
     */
    Double yValue;

    public ChartLineItem(String xValue, Double yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public String getxValue() {
        return xValue;
    }

    public Double getyValue() {
        return yValue;
    }
}
