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


/**
 * This class is the gui class of Determining fuel Rates controller.
 * this class will show to the marketing manager the current and max fuel rates
 * and will allow him/her to make a request to change new fuel rate,
 * the request will be sended to the ceo of the company.
 * @author Barak
 * @version final
 */

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
	
	
	/**
	 * This function will be activated when the user clicks on submit after he insert new
	 * fuel rate request. it will check the correctness of the fields and if all good
	 * it will send the message to the server to send the request.
	 * else, it will display an error message.
	 * @param event
	 */
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
				lblErrorMessage.setText("");
				sendRateRequest(newPrice);
				ObjectContainer.showMessage("Error", "Request Successesful",
						"Request sent to CEO\n" + cbFuelType.getValue() + "\nFrom " + txtCurrPrice.getText()
						+ " -> To " + txtNewPrice.getText() +" ");
				clearFields();
				//updateFuel(newPrice);
			}
		}
	}
	/**
	 * this function clear the fields of the text boxes
	 */
	private void clearFields() {
		txtCurrPrice.setText("");
		txtMaxPrice.setText("");
		txtNewPrice.setText("");
		cbFuelType.setValue(cbFuelType.getItems().get(0));		
	}

	/*
	 * this function gets the new Price rate and send a request to the server to handle
	 * the message about the will to send new rate request
	 */
	public void sendRateRequest(String newPrice) {
		JsonObject json = new JsonObject();
		String fuelType = FuelType.enumToString(currentFuel.getFuelType());

		json.addProperty("fuelType", fuelType);
		json.addProperty("pricePerLitter", newPrice);
		Message msg = new Message(MessageType.SEND_RATE_REQUEST, json.toString());
		ClientUI.accept(msg);
	}
	
	/**
	 * this function send a request to the server with message to get 
	 * all of the fuel types we work with.
	 * @return JsonArray of fuel types.
	 */
	
	public JsonArray getFuelTypes(){		
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("fuelTypes").getAsJsonArray();
	}
	/**
	 * this function get the new price to be updated and send a request to the server
	 * for update the fuel rate.
	 * @param newPrice - the new price to update
	 */
	
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
	
	/**
	 * this function get the name of the fuel type and send a request to the server to 
	 * create and get the Fuel object by the type with all the details.
	 * @param fuelType - the fuel type object.
	 */
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

	/**
	 * this function check the fields that the user inserted, and make
	 * some input validity tests.
	 * @param newPrice - the new price to be updated
	 * @param errorMessage - error message to display if bad input
	 * @return boolean true/false if tests passed or not.
	 */
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

	/**
	 * this function display the options of fuel types to choose in the choice box
	 * it uses the method "getFuelTypes" that i explained up above
	 */
	public void showOptionOfFuelTypeChoiseBox() {
		JsonArray fuelTypes = getFuelTypes();
		cbFuelType.getItems().add("Choose type");
		for (int i = 0; i < fuelTypes.size(); i++) {
			cbFuelType.getItems().add(fuelTypes.get(i).getAsString());
		}
		cbFuelType.setValue(cbFuelType.getItems().get(0));
	}

	/**
	 * this function set the correct details in the text fields by 
	 * the type of fuel the user chose. 
	 * @param type
	 */

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
	
	/**
	 *this function load the page 
	 * @param changePane 
	 */
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
	
	/**
	 * this function Initialize the page when the user gets into this page
	 */
	private void initUI() {
		btnSubmit.setId("dark-blue");
		btnSubmit.setDefaultButton(true);
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
