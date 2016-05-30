package ui.chartui.klinear;

import bl.graphbl.KLinearChartImpl;
import blservice.graphblservice.KLinearChartBLService;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import vo.chart.kchart.KChartVO;
import java.io.IOException;
import java.util.List;

/**
 * Created by Seven on 16/3/16.
 */
public class KLinearController {

    KLinearChartBLService kLinearChartBLService;
    private List<KChartVO> data;
    private Timeline animation;
    XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();

    public CandleStickChart createKLinearChart(String stockNum,PeriodEnum period,String startDate,String endDate) throws NotFoundException, BadInputException, IOException {

        kLinearChartBLService=new KLinearChartImpl();
        data=kLinearChartBLService.getKChartVO(stockNum,period,startDate,endDate);

        double lower = kLinearChartBLService.getLowerBound(stockNum,period,startDate,endDate);
        double upper = kLinearChartBLService.getUpperBound(stockNum,period,startDate,endDate);
        double step = (upper-lower)/5;
        //判断是否有upper=lower=0的情况
        final NumberAxis yAxis;
        if(upper==0&lower==0){
            yAxis = new NumberAxis();
            yAxis.setLabel("股票价格");
        }else {
            yAxis = new NumberAxis("股票价格", lower*0.95, upper*1.05, step);
        }
        final CategoryAxis xAxis = new CategoryAxis();

        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);

        xAxis.setLabel("");

        for (int i = 0; i < data.size(); i++) {
            KChartVO day = data.get(i);
            series1.getData().add(
                    new XYChart.Data<String, Number>(day.getTime(),day.getOpen(),
                            new CandleStickExtraValues(day.getClose(), day.getHigh(), day.getLow(), day.getAverage())));
        }
        ObservableList<XYChart.Series<String, Number>> data = bc.getData();
        if (data == null) {
            data = FXCollections.observableArrayList(series1);
            bc.setData(data);
        } else {
            bc.getData().add(series1);
        }
        bc.getStylesheets().add(KLinearController.class.getResource("CandleChart.css").toExternalForm());
        bc.getStyleClass().add("KLinearChart");
        return bc;
    }
}
