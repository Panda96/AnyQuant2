package ui.navigationui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import ui.UIController;
import ui.UIMain;

import java.io.IOException;



/**
 * Created by JiachenWang on 16/3/9
 */
public class NavigationController extends UIController {
    @FXML  HBox tabs_HBox;
    @FXML  AnchorPane outerPane;
    @FXML  Button market;
    @FXML  Button collections;
    @FXML  Button list;
    @FXML  Button industry;

    public static Parent launch(UIMain mainui) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationController.class.getResource("rawnavigation.fxml"));
        AnchorPane pane = loader.load();
        NavigationController controller = loader.getController();
        controller.init();
        controller.outerPane = pane;
        controller.setMainUI(mainui);
        return pane;
    }

	public void init() {

	}


    public void jumpToBench() {
        this.mainUI.gotoBenchPane();
        market.requestFocus();
    }

    public void jumpToStockManage() {
        this.mainUI.gotoStockManage();
        collections.requestFocus();
    }

    public void jumpToAllStock() {
        this.mainUI.gotoAllStockView(false);
        list.requestFocus();
    }

    public void jumpToIndustryAnalysis(){
        this.mainUI.gotoIndustryAnalysis();
        industry.requestFocus();
    }

    public void hide(Event event) {
//        TranslateTransition translate = new TranslateTransition(Duration.millis(1000));
//        translate.setToX(0);
//        translate.setToY(0);
//
//        ParallelTransition transition = new ParallelTransition(tabs_HBox,translate);
//        transition.play();
    }

    public void show(Event event) {
//        TranslateTransition translate = new TranslateTransition(Duration.millis(1000));
//        translate.setToX(354);
//        translate.setToY(0);
//
//        ParallelTransition transition = new ParallelTransition(tabs_HBox,translate);
//        transition.play();
    }
}