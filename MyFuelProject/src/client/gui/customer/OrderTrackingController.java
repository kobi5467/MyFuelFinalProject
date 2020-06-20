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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * 
 * 
 * This class is the gui class of Order Tracking form. this class will contain
 * and present the Home Heating Fuel orders of the customer. it will allow him
 * to track his/her Home heating fuel orders.
 * 
 * @author Barak
 * @version final
 */

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

	@FXML
	private Label lblOrderNumber;

	@FXML
	private Label lblStatus;

	@FXML
	private Label lblSupplyDate;

	@FXML
	private Label lblFuelType;

	private ArrayList<AnchorPane> orderPanes;
	/**
	 * Array list of order panes that will open and updated in dynamic way
	 */
	public static ArrayList<OrderPane> orderPaneControllers;
	private JsonArray orders;
	private boolean showOnlyOpenOrdersFlag = false;
	/**
	 * Numbers of orders that open right now
	 */
	public static int currentOrderOpen = 0;

	/**
	 * This method will be activated after the user will click on "Search" button to
	 * search specific order by its ID
	 * 
	 * @param event
	 */
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

		} else {
			lblerrorMessage.setText("");
			txtTypeOrderID.setText("");
			ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnOnlyOpenOrder);
			showOnlyOpenOrdersFlag = false;
			// showOnlyOpenOrdersFlag=true;
			showOrderByID(OrderID);
		}
	}

	/**
	 * This method will send to the server request to get the current Home Heating
	 * Fuel Orders of this user. it will insert the result to an array of the orders
	 * for any use.
	 * 
	 * @return Json Array of the Home Heating Fuel Orders
	 */

	public JsonArray getHHFOrders() {
		JsonObject json = new JsonObject();
		json.addProperty("userName", ObjectContainer.currentUserLogin.getUsername());
		Message msg = new Message(MessageType.GET_HOME_HEATING_FUEL_ORDERS, json.toString());
		ClientUI.accept(msg);

		JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray HHFOrders = responseJson.get("HHFOrders").getAsJsonArray();
		return HHFOrders;
	}

	/**
	 * This function will insert to the VBOX container only panes of orders that
	 * their status is "WAITING" which means its still an open order.
	 */
	public void showOnlyOpenOrders() {
		vbOrdersContainer.getChildren().clear();
		for (int i = 0; i < orderPanes.size(); i++) {
			String orderStatus = orders.get(i).getAsJsonObject().get("orderStatus").getAsString();
			if (orderStatus.equals("OnGoing"))
				vbOrdersContainer.getChildren().add(orderPanes.get(i));
		}
	}

	/**
	 * This function will insert to the VBOX container orders that equals to the
	 * given orderID param. it will be used in the "OnSearch" method up above.
	 * 
	 * @param orderID
	 */

	public void showOrderByID(String orderID) {
		vbOrdersContainer.getChildren().clear();
		for (int i = 0; i < orderPanes.size(); i++) {
			String orderID2 = orders.get(i).getAsJsonObject().get("orderID").getAsString();
			if (orderID2.equals(orderID))
				vbOrdersContainer.getChildren().add(orderPanes.get(i));
		}
	}

	/**
	 * This function will insert to the VBOX container all of the order Panes
	 * without any condition.
	 */

	public void showAllOrders() {
		vbOrdersContainer.getChildren().clear();
		for (int i = 0; i < orderPanes.size(); i++) {
			vbOrdersContainer.getChildren().add(orderPanes.get(i));
		}
	}

	/**
	 * This function will be activated only when the user click on show only open
	 * order button and after it clicks once it will transform into "Show all
	 * orders" button it will use both functions from up above "ShowOnlyOpenOrders"
	 * and "ShowAllOrders"
	 * 
	 * @param event
	 */

	@FXML
	void OnShowOnlyOpenOrders(ActionEvent event) {
		lblerrorMessage.setText("");
		txtTypeOrderID.setText("");
		if (!showOnlyOpenOrdersFlag) {
			showOnlyOpenOrders();
			ObjectContainer.setButtonImage(ObjectContainer.checked, btnOnlyOpenOrder);
		} else {
			showAllOrders();
			ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnOnlyOpenOrder);
		}
		showOnlyOpenOrdersFlag = !showOnlyOpenOrdersFlag;
	}

	/**
	 * This function will check in the order panes which OrderPane has the given ID
	 * returns boolean flag if its exist or not.
	 * 
	 * @param orderID
	 * @return boolean flag
	 */
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
	
	/**
	 * this function load this pane
	 * 
	 * @param paneChange
	 */

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

	/**
	 * this function Initialize the pane by how we want it to be presented
	 */
	private void initUI() {
		lblerrorMessage.setText("");
		btnSearch.setId("dark-blue");
		ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnOnlyOpenOrder);
		vbOrdersContainer.setSpacing(5);
		orders = getHHFOrders();
		orderPanes = new ArrayList<>();
		orderPaneControllers = new ArrayList<>();
		for (int i = 0; i < orders.size(); i++) {
			OrderPane orderPane = new OrderPane();
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			orderPaneControllers.add(orderPane.load(orders.get(i).getAsJsonObject(), color));
			AnchorPane pane = orderPaneControllers.get(i).getMainPane();
			orderPanes.add(pane);
		}
		showAllOrders();
	}

}
