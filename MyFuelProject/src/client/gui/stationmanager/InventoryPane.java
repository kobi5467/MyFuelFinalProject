package client.gui.stationmanager;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
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
						ObjectContainer.inventoryController.updateFuelInventory(txtThreshold.getText(), txtMaxAmount.getText(),
								txtFuelType.getText());
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

	public void initUI(JsonObject inventory) {
		// fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount
		lblErrorMessage.setText("");
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
				new Image(getClass().getResource("../../../images/InventoryPaneBackround.png").toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		mainInventoryPane.setBackground(background);
	}

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

	public boolean checkFields(String Threshold, String maxAmount) {
		boolean flag1 = ObjectContainer.checkIfStringContainsOnlyNumbersFloatType(Threshold);
		boolean flag2 = ObjectContainer.checkIfStringContainsOnlyNumbersFloatType(maxAmount);

		if (flag1 && flag2) {
			return true;
		}
		return false;
	}

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
