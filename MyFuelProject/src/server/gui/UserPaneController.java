package server.gui;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
import client.gui.customer.OrderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class UserPaneController {

    @FXML
    private Pane mainUserPane;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblFullName;

    @FXML
    private Label lblPermission;

    @FXML
    private Label lblLoggedInTime;

    private String userName = "";
    
    public UserPaneController load(JsonObject userJson, String color) {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("UserPane.fxml"));

		UserPaneController pane = null;
		try {
			mainUserPane = loader.load();
			pane = loader.getController();
			pane.initUI(userJson, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pane;
    }

	private void initUI(JsonObject userJson, String color) {
		userName = userJson.get("userName").getAsString();
		lblUserName.setText(userJson.get("userName").getAsString());
		lblFullName.setText(userJson.get("name").getAsString());
		lblPermission.setText(userJson.get("userPermission").getAsString());
		lblLoggedInTime.setText(userJson.get("loginTime").getAsString());
		
		mainUserPane.setStyle(""
				+ "-fx-background-color:"+color+";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:2px;");
	}
	
	public Pane getMainPane() {
		return mainUserPane;
	}
	
	public String getUserName() {
		return userName;
	}

	public void updateColor(String color) {
		mainUserPane.setStyle(""
				+ "-fx-background-color:"+color+";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:2px;");		
	}
}
