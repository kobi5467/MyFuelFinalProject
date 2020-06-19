package client.gui.allusers;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import client.gui.customer.FastFuelController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * This class responsible to manage the LogIn in all the pane.
 * @author oyomtov
 * @version - Final
 */
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

    @FXML
    private Button btnFastFuel;
    
    //************ LOCAL VARIABLE ************
    
    @FXML
    void onFastFuel(ActionEvent event) {
    	if(ObjectContainer.fastFuelController == null) {
			ObjectContainer.fastFuelController = new FastFuelController();
		}
		ObjectContainer.fastFuelController.start();
		ObjectContainer.loginStage.hide();
    }

    
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
		setButtonImage("/images/FastFuel.png", btnFastFuel);
		ObjectContainer.setTextFieldLimit(txtUsername, 15);
		lblErrorMessage.setText("");
		txtUsername.setText("");
		txtPassword.setText("");
		txtUsername.setStyle("-fx-text-fill:#000000;");
		txtPassword.setStyle("-fx-text-fill:#000000;");
	}
	
	public void setButtonImage(String url, Button btn) {
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}
}
