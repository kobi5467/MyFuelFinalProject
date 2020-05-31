package client.gui;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;

public class SupplierController {

	@FXML
	private Pane paneSupplier;

	@FXML
	private Label lblOrderId;

	@FXML
	private TextField txtOrderId;

	@FXML
	private Button btnSearch;

	@FXML
	private Label lblError;

	@FXML
	private Label lblShowOnlyOpenOrder;

	@FXML
	private Button btnShowOnlyOpenorder;

	@FXML
	private ScrollPane spOrders;

	@FXML
	void onShowOnlyOpenOrder(ActionEvent event) {

		txtOrderId.setText("");
		lblError.setText("");
		// if(CheckForOpenOrders()==false)
		// lblError.setText("There are not exists open orders");

	}

	@FXML
	void OnSearch(ActionEvent event) {

		JsonObject request = new JsonObject();
		request.addProperty("supplierID", "123");
		if (checkOrderId() == true)
		{
			Message msg = new Message(MessageType.GET_FUEL_INVENTORY_ORDERS,request.toString());
			ClientUI.accept(msg);

			JsonObject response = ObjectContainer.currentMessageFromServer
					.getMessageAsJsonObject();
			JsonArray fuelInventoryorders = response.get("orders")
					.getAsJsonArray();
			for (int i = 0; i < fuelInventoryorders.size(); i++) {
				JsonObject order = fuelInventoryorders.get(i).getAsJsonObject();
				System.out.println(order.toString());
			}
		}
	}

	public  boolean checkOrderId() {

		if (txtOrderId.getText().isEmpty()){
			lblError.setText("please enter order ID!");
			return false;
		}
		else if (ObjectContainer.checkIfStringContainsOnlyNumbers(txtOrderId
				.getText())){
			lblError.setText("Insert digits only!");
			return false;
		}
		// else if(checkIfOrderIdExists(txtOrderId.getText())==false)
		// lblError.setText("Order id is not exists");
		else{
			lblError.setText("");
			return true;
					}

	}

	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SupplierForm.fxml"));

		try {
			paneSupplier = loader.load();
			changePane.getChildren().add(paneSupplier);
			ObjectContainer.supplierController = loader.getController();
			ObjectContainer.supplierController.initUI();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initUI() {

		lblError.setText("");
		lblError.setVisible(true);

	}

}
