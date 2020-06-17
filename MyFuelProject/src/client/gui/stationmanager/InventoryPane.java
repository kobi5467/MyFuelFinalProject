package client.gui.stationmanager;

import java.io.IOException;

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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.shape.Line;

/**
 * This class is for only one sub-Inventory pane that will be loaded in the main InventoryForm
 * every pane and its actions that we enable for the user to do will be handled in here
 * and how we want to present every single sub pane will handled in this class
 * @author Barak
 * @version final
 */

public class InventoryPane {

	@FXML
	private AnchorPane mainInventoryPane;

	@FXML
	private Button btnEdit;

	@FXML
	private TextField txtFuelType;

	@FXML
	private TextField txtThreshold;

	@FXML
	private TextField txtMaxAmount;

	@FXML
	private Label lblAmount;

	@FXML
	private ProgressBar pbFuelAmount;

	@FXML
	private Line thresholdLine;

	@FXML
	private Label lblErrorMessage;
	
	private String amount=null;

	
	/**
	 * this function will be activated when the user clicks on Edit button.
	 * once the edit button clicked it swap into save button, and let the user
	 * to change the fields of the threshold and max amount of this specific pane clicked.
	 * after the user click on save, and all the fields are valid, 
	 * a message will be sent to the server with the request to update
	 * the fuel inventory of this specific pane and fuel type.
	 * @param event
	 */
	@FXML
	void onEdit(ActionEvent event) {
		if (btnEdit.getText().equals("Edit")) {
			txtThreshold.setDisable(false);
			txtMaxAmount.setDisable(false);
			btnEdit.setText("Save");
			lblErrorMessage.setText("");
		} else {
			txtThreshold.setDisable(true);
			txtMaxAmount.setDisable(true);
			btnEdit.setText("Edit");
			// BARAK ADDED THIS

			JsonArray inventoryOfStation = new JsonArray();
			inventoryOfStation = ObjectContainer.inventoryController.getInventoryOfStation();
			for (int i = 0; i < inventoryOfStation.size(); i++) {
				JsonObject json = inventoryOfStation.get(i).getAsJsonObject();
				if (json.get("fuelType").getAsString().equals(txtFuelType.getText())) {
					if (AfterSave()) {
						//JsonObject json = new JsonObject();
						
						//Message msg = new Message(MessageType.GET_STATION_ID_BY_USER_NAME, ObjectContainer.currentUserLogin.getUsername());
						//ClientUI.accept(msg);
						//JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
						
						//String stationID = response.get("stationID").getAsString();
						String userName= ObjectContainer.currentUserLogin.getUsername();
					//	JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
						//JsonArray HHFOrders = responseJson.get("HHFOrders").getAsJsonArray();
						ObjectContainer.inventoryController.updateFuelInventory(txtThreshold.getText(), txtMaxAmount.getText(),
								txtFuelType.getText(),userName);
						inventoryOfStation = ObjectContainer.inventoryController.getInventoryOfStation();
						json = inventoryOfStation.get(i).getAsJsonObject();
						UpdateThisPane(json);
					}
					else {
						txtThreshold.setDisable(false);
						txtMaxAmount.setDisable(false);
						btnEdit.setText("Save");
					}

				}
			}
					
		}
		
	}

	
	/**
	 * this function loads the specific fuel type inventory pane, it gets the inventory 
	 * and load its form and details.
	 * @param inventory - specific fuel type inventory of this pane
	 * @return pane- the pane that was loaded
	 */
	public InventoryPane load(JsonObject inventory) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("InventoryPane.fxml"));
		InventoryPane pane = null;
		try {
			mainInventoryPane = loader.load();
			pane = loader.getController();
			pane.initUI(inventory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pane;
	}

	/**
	 * this function Initialize the pane by its inventory details. sol pane one time.
	 * @param inventory - the inventory as JsonObject with all the data
	 */
	
	public void initUI(JsonObject inventory) {
		// fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount
		lblErrorMessage.setText("");
		btnEdit.setId("dark-blue");
		amount=inventory.get("currentFuelAmount").getAsString();
		txtFuelType.setText(inventory.get("fuelType").getAsString());
		txtThreshold.setText(inventory.get("thresholdAmount").getAsString());
		txtMaxAmount.setText(inventory.get("maxFuelAmount").getAsString());
		lblAmount
				.setText(inventory.get("currentFuelAmount").getAsString() + " / " + txtMaxAmount.getText() + "Litters");

		float ratio = (inventory.get("currentFuelAmount").getAsFloat() / inventory.get("maxFuelAmount").getAsFloat());
		pbFuelAmount.setProgress(ratio);
		float threshold = (inventory.get("thresholdAmount").getAsFloat() / inventory.get("maxFuelAmount").getAsFloat());
		double x = 120 + threshold * pbFuelAmount.getPrefWidth();
		thresholdLine.setLayoutX(x);
		txtThreshold.setDisable(true);
		txtMaxAmount.setDisable(true);
		txtFuelType.setDisable(true);
		
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource("/images/InventoryPaneBackround.png").toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		mainInventoryPane.setBackground(background);
	}

	
	/**
	 * this function update the pane visibility after the user sets new values.
	 * it gets the current inventory of the fuel and update it on the screen.
	 * @param inventory
	 */
	public void UpdateThisPane(JsonObject inventory) {
		// fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount

		lblErrorMessage.setText("");
		txtFuelType.setText(inventory.get("fuelType").getAsString());
		txtThreshold.setText(inventory.get("thresholdAmount").getAsString());
		txtMaxAmount.setText(inventory.get("maxFuelAmount").getAsString());
		lblAmount
				.setText(inventory.get("currentFuelAmount").getAsString() + " / " + txtMaxAmount.getText() + "Litters");

		float ratio = (inventory.get("currentFuelAmount").getAsFloat() / inventory.get("maxFuelAmount").getAsFloat());
		pbFuelAmount.setProgress(ratio);
		float threshold = (inventory.get("thresholdAmount").getAsFloat() / inventory.get("maxFuelAmount").getAsFloat());
		double x = 120 + threshold * pbFuelAmount.getPrefWidth();
		thresholdLine.setLayoutX(x);
		txtThreshold.setDisable(true);
		txtMaxAmount.setDisable(true);
	

	}
	
	/**
	 * this function check after the save button clicked if all of the fields are correct
	 * filled and returns true/false with the answer. 
	 * this function get used before the update request sent.
	 * @return true/false
	 */
	
	public boolean AfterSave() {
		String errorMessage = "";
		String threshold1 = txtThreshold.getText().trim();
		String maxAmount1 = txtMaxAmount.getText().trim();
		String fuelType1 = txtFuelType.getText().trim();
		if (!checkFields(txtThreshold.getText(), txtMaxAmount.getText())) {
			errorMessage = "Only Numbers!";
			lblErrorMessage.setText(errorMessage);
			return false;

		} else if (checkFields2(txtThreshold.getText(), txtMaxAmount.getText(), amount, lblErrorMessage)) {
			errorMessage = "";
			return false;

		} else if (threshold1.isEmpty() || maxAmount1.isEmpty()) {
			errorMessage = "Please Fill All Fields!";
			lblErrorMessage.setText(errorMessage);
			return false;
		} else {
			errorMessage = "";
			lblErrorMessage.setText(errorMessage);
			return true;

		}
	}

	
	/**
	 * this function check the fields entered and make validity tests.
	 * it gets the threshold and the max amount inserted as an input.
	 * return boolean if the tests has passed or not.
	 * it checks some tests like if the input is only numbers.
	 * @param Threshold - threshold level number
	 * @param maxAmount - the max of amount can be in the inventory of this specific fuel type
	 * @return true/false
	 */
	public boolean checkFields(String Threshold, String maxAmount) {
		boolean flag1 = ObjectContainer.checkIfStringContainsOnlyNumbersFloatType(Threshold);
		boolean flag2 = ObjectContainer.checkIfStringContainsOnlyNumbersFloatType(maxAmount);

		if (flag1 && flag2) {
			return true;
		}
		return false;
	}
	/**
	 *  * this function check the fields entered and make validity tests.
	 * it gets the threshold and the max amount inserted as an input.
	 * return boolean if the tests has passed or not.
	 * it checks some tests like if the threshold is bigger than the max amount
	 * or the max amount is smaller than the current amount and else more
	 * @param Threshold - threshold level number
	 * @param maxAmount - max amount number
	 * @param Amount - the current amount of the fuel type inventory
	 * @param lblErrorMessage - the error message label to set
	 * @return true/false
	 */
	public boolean checkFields2(String Threshold, String maxAmount,String Amount, Label lblErrorMessage) {

		Float thresholdF = 0f;
		String errorMessage = "";
		Float maxF = 0f;
		Float amountF=0f;
		boolean flag = false;
		thresholdF = Float.parseFloat(Threshold);
		maxF = Float.parseFloat(maxAmount);
		amountF=Float.parseFloat(Amount);
		if (thresholdF < 0 || maxF < 0) {
			errorMessage = "Illegal values!";
			lblErrorMessage.setText(errorMessage);
			flag = true;
		}
		if (thresholdF > maxF) {
			errorMessage = "ERROR!: Threshold > Max amount!";
			lblErrorMessage.setText(errorMessage);
			flag = true;
		}
		if(amountF>maxF) {
			errorMessage = "ERROR!: Current Amount > Max amount!";
			lblErrorMessage.setText(errorMessage);
			flag = true;
		}
		
		return flag;
	}

	public AnchorPane getMainPane() {
		return mainInventoryPane;
	}
}
