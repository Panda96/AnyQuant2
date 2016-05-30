package ui.chartui.linearchart;

import javafx.scene.chart.XYChart;

import java.util.HashMap;

/**
 * Created by kylin on 16/4/12.
 */
public class MyChartSeries {

    private String name;

    private HashMap<String,Double> xyItem;

    public MyChartSeries(String name, HashMap<String, Double> xyItem) {
        this.name = name;
        this.xyItem = xyItem;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Double> getXyItem() {
        return xyItem;
    }

    XYChart.Series<String, Number> getStrNumLineSeries(){
        return null;
    }
}
