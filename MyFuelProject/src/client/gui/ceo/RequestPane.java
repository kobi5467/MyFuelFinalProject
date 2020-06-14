package client.gui.ceo;


import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class RequestPane {

	@FXML
    private AnchorPane mainRequestPane;

    @FXML
    private Pane requestPane;

    @FXML
    private Label lblRequestID;

    @FXML
    private TextField txtRequestID;

    @FXML
    private Label lblCurrentPrice;

    @FXML
    private TextField txtCurrentPrice;

    @FXML
    private Label lblNewPrice;

    @FXML
    private TextField txtNewPrice;

    @FXML
    private Label lblFuelType;

    @FXML
    private TextField txtFuelType;

    @FXML
    private Label lblCreateTime;

    @FXML
    private TextField txtCreateTime;

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnDecline;

    @FXML Pane viewPane;

    @FXML
    private Label lblReason;

    @FXML
    private TextField txtReason;

    @FXML
    private Button btnSubmit;
    
    public boolean  tookDecision=false;
    public boolean clickedDecline = false;
    
    @FXML
    void onApprove(ActionEvent event) {
    	JsonObject json = new JsonObject();
    	JsonObject json2= new JsonObject();
    	

		json2.addProperty("fuelType", txtFuelType.getText());
		json2.addProperty("pricePerLitter", txtNewPrice.getText());
    	
    	json.addProperty("reasonOfDecline", "");
    	json.addProperty("decision", true);	
    	json.addProperty("requestID", txtRequestID.getText());
    	
    	Message msg = new Message(MessageType.UPDATE_DECISION, json.toString());
		ClientUI.accept(msg);
		msg = new Message(MessageType.UPDATE_FUEL, json2.toString());
		ClientUI.accept(msg);
		
		tookDecision=true;
		ObjectContainer.ratesToApproveController.initUI();

    }

    @FXML
    void onDecline(ActionEvent event) {
    	clickedDecline = !clickedDecline;
    	if(clickedDecline) {
    		viewPane.setVisible(true);
    		mainRequestPane.setPrefHeight(105);
    	}else {
    		viewPane.setVisible(false);
    		mainRequestPane.setPrefHeight(52);
    	}
    }

    @FXML
    void onSubmit(ActionEvent event) {
    	JsonObject json = new JsonObject();
    	json.addProperty("reasonOfDecline", txtReason.getText());
    	json.addProperty("decision", false);	
    	json.addProperty("requestID", txtRequestID.getText());
    	Message msg = new Message(MessageType.UPDATE_DECISION, json.toString());
		ClientUI.accept(msg);
		//mainRequestPane.setVisible(false);
		tookDecision=true;
		ObjectContainer.ratesToApproveController.showAllRequests();		
		ObjectContainer.ratesToApproveController.initUI();
		

    }

    
    public AnchorPane load(JsonObject request, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("RequestPane.fxml"));

		RequestPane pane = null;
		try {
			mainRequestPane = loader.load();
			pane = loader.getController();
			pane.initUI(request, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mainRequestPane;
	}
    
    public void initUI(JsonObject request, String color) {
		//mainRequestPane.setStyle("-fx-background-color:" + color + ";");
		mainRequestPane.setStyle(""
				+ "-fx-background-color:" + color + ";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:2px;");
		setButtonImage("../../../images/v_icon_30px.png", btnApprove);
		setButtonImage("../../../images/error_icon_30px.png", btnDecline);
		//setButtonImage("../../../images/v_icon.png", btnSubmit);
		btnApprove.setText("");
		btnDecline.setText("");
		
		btnSubmit.setId("dark-blue");

		requestPane.setVisible(true);
		viewPane.setVisible(false);
		mainRequestPane.setPrefHeight(52);
		fillData(request);
	}
    
    private void fillData(JsonObject request) {

		txtRequestID.setText(request.get("requestID").getAsString());
		txtCurrentPrice.setText(request.get("currentPrice").getAsString());
		
		txtNewPrice.setText(request.get("newPrice").getAsString());
		txtFuelType.setText(request.get("fuelType").getAsString());
		txtCreateTime.setText(request.get("createTime").getAsString());

		txtRequestID.setEditable(false);
		txtCurrentPrice.setEditable(false);
		txtNewPrice.setEditable(false);
		txtFuelType.setEditable(false);
		txtCreateTime.setEditable(false);
		
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

