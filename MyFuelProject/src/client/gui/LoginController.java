package client.gui;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.User;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblLoginTitle;

    @FXML
    private ImageView imgUser;

    @FXML
    private ImageView imgPass;

    @FXML
    private Label lblErrorMessage;
    
    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnExit;

    //************ LOCAL VARIABLE ************
    
    @FXML
    void onExit(ActionEvent event) {
    	//TODO - close connection from server here.
    	ObjectContainer.loginStage.close();
    	System.exit(0);
    }

    @FXML
    void onLogin(ActionEvent event) {
    	String userName = txtUsername.getText().trim();
    	String password = txtPassword.getText().trim();
    	
    	if(userName.isEmpty() || password.isEmpty()) {
    		lblErrorMessage.setText("Please fill all fields..");
    	}else {
    		if(checkLogin(userName,password)) {
    			ObjectContainer.currentUserLogin.setUsername(userName);
    			MoveToHomeForm();
    		}
    	}
    }

    public boolean checkLogin(String userName, String password) {
    	boolean isValid = false;
		
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		json.addProperty("password", password);
		
		Message msg = new Message(MessageType.CHECK_LOGIN,json.toString());
		ClientUI.accept(msg);
		
		Message response = ObjectContainer.currentMessageFromServer;
		JsonObject responseJson = response.getMessageAsJsonObject();
		
		isValid = responseJson.get("isValid").getAsBoolean();
		if(!isValid) {
			lblErrorMessage.setText(responseJson.get("errorMessage").getAsString());
		}
		
		return isValid;
    }
    
    public void MoveToHomeForm() {
    	if(ObjectContainer.mainFormController == null) {
    		ObjectContainer.mainFormController = new MainFormController();
    	}
    	initUI(); // to clear all login fields.
    	ObjectContainer.mainFormController.start();
	}

	@FXML
    void onMinimize(ActionEvent event) {
    	ObjectContainer.loginStage.setIconified(true);
    }
    
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new  FXMLLoader();
    	loader.setLocation(getClass().getResource("LoginForm.fxml"));
    	
    	mainLoginPane = loader.load();
		ObjectContainer.loginController = loader.getController();
		ObjectContainer.loginController.initUI();
		ObjectContainer.currentUserLogin = new User();
		ObjectContainer.allowDrag(mainLoginPane, ObjectContainer.loginStage);
		
		Scene scene = new Scene(mainLoginPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void initUI() {
		btnLogin.setDefaultButton(true);
		ObjectContainer.setTextFieldLimit(txtUsername, 15);
		lblErrorMessage.setText("");
		txtUsername.setText("");
		txtPassword.setText("");
	}
}
