package client.gui;

import java.io.IOException;

import javax.management.monitor.StringMonitor;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Fuel;
import entitys.Message;
import entitys.enums.FuelType;
import entitys.enums.MessageType;
import entitys.enums.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import server.dbLogic.DBConnector;

public class OrderPane {

	@FXML
	private AnchorPane mainOrderPane;

	@FXML
	private Pane viewPane;

	@FXML
	private TextField txtFuelType;

	@FXML
	private TextField txtAmount;

	@FXML
	private TextField txtPricePerLitter;

	@FXML
	private TextField txtTotalPrice;

	@FXML
	private TextField txtSupplyDate;

	@FXML
	private Pane orderPane;

	@FXML
	private TextField txtOrderNumber;

	@FXML
	private TextField txtStatus;

	@FXML
	private Button btbView;

	private Fuel currentFuel;

	public String status;

	@FXML
	void onView(ActionEvent event) {
		if (viewPane.isVisible()) {
			btbView.setVisible(true);
			viewPane.setVisible(false);
			btbView.setText("+");
			mainOrderPane.setPrefSize(orderPane.getPrefWidth(), orderPane.getPrefHeight());

		} else {
			btbView.setVisible(true);
			viewPane.setVisible(true);
			btbView.setText("-");
			mainOrderPane.setPrefSize(viewPane.getPrefWidth(), viewPane.getPrefHeight());

		}
	}

	public AnchorPane load(JsonObject HHFOrder, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("OrderPane.fxml"));

		OrderPane pane = null;
		try {
			mainOrderPane = loader.load();
			pane = loader.getController();
			pane.initUI(HHFOrder, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mainOrderPane;
	}

	public void initUI(JsonObject HHFOrder, String color) {
		mainOrderPane.setStyle("-fx-background-color:" + color + ";");
		orderPane.setVisible(true);
		viewPane.setVisible(false);
		fillData(HHFOrder);
	}

	private void fillData(JsonObject HHFOrder) {

		System.out.println(HHFOrder);
		txtOrderNumber.setText(HHFOrder.get("orderID").getAsString());
		txtStatus.setText(HHFOrder.get("orderStatus").getAsString());
		status = HHFOrder.get("orderStatus").getAsString();
		

		txtFuelType.setText("Home Heating Fuel");
		txtAmount.setText(HHFOrder.get("fuelAmount").getAsString());
		getFuelObjectByType("Home Heating Fuel");
		txtPricePerLitter.setText(String.valueOf(currentFuel.getPricePerLitter()));
		txtTotalPrice.setText(HHFOrder.get("totalPrice").getAsString());
		txtSupplyDate.setText(HHFOrder.get("dateSupply").getAsString());

		txtOrderNumber.setEditable(false);
		txtStatus.setEditable(false);
		txtFuelType.setEditable(false);
		txtAmount.setEditable(false);
		txtPricePerLitter.setEditable(false);
		txtTotalPrice.setEditable(false);
		txtSupplyDate.setEditable(false);

	}

	public void getFuelObjectByType(String fuelType) {
		JsonObject json = new JsonObject();
		json.addProperty("fuelType", fuelType);

		Message msg = new Message(MessageType.GET_FUEL_BY_TYPE, json.toString());
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		fuelType = response.get("fuelType").getAsString();
		FuelType fuelTypeResponse = FuelType.stringToEnumVal(fuelType);
		float pricePerLitter = response.get("pricePerLitter").getAsFloat();
		float maxPricePerLitter = response.get("maxPricePerLitter").getAsFloat();

		currentFuel = new Fuel(fuelTypeResponse, pricePerLitter, maxPricePerLitter);
	}

}