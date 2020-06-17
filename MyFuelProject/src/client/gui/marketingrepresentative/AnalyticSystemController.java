package client.gui.marketingrepresentative;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class AnalyticSystemController {

	@FXML
	private Pane mainAnalyticPane;

	@FXML
	private TableView<String> tblDataView;

	@FXML
	private Button btnShowRanks;

	@FXML
	private Button btnCustomerType;

	@FXML
	private Button btnCertainHours;

	@FXML
	private Button btnFuelType;

	@FXML
	private Button btnSort;

	@FXML
	private ChoiceBox<String> cbCertainHours;

	@FXML
	private ChoiceBox<String> cbCustomerType;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private Label lblTitleOfPane;

	private boolean isCertainHours = false;
	private boolean isCustomerType = false;
	private boolean isFuelType = false;
	private boolean isShowRanks = false;
	
	@FXML
	void onCertainHours(ActionEvent event) {
		isCertainHours = !isCertainHours;
		if(isCertainHours) {
			isShowRanks = false;
			setButtonsImages("/images/unchecked.png", btnShowRanks);
		}
		String url = isCertainHours ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url,btnCertainHours);
		cbCertainHours.setVisible(isCertainHours);
		if(cbCertainHours.isVisible()) cbCertainHours.setValue(cbCertainHours.getItems().get(0));
	}

	@FXML
	void onCustomerType(ActionEvent event) {
		isCustomerType = !isCustomerType;
		if(isCustomerType) {
			isShowRanks = false;
			setButtonsImages("/images/unchecked.png", btnShowRanks);
		}
		String url = isCustomerType ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url,btnCustomerType);
		cbCustomerType.setVisible(isCustomerType);
		if(cbCustomerType.isVisible()) cbCustomerType.setValue(cbCustomerType.getItems().get(0));
	}

	@FXML
	void onFuelType(ActionEvent event) {
		isFuelType = !isFuelType;
		if(isFuelType) {
			isShowRanks = false;
			setButtonsImages("/images/unchecked.png", btnShowRanks);
		}
		String url = isFuelType ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url,btnFuelType);
		cbFuelType.setVisible(isFuelType);
		if(cbFuelType.isVisible()) cbFuelType.setValue(cbFuelType.getItems().get(0));
	}

	@FXML
	void onShowRanks(ActionEvent event) {
		isShowRanks = !isShowRanks;
		String url = isShowRanks ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url,btnShowRanks);
		if(isShowRanks) {
			url = "/images/unchecked.png";
			isFuelType = false;
			setButtonsImages(url, btnFuelType);
			isCustomerType = false;
			setButtonsImages(url, btnCustomerType);
			isCertainHours = false;
			setButtonsImages(url, btnCertainHours);
			cbCertainHours.setVisible(false);
			cbCustomerType.setVisible(false);
			cbFuelType.setVisible(false);
		}
	}

	@FXML
	void onSort(ActionEvent event) {
		String fuelType = cbFuelType.getValue();
		String customerType = cbCustomerType.getValue();
		String certainHours = cbCertainHours.getValue();
		
		JsonObject json = new JsonObject();
		json.addProperty("fuelType", fuelType);
		json.addProperty("customerType", customerType);
		json.addProperty("certainHours", certainHours);
	}

	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("AnalyticSystem.fxml"));

		try {
			mainAnalyticPane = loader.load();
			changePane.getChildren().add(mainAnalyticPane);
			ObjectContainer.analyticSystemController = loader.getController();
			ObjectContainer.analyticSystemController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initUI() {
		initCB();
		btnSort.setId("dark-blue");
		String url = "/images/unchecked.png";
		setButtonsImages(url, btnCertainHours);
		setButtonsImages(url, btnCustomerType);
		setButtonsImages(url, btnFuelType);
		setButtonsImages(url, btnShowRanks);
		
	}

	private void initCB() {
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);
		JsonArray fuelTypes = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelTypes")
				.getAsJsonArray();

		ObjectContainer.setChoiceOptionOfChoiceBox(cbFuelType, fuelTypes, "Choose:");

		JsonArray hours = new JsonArray();
		hours.add("7:00 - 12:00");
		hours.add("12:00 - 20:00");
		hours.add("20:00 - 7:00");
		ObjectContainer.setChoiceOptionOfChoiceBox(cbCertainHours, hours, "Choose");

		JsonArray customerTypes = new JsonArray();
		customerTypes.add("Private");
		customerTypes.add("Company");
		ObjectContainer.setChoiceOptionOfChoiceBox(cbCustomerType, customerTypes, "Choose");
		
		cbFuelType.setVisible(false);
		cbCustomerType.setVisible(false);
		cbCertainHours.setVisible(false);
	}
	
	private void setButtonsImages(String url, Button btn) {		
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}
}
