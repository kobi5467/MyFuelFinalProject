package client.gui.customer;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class OrderTrackingController {

	@FXML
	private Pane OrderTrackingControlPane;

	@FXML
	private TextField txtTypeOrderID;

	@FXML
	private Button btnSearch;

	@FXML
	private Label lblerrorMessage;

	@FXML
	private Button btnOnlyOpenOrder;

	@FXML
	private ScrollPane spOrdersContainer;

	@FXML
	private VBox vbOrdersContainer;

	@FXML
	private Label lblOrderID;

	private ArrayList<AnchorPane> orderPanes;
	public static ArrayList<OrderPane> orderPaneControllers;
	private JsonArray orders;
	private boolean showOnlyOpenOrdersFlag = false;
	public static int currentOrderOpen = 0;	
	
	
	@FXML
	void OnSearch(ActionEvent event) {
		// boolean flag = true;

		String OrderID = txtTypeOrderID.getText().trim();
		String errorMessage = "";

		if (OrderID.isEmpty()) {

			errorMessage = "Please fill all fields";
			lblerrorMessage.setText(errorMessage);
			showAllOrders();
			// check if all fields are filled
		} else if (!ObjectContainer.checkIfStringContainsOnlyNumbers(OrderID)) {
			errorMessage = "Insert digits only!";
			lblerrorMessage.setText(errorMessage);
			showAllOrders();
		}

		else if (CheckIfOrderExists(OrderID) == false) {
			errorMessage = "Order ID doesnt exist!";
			lblerrorMessage.setText(errorMessage);
			showAllOrders();

		}
		else {
			lblerrorMessage.setText("");
			txtTypeOrderID.setText("");
			btnOnlyOpenOrder.setText("Show only open orders");
			showOnlyOpenOrdersFlag = false;
			// showOnlyOpenOrdersFlag=true;
			showOrderByID(OrderID);
		}
	}

	public JsonArray getHHFOrders() {
		JsonObject json = new JsonObject();
		json.addProperty("userName", ObjectContainer.currentUserLogin.getUsername());
		Message msg = new Message(MessageType.GET_HOME_HEATING_FUEL_ORDERS, json.toString());
		ClientUI.accept(msg);

		JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray HHFOrders = responseJson.get("HHFOrders").getAsJsonArray();
		return HHFOrders;
	}

	public void showOnlyOpenOrders() {
		vbOrdersContainer.getChildren().clear();
		for (int i = 0; i < orderPanes.size(); i++) {
			String orderStatus = orders.get(i).getAsJsonObject().get("orderStatus").getAsString();
			if (orderStatus.equals("WAITING"))
				vbOrdersContainer.getChildren().add(orderPanes.get(i));
		}
	}

	public void showOrderByID(String orderID) {
		vbOrdersContainer.getChildren().clear();
		for (int i = 0; i < orderPanes.size(); i++) {
			String orderID2 = orders.get(i).getAsJsonObject().get("orderID").getAsString();
			if (orderID2.equals(orderID))
				vbOrdersContainer.getChildren().add(orderPanes.get(i));
			

		}

	}

	public void showAllOrders() {
		vbOrdersContainer.getChildren().clear();
		for (int i = 0; i < orderPanes.size(); i++) {
			vbOrdersContainer.getChildren().add(orderPanes.get(i));
		}
	}

	@FXML
	void OnShowOnlyOpenOrders(ActionEvent event) {
		if (!showOnlyOpenOrdersFlag) {
			showOnlyOpenOrders();
			btnOnlyOpenOrder.setText("Show all orders");
			txtTypeOrderID.setText("");
			showOnlyOpenOrdersFlag = true;
			

		} else {
			showAllOrders();
			txtTypeOrderID.setText("");
			btnOnlyOpenOrder.setText("Show only open orders");
			showOnlyOpenOrdersFlag = false;
		}
	}

	public void openOrderByIndex(OrderPane p) {
		for (int i = 0; i < orderPaneControllers.size(); i++) {
			if(p != null && orderPaneControllers.get(i).equals(p)) {
				orderPaneControllers.get(i).showView();
			}else {
				orderPaneControllers.get(i).hideView();
			}
		}
	}
	
	public boolean CheckIfOrderExists(String orderID) {
		vbOrdersContainer.getChildren().clear();
		boolean flag = false;
		for (int i = 0; i < orderPanes.size(); i++) {
			String orderID2 = orders.get(i).getAsJsonObject().get("orderID").getAsString();
			if (orderID2.equals(orderID))
				flag = true;
		}
		return flag;
	}

	public void load(Pane paneChange) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("OrderTrackingForm.fxml"));

		try {
			OrderTrackingControlPane = loader.load();
			paneChange.getChildren().add(OrderTrackingControlPane);
			ObjectContainer.orderTrackingController = loader.getController();
			ObjectContainer.orderTrackingController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		lblerrorMessage.setText("");
		orders = getHHFOrders();
		orderPanes = new ArrayList<>();
		orderPaneControllers = new ArrayList<>();
		for (int i = 0; i < orders.size(); i++) {
			OrderPane orderPane = new OrderPane();
			String color = i % 2 == 0 ? "#0240FF" : "#024079";
			orderPaneControllers.add(orderPane.load(orders.get(i).getAsJsonObject(), color));
			AnchorPane pane = orderPaneControllers.get(i).getMainPane();
			orderPanes.add(pane);
		}
		showAllOrders();
	}

}
