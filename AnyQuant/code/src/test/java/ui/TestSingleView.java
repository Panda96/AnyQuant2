package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.stockviewui.SingleStockViewController;

public class TestSingleView extends Application{

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
//        TestSingleView.primaryStage = primaryStage;
//        Scene singleStockView = new Scene(SingleStockViewController.launch("sh600050","2016-03-06","2016-04-06"));
//
//        primaryStage.setScene(singleStockView);
//        primaryStage.show();
	}

}
