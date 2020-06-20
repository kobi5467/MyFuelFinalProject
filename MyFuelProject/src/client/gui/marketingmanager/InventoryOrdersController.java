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

/**
 * This class responsible to show all the inventory orders to the station manager.
 * Than, the station manager choose if approve or deny the orders before they send to supplier.
 * @author Or Yom Tov
 * @version - Final
 */
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
    private Label lblNoOrders;
    
    @FXML
    private VBox vbOrderPane;
    
    private ArrayList<InventoryOrderPaneController> order = new ArrayList<>();
    private JsonArray orders;
    
    /**
	 * This method responsible to get the 'fxml' file and call to the method that init the UI.
	 * @param changePane - This is the value that responsible to change the panes by the correct button.
	 */
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
	/**
	 * This method responsible to init all the buttons, texts, lables and etc.
	 * Than call "getInventoryOrdersFromDB" method.
	 */
	private void initUI() {
		vbOrderPane.setSpacing(5);
		getInvertoryOrdersFromDB();
		
	}
	/**
	 * This method responsible to request from the server to get the orders data.
	 * After that, init Json Array with the data and call "initOrderVbox" method.
	 */
	private void getInvertoryOrdersFromDB() {
		JsonObject order = new JsonObject();
		order.addProperty("orders", "orders2");
		Message msg = new Message(MessageType.GET_INVENTORY_ORDERS_FROM_DB_FOR_MARKETING_MANAGER, order.toString());
		ClientUI.accept(msg);
		JsonObject orders = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ordersArray = orders.get("orders").getAsJsonArray();
		initOrderVBox(ordersArray);
	}
	/**
	 * This method responsible to init the VBox with the dynamic panes from the other controller.
	 * @param orders - Json Array with all the orders data.
	 */
	private void initOrderVBox(JsonArray orders) {
		this.orders = orders;
		
		if(orders.size() == 0) {
			lblNoOrders.setVisible(true);
			vbOrderPane.setVisible(false);
			return;
		}else {
			lblNoOrders.setVisible(false);
			vbOrderPane.setVisible(true);
		}
		
		for (int i = 0; i < orders.size(); i++) {
			InventoryOrderPaneController invertoryOrder = new InventoryOrderPaneController();
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			order.add(invertoryOrder.load(orders.get(i).getAsJsonObject(), color));
			vbOrderPane.getChildren().add(order.get(i).getOrderPane());
		}
	}
	/**
	 * This method responsible to update the GUI with the current orders after deny or approve.
	 * @param orderID - Order ID string value.
	 */
	public void updateOrder(String orderID) {
		vbOrderPane.getChildren().clear();
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getAsJsonObject().get("orderID").getAsString().equals(orderID)) {
				orders.remove(i);
				order.remove(i);
			} 
		}
		if(orders.size() == 0) {
			lblNoOrders.setVisible(true);
			vbOrderPane.setVisible(false);
			return;
		}else {
			lblNoOrders.setVisible(false);
			vbOrderPane.setVisible(true);
		}
		
		for(int i = 0; i < orders.size(); i++)
			vbOrderPane.getChildren().add(order.get(i).getOrderPane());
		
		
		
	}

}
