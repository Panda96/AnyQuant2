package ui.stockviewui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Seven on 16/3/10.
 */
public class TestSingleView extends Application{
    /**
     * The main entry point for all JavaFX applications.
     * The startPrice method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root=SingleStockViewController.launch("sh600050","2016-03-06","2016-04-06");
//        Parent root=AllStockViewController.launch();
        Scene scene = new Scene(root,968,700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
