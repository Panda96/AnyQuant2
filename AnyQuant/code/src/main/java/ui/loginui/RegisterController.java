package ui.loginui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ui.UIController;

/**
 * Created by kylin on 16/3/10.
 */
public class RegisterController extends UIController{

    @FXML
    public Button signUpButton;

    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    @FXML
    public PasswordField passwordAgain;

    @FXML
    public Label errorMessage;

    @FXML
    public Button cancelButton;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        errorMessage.setText("");
    }

    public void signUp(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
//        this.mainUI.gotoLogin();
    }
}
