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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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

	@FXML
	void onApprove(ActionEvent event) {
		String orderID = orders.get("orderID").getAsString();
		String reason = "not in the stock";
		ObjectContainer.showMessage("yes_no", "Confirm delivery",
				"Are you sure you want to confirm\n order delivery? " + orderID);

		if (ObjectContainer.yesNoMessageResult) {
			JsonObject updateOrder = new JsonObject();
			updateOrder.addProperty("orderID", orderID);
			updateOrder.addProperty("orderStatus", "supllied");
			updateOrder.addProperty("reason", reason);
			Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
			ClientUI.accept(msg);
			txtStatus.setText("supllied");
			orderPane.setPrefHeight(50);
			orderPane.setMinHeight(50);
			setVisibleApproveAndDeclineButtons(false);
		}
	}

	@FXML
	void onDecline(ActionEvent event) {
		this.btnDeclineIsPress = !this.btnDeclineIsPress;
		if (this.btnDeclineIsPress) {
			resasonOfDeclinePane.setVisible(true);
			orderPane.setPrefHeight(92);
			orderPane.setMinHeight(92);
		} else {
			resasonOfDeclinePane.setVisible(false);
			orderPane.setPrefHeight(50);
			orderPane.setMinHeight(50);
		}
	}
	
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
			updateOrder.addProperty("orderStatus", "cancel");
			updateOrder.addProperty("reason", tfReasonOfDecline.getText().trim());
			Message msg = new Message(MessageType.UPDATE_ORDER_IN_DB, updateOrder.toString());
			ClientUI.accept(msg);
			txtStatus.setText("cancel");
			resasonOfDeclinePane.setVisible(false);
			orderPane.setPrefHeight(50);
			orderPane.setMinHeight(50);
			setVisibleApproveAndDeclineButtons(false);
		}
	}
	// **************************************************End of on press  function***************************************

	
	// **************************************************Initialize  function********************************************
	private void initUI(JsonObject orders, String color) {
		btnSubmit.setId("dark-blue");
		this.orders = orders;
		orderPane.setStyle(""
				+ "-fx-background-color:"+color+";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:3px;");
		String status = orders.get("orderStatus").getAsString();
		if(status.equals("supllied") || status.equals("cancel")) {
			setVisibleApproveAndDeclineButtons(false);
		}
		txtOrderId.setText(orders.get("orderID").getAsString());
		txtStationId.setText(orders.get("stationID").getAsString());
		txtFuelType.setText(orders.get("fuelType").getAsString());
		txtAmount.setText(orders.get("fuelAmount").getAsString());
		TxtTotalPrice.setText(orders.get("totalPrice").getAsString());
		txtStatus.setText(orders.get("orderStatus").getAsString());
		txtDate.setText(orders.get("orderDate").getAsString());

		orderPane.setPrefHeight(50);
		resasonOfDeclinePane.setVisible(false);
	}
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
	public Pane getOrdersPane() {
		return orderPane;
	}
	// **************************************************End of initialize  function*************************************

	// **************************************************Test  function**************************************************
	private Boolean checkReasonField() {
		if (tfReasonOfDecline.getText().isEmpty())
			return false;
		return true;
	}
	// **************************************************End of test  function*******************************************
	public void setVisibleApproveAndDeclineButtons(Boolean flag) {
		btnApprove.setVisible(flag);
		btnDecline.setVisible(flag);
	}



}
