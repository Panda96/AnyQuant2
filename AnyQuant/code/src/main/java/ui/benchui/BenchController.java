package ui.benchui;

import bl.blfactory.BLFactory;
import blservice.stockviewblservice.StockViewService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import tool.enums.PeriodEnum;
import tool.exception.BadInputException;
import tool.exception.NotFoundException;
import ui.UIController;
import ui.chartui.klinear.CandleStickChart;
import ui.chartui.klinear.KLinearController;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Lin on 2016/4/12.
 */
public class BenchController extends UIController {
    @FXML
    GridPane benchPane;

    @FXML
    Button search;

    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;


    private String startDate,endDate;


    private StackPane candle;

    private String bench = "";

    private PeriodEnum periodState;


    public static Parent launch() throws IOException {
        FXMLLoader loader = new FXMLLoader(BenchController.class.getResource("benchcandleview.fxml"));
        Pane pane = loader.load();

        return pane;
    }

    @FXML
    public void initialize(){

        periodState = PeriodEnum.DAY;

        try {
            StockViewService stockViewService = BLFactory.getInstance().getStockViewService();
            bench = stockViewService.getBenchNamesAndNumbers().get("大盘").get("上证指数");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }




        candle = new StackPane();
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusMonths(2));
        setDates();

        showCandle();

    }

    public void showCandle() {

        candle.setVisible(false);
        candle = new StackPane();


        KLinearController kLinearController = new KLinearController();
        CandleStickChart candleChart = null;
        try {

            candleChart = kLinearController.createKLinearChart(bench, periodState, startDate, endDate);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        candle.getChildren().add(candleChart);
        benchPane.add(candle,0,1);
        benchPane.setFillHeight(candle,true);


    }

    public void setPeriodState(Event event) {
        String period = ((Button) event.getSource()).getText();
        periodState = PeriodEnum.getPeriodEnum(period);
        int n = periodState.getNum();

        startDatePicker.setValue(LocalDate.now().minusMonths(n));
        endDatePicker.setValue(LocalDate.now());
        setDates();

        showCandle();
    }

    public void setDates() {
        startDate = startDatePicker.getValue().toString();
        endDate = endDatePicker.getValue().toString();

    }

    public void search() {
        setDates();
        showCandle();
    }






}
