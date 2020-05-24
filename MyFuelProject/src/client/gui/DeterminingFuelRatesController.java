package client.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Fuel;
import entitys.Message;
import entitys.enums.FuelType;
import entitys.enums.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DeterminingFuelRatesController {

	@FXML
	private TextField txtCurrPrice;

	@FXML
	private TextField txtMaxPrice;

	@FXML
	private TextField txtNewPrice;

	@FXML
	private Button btnSubmit;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private Label lblErrorMessage;

	private Fuel currentFuel;
	
	@FXML
	void onSubmit(ActionEvent event) {
		boolean flag = true;
		Fuel fuel = null;
		String newPrice = txtNewPrice.getText().trim();
		String errorMessage = "";

		if (newPrice.isEmpty() || cbFuelType.getValue().equals("Choose type")) {

			errorMessage = "Please fill all fields";
			lblErrorMessage.setText(errorMessage);
			// check if all fields are filled
		} else {
			flag = checkFields(newPrice, errorMessage);
			if (flag) {
				updateFuel(fuel, newPrice);
				txtCurrPrice.setText(newPrice);
			}
		}
	}
	
	
	public JsonArray getFuelTypes(){		
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);
		
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		System.out.println(response.toString());
		
		return response.get("fuelTypes").getAsJsonArray();
		
	}
	
	public void updateFuel(Fuel fuel,String newPrice) {
		JsonObject json = new JsonObject();
		System.out.println(fuel.getFuelType());
		String fuelType = FuelType.enumToString(fuel.getFuelType());

		json.addProperty("fuelType", fuelType);
		json.addProperty("pricePerLitter", newPrice);
		Message msg = new Message(MessageType.UPDATE_FUEL, json.toString());
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}
	

	public void getFuelObjectByType(String fuelType) {
		JsonObject json = new JsonObject();
		json.addProperty("fuelType", fuelType);

		Message msg = new Message(MessageType.GET_FUEL_BY_TYPE, json.toString());
		ClientUI.clientController.handleMessageFromClient(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		fuelType = response.get("fuelType").getAsString();
		FuelType fuelTypeResponse = FuelType.stringToEnumVal(fuelType);
		float pricePerLitter = response.get("pricePerLitter").getAsFloat();
		float maxPricePerLitter = response.get("maxPricePerLitter").getAsFloat();

		currentFuel = new Fuel(fuelTypeResponse, pricePerLitter, maxPricePerLitter);
	}

	public Boolean checkFields(String newPrice, String errorMessage) {
		Float newPriceint = -1f;
		Float maxPriceint = -1f;
		Float currPriceint = -1f;
		try {
			newPriceint = Float.parseFloat(newPrice);
			maxPriceint = currentFuel.getMaxPricePerLitter();
			currPriceint = currentFuel.getPricePerLitter();
			if (newPriceint > maxPriceint) {
				lblErrorMessage.setText("New price higher than the maximum price");
			} else {
				errorMessage = "";
				lblErrorMessage.setText(errorMessage);
				return true;
			}
		} catch (Exception e) {
			lblErrorMessage.setText("Only digits!");
			return false;

		}
		return false;
	}

	public void showOptionOfFuelTypeChoiseBox() {
		JsonArray fuelTypes = getFuelTypes();
		cbFuelType.getItems().add("Choose type");
		int flag = 0;
		for (int i = 0; i < fuelTypes.size(); i++) {
			cbFuelType.getItems().add(fuelTypes.get(i).getAsString());
		}
		cbFuelType.setValue(cbFuelType.getItems().get(0));
	}


	private void afterChooseType(String type) {
		Fuel fuel;

		if (!type.equals("Choose type")) {
			getFuelObjectByType(type);

			txtCurrPrice.setText(String.valueOf(currentFuel.getPricePerLitter()));
			txtMaxPrice.setText(String.valueOf(currentFuel.getMaxPricePerLitter()));
			txtNewPrice.setText("");

		} else {
			txtCurrPrice.setText("");
			txtMaxPrice.setText("");
		}

	}
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("DeterminingFuelRatesForm.fxml"));

		try {
			Pane pane = loader.load();
			changePane.getChildren().add(pane);
			ObjectContainer.determiningFuelRatesController = loader.getController();
			ObjectContainer.determiningFuelRatesController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initUI() {
		lblErrorMessage.setText("");
		txtCurrPrice.setEditable(false);
		txtMaxPrice.setEditable(false);
		showOptionOfFuelTypeChoiseBox();
		// afterChooseType();
		cbFuelType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				afterChooseType(cbFuelType.getItems().get((Integer) number2));
			}
		});

	}
//	public void updatePaymentFormOnPaymentMethodClick() {
//		cbPaymentMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//				String value = cbPaymentMethod.getItems().get((Integer) number2);
//
//				if (value.equals("Choose type") || value.equals("Cash"))
//					showCreditCardFields(false);
//				else if (value.equals("Credit Card"))
//					showCreditCardFields(true);
//			}
//		});
//	}

	
}
