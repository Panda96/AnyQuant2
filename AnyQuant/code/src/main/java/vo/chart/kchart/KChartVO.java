package vo.chart.kchart;

import tool.enums.PeriodEnum;
import vo.chart.common.ChartVO;

/**
 * Created by JiachenWang on 2016/3/16.
 */
public class KChartVO extends ChartVO {

    private double open;
    private double close;
    private double high;
    private double low;
    private double average;

    public KChartVO(PeriodEnum period, String time, double open, double close, double high, double low, double average) {
        super(period, time);
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.average = average;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getAverage() {
        return average;
    }

    public double getLow() {
        return low;
    }
}
