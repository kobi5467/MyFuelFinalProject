package client.gui.marketingmanager;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class InventoryOrderPaneController {

    @FXML
    private Pane orderPane;

    @FXML
    private Text lblOrderID;

    @FXML
    private Text lblFuelType;

    @FXML
    private Text lblAmount;

    @FXML
    private Text lblSupplierName;

    @FXML
    private Label lblDenyReason;

    @FXML
    private TextField txtReasonDeny;

    @FXML
    private Button btnSubmitReason;
    
    @FXML
    private Button btnDeny;

    @FXML
    private Button btnApprove;

    private JsonObject orders;
    private boolean isDenny = false;
    
    /**
     * This method responsible to check the reason text, after that, take the data to Json object,
     * Send request to the server for update the data in the DB.
     * @param event - ActionEvent from the gui when we press on submit reason button.
     */
    @FXML
    void onSubmitReason(ActionEvent event) {
    	if(txtReasonDeny.getText().equals("") || txtReasonDeny.getText().equals("Please write reason of deny"))
    		txtReasonDeny.setText("Please write reason of deny");
    	String orderID = orders.get("orderID").getAsString();
    	ObjectContainer.showMessage("yes_no", "Deny Order", "Are you sure you want to Deny\n order number " + orderID);
    	if(ObjectContainer.yesNoMessageResult) {
        	JsonObject updateOrder = new JsonObject();
    		updateOrder.addProperty("orderID", orderID);
    		updateOrder.addProperty("orderStatus", "DeniedByStationManager");
    		updateOrder.addProperty("reason", txtReasonDeny.getText());
    		Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
    		ClientUI.accept(msg);
    		ObjectContainer.inventoryOrdersController.updateOrder(orderID);
    	}
    }
    /**
     * This method responsible to show "YES_NO" message box after approve the order,
     * After that, send request to the server for update the data in the DB.
     * @param event - ActionEvent from the gui when we press on approve order button.
     */
    @FXML
    void onApprove(ActionEvent event) {
    	String orderID = orders.get("orderID").getAsString();
    	ObjectContainer.showMessage("yes_no", "Approve Order", "Are you sure you want to Approve\n order number " + orderID);
    	if(ObjectContainer.yesNoMessageResult) {
    		JsonObject updateOrder = new JsonObject();
    		updateOrder.addProperty("orderID", orderID);
    		updateOrder.addProperty("orderStatus", "SENT_TO_SUPPLIER");
    		updateOrder.addProperty("reason", "");
    		Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
    		ClientUI.accept(msg);
    		ObjectContainer.inventoryOrdersController.updateOrder(orderID);
    	}
    }
    /**
     * This method responsible to show "YES_NO" message box after deny the order,
     * If yes, show the submit button and text field.
     * @param event - ActionEvent from the gui when we press on deny order button.
     */
    @FXML
    void onDeny(ActionEvent event) {
    	isDenny = !isDenny;
    	if(isDenny) {
    		lblDenyReason.setVisible(true);
    		txtReasonDeny.setVisible(true);
    		btnSubmitReason.setVisible(true);
    		orderPane.setPrefHeight(92);
    	}else {
    		lblDenyReason.setVisible(false);
    		txtReasonDeny.setVisible(false);
    		btnSubmitReason.setVisible(false);
    		orderPane.setPrefHeight(48);
    	}
    }
    /**
     * This method responsible to return the main pane of this controller for show it on the GUI.
     * @return - Return pane.
     */
    public Pane getOrderPane(){
    	return orderPane;
    }
    
    /**
	 * This method responsible to get the 'fxml' file and call to the method that init the UI.
	 * @param changePane - This is the value that responsible to change the panes by the correct button.
	 */
	public InventoryOrderPaneController load(JsonObject orders, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("InventoryOrderPane.fxml"));
		InventoryOrderPaneController pane = null;
		try {
			orderPane = loader.load();
			pane = loader.getController();
			pane.initUI(orders, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	
	public void setButtonImage(String url, Button btn) {
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}	
	
	/**
	 * This method responsible to init all the buttons, texts, lables and etc.
	 */
	private void initUI(JsonObject orders, String color) {
		
		this.orders = orders;
		btnSubmitReason.setId("dark-blue");
		setButtonImage("../../../images/v_icon_30px.png", btnApprove);
		setButtonImage("../../../images/error_icon_30px.png", btnDeny);
		lblDenyReason.setVisible(false);
		txtReasonDeny.setVisible(false);
		btnSubmitReason.setVisible(false);
		orderPane.setPrefHeight(48);
		orderPane.setStyle(""
				+ "-fx-background-color:" + color + ";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:3px;");
		lblOrderID.setText(orders.get("orderID").getAsString());
		lblFuelType.setText(orders.get("fuelType").getAsString());
		lblAmount.setText(orders.get("fuelAmount").getAsString());
		lblSupplierName.setText(orders.get("supplierID").getAsString());
	}

}
