package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.chartui.linearchart.LinearChartController;

/**
 * Created by kylin on 16/3/20.
 */
public class TestLinearChart extends Application{

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        TestLinearChart.primaryStage = primaryStage;
        Scene singleStockView = new Scene(LinearChartController.launch());

        primaryStage.setScene(singleStockView);
        primaryStage.show();
    }
}
