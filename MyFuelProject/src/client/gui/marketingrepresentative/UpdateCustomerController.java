package client.gui.marketingrepresentative;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UpdateCustomerController {

	/******************************* Main Pane ********************************/
	@FXML
	private TextField txtEnterYourCustomerID;

	@FXML
	private Button btnSubmitCustomerID;

	@FXML
	private Label lblErrorText;

    @FXML
    private Button btnCustomerDetails;

    @FXML
    private Button btnCreditCardDetails;

    @FXML
    private Button btnVehicledDetails;

    @FXML
    private Button btnPurchaseDetails;

	/******************************* Details Pane ******************************/
	@FXML
	private Pane Pane2;
	
    @FXML
    private Pane userPane;

	@FXML
	private Label lblCustomerDetails;

	@FXML
	private Pane updateCustomer;

	@FXML
	private TextField txtUserNameUpdate;

	@FXML
	private TextField txtCustomerNameUpdate;

	@FXML
	private TextField txtEmailUpdate;

	@FXML
	private TextField txtPhoneUpdate;

	@FXML
	private TextField txtCity;

	@FXML
	private TextField txtAddressUpdate;

	@FXML
	private TextField txtPurchaseModelType;

	@FXML
	private Button btnAddVehicle;

	@FXML
	private Button btnEditDetails;

	@FXML
	private Button btnSaveDetails;

	@FXML
	private Text lblCustomerUserName;

	@FXML
	private Text lblCustomerName;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblPhoneNumber;

	@FXML
	private Label lblAddress;

	@FXML
	private Label lblCity;

	/******************************* CreditCard Pane ******************************/
	@FXML
	private Pane Pane3;

	@FXML
	private Label lblCreditCardDetails;

	@FXML
	private Button btnUpdateCreditCard;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private TextField txtCVV;

	@FXML
	private Label lblCreditCardNumber;

	@FXML
	private Label lblDateValidition;

	@FXML
	private Label lblCVV;

	@FXML
	private Button btnAddCreditCard;

	@FXML
	private Button btnViewCreditCard;
	
    @FXML
    private ChoiceBox<String> cbMounth;

    @FXML
    private ChoiceBox<String> cbYear;

	/************************ Edit Vehicles *************************************/
	@FXML
	private Label lblVehicles;

    @FXML
    private Pane VehiclesPane;

	@FXML
	private ScrollPane VehicleScrollPane;

	@FXML
	private VBox vbVehicleContainer;

	@FXML
	private ChoiceBox<String> cbVehicles;

	@FXML
	private ChoiceBox<String> cbAddFuelType;

	@FXML
	private TextField txtVehicleNumber;

	@FXML
	private TextField txtFuelType;

	/*********************** Purchase Model ************************************/
	@FXML
	private Label lblPurchaceModel;
	
    @FXML
    private Pane PurchaseModelPane;

	@FXML
	private Label lblChoosePurchaseModel;

	@FXML
	private Label lblFuelCompany;

	@FXML
	private Button btnUpdatePurchaseModel;

	@FXML
	private Button btnEditPurchaseModel;

	@FXML
	private ChoiceBox<String> cbNewPurchaseModel;

	@FXML
	private ChoiceBox<String> cbCompanyName1;

	@FXML
	private ChoiceBox<String> cbCompanyName2;

	@FXML
	private ChoiceBox<String> cbCompanyName3;
	
    @FXML
    private TextField txtFuelStation1;

    @FXML
    private TextField txtFuelStation2;

    @FXML
    private TextField txtFuelStation3;

	@FXML
	private ChoiceBox<String> cb2or3;

	@FXML
	private Label lblEroorPurchaseModel;

	/******************************* Implements ********************************/

	private ArrayList<CustomerVehiclesController> customerVehicles = new ArrayList<>();
	private JsonArray vehicles;
	JsonObject customerDetails = new JsonObject();
	private int flag = 0; //The flag will be 1 if customer want to insert credit card.

	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("UpdateCustomerForm.fxml"));

		try {
			updateCustomer = loader.load();
			changePane.getChildren().add(updateCustomer);
			ObjectContainer.updateCustomerController = loader.getController();
			ObjectContainer.updateCustomerController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		txtEnterYourCustomerID.requestFocus();
		btnSubmitCustomerID.setDefaultButton(true);
		btnSubmitCustomerID.setId("dark-blue");
        btnCustomerDetails.setStyle("-fx-border-color: #00008B; -fx-border-width: 5;");
        userPane.setVisible(true);
		Pane3.setVisible(false);//creditcard pane
		VehiclesPane.setVisible(false);
		PurchaseModelPane.setVisible(false);
		txtUserNameUpdate.setDisable(true);
		txtCustomerNameUpdate.setDisable(true);
		txtEmailUpdate.setDisable(true);
		txtPhoneUpdate.setDisable(true);
		txtAddressUpdate.setDisable(true);
		txtCity.setDisable(true);
		txtPurchaseModelType.setDisable(true);
		txtCreditCard.setDisable(true);
		txtCVV.setDisable(true);
		lblErrorText.setText("");
		lblAddress.setText("");
		lblCity.setText("");
		lblEmail.setText("");
		lblPhoneNumber.setText("");
		txtCreditCard.setDisable(true);
		txtCVV.setDisable(true);
		lblCreditCardNumber.setText("");
		lblCVV.setText("");
		lblDateValidition.setText("");
		lblCreditCardDetails.setStyle("-fx-text-fill: white; -fx-font-size:12px; -fx-font-weight:bold;");
		lblVehicles.setStyle("-fx-text-fill: white; -fx-font-size:12px; -fx-font-weight:bold;");
		lblCustomerDetails.setStyle("-fx-text-fill: white; -fx-font-size:12px; -fx-font-weight:bold;");
		lblPurchaceModel.setStyle("-fx-text-fill: white; -fx-font-size:12px; -fx-font-weight:bold;");
		lblCustomerUserName.setStyle("-fx-text-fill: white; -fx-font-size:12px; -fx-font-weight:bold;");
		lblCustomerName.setStyle("-fx-text-fill: white; -fx-font-size:12px; -fx-font-weight:bold;");
		VehicleScrollPane.setStyle("-fx-background-color:#F0FFFF;");
		cbCompanyName1.setVisible(false);
		cbCompanyName2.setVisible(false);
		cbCompanyName3.setVisible(false);
		cbNewPurchaseModel.setVisible(false);
		cb2or3.setVisible(false);
		lblChoosePurchaseModel.setVisible(false);
		lblFuelCompany.setVisible(false);
		btnSaveDetails.setVisible(false);
		btnUpdateCreditCard.setVisible(false);
		btnUpdatePurchaseModel.setVisible(false);
		btnAddVehicle.setDisable(true);
		txtFuelStation1.setDisable(true);
		txtFuelStation1.setVisible(true);
		txtFuelStation2.setDisable(true);
		txtFuelStation2.setVisible(false);
		txtFuelStation3.setDisable(true);
		txtFuelStation3.setVisible(false);

	}
	
    @FXML
    void onCustomerDetails(ActionEvent event) {
    	userPane.setVisible(true);
        btnCustomerDetails.setStyle("-fx-border-color: #00008B; -fx-border-width: 5;");
		Pane3.setVisible(false);//creditcard pane
		btnCreditCardDetails.setStyle("");
		VehiclesPane.setVisible(false);
		btnVehicledDetails.setStyle("");
		PurchaseModelPane.setVisible(false);
		btnPurchaseDetails.setStyle("");
    }
    
    @FXML
    void onCreditCardDetails(ActionEvent event) {
    	userPane.setVisible(false);
    	btnCustomerDetails.setStyle("");
		Pane3.setVisible(true);//creditcard pane
	    btnCreditCardDetails.setStyle("-fx-border-color: #00008B; -fx-border-width: 5;");
		VehiclesPane.setVisible(false);
		btnVehicledDetails.setStyle("");
		PurchaseModelPane.setVisible(false);
		btnPurchaseDetails.setStyle("");
    }
	
    @FXML
    void onVehiclesDetails(ActionEvent event) {
    	userPane.setVisible(false);
    	btnCustomerDetails.setStyle("");
		Pane3.setVisible(false);//creditcard pane
		btnCreditCardDetails.setStyle("");
		VehiclesPane.setVisible(true);
	    btnVehicledDetails.setStyle("-fx-border-color: #00008B; -fx-border-width: 5;");
		PurchaseModelPane.setVisible(false);
		btnPurchaseDetails.setStyle("");
    }
    
    @FXML
    void onPurchaseDetails(ActionEvent event) {
    	userPane.setVisible(false);
    	btnCustomerDetails.setStyle("");
		Pane3.setVisible(false);//creditcard pane
		VehiclesPane.setVisible(false);
		 btnVehicledDetails.setStyle("");
		PurchaseModelPane.setVisible(true);
	    btnPurchaseDetails.setStyle("-fx-border-color: #00008B; -fx-border-width: 5;");
    }

	@FXML
	void onSubmit(ActionEvent event) {

		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(txtEnterYourCustomerID.getText()))
			lblErrorText.setText("Please insert only digits, try Again");
		if (customerIsExist(txtEnterYourCustomerID.getText())) {
			takeDataFromDB(txtEnterYourCustomerID.getText());
			btnAddVehicle.setDisable(false);
			getVehicleData(txtEnterYourCustomerID.getText());
			getPurchaseModelsFromData(txtEnterYourCustomerID.getText());
			lblErrorText.setText("");
		} else {
			lblErrorText.setText("The Customer ID not exist!");

		}
	}

	private void getPurchaseModelsFromData(String customerID) {
		JsonObject customer = new JsonObject();
		customer.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_CUSTOMER_FUEL_TYPE, customer.toString());
		ClientUI.accept(msg);
		JsonObject customerFuelType = new JsonObject();
		customerFuelType = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		String[] res = customerFuelType.get("fuelCompanies").getAsString().split(",");
		switch(res.length) {
		case 1:
			txtFuelStation1.setVisible(true);
			txtFuelStation1.setText(res[0]);
			txtFuelStation2.setVisible(false);
			txtFuelStation3.setVisible(false);
			break;
		case 2:
			txtFuelStation1.setVisible(true);
			txtFuelStation1.setText(res[0]);
			txtFuelStation2.setVisible(true);
			txtFuelStation2.setText(res[1]);
			txtFuelStation3.setVisible(false);
			break;
		case 3:
			txtFuelStation1.setVisible(true);
			txtFuelStation1.setText(res[0]);
			txtFuelStation2.setVisible(true);
			txtFuelStation2.setText(res[1]);
			txtFuelStation3.setVisible(true);
			txtFuelStation3.setText(res[2]);
		}
		
		
	}

	private void takeDataFromDB(String customerID) {
		txtUserNameUpdate.setText("");
		JsonObject customer = new JsonObject();
		customer.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_CUSTOMER_DETAILS_BY_ID, customer.toString());
		ClientUI.accept(msg);
		customerDetails = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		txtUserNameUpdate.setText(customerDetails.get("userName").getAsString());
		txtCustomerNameUpdate.setText(customerDetails.get("name").getAsString());
		txtPhoneUpdate.setText(customerDetails.get("phoneNumber").getAsString());
		txtCity.setText(customerDetails.get("city").getAsString());
		txtAddressUpdate.setText(customerDetails.get("street").getAsString());
		txtEmailUpdate.setText(customerDetails.get("email").getAsString());
		txtPurchaseModelType.setText(customerDetails.get("purchaseModelType").getAsString());
		if(customerDetails.get("paymentMethod").getAsString().equals("Credit Card")) {
			Message msg2 = new Message(MessageType.GET_CREDIT_CARD_DETAILS_BY_ID, customer.toString());
			ClientUI.accept(msg2);
			JsonObject creditCardDetails = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
			txtCreditCard.setText(creditCardDetails.get("creditCardNumber").getAsString());
			txtCVV.setText(creditCardDetails.get("cvv").getAsString());
			String[] res = creditCardDetails.get("validationDate").getAsString().split("/");
			cbMounth.getItems().add(res[0]);
			cbYear.getItems().add(res[1]);
		}
		else {
			cbMounth.getItems().add("01");
			cbYear.getItems().add("2020");
		}
		for (int i = 0; i < 12; i++) {
			StringBuilder j = new StringBuilder();
			if(i < 9) j.append("0");
			j.append("" + (i + 1));
			if (j.toString().equals(cbMounth.getItems().get(0))) continue;
			cbMounth.getItems().add(j.toString());
		}
		for(int i = 2020; i<=2030; i++) {
			StringBuilder k = new StringBuilder();
			k.append("" + (i));
			if (k.toString().equals(cbYear.getItems().get(0))) continue;
			cbYear.getItems().add(k.toString());
		}
		
		cbMounth.setValue(cbMounth.getItems().get(0));
		cbYear.setValue(cbYear.getItems().get(0));
	}

	private boolean customerIsExist(String customerID) {
		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		Message message = new Message(MessageType.CHECK_IF_CUSTOMER_EXIST, json.toString());
		ClientUI.accept(message);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("isExist").getAsBoolean();
	}

	@FXML
	void onEditDetails(ActionEvent event) {
		txtEmailUpdate.setDisable(false);
		txtPhoneUpdate.setDisable(false);
		txtAddressUpdate.setDisable(false);
		txtCity.setDisable(false);
		btnSaveDetails.setVisible(true);
		btnEditDetails.setVisible(false);
	}

	@FXML
	void onSaveDetails(ActionEvent event) {
		int count = 4;
		if (!checkEmpty(txtEmailUpdate.getText())) {
			lblEmail.setText("Please Enter Email");
			count--;
		}
		if (!checkPhoneNumber(txtPhoneUpdate.getText()) || !checkEmpty(txtPhoneUpdate.getText())) {
			lblPhoneNumber.setText("Illegal phone number, try again");
			count--;
		}
		if (!checkEmpty(txtAddressUpdate.getText())) {
			lblAddress.setText("Fill Street please");
			count--;
		}
		if (!checkEmpty(txtCity.getText())) {
			lblCity.setText("Fill City please");
			count--;
		}

		if (count == 4) {
			lblErrorText.setText("");
			lblAddress.setText("");
			lblEmail.setText("");
			lblPhoneNumber.setText("");
			lblCity.setText("");
			txtEmailUpdate.setDisable(true);
			txtPhoneUpdate.setDisable(true);
			txtAddressUpdate.setDisable(true);
			txtCity.setDisable(true);
			btnSaveDetails.setVisible(false);
			btnEditDetails.setVisible(true);
			updateDetailsInDB(txtEnterYourCustomerID.getText());
		}
	}

	public void updateDetailsInDB(String customerID) {
		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		json.addProperty("userName", txtUserNameUpdate.getText());
		json.addProperty("customerName", txtCustomerNameUpdate.getText());
		json.addProperty("email", txtEmailUpdate.getText());
		json.addProperty("phoneNumber", txtPhoneUpdate.getText());
		json.addProperty("street", txtAddressUpdate.getText());
		json.addProperty("city", txtCity.getText());

		Message msg = new Message(MessageType.UPDATE_CUSTOMER_DETAILS, json.toString());
		ClientUI.accept(msg);
		JsonObject customerUpdate = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();

	}

	private boolean checkEmpty(String text) {
		return (!text.isEmpty());
	}

	private boolean checkPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() < 9 || phoneNumber.length() > 10 || phoneNumber.isEmpty())
			return false;
		return true;
	}

	@FXML
	void onEditCreditCard(ActionEvent event) {
		if(txtCreditCard.getText().equals("")) flag = 1;
		txtCreditCard.setDisable(false);
		txtCVV.setDisable(false);
		btnViewCreditCard.setVisible(false);
		btnUpdateCreditCard.setVisible(true);
	}

	@FXML
	void onUpdateCreditCard(ActionEvent event) {
		StringBuilder dateChecker = new StringBuilder();
		dateChecker.append(cbMounth.getValue());
		dateChecker.append("/");
		dateChecker.append(cbYear.getValue());
		if (checkCreditCardValues(txtCreditCard.getText(), txtCVV.getText(), dateChecker.toString())) {
			lblCreditCardNumber.setText("");
			lblCVV.setText("");
			lblDateValidition.setText("");
			txtCreditCard.setDisable(true);
			txtCVV.setDisable(true);
			btnViewCreditCard.setVisible(true);
			btnUpdateCreditCard.setVisible(false);
			updateCreditCardInDB(txtEnterYourCustomerID.getText());
		}
	}

	private void updateCreditCardInDB(String customerID) {
		JsonObject json = new JsonObject();
		StringBuilder dateValidation = new StringBuilder();
		dateValidation.append(cbMounth.getValue());
		dateValidation.append("/");
		dateValidation.append(cbYear.getValue());
		json.addProperty("customerID", customerID);
		json.addProperty("creditCard", txtCreditCard.getText());
		json.addProperty("cvv", txtCVV.getText());
		json.addProperty("dateValidation", dateValidation.toString());
		if (flag == 0) {
			Message msg = new Message(MessageType.UPDATE_CREDIT_CARD_DETAILS, json.toString());
			ClientUI.accept(msg);
		}
		else {
			Message msg = new Message(MessageType.INSERT_CREDIT_CARD_DETAILS, json.toString());
			ClientUI.accept(msg);
		}
		JsonObject customerUpdate = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		
	}

	private boolean checkCreditCardValues(String creditCardNumber, String cvv, String dateValidation) {
		if (creditCardNumber.isEmpty() || creditCardNumber.length() < 8 || creditCardNumber.length() > 16) {
			lblCreditCardNumber.setText("Please enter legal CreditCard Number");
			return false;
		}
		if (cvv.isEmpty() || cvv.length() < 3 || cvv.length() > 4) {
			lblCVV.setText("Please enter legal CVV");
			return false;
		}
		if (dateValidation.isEmpty()) {
			lblDateValidition.setText("Please enter date validation");
			return false;
		}
		return true;
	}

	@FXML
	void onEditVehicle(ActionEvent event) {
		String newVehicleNumber = txtVehicleNumber.getText();
		String newFuelType = cbAddFuelType.getSelectionModel().getSelectedItem();
		String vehicleToID = txtEnterYourCustomerID.getText();
		
		JsonObject newVehicle = new JsonObject();
		newVehicle.addProperty("customerID", vehicleToID);
		newVehicle.addProperty("fuelType", newFuelType);
		newVehicle.addProperty("vehicleNumber", newVehicleNumber);
		updateVehicleInDB(newVehicle);

		// Update new Vehicle in GUI
		vehicles.add(newVehicle);
		CustomerVehiclesController customerVehiclesController = new CustomerVehiclesController();
		String color = vehicles.size() % 2 == 0 ? "#0240FF" : "#024079";
		customerVehicles
				.add(customerVehiclesController.load(vehicles.get(vehicles.size() - 1).getAsJsonObject(), color));
		vbVehicleContainer.getChildren().add(customerVehicles.get(vehicles.size() - 1).getVehiclePane());
		txtVehicleNumber.setText("");
	}

	private void updateVehicleInDB(JsonObject newVehicle) {
		Message msg = new Message(MessageType.UPDATE_VEHICLES_IN_DB, newVehicle.toString());
		ClientUI.accept(msg);
		JsonObject updateVehicle = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}

	private void getVehicleData(String customerID) {
		JsonObject vehicle = new JsonObject();
		vehicle.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID, vehicle.toString());
		ClientUI.accept(msg);
		JsonArray vehicles = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("vehicles")
				.getAsJsonArray();
		initVehcilesChoiseBox(vehicles);
		initFuelTypes();
	}

	private void initFuelTypes() {
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray fuelTypes = response.get("fuelTypes").getAsJsonArray();

		cbAddFuelType.getItems().add("Choose Fuel Type");
		for (int i = 0; i < fuelTypes.size(); i++) {
			if (!fuelTypes.get(i).getAsString().equals("Home Heating Fuel")) {
				cbAddFuelType.getItems().add(fuelTypes.get(i).getAsString());
			}
		}
		cbAddFuelType.setValue(cbAddFuelType.getItems().get(0));
	}

	private void initVehcilesChoiseBox(JsonArray vehicles) {
		this.vehicles = vehicles;
		for (int i = 0; i < vehicles.size(); i++) {
			CustomerVehiclesController customerVehiclesController = new CustomerVehiclesController();
			String color = i % 2 == 0 ? "#0240FF" : "#024079";
			customerVehicles.add(customerVehiclesController.load(vehicles.get(i).getAsJsonObject(), color));
			vbVehicleContainer.getChildren().add(customerVehicles.get(i).getVehiclePane());
		}
	}

	public void updateVehicles(String vehicleNumber) {
		vbVehicleContainer.getChildren().clear();
		for (int i = 0; i < vehicles.size(); i++) {
			if (vehicles.get(i).getAsJsonObject().get("vehicleNumber").getAsString().equals(vehicleNumber)) {
				vehicles.remove(i);
				customerVehicles.remove(i);
			} 
		}
		
		for(int i = 0; i < vehicles.size(); i++) {
			vbVehicleContainer.getChildren().add(customerVehicles.get(i).getVehiclePane());
		}
	}

	@FXML
	void onEditPurchaseModel(ActionEvent event) {
		btnEditPurchaseModel.setVisible(false);
		btnUpdatePurchaseModel.setVisible(true);
		lblChoosePurchaseModel.setVisible(true);
		lblFuelCompany.setVisible(true);
		cbCompanyName2.setVisible(false);
		cbCompanyName3.setVisible(false);
		cb2or3.setVisible(false);
		cbCompanyName1.setDisable(false);
		cbCompanyName2.setDisable(false);
		cbCompanyName3.setDisable(false);
		cb2or3.setDisable(false);
		cbNewPurchaseModel.setDisable(false);


		if (!lblEroorPurchaseModel.getText().equals("Done")) {
			// Init cbNewPurchaseModel:
			Message msg = new Message(MessageType.GET_PURCHASE_MODELS, "");
			ClientUI.accept(msg);
			JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
			JsonArray types = response.get("purchaseModelTypes").getAsJsonArray();
			cbNewPurchaseModel.setVisible(true);
			cbNewPurchaseModel.getItems().add("Choose Purchase Model");
			for (int i = 0; i < types.size(); i++)
				cbNewPurchaseModel.getItems().add(types.get(i).getAsString());
			cbNewPurchaseModel.setValue(cbNewPurchaseModel.getItems().get(0));

			// Init cbCompanyNames:
			cbCompanyName1.setVisible(true);
			Message msg2 = new Message(MessageType.GET_FUEL_COMPANIES_NAMES, "");
			ClientUI.accept(msg2);
			JsonObject response2 = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
			JsonArray companies = response2.get("fuelCompanies").getAsJsonArray();
			cbCompanyName1.setVisible(true);
			cbCompanyName1.getItems().add("Choose Company Name");
			cbCompanyName2.getItems().add("Choose Company Name");
			cbCompanyName3.getItems().add("Choose Company Name");
			for (int i = 0; i < companies.size(); i++) {
				cbCompanyName1.getItems().add(companies.get(i).getAsString());
				cbCompanyName2.getItems().add(companies.get(i).getAsString());
				cbCompanyName3.getItems().add(companies.get(i).getAsString());
			}
			cbCompanyName1.setValue(cbCompanyName1.getItems().get(0));
			cbCompanyName2.setValue(cbCompanyName1.getItems().get(0));
			cbCompanyName3.setValue(cbCompanyName1.getItems().get(0));

			cb2or3.getItems().add("2");
			cb2or3.getItems().add("3");
			cb2or3.setValue(cb2or3.getItems().get(0));

			cbNewPurchaseModel.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
					switch (number2.toString()) {
					case "1":
						cb2or3.setVisible(false);
						cb2or3.setValue(cb2or3.getItems().get(0));
						cbCompanyName2.setVisible(false);
						cbCompanyName3.setVisible(false);
						cbCompanyName2.setValue(cbCompanyName1.getItems().get(0));
						cbCompanyName3.setValue(cbCompanyName1.getItems().get(0));
						break;
					case "2":
						cb2or3.setVisible(true);
						cbCompanyName2.setVisible(true);
						cbCompanyName3.setVisible(false);
						break;
					}

				}
			});

			cbCompanyName1.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
					String value1 = cbCompanyName1.getItems().get((Integer)number2);
					if (!value1.equals("Choose Company Name")) {
						if (value1.equals(cbCompanyName2.getValue())
								|| value1.equals(cbCompanyName3.getValue())) {
							lblEroorPurchaseModel.setVisible(true);
						} else
							lblEroorPurchaseModel.setVisible(false);
					}

				}
			});

			cb2or3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
					switch (number2.toString()) {
					case "0":
						cbCompanyName3.setVisible(false);
						cbCompanyName3.setValue(cbCompanyName1.getItems().get(0));
						break;
					case "1":
						cbCompanyName3.setVisible(true);
						break;
					}

				}
			});

			cbCompanyName2.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
					String value2 = cbCompanyName2.getItems().get((Integer)number2);
					if (!cbCompanyName1.getValue().equals("Choose Company Name")) {
						if (value2.equals(cbCompanyName1.getValue())
								|| value2.equals(cbCompanyName3.getValue())) {
							lblEroorPurchaseModel.setVisible(true);
							lblEroorPurchaseModel.setText("You cant choose the same company name, try again!");
						} else
							lblEroorPurchaseModel.setVisible(false);
					}
				}
			});

			cbCompanyName3.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
					String value3 = cbCompanyName3.getItems().get((Integer)number2);
					if (value3.equals(cbCompanyName1.getValue())
							|| value3.equals(cbCompanyName2.getValue())) {
						lblEroorPurchaseModel.setText("You cant choose the same company name, try again!");
						lblEroorPurchaseModel.setVisible(true);
					} else
						lblEroorPurchaseModel.setVisible(false);
				}
			});

		}

	}

	@FXML
	void onUpdatePurchaseModel(ActionEvent event) {
		int flag = 0;
		switch (cbNewPurchaseModel.getValue()) {
		case "Exclusive access":
			if (cbCompanyName1.getValue().equals("Choose Company Name")) {
				lblEroorPurchaseModel.setText("Error, Choose Company Name!");
				lblEroorPurchaseModel.setVisible(true);
			} else {
				lblEroorPurchaseModel.setVisible(false);
				flag = 1;
			}
			break;
		case "Expensive access":
			if (cb2or3.getValue().equals("3")) {
				if (cbCompanyName1.getValue().equals("Choose Company Name")
						|| cbCompanyName2.getValue().equals("Choose Company Name")
						|| cbCompanyName3.getValue().equals("Choose Company Name")) {
					lblEroorPurchaseModel.setText("Error, Choose Companies Name!");
					lblEroorPurchaseModel.setVisible(true);
				} else {
					lblEroorPurchaseModel.setVisible(false);
					flag = 1;
				}
			} else {
				if (cbCompanyName1.getValue().equals("Choose Company Name")
						|| cbCompanyName2.getValue().equals("Choose Company Name")) {
					lblEroorPurchaseModel.setText("Error, Choose Companies Name!");
					lblEroorPurchaseModel.setVisible(true);
				} else {
					lblEroorPurchaseModel.setVisible(false);
					flag = 1;
				}
			}
			break;
		}

		if (flag != 0) {
			btnEditPurchaseModel.setVisible(true);
			btnUpdatePurchaseModel.setVisible(false);
			lblEroorPurchaseModel.setVisible(false);
			cbCompanyName1.setDisable(true);
			cbCompanyName2.setDisable(true);
			cbCompanyName3.setDisable(true);
			cb2or3.setDisable(true);
			cbNewPurchaseModel.setDisable(true);
			lblEroorPurchaseModel.setText("Done");
			updatePurchaseModelInDB();
		}

	}

	private void updatePurchaseModelInDB() {
		JsonObject purchaseModelJson = new JsonObject();
		purchaseModelJson.addProperty("customerID", txtEnterYourCustomerID.getText());
		purchaseModelJson.addProperty("purchaseModelType", cbNewPurchaseModel.getValue());
		if(cbNewPurchaseModel.getValue().equals("Expensive access")) {
			if(cb2or3.getValue().equals("3")) {
				purchaseModelJson.addProperty("fuelCompanies", cbCompanyName1.getValue() + "," + cbCompanyName2.getValue() + "," + cbCompanyName3.getValue());
			}
			else
				purchaseModelJson.addProperty("fuelCompanies", cbCompanyName1.getValue() + "," + cbCompanyName2.getValue());
		}
		else
			purchaseModelJson.addProperty("fuelCompanies", cbCompanyName1.getValue());
		
		Message msg = new Message(MessageType.UPDATE_PURCHASE_MODEL_IN_DB, purchaseModelJson.toString());
		ClientUI.accept(msg);
		JsonObject purchaseModelJsonUpdate = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}
	
}
