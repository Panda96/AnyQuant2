package ui.chartui.klinear;

/**
 * Created by Seven on 16/3/21.
 */
public class CandleStickExtraValues {
    /** Data extra values for storing close, high , low and average . */

    private double close;
    private double high;
    private double low;
    private double average;

    public CandleStickExtraValues(double close, double high, double low, double average) {
        this.close = close;
        this.high = high;
        this.low = low;
        this.average = average;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getAverage() {
        return average;
    }

    }

