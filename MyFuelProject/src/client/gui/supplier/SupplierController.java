package client.gui.supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
/**
 * This class contain the orders that supplier needs to provide for station fuel.
 * All the order of the supplier will loaded from SupllierOrderPane to this class.
 * The user can search order by order id and he can see only open orders.
 * @author Or Maman
 * @version final
 *
 */
public class SupplierController {

	@FXML
	private Pane paneSupplier;

	@FXML
	private Label lblOrderId;

	@FXML
	private TextField tfOrderIdInput;

	@FXML
	private Button btnSearch;

	@FXML
	private Label lblError;

	@FXML
	private Label lblShowOnlyOpenOrder;

	@FXML
	private ScrollPane spOrders;

	@FXML
	private VBox vbocOrdersPane;

	@FXML
	private CheckBox cbShowOpenOrder;

	@FXML
	private Text txtOrderId;

	@FXML
	private Text txtFuelType;

	@FXML
	private Text txtAmount;

	@FXML
	private Text txtStatus;

	@FXML
	private Text txtStationId;

	@FXML
	private Text txtTotalPrice;

	@FXML
	private Button btnShowOpenOrder;
	@FXML
	private Text txtAllOrdersSupplied;
	@FXML
	private Text txtDate;
	@FXML
	private Text txtApproveDecline;
	private ArrayList<SupllierOrderPane> order = new ArrayList<>();
    private String orderid=null; 
    private Boolean isPress=false;
    private String supplierId="123456";

	// **************************************************On press  function**********************************************q
    /**
     * This function displays all the open orders that the supplier needs to provide.
     * @param event while the user press on choice box 'Show open orders'.
     */
	@FXML
	void onPressShowOpenOrder(ActionEvent event) {
		txtAllOrdersSupplied.setVisible(false);
		this.isPress = !this.isPress;
		if (this.isPress)
			setButtonImage("../../../images/checked.png", btnShowOpenOrder);
		else
			setButtonImage("../../../images/unchecked.png", btnShowOpenOrder);
		this.orderid = null;
		txtOrderId.setText("");
		getDataBySupplierID(this.supplierId, this.isPress, this.orderid);

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
	 * This function provider to search specific order by order id.
	 * @param event while the user press on search button.
	 */
	@FXML
	void OnSearch(ActionEvent event) {
		this.isPress = false;
		setButtonImage("../../../images/unchecked.png", btnShowOpenOrder);
		txtAllOrdersSupplied.setVisible(false);
		if (checkOrderId()) {
			this.orderid = tfOrderIdInput.getText();
			getDataBySupplierID(this.supplierId, this.isPress, this.orderid);
		} else
			getDataBySupplierID(this.supplierId, this.isPress, null);
	}

	/**
	 * This function initialize the main pain and loaded SupllierOrderPane to the vbox in this main pain.
	 */
	private void initUI() {
		vbocOrdersPane.setSpacing(5);
		btnSearch.setId("dark-blue");
		setButtonImage("../../../images/unchecked.png", btnShowOpenOrder);
		String supplierID = "777";
		txtAllOrdersSupplied.setVisible(false);
		lblError.setText("");
		lblError.setVisible(true);
		getDataBySupplierID(supplierID, false, this.orderid);
	}

	/**
	 * This function gets SupplierForm.fxml and load him to the main pain.
	 * @param changePane - change the exist pain to SupplierForm.fxml.
	 */
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SupplierForm.fxml"));
		try {
			paneSupplier = loader.load();
			changePane.getChildren().add(paneSupplier);
			ObjectContainer.supplierController = loader.getController();
			ObjectContainer.supplierController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function load all the orders of the supplier and append him to the main pane.
	 * @param orders that include all the orders that contain to supplier.
	 */
	private void initSupplierOrdersVBox(JsonArray orders) {
		for (int i = 0; i < orders.size(); i++) {
			SupllierOrderPane supplierOrder = new SupllierOrderPane();
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			order.add(supplierOrder.load(orders.get(i).getAsJsonObject(), color));
			vbocOrdersPane.getChildren().add(order.get(i).getOrdersPane());
		}
	}
	/**
	 * This function load all the open orders from JsonArray and append him to the main pane.
	 * @param orders that include all the orders that contain to supplier.
	 */
	private void initOpenSupplierOrdersVBox(JsonArray orders) {
		Boolean openOrderExist = false;
		int index = 0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getAsJsonObject().get("orderStatus").getAsString().equals("SENT_TO_SUPPLIER")) {
				SupllierOrderPane supplierOrder = new SupllierOrderPane();
				String color = index++ % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
				order.add(supplierOrder.load(orders.get(i).getAsJsonObject(), color));
				vbocOrdersPane.getChildren().add(order.get(i).getOrdersPane());
				openOrderExist = true;
			}
		}
		if (!openOrderExist && isPress)
			txtAllOrdersSupplied.setVisible(true);
	}
	/**
	 * This function load specific order that the user want to find from JsonArray and
	 * append him to the main pane.
	 * @param orders that include all the orders that contain to supplier.
	 */
	private void initSupplierOrderByIdVBox(JsonArray orders, String orderId) {
		Boolean orderIdExist = false;
		int index = 0;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getAsJsonObject().get("orderID").getAsString().equals(orderId) && !orderIdExist) {
				SupllierOrderPane supplierOrder = new SupllierOrderPane();
				String color = index++ % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
				order.add(supplierOrder.load(orders.get(i).getAsJsonObject(), color));
				vbocOrdersPane.getChildren().add(order.get(i).getOrdersPane());
				orderIdExist = true;
			}
		}
		if (!orderIdExist)
			lblError.setText("Order id didnt exist!");

	}
	
	
	/**
	 * This function check if the order id that the user want to search is valid order id.
	 * @return true if the order is valid, else we return false.
	 */
	public boolean checkOrderId() {
		if (tfOrderIdInput.getText().isEmpty()) {
			getDataBySupplierID(this.supplierId, false, this.orderid);
			lblError.setText("Please enter order id!");
			return false;
		} else if (!ObjectContainer.checkIfStringContainsOnlyNumbers(tfOrderIdInput.getText())) {
			lblError.setText("Please enter only numbers!");
			return false;
		}
		lblError.setText("");
		return true;

	}

	/**
	 * This function get all the order data for the supplier which is now connected
	 * @param supplierID is the id of the user that connected right now.
	 * @param showOpenOrder is indicates that the user want to see only open orders.
	 * @param OrderId is indicates that the user want to see only this order id.
	 */
	private void getDataBySupplierID(String supplierID, Boolean showOpenOrder, String OrderId) {
		JsonObject order = new JsonObject();
		order.addProperty("supplierID", supplierID);
		Message msg = new Message(MessageType.GET_ORDERS_BY_SUPLLIER_ID, order.toString());
		ClientUI.accept(msg);
		JsonObject orders = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ordersArray = orders.get("orders").getAsJsonArray();
		ordersArray = sortOrderByOrderDate(ordersArray);
		vbocOrdersPane.getChildren().clear();
		if (showOpenOrder) {
			initOpenSupplierOrdersVBox(ordersArray);
		} else if (OrderId != null) {
			initSupplierOrderByIdVBox(ordersArray, OrderId);
			OrderId = null;
		} else {
			initSupplierOrdersVBox(ordersArray);
		}
	}

	/**
	 * This function get  the orders of supplier and sort him by date.
	 * @param jsonArray contain all the orders.
	 * @return jsonArray that sorted by date.
	 */
	private JsonArray sortOrderByOrderDate(JsonArray jsonArray) {
		List<JsonObject> list = new ArrayList<>();
		JsonArray sortedJsonArray = new JsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			list.add(jsonArray.get(i).getAsJsonObject());
		}
		Collections.sort(list, new Comparator<JsonObject>() {
			@Override
			public int compare(JsonObject a, JsonObject b) {
				String str1 = new String();
				String str2 = new String();
				try {
					str1 = (String) a.get("orderDate").getAsString();
					str2 = (String) b.get("orderDate").getAsString();
				} catch (JsonIOException e) {
					e.printStackTrace();
				}
				return str1.compareTo(str2);
			}
		});
		for (int i = 0; i < jsonArray.size(); i++) {
			sortedJsonArray.add(list.get(i));
		}
		return sortedJsonArray;
	}

}
