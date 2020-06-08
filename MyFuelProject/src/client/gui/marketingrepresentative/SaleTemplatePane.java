package client.gui.marketingrepresentative;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class SaleTemplatePane {

	@FXML
	private Pane mainSaleTemplatesPane;

	@FXML
	private Button btnOpen;

	@FXML
	private Button btnRemove;

	@FXML
	private Pane closePane;

	@FXML
	private Label lblSaleName;

	@FXML
	private Label lblSaleType;

	@FXML
	private Pane editPane;

	@FXML
	private Button btnChooseFuelType;

	@FXML
	private Button btnChooseCustomerType;

	@FXML
	private Button btnChooseCertainHours;

	@FXML
	private TextField txtSaleName;

	@FXML
	private Spinner<String> cbDiscountRate;

	@FXML
	private Label lblDiscountRate;
	
	@FXML
	private ChoiceBox<String> cbFuelTypes;

	@FXML
	private ChoiceBox<String> cbCustomerType;

	@FXML
	private Label lblFrom;

	@FXML
	private Spinner<String> cbFromHour;

	@FXML
	private Label lblTo;

	@FXML
	private Spinner<String> cbToHour;

	@FXML
	private Button btnEdit;

	@FXML
    private Button btnRunStop;
	
	@FXML
	private TextField txtDescription;
	/************** global variables ***************/
	private JsonObject saleTemplate;
	public boolean isNew = false;

	public static String BY_FUEL_TYPE = "BY_FUEL_TYPE";
	public static String BY_CUSTOMER_TYPE = "BY_CUSTOMER_TYPE";
	public static String BY_CRETIAN_HOURS = "BY_CRETIAN_HOURS";

	private boolean isCertainHoursClicked;
	private boolean isCustomerTypeClicked;
	private boolean isFuelTypeClicked;
	private boolean isOpen;

	private String checkedURL = "../../../images/checked.png";
	private String uncheckedURL = "../../../images/unchecked.png";

	private String to = "";
	private String from = "";
	private int isRunning;
	
	/**************** functions ***********************/
	
	@FXML
	void onRun(ActionEvent event) {
		String saleTemplateName = saleTemplate.get("saleTemplateName").getAsString();
		if(isRunning == 0) {
			if(ObjectContainer.saleTemplateController.checkIfCanRunSale(saleTemplateName)) {
				saleTemplate.addProperty("isRunning", 1);
				Message msg = new Message(MessageType.UPDATE_RUNNING_SALE, saleTemplate.toString());
				ClientUI.accept(msg);
				setImageButton("../../../images/stop.png", btnRunStop);
			}else {
				ObjectContainer.showMessage("Error", "Run Sale", "Can't run two sales at the same time.\nPlease stop other sale before.");
				return;
			}
		}else {
			saleTemplate.addProperty("isRunning", 0);
			Message msg = new Message(MessageType.UPDATE_RUNNING_SALE, saleTemplate.toString());
			ClientUI.accept(msg);
			setImageButton("../../../images/run.png", btnRunStop);
		}
		isRunning = 1 - isRunning;
		changeCSS();
	}
	
	private void changeCSS() {
		mainSaleTemplatesPane.setStyle(""
				+ "-fx-background-color:#123456;"
				+ "-fx-border-width:3px;"
				+ "-fx-border-color:" + ((isRunning == 1) ? "#00ff00;" : "#ffffff;"));
	}

	@FXML
	void onChooseCertainHours(ActionEvent event) {
		isCertainHoursClicked = !isCertainHoursClicked;
		String url = isCertainHoursClicked ? checkedURL : uncheckedURL;
		viewCertainHours(isCertainHoursClicked);
		setImageButton(url, btnChooseCertainHours);
		if (isCertainHoursClicked) {
			if (!from.equals("")) {
				cbFromHour.getValueFactory().setValue(from);
				cbToHour.getValueFactory().setValue(to);
			}
		}
	}

	@FXML
	void onChooseCustomerType(ActionEvent event) {
		isCustomerTypeClicked = !isCustomerTypeClicked;
		String url = isCustomerTypeClicked ? checkedURL : uncheckedURL;
		cbCustomerType.setVisible(isCustomerTypeClicked);
		setImageButton(url, btnChooseCustomerType);
	}

	@FXML
	void onChooseFuelType(ActionEvent event) {
		isFuelTypeClicked = !isFuelTypeClicked;
		String url = isFuelTypeClicked ? checkedURL : uncheckedURL;
		cbFuelTypes.setVisible(isFuelTypeClicked);
		setImageButton(url, btnChooseFuelType);
	}

	@FXML
	void onEdit(ActionEvent event) {
		if (btnEdit.getText().equals("Save")) {
			boolean isValid = checkAllFields();
			if (isValid) {
				if (isNew) {
					addNewSale();
					ObjectContainer.saleTemplateController.initUI();
				} else {
					updateExistSale();
				}
				btnEdit.setText("Edit");
				setDisableAll(true);
			} else {
				ObjectContainer.showMessage("Error", "Input Error", "Please fill all fields , or check sale type.");
			}
		} else {
			btnEdit.setText("Save");
			setDisableAll(false);
		}
	}

	private void updateExistSale() {
		removeSale();
		addNewSale();
		fillData();
	}
	private void removeSale() {
		Message msg = new Message(MessageType.REMOVE_SALE_TEMPLATE, saleTemplate.toString());
		ClientUI.accept(msg);
	}
	
	private void addNewSale() {
		saleTemplate = new JsonObject();
		saleTemplate.addProperty("saleTemplateName", txtSaleName.getText());
		saleTemplate.addProperty("description", txtDescription.getText());
		saleTemplate.addProperty("discountRate", Integer.parseInt(cbDiscountRate.getValue().split("%")[0]));
		saleTemplate.addProperty("isRunning", isRunning);
		JsonObject saleData = new JsonObject();
		JsonArray types = new JsonArray();
		if (isCustomerTypeClicked) {
			types.add(BY_CUSTOMER_TYPE);
			saleData.addProperty("customerType", cbCustomerType.getValue());
		}
		if (isFuelTypeClicked) {
			types.add(BY_FUEL_TYPE);
			saleData.addProperty("fuelType", cbFuelTypes.getValue());
		}
		if (isCertainHoursClicked) {
			types.add(BY_CRETIAN_HOURS);
			saleData.addProperty("from", cbFromHour.getValue());
			saleData.addProperty("to", cbToHour.getValue());
		}
		saleData.add("saleTypes", types);
		saleTemplate.add("saleData", saleData);
		Message msg = new Message(MessageType.ADD_NEW_SALE_TEMPLATE, saleTemplate.toString());
		ClientUI.accept(msg);
	}

	private boolean checkAllFields() {
		boolean isValid = true;
		isValid = !txtSaleName.getText().isEmpty();
		isValid = !cbDiscountRate.getValue().equals("0%") && isValid;
		isValid = !isFuelTypeClicked ? isValid
				: isFuelTypeClicked && !cbFuelTypes.getValue().equals(cbFuelTypes.getItems().get(0)) && isValid;
		isValid = !isCustomerTypeClicked ? isValid
				: isCustomerTypeClicked && !cbCustomerType.getValue().equals(cbCustomerType.getItems().get(0))
						&& isValid;
		isValid = !isCertainHoursClicked ? isValid
				: isCertainHoursClicked && !checkIfFromIsBeforeToTime() && isValid;
		isValid = !txtDescription.getText().isEmpty() && isValid;
		return isValid;
	}

	private boolean checkIfFromIsBeforeToTime() {
		String[] fromTime = cbFromHour.getValue().split(":");
		String[] toTime = cbToHour.getValue().split(":");
		if (Integer.parseInt(fromTime[0]) < Integer.parseInt(toTime[0])) {
			return false;
		}
		if (Integer.parseInt(fromTime[0]) == Integer.parseInt(toTime[0])
				&& Integer.parseInt(fromTime[1]) < Integer.parseInt(toTime[1])) {
			return false;
		}
		return true;
	}

	@FXML
	void onOpen(ActionEvent event) {
		if (isNew)
			return;
		isOpen = !isOpen;
		if (isOpen) {
			if (!isNew)
				fillData();
			open();
		} else {
			close();
		}
		btnEdit.setText(isOpen ? "Edit" : btnEdit.getText());
	}
	
	@FXML
	void onRemove(ActionEvent event) {
		if (!isNew) {
			ObjectContainer.showMessage("yes_no", "Remove Sale", "Are you sure you want to remove this sale?");
			if(!ObjectContainer.yesNoMessageResult) {
				return;
			}
			removeSale();
		}
		ObjectContainer.saleTemplateController.initUI();
	}

	public SaleTemplatePane load(JsonObject saleTemplate) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SaleTemplatePane.fxml"));
		SaleTemplatePane pane = null;
		try {
			mainSaleTemplatesPane = loader.load();
			pane = loader.getController();
			pane.initUI(saleTemplate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pane;
	}

	private void initUI(JsonObject saleTemplate) {
		isCertainHoursClicked = false;
		isCustomerTypeClicked = false;
		isFuelTypeClicked = false;
		isOpen = false;

		setImageButton(uncheckedURL, btnChooseCertainHours);
		setImageButton(uncheckedURL, btnChooseCustomerType);
		setImageButton(uncheckedURL, btnChooseFuelType);
		btnEdit.setId("dark-blue");

		initCB();
		initHoursSpinners();
		if (saleTemplate == null) {
			this.saleTemplate = new JsonObject();
			isNew = true;
			setImageButton("../../../images/minus_icon.png", btnOpen);
			setImageButton("../../../images/remove.png", btnRemove);
			btnRunStop.setVisible(false);
			btnEdit.setText("Save");
			open();
			setDisableAll(false);
		} else {
			this.saleTemplate = saleTemplate;
			isRunning = saleTemplate.get("isRunning").getAsInt();
			
			if(ObjectContainer.currentUserLogin.getUserPermission()  == UserPermission.MARKETING_MANAGER) {
				btnRemove.setVisible(false);
				btnRunStop.setVisible(true);
				btnEdit.setVisible(false);
				String url = "../../../images/" + ((isRunning == 1) ? "stop" : "run") + ".png";
				setImageButton(url, btnRunStop);
			}else {																 // MARKETING_REPRESENTATIVE
				btnRemove.setVisible(true);
				btnRunStop.setVisible(false);
				setImageButton("../../../images/remove.png", btnRemove);
				btnEdit.setVisible(true);
				btnEdit.setText("Edit");
			}
			
			fillData();
			close();
		}
	}

	private void fillData() {
		changeCSS();
		setDisableAll(false);
		lblSaleName.setText(saleTemplate.get("saleTemplateName").getAsString());
		txtSaleName.setText(saleTemplate.get("saleTemplateName").getAsString());
		lblDiscountRate.setText(saleTemplate.get("discountRate").getAsString() + "%");
		cbDiscountRate.getValueFactory().setValue(saleTemplate.get("discountRate").getAsString() + "%");
		JsonObject saleData = saleTemplate.get("saleData").getAsJsonObject();
		JsonArray saleTypes = saleData.get("saleTypes").getAsJsonArray();
		String types = "";
		for(int i = 0; i < saleTypes.size(); i++) {
			String type = saleTypes.get(i).getAsString();
			if(type.equals(BY_FUEL_TYPE)) {
				isFuelTypeClicked = true;
				cbFuelTypes.setVisible(true);
				setImageButton(checkedURL, btnChooseFuelType);
				cbFuelTypes.setValue(saleData.get("fuelType").getAsString());
				types += "FUEL, ";
			}
			if(type.equals(BY_CUSTOMER_TYPE)) {
				isCustomerTypeClicked = true;
				cbCustomerType.setVisible(true);
				setImageButton(checkedURL, btnChooseCustomerType);
				cbCustomerType.setValue(saleData.get("customerType").getAsString());
				types += "CUSTOMER, ";
			}
			if(type.equals(BY_CRETIAN_HOURS)) {
				isCertainHoursClicked = true;
				viewCertainHours(true);
				setImageButton(checkedURL, btnChooseCertainHours);
				from = saleData.get("from").getAsString();
				to = saleData.get("to").getAsString();
				cbFromHour.getValueFactory().setValue(from);
				cbToHour.getValueFactory().setValue(to);
				
				types += "HOURS, ";
			}
		}
		lblSaleType.setText(types.substring(0,types.length() - 2));
		txtDescription.setText(saleTemplate.get("description").getAsString());
	}

	private void setDisableAll(boolean b) {
		txtSaleName.setDisable(b);
		txtDescription.setDisable(b);
		cbCustomerType.setDisable(b);
		cbFuelTypes.setDisable(b);
		cbFromHour.setDisable(b);
		cbToHour.setDisable(b);
		cbDiscountRate.setDisable(b);
		btnChooseCertainHours.setDisable(b);
		btnChooseCustomerType.setDisable(b);
		btnChooseFuelType.setDisable(b);
	}

	private void open() {
		setImageButton("../../../images/minus_icon.png", btnOpen);
		closePane.setVisible(false);
		editPane.setVisible(true);
		cbCustomerType.setVisible(isCustomerTypeClicked);
		cbFuelTypes.setVisible(isFuelTypeClicked);
		viewCertainHours(isCertainHoursClicked);
		isOpen = true;
		mainSaleTemplatesPane.setPrefHeight(270);
		setDisableAll(true);
	}

	private void close() {
		isOpen = false;
		closePane.setVisible(true);
		editPane.setVisible(false);
		setImageButton("../../../images/add_icon.png", btnOpen);
		mainSaleTemplatesPane.setPrefHeight(60);
	}

	private void initHoursSpinners() {
		ObservableList<String> hours = FXCollections.observableArrayList();
		for (int i = 0; i < 48; i++) {
			String hour = i >= 20 ? i / 2 + "" : "0" + i / 2;
			String min = i % 2 == 0 ? "00" : "30";
			hours.add(hour + ":" + min);
		}
		SpinnerValueFactory<String> valueFactoryFrom = new SpinnerValueFactory.ListSpinnerValueFactory<String>(hours);
		SpinnerValueFactory<String> valueFactoryTo = new SpinnerValueFactory.ListSpinnerValueFactory<String>(hours);
		valueFactoryFrom.setValue("00:00");
		valueFactoryTo.setValue("00:00");
		cbFromHour.setValueFactory(valueFactoryFrom);
		cbToHour.setValueFactory(valueFactoryTo);
	}

	private void setImageButton(String url, Button btn) {
		BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}

	public void viewCertainHours(boolean flag) {
		if (!flag) {
			cbFromHour.getValueFactory().setValue("00:00");
			cbToHour.getValueFactory().setValue("00:00");
		}
		lblFrom.setVisible(flag);
		lblTo.setVisible(flag);
		cbFromHour.setVisible(flag);
		cbToHour.setVisible(flag);
	}

	private void initCB() {
		cbCustomerType.getItems().clear();
		cbCustomerType.getItems().add("Choose:");
		cbCustomerType.getItems().add("Private");
		cbCustomerType.getItems().add("Company");
		cbCustomerType.setValue(cbCustomerType.getItems().get(0));

		JsonArray fuelTypes = getFuelTypes();
		cbFuelTypes.getItems().clear();
		cbFuelTypes.getItems().add("Choose:");
		for (int i = 0; i < fuelTypes.size(); i++) {
			cbFuelTypes.getItems().add(fuelTypes.get(i).getAsString());
		}
		cbFuelTypes.setValue(cbFuelTypes.getItems().get(0));

		ObservableList<String> discountRates = FXCollections.observableArrayList();
		for (int i = 0; i < 101; i++) {
			discountRates.add(i + "%");
		}
		SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(
				discountRates);
		valueFactory.setValue("0%");
		cbDiscountRate.setValueFactory(valueFactory);

	}

	private JsonArray getFuelTypes() {
		JsonObject json = new JsonObject();
		Message msg = new Message(MessageType.GET_FUEL_TYPES, json.toString());
		ClientUI.accept(msg);

		return ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelTypes").getAsJsonArray();
	}

	public Pane getMainPane() {
		return mainSaleTemplatesPane;
	}

}