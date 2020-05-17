package client.gui;

import java.io.IOException;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
//    	ClientUI.clientController.closeConnection();
    	ObjectContainer.loginStage.close();
    }

    @FXML
    void onLogin(ActionEvent event) {

    }

    public Pane getMainPane() {
    	return mainLoginPane;
    }
    
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new  FXMLLoader();
    	loader.setLocation(getClass().getResource("LoginForm.fxml"));
    	
    	Pane root = loader.load();
		ObjectContainer.loginController = loader.getController();
		Pane p = ObjectContainer.loginController.getMainPane();
		ObjectContainer.allowDrag(p, ObjectContainer.loginStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
