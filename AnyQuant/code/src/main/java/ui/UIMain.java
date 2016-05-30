package ui;

import bl.blfactory.BLFactory;
import bl.stockviewbl.LoginImpl;
import blservice.stockviewblservice.LoginBLService;
import data.download.RefreshCacheThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import tool.constant.LocalInfo;
import tool.constant.ResultMsg;
import tool.constant.UserInfo;
import tool.exception.NotFoundException;
import ui.benchui.BenchController;
import ui.chartsmanageui.ChartsManageController;
import ui.dialog.WarningDialogController;
import ui.industryanalysisui.IndustryAnalysisController;
import ui.navigationui.NavigationController;
import ui.stockmanageui.StockManageController;
import ui.stockviewui.AllStockViewController;

/**
 * Created by JiachenWang on 2016/3/8.
 */
public class UIMain extends Application {

    private Stage stage;

    private UserInfo loggedUser;

    private LoginBLService loginBlService;

    private final double MINIMUM_WINDOW_WIDTH = 400.0;
    private final double MINIMUM_WINDOW_HEIGHT = 400.0;

    private Parent pane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        //刷新数据
        try {
            BLFactory.getInstance();
            RefreshCacheThread refreshCache = new RefreshCacheThread();
            refreshCache.start();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
            ResultMsg resultMsg = new ResultMsg(false,"");
            WarningDialogController dialog = null;
            try {
                dialog = WarningDialogController.newDialog("Network is unreachable",resultMsg);
                dialog.stage.showAndWait();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println("there is nothing i can do");
            }
        } finally {
            stage = primaryStage;
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setTitle("AnyQuant——您的量化交易专家");
            this.stage.getIcons().add(new Image("file:icon.png"));

            try {
                pane = NavigationController.launch(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            loginBlService = new LoginImpl();
            userLogin("test","test");

            gotoAllStockView(false);
            primaryStage.show();
        }

    }

    public UserInfo getLoggedUser() {
        return loggedUser;
    }

    public ResultMsg userLogin(String userId, String password) {
        ResultMsg result = this.loginBlService.logIn(userId, password);
        if (result.isPass()) {
            System.out.println("log in success");
            loggedUser = new UserInfo(userId, password);
            new LocalInfo(userId, password);
            gotoAllStockView(false);
        }
        return result;
    }

    public void gotoStockManage() {
        try {
            StockManageController controller = (StockManageController) replaceSceneContent("stockmanageui/stockManagement.fxml");
            controller.setMainUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoBenchPane() {
        try {
            BenchController controller = (BenchController) replaceSceneContent("benchui/benchcandleview.fxml");
            controller.setMainUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void gotoAllStockView(boolean isBenchmark) {
        try {
            AllStockViewController.setIsBenchmark(isBenchmark);
            AllStockViewController controller = (AllStockViewController) replaceSceneContent("stockviewui/allStockTableView.fxml");
            controller.setMainUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void gotoChartsManage(String stockNum){
        try {
            ChartsManageController controller = (ChartsManageController)replaceSceneContent("chartsmanageui/chartsManage.fxml");
            controller.setStock(stockNum);
            controller.setMainUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoIndustryAnalysis(){

        try {
            IndustryAnalysisController controller = (IndustryAnalysisController)replaceSceneContent("industryanalysisui/industryanalysis.fxml");
            controller.setMainUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private UIController replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = this.getFXMLLoader(fxml);
        Pane root = this.getParentRoot(loader,fxml);

        VBox vbox=new VBox();
        vbox.getChildren().add(pane);
        vbox.getChildren().add(root);

        stage.setScene(new Scene(vbox));
        stage.sizeToScene();

        return (UIController) loader.getController();
    }

    private FXMLLoader getFXMLLoader(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //Sets the builder factory used by this loader.
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        //Sets the location used to resolve relative path attribute values.
        loader.setLocation(getClass().getResource(fxml));
        return loader;
    }

    private Pane getParentRoot(FXMLLoader loader,String fxml) throws IOException {
        //read fxml file as inputStream
        InputStream in = getClass().getResourceAsStream(fxml);
        //the root elememt in fxml file
        //Loads an object hierarchy from a FXML document.
        Pane root = loader.load(in);
        return root;
    }


}
