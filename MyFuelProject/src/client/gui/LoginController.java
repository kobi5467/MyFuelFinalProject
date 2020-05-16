package client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class LoginController {

    @FXML
    private Pane mainLoginPane;

    @FXML
    private ImageView imgLoginBackground;

    @FXML
    private Pane loginPane;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblLoginTitle;

    @FXML
    private ImageView imgUser;

    @FXML
    private ImageView imgPass;

    @FXML
    private ImageView btnClose;

    @FXML
    void onCloseWindow(MouseEvent event) {
    	System.out.println("close !!");
    }

    @FXML
    void onLogin(ActionEvent event) {

    }

}
