package client.gui.supplier;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class is for only one sub-Order pane that
 * will be loaded to the main in Supplier Orders. 
 * Every pane and its actions that we enable for the user to do, will be handled in here
 * and how we want to present every single sub pane will handled in this class.
 * @author Or Maman
 * @version final
 */
public class SupllierOrderPane {

	@FXML
	private Text txtOrderId;

	@FXML
	private Text txtStationId;

	@FXML
	private Text txtFuelType;

	@FXML
	private Text txtAmount;

	@FXML
	private Text TxtTotalPrice;

	@FXML
	private Button btnApprove;

	@FXML
	private Button btnDecline;

	@FXML
	private Text txtStatus;

	@FXML
	private Text txtReasonOfDecline;

	@FXML
	private TextField tfReasonOfDecline;

	@FXML
	private Button btnSubmit;
	@FXML
	private Pane resasonOfDeclinePane;

	@FXML
	private Pane orderPane;

	@FXML
	private Pane paneOrder;
    @FXML
    private Text txtDate;

	private JsonObject orders;
	Boolean btnDeclineIsPress = false;
	// **************************************************On press  function**********************************************
	/**
	 * This function change  single order status from 'waiting' to 'done'.
	 * The function send request to server controller for update the oder in data base.
	 * @param event while the user press on 'Approve' button.
	 */
	@FXML
	void onApprove(ActionEvent event) {
		String orderID = orders.get("orderID").getAsString();
		String reason = "";
		ObjectContainer.showMessage("yes_no", "Confirm delivery",
				"Are you sure you want to confirm\n order delivery? " + orderID);

		if (ObjectContainer.yesNoMessageResult) {
			JsonObject updateOrder = new JsonObject();
			updateOrder.addProperty("orderID", orderID);
			updateOrder.addProperty("orderStatus", "Supllied");
			updateOrder.addProperty("reason", reason);
			Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
			ClientUI.accept(msg);
			txtStatus.setText("Supllied");
			orderPane.setPrefHeight(55);
			orderPane.setMinHeight(55);
			setVisibleApproveAndDeclineButtons(false);
			updateInventory();
		}
	}

	private void updateInventory() {
		JsonObject json = new JsonObject();
		json.addProperty("stationID", txtStationId.getText());
		json.addProperty("fuelAmount", Float.parseFloat(txtAmount.getText()));
		json.addProperty("fuelType", txtFuelType.getText());
		
		Message msg = new Message(MessageType.UPDATE_FUEL_AMOUNT_INVENTORY, json.toString());
		ClientUI.accept(msg);
	}

	/**
	 * After pressing on 'Decline' button, the function
	 * will extend the single order pane for writing reason for cancellation order.
	 * @param event while the user press on 'Decline' button.
	 */
	@FXML
	void onDecline(ActionEvent event) {
		this.btnDeclineIsPress = !this.btnDeclineIsPress;
		if (this.btnDeclineIsPress) {
			resasonOfDeclinePane.setVisible(true);
			orderPane.setPrefHeight(100);
			orderPane.setMinHeight(100);
		} else {
			resasonOfDeclinePane.setVisible(false);
			orderPane.setPrefHeight(55);
			orderPane.setMinHeight(55);
		}
	}
	
	/**
	 * This function change single order status from 'waiting' to 'decline'.
	 * The function check if the user write reason for cancellation and
	 * verify with him whether he approves the cancellation by displaying a popup message.
	 * @param event while the user press on 'Submit' button.
	 */
	@FXML
	void onSubmit(ActionEvent event) {
		String orderID = orders.get("orderID").getAsString();
		Boolean flag = checkReasonField();
		if (!flag)
			ObjectContainer.showMessage("ok", "Error", "Please fill reason");
		else
			ObjectContainer.showMessage("yes_no", "Confirm delivery",
					"Are you sure you want to cancel\n order delivery? " + orderID);
		flag = ObjectContainer.yesNoMessageResult && flag;
		if (flag) {
			JsonObject updateOrder = new JsonObject();
			updateOrder.addProperty("orderID", orderID);
			updateOrder.addProperty("orderStatus", "DeniedBySupplier");
			updateOrder.addProperty("reason", tfReasonOfDecline.getText().trim());
			Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
			ClientUI.accept(msg);
			txtStatus.setText("Denied");
			resasonOfDeclinePane.setVisible(false);
			orderPane.setPrefHeight(50);
			orderPane.setMinHeight(50);
			setVisibleApproveAndDeclineButtons(false);
		}
	}

	/**
	 *This function gets single order and initialize the order data and the style of the pain.
	 * @param JsonObject is a single order data.
	 * @param String color is the background color of the order pane.
	 */
	private void initUI(JsonObject orders, String color) {
		btnSubmit.setId("dark-blue");
		btnApprove.setId("dark-blue");
		btnDecline.setId("dark-blue");
		
		this.orders = orders;
		orderPane.setStyle(""
				+ "-fx-background-color:"+color+";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:3px;");
		String status = orders.get("orderStatus").getAsString();
		if(status.contains("Denied") || status.equals("Supllied")) {
			setVisibleApproveAndDeclineButtons(false);
		}
		
		txtOrderId.setText(orders.get("orderID").getAsString());
		txtStationId.setText(orders.get("stationID").getAsString());
		txtFuelType.setText(orders.get("fuelType").getAsString());
		txtAmount.setText(orders.get("fuelAmount").getAsString());
		TxtTotalPrice.setText(orders.get("totalPrice").getAsString());
		String orderStatus = orders.get("orderStatus").getAsString().equals("SENT_TO_SUPPLIER") ? 
				"Open" :  orders.get("orderStatus").getAsString().contains("Denied") ?
						"Denied" : orders.get("orderStatus").getAsString();
		txtStatus.setText(orderStatus);
		txtDate.setText(orders.get("orderDate").getAsString().split(" ")[0]);

		orderPane.setPrefHeight(55);
		resasonOfDeclinePane.setVisible(false);
	}
	/**
	 * This function gets SupplierOrderPan.fxml and load it to the main pain.
	 * @param orders is a single order data.
	 * @param color is the background color of the order pane.
	 * @return SupllierOrderPane object that will be loaded to the main in Supplier Orders. 
	 */
	public SupllierOrderPane load(JsonObject orders, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SupplierOrderPane.fxml"));
		SupllierOrderPane pane = null;
		try {
			orderPane = loader.load();
			pane = loader.getController();
			pane.initUI(orders, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;

	}
	/**
	 * This function return single order pane.
	 * @return orderPane that will be loaded to the main in Supplier Orders. 
	 */
	public Pane getOrdersPane() {
		return orderPane;
	}

	/**
	 * This function check if the user put a reason in reason field.
	 * @return true if this test passed, else the function will return false.
	 */
	private Boolean checkReasonField() {
		if (tfReasonOfDecline.getText().isEmpty())
			return false;
		return true;
	}
	
	/**
	 * This function hiding 'Approve' and 'Decline' buttons,
	 * in case the user want to cancellation his order.
	 * @param flag will we true if the user want to hide this buttons and false to show 
	 * the buttons to the user.
	 */
	public void setVisibleApproveAndDeclineButtons(Boolean flag) {
		btnApprove.setVisible(flag);
		btnDecline.setVisible(flag);
	}



}
