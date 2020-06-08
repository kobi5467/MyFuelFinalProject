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
import javafx.scene.layout.AnchorPane;
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

    @FXML
    private Pane viewPane;

    @FXML
    private Label lblReason;

    @FXML
    private TextField txtReason;

    @FXML
    private Button btnSubmit;
    
    public boolean  tookDecision=false;

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
		Message msg2 = new Message(MessageType.UPDATE_FUEL, json2.toString());
		ClientUI.accept(msg2);
		
		//mainRequestPane.setVisible(false);
		tookDecision=true;
		ObjectContainer.ratesToApproveController.showAllRequests();

    }

    @FXML
    void onDecline(ActionEvent event) {
    
    		viewPane.setVisible(true);
			mainRequestPane.setPrefSize(viewPane.getPrefWidth(), viewPane.getPrefHeight());
			
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

    }

    
    public AnchorPane load(JsonObject request, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("requestPane.fxml"));

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
		mainRequestPane.setStyle("-fx-background-color:" + color + ";");
		requestPane.setVisible(true);
		viewPane.setVisible(false);
		fillData(request);
	}
    
    private void fillData(JsonObject request) {

		//System.out.println(HHFOrder);
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
    
    
}

