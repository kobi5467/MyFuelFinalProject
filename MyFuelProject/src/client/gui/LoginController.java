package client.gui;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label lblErrorMessage;
    
    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnExit;

    //************ LOCAL VARIABLE ************
    private UserPermission userPermission;
    
    @FXML
    void onExit(ActionEvent event) {
    	//TODO - close connection from server here.
    	ObjectContainer.loginStage.close();
    }

    @FXML
    void onLogin(ActionEvent event) {
    	String userName = txtUsername.getText().trim();
    	String password = txtPassword.getText().trim();
    	String errorMessage = "";
    	
    	if(userName.isEmpty() || password.isEmpty()) {
    		errorMessage = "Please fill all fields";
    	}else {
    		boolean isValid = checkIfFieldsAreCorrect(userName, password);
    		if(isValid) {
    			System.out.println("Success !!");
    			MoveToHomeForm();
    		}else {
    			errorMessage = "user name or password are incorrect..";
    		}
    	}
    	lblErrorMessage.setText(errorMessage);
    }

    public void MoveToHomeForm() {
    	if(ObjectContainer.mainFormController == null) {
    		ObjectContainer.mainFormController = new MainFormController();
    	}
    	initUI();
    	ObjectContainer.mainFormController.start(userPermission);
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
		ObjectContainer.allowDrag(mainLoginPane, ObjectContainer.loginStage);
		
		Scene scene = new Scene(mainLoginPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void initUI() {
		lblErrorMessage.setText("");
		txtUsername.setText("");
		txtPassword.setText("");
	}

	public boolean checkIfFieldsAreCorrect(String userName, String password) {
		boolean isCorrect = false;
		
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		json.addProperty("password", password);
		
		Message msg = new Message(MessageType.CHECK_LOGIN,json.toString());
		ClientUI.clientController.handleMessageFromClient(msg);
		
		Message response = ObjectContainer.currentMessageFromServer;
		JsonObject responseJson = response.getMessageAsJsonObject();
		
		if(responseJson.get("isValid").getAsBoolean()) {
			isCorrect = true;
			userPermission = UserPermission.stringToEnumVal(responseJson.get("permission").getAsString());
		}
		return isCorrect;
	}
}
