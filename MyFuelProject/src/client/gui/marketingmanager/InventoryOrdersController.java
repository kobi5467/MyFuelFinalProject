package client.gui.marketingmanager;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class InventoryOrdersController {

    @FXML
    private AnchorPane InventoryOrdersPane;

    @FXML
    private Pane mainPane;

    @FXML
    private Label lblOrderID;

    @FXML
    private Label lblFuelType;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblSupplierName;

    @FXML
    private Label lblApprove;

    @FXML
    private Label lblDeny;

    @FXML
    private VBox vbOrderPane;
    
    private ArrayList<InventoryOrderPaneController> order = new ArrayList<>();
    private JsonArray orders;
    
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("InventoryOrders.fxml"));

		try {
			mainPane = loader.load();
			changePane.getChildren().add(mainPane);
			ObjectContainer.inventoryOrdersController = loader.getController();
			ObjectContainer.inventoryOrdersController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		
		lblOrderID.setText("Order ID");
		lblOrderID.setStyle("-fx-text-fill: #F0FFFF; -fx-font-size:20px; -fx-font-weight:bold;");
		lblFuelType.setText("Fuel Type");
		lblFuelType.setStyle("-fx-text-fill: #F0FFFF; -fx-font-size:20px; -fx-font-weight:bold;");
		lblAmount.setText("Amount");
		lblAmount.setStyle("-fx-text-fill: #F0FFFF; -fx-font-size:20px; -fx-font-weight:bold;");
		lblSupplierName.setText("Supplier ID");
		lblSupplierName.setStyle("-fx-text-fill: #F0FFFF; -fx-font-size:20px; -fx-font-weight:bold;");
		lblApprove.setText("Approve/");
		lblApprove.setStyle("-fx-text-fill: green; -fx-font-size:20px; -fx-font-weight:bold;");
		lblDeny.setText("Deny");
		lblDeny.setStyle("-fx-text-fill: red; -fx-font-size:20px; -fx-font-weight:bold;");
		
		getInvertoryOrdersFromDB();
		
	}

	private void getInvertoryOrdersFromDB() {
		JsonObject order = new JsonObject();
		order.addProperty("orders", "orders2");
		Message msg = new Message(MessageType.GET_ORDERS_FROM_DB, order.toString());
		ClientUI.accept(msg);
		JsonObject orders = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ordersArray = orders.get("orders").getAsJsonArray();
		initOrderVBox(ordersArray);
	}

	private void initOrderVBox(JsonArray orders) {
		this.orders = orders;
		for (int i = 0; i < orders.size(); i++) {
			InventoryOrderPaneController invertoryOrder = new InventoryOrderPaneController();
			String color = i % 2 == 0 ? "#0240FF" : "#024079";
			order.add(invertoryOrder.load(orders.get(i).getAsJsonObject(), color));
			vbOrderPane.getChildren().add(order.get(i).getOrderPane());
		}
		
	}

	public void updateOrder(String orderID) {
		vbOrderPane.getChildren().clear();
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getAsJsonObject().get("orderID").getAsString().equals(orderID)) {
				orders.remove(i);
				order.remove(i);
			} 
		}
		
		for(int i = 0; i < orders.size(); i++) {
			vbOrderPane.getChildren().add(order.get(i).getOrderPane());
		}
		
	}

}
