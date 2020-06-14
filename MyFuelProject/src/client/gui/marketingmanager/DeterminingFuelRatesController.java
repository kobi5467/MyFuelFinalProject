package client.gui.marketingmanager;

import java.io.IOException;

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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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
		String newPrice = txtNewPrice.getText().trim();
		String errorMessage = "";

		lblErrorMessage.setStyle(""
				+ "-fx-text-fill:#ff0000;" 
				+ "-fx-font-weight: bold;"
				+ "-fx-font-size:14pt;"
				);
		if (newPrice.isEmpty() || cbFuelType.getValue().equals("Choose type")) {

			errorMessage = "Please fill all fields";
			lblErrorMessage.setText(errorMessage);
			// check if all fields are filled
		} else {
			flag = checkFields(newPrice, errorMessage);
			if (flag) {
				sendRateRequest(newPrice);
				//updateFuel(newPrice);
			}
		}
	}
	
	public void sendRateRequest(String newPrice) {
		JsonObject json = new JsonObject();
		String fuelType = FuelType.enumToString(currentFuel.getFuelType());

		json.addProperty("fuelType", fuelType);
		json.addProperty("pricePerLitter", newPrice);
		Message msg = new Message(MessageType.SEND_RATE_REQUEST, json.toString());
		ClientUI.accept(msg);
		
		lblErrorMessage.setText("Request to update sended succesfully");
		lblErrorMessage.setStyle(""
				+ "-fx-text-fill:#00ff00;" 
				+ "-fx-font-weight: bold;"
				+ "-fx-font-size:14pt;"
				);
		txtCurrPrice.setText("");
		txtMaxPrice.setText("");
		txtNewPrice.setText("");
		cbFuelType.setValue(cbFuelType.getItems().get(0));
		
		
	}
	
	public JsonArray getFuelTypes(){		
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("fuelTypes").getAsJsonArray();
	}
	
	public void updateFuel(String newPrice) {
		JsonObject json = new JsonObject();
		String fuelType = FuelType.enumToString(currentFuel.getFuelType());

		json.addProperty("fuelType", fuelType);
		json.addProperty("pricePerLitter", newPrice);
		Message msg = new Message(MessageType.UPDATE_FUEL, json.toString());
		ClientUI.accept(msg);
		
		lblErrorMessage.setText("Update Successfully");
		lblErrorMessage.setStyle(""
				+ "-fx-text-fill:#00ff00;" 
				+ "-fx-font-weight: bold;"
				+ "-fx-font-size:14pt;"
				);
		txtCurrPrice.setText("");
		txtMaxPrice.setText("");
		txtNewPrice.setText("");
		cbFuelType.setValue(cbFuelType.getItems().get(0));
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

	public Boolean checkFields(String newPrice, String errorMessage) {
		Float newPriceint = -1f;
		Float maxPriceint = -1f;
		Float currPriceint = -1f;
		try {
			newPriceint = Float.parseFloat(newPrice);
			maxPriceint = currentFuel.getMaxPricePerLitter();
			currPriceint = currentFuel.getPricePerLitter();
			if (newPriceint > maxPriceint ) {
				lblErrorMessage.setText("New price higher than the maximum price");
			}else if(newPriceint<=0) {
				lblErrorMessage.setText("Only positive price!");

			} else  {
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
		for (int i = 0; i < fuelTypes.size(); i++) {
			cbFuelType.getItems().add(fuelTypes.get(i).getAsString());
		}
		cbFuelType.setValue(cbFuelType.getItems().get(0));
	}


	private void afterChooseType(String type) {
		if (!type.equals("Choose type")) {
			getFuelObjectByType(type);
			lblErrorMessage.setText("");
			lblErrorMessage.setStyle(""
					+ "-fx-text-fill:#ff0000;" 
					+ "-fx-font-weight: bold;"
					+ "-fx-font-size:14pt;"
					);
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
		btnSubmit.setId("dark-blue");
		lblErrorMessage.setText("");
		lblErrorMessage.setStyle(""
				+ "-fx-text-fill:#ff0000;" 
				+ "-fx-font-weight: bold;"
				+ "-fx-font-size:14pt;"
				);
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

	
}
