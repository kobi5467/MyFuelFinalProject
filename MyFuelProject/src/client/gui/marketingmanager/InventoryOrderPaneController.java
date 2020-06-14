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
    
    @FXML
    void onSubmitReason(ActionEvent event) {
    	if(txtReasonDeny.getText().equals("") || txtReasonDeny.getText().equals("Please write reason of deny"))
    		txtReasonDeny.setText("Please write reason of deny");
    	else {
        	String orderID = orders.get("orderID").getAsString();
        	JsonObject updateOrder = new JsonObject();
    		updateOrder.addProperty("orderID", orderID);
    		updateOrder.addProperty("orderStatus", "Denied");
    		updateOrder.addProperty("reason", txtReasonDeny.getText());
    		Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
    		ClientUI.accept(msg);
    		ObjectContainer.inventoryOrdersController.updateOrder(orderID);
    	}

    }
    
    @FXML
    void onApprove(ActionEvent event) {
    	String orderID = orders.get("orderID").getAsString();
    	ObjectContainer.showMessage("yes_no", "Approve Order", "Are you sure you want to Approve\n order number " + orderID);
    	if(ObjectContainer.yesNoMessageResult) {
    		JsonObject updateOrder = new JsonObject();
    		updateOrder.addProperty("orderID", orderID);
    		updateOrder.addProperty("orderStatus", "Approved");
    		updateOrder.addProperty("reason", "");
    		Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
    		ClientUI.accept(msg);
    		ObjectContainer.inventoryOrdersController.updateOrder(orderID);
    	}
    }

    @FXML
    void onDeny(ActionEvent event) {
    	String orderID = orders.get("orderID").getAsString();
    	ObjectContainer.showMessage("yes_no", "Deny Order", "Are you sure you want to Deny\n order number " + orderID);
    	if(ObjectContainer.yesNoMessageResult) {
        	lblDenyReason.setVisible(true);
        	txtReasonDeny.setVisible(true);
        	btnSubmitReason.setVisible(true);
    	}
    }
    
    public Pane getOrderPane(){
    	return orderPane;
    }

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

	private void initUI(JsonObject orders, String color) {
		this.orders = orders;
		lblDenyReason.setVisible(false);
		txtReasonDeny.setVisible(false);
		btnSubmitReason.setVisible(false);
		btnSubmitReason.setId("dark-blue");
		orderPane.setStyle("-fx-background-color:" + color + ";");
		lblOrderID.setText(orders.get("orderID").getAsString());
		lblFuelType.setText(orders.get("fuelType").getAsString());
		lblAmount.setText(orders.get("fuelAmount").getAsString());
		lblSupplierName.setText(orders.get("supplierID").getAsString());
	}

}
