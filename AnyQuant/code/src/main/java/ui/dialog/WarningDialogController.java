package ui.dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tool.constant.ResultMsg;
import ui.UIController;

import java.io.IOException;

public class WarningDialogController extends UIController {

    public Label message_Field;
    private String message;
    private ResultMsg yesOrNo;

    public Stage stage;

    public static WarningDialogController newDialog(String message, ResultMsg yesOrNo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WarningDialogController.class.getResource("warningDialog.fxml"));
        Pane pane = loader.load();

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
//		stage.initOwner(UIMain.primaryStage);
        stage.setScene(new Scene(pane));

        WarningDialogController controller = (WarningDialogController) loader.getController();

        controller.yesOrNo = yesOrNo;
        controller.message = message;
        controller.stage = stage;
        controller.initialize();

        return controller;

    }

    private void initialize() {
        message_Field.setText(this.message);
    }

    public void ok(ActionEvent actionEvent) {
        this.yesOrNo.setPass(true);
        stage.close();

    }

    public void cancel(ActionEvent actionEvent) {
        stage.close();
    }
}
