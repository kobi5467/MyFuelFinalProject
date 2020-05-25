package client.gui;

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

public class OrderTrackingController {

	@FXML
	private Pane OrderTrackingControlPane;

	@FXML
	private TextField txtTypeOrderID;

	@FXML
	private Button btnSearch;

	@FXML
	private Button btnOnlyOpenOrder;

	@FXML
    private Label lblerrorMessage;
	
	@FXML
	void OnSearch(ActionEvent event) {
		boolean flag = true;

		String OrderID = txtTypeOrderID.getText().trim();
		String errorMessage = "";

		

		if (OrderID.isEmpty()) {

			errorMessage = "Please fill all fields";
			lblerrorMessage.setText(errorMessage);
			// check if all fields are filled
		}  if (ObjectContainer.checkIfStringContainsOnlyNumbers(OrderID)) {
			errorMessage = "Insert digits only!";
			lblerrorMessage.setText(errorMessage);
		}
		

		else
			txtTypeOrderID.setText("");
		System.out.println();
	}

	@FXML
	void OnShowOnlyOpenOrders(ActionEvent event) {

		txtTypeOrderID.setText("");

	}
	
	public void CheckIfOrderExists(String orderID) {
		JsonObject json = new JsonObject();
		json.addProperty("orderID", orderID);

		Message msg = new Message(MessageType.CHECK_IF_ORDER_EXISTS, json.toString());
		ClientUI.clientController.handleMessageFromClient(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		//FuelType fuelTypeResponse = FuelType.stringToEnumVal(response.get("fuelType").getAsString());
		//float pricePerLitter = response.get("pricePerLitter").getAsFloat();
		//float maxPricePerLitter = response.get("maxPricePerLitter").getAsFloat();

		//Fuel fuel = new Fuel(fuelTypeResponse, pricePerLitter, maxPricePerLitter);
		//return fuel;
	}
	

	public void load(Pane paneChange){

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
		
	}

	
}
