package client.gui.marketingrepresentative;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.CreditCard;
import entitys.Customer;
import entitys.FuelCompany;
import entitys.Message;
import entitys.PurchaseModel;
import entitys.SubscribeType;
import entitys.Vehicle;
import entitys.enums.FuelType;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
/**
 * This class responsible to registration a new customer.
 * @author Kobi Malka
 * @version - Final
 */
public class CustomerRegistrationController {

	@FXML
	private Pane mainRegistrationPane;

	/******************************* STAGE 1 *******************************/
	@FXML
	private Pane stageOne;

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

    @FXML
    private TextField txtShowPassword;
	
	@FXML
	private TextField txtCustomerID;

	@FXML
	private TextField txtCustomerName;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtPhoneNumber;

	@FXML
	private TextField txtCity;

	@FXML
	private TextField txtStreet;

	@FXML
	private Label lblUserNameError;

	@FXML
	private Label lblPasswordError;

	@FXML
	private Label lblIDError;

	@FXML
	private Label lblEmailError;

	@FXML
	private Label lblPhoneError;

	@FXML
	private Label lblCustomerNameError;

	@FXML
	private Button btnShowPassword;
	
	@FXML
    private ChoiceBox<String> cbCustomerType;

    @FXML
    private Label lblCustomerTypeError;

    @FXML
    private Label lblCityError;

    @FXML
    private Label lblStreetError;
    
    @FXML
    private ImageView imgUsernameError;

    @FXML
    private ImageView imgPasswordError;

    @FXML
    private ImageView imgCustomerIDError;

    @FXML
    private ImageView imgCustomerNameError;

    @FXML
    private ImageView imgCustomerTypeError;

    @FXML
    private ImageView imgEmailError;

    @FXML
    private ImageView imgPhoneNumberError;

    @FXML
    private ImageView imgCityError;

    @FXML
    private ImageView imgStreetError;
    

    @FXML
    private ChoiceBox<String> cbSubscribeType;

    @FXML
    private Label lblSubscribeTypeError;

    @FXML
    private ImageView imgSubscribeType;

	/******************************* STAGE 2 *******************************/
	
	@FXML
	private Pane stageTwo;

	@FXML
	private ChoiceBox<String> cbPurchaseModel;

	@FXML
	private ChoiceBox<String> cbPaymentMethod;

	@FXML
    private Pane creditCardPane;
	
	@FXML
	private TextField txtCardNumber;

	@FXML
	private TextField txtCVV;

	@FXML
    private ChoiceBox<String> cbMonth;

    @FXML
    private ChoiceBox<String> cbYear;


	@FXML
	private Button btnHelp;

	@FXML
	private ChoiceBox<String> cbFuelCompany;

	@FXML
	private ChoiceBox<String> cbFuelCompany2;
	
	@FXML
	private ChoiceBox<String> cbFuelCompany3;
	@FXML
	private Label lblCvvError;

	@FXML
	private Label lblCardNumberError;

	@FXML
	private Label lblDateError;

	@FXML
	private Button btnAddFuelCompany;

    @FXML
    private Label lblPurchaseModelError;

    @FXML
    private Label lblPaymentMethodError;

    @FXML
    private Label lblFuelCompanyError;
    
    @FXML
    private ImageView imgHelp;
	
	/******************************* STAGE 3 *******************************/
	
	@FXML
	private Pane stageThree;

	@FXML
	private Button btnAddVehicle;

	@FXML
	private TextArea txtVehicleView;

	@FXML
	private TextField txtVehicleNumber;

	@FXML
	private ChoiceBox<String> cbVehicleFuelType;

	@FXML
    private ScrollPane spVehicleContainer;

    @FXML
    private VBox vbVehicleContainer;
	
    @FXML
    private Label lblVehicleError;
    
	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

    @FXML
    private ImageView imgStage;
	
	/* Local variables */
	
	private int currentStage;
	private Customer customer;
	private JsonArray fuelTypes;
	private ArrayList<VehiclePane> vehiclePanes;
	
	private int countFuelCompany = 1;
	
	/**
	 * This method is responsible to init the choices box.
	 * @param event - when we press on add fuel company
	 */
	@FXML
	void addFuelCompanyCB(ActionEvent event) {
		if(btnAddFuelCompany.getLayoutX() == 600) {
			btnAddFuelCompany.setLayoutX(750);
			cbFuelCompany3.setVisible(true);
			setBackgroundImage('-');
			cbFuelCompany3.setValue(cbFuelCompany3.getItems().get(0));
		}else {
			btnAddFuelCompany.setLayoutX(600);
			cbFuelCompany3.setVisible(false);
			setBackgroundImage('+');
		}
	}
	/**
	 * This method is responsible to delete vehicle from the list.
	 * @param vehicle - vehicle Object
	 */
	public void deleteVehicleFromList(Vehicle vehicle) {
		for(int i =0; i < vehiclePanes.size(); i++) {
			if(vehiclePanes.get(i).vehicle.equals(vehicle)) {
				customer.getVehicles().remove(i);
				vehiclePanes.remove(i);
				break;
			}
		}
		updateVbChildren();
	}
	/**
	 * This method is responsible to set back ground image
	 * @param c - value of icon
	 */
	private void setBackgroundImage(char c) {
		String url = "/images/";
		switch(c) {
		case '-':
			url += "minus_icon.png";
			break;
		case '+':
			url += "add_icon.png";
			break;
		}
		setButtonsImages(url, btnAddFuelCompany);
	}
	/**
	 * This method is responsible to move to previous pane.
	 * @param event - when we pressed on back button.
	 */
	@FXML
	void onBack(ActionEvent event) {
		//
		changeStage(-1);
	}
	/**
	 * This method is responsible to move on next pane.
	 * @param event - when we pressed on next button.
	 */
	@FXML
	void onNext(ActionEvent event) {
		boolean isValid = false;
		switch (currentStage) {
		case 1:
			// next1
			isValid = checkInputValidationStageOne();
			if(isValid) {
				updateCustomerObjectDetailsStage1();
				changeStage(1);
			}
			break;
		case 2:
			//next2
			isValid = checkInputValidationStageTwo();
			if(isValid) {
				updateCustomerObjectDetailsStage2();
				changeStage(1);
			}
			break;
		case 3:
			//submit
			isValid = checkInputValidationStageThree();
			if(isValid) {
				register();
				String msg = customer.getName() + " registered successfully.\n"+
							"You have registered " + customer.getVehicles().size() + " vehicles.";
				ObjectContainer.showMessage("Error", "Register Successful", msg);
				ObjectContainer.mainFormController.setPane("Home");
			}
			break;
		default:
			break;
		}
	}
	/**
	 * This method is responsible to request from the server to register customer.
	 */
	private void register() {
		//register customer.
		String customerJsonString = new Gson().toJson(customer);
		Message msg = new Message(MessageType.REGISTER_CUSTOMER, customerJsonString);
		ClientUI.accept(msg);
	}
	/**
	 * This method is responsible to update customer in pane 2
	 */
	private void updateCustomerObjectDetailsStage2() {
		if(cbPaymentMethod.getValue().equals("Credit Card")) {
			CreditCard creditCard = new CreditCard(customer.getCustomerId(), 
					txtCardNumber.getText(),cbMonth.getValue() + "/"+cbYear.getValue() , txtCVV.getText());
			customer.setCreditCard(creditCard);	
		}
		ArrayList<FuelCompany> companies = new ArrayList<>();
		switch(countFuelCompany) {
		case 3: companies.add(new FuelCompany(cbFuelCompany3.getValue()));
		case 2: companies.add(new FuelCompany(cbFuelCompany2.getValue()));
		case 1: companies.add(new FuelCompany(cbFuelCompany.getValue()));			
		}
		PurchaseModel purchaseModel = new PurchaseModel(cbPurchaseModel.getValue(), 0, companies);
		customer.setPurchaseModel(purchaseModel);
		customer.setPaymentMethod(cbPaymentMethod.getValue());
	}
	/**
	 * This method is responsible to update customer details in pane 1
	 */
	private void updateCustomerObjectDetailsStage1() {
		//stage 1 values
		customer.setUsername(txtUsername.getText());
		customer.setPassword(txtPassword.getText());
		customer.setCustomerId(txtCustomerID.getText());
		customer.setName(txtCustomerName.getText());
		customer.setEmail(txtEmail.getText());
		customer.setPhoneNumber(txtPhoneNumber.getText());
		customer.setCity(txtCity.getText());
		customer.setStreet(txtStreet.getText());
		customer.setCustomerId(txtCustomerID.getText());
		customer.setUserPermission(UserPermission.CUSTOMER);
		customer.setCustomerType(cbCustomerType.getValue());
		customer.setSubscribeType(new SubscribeType(cbSubscribeType.getValue(), 0));
	}

	/************************************** Check validation of stage 1 **************************************/
	/**
	 * This method is responsible to check input validation
	 * @return - return boolean value
	 */
	private boolean checkInputValidationStageOne() {
		initErrorLabels();
		boolean isValid = true;
		isValid = checkUserName(txtUsername.getText().trim());
		isValid = checkPassword(txtPassword.getText().trim()) && isValid;
		isValid = checkCustomerID(txtCustomerID.getText().trim()) && isValid;
		isValid = checkCustomerName(txtCustomerName.getText().trim()) && isValid;
		isValid = checkCustomerType(cbCustomerType.getValue().trim()) && isValid;
		isValid = checkSubscribeType(cbSubscribeType.getValue().trim()) && isValid;
		isValid = checkEmail(txtEmail.getText().trim()) && isValid;
		isValid = checkPhoneNumber(txtPhoneNumber.getText().trim()) && isValid;
		isValid = checkCity(txtCity.getText().trim()) && isValid;
		isValid = checkStreet(txtStreet.getText().trim()) && isValid;
		
		return isValid;
	}
	/**
	 * This method is responsible to chec customre type.
	 * @param type - string value of type
	 * @return - return boolean value.
	 */
	private boolean checkCustomerType(String type) {
		if(type.equals(cbCustomerType.getItems().get(0))) {
			lblCustomerTypeError.setText("Please choose customer type");
			setErrorImage(imgCustomerTypeError, "/images/error_icon.png");
			return false;
		}
		setErrorImage(imgCustomerTypeError, "/images/v_icon.png");
		return true; 
	}
	/**
	 * This method is responsible to check street value.
	 * @param street - string value of street.
	 * @return - return boolean value.
	 */
	private boolean checkStreet(String street) {
		if(street.isEmpty()) {
			lblStreetError.setText("Please fill street name and number");
			setErrorImage(imgStreetError, "/images/error_icon.png");
			return false;
		}
		setErrorImage(imgStreetError, "/images/v_icon.png");
		return true;
	}
	/**
	 * This method is responsible to check city value.
	 * @param city - string value of city.
	 * @return - return boolean value.
	 */
	private boolean checkCity(String city) {
		if(city.isEmpty()) {
			lblCityError.setText("Please fill city name");
			setErrorImage(imgCityError, "/images/error_icon.png");
			return false;
		}
		setErrorImage(imgCityError, "/images/v_icon.png");
		return true;
	}
	/**
	 * This method is responsible to check subscribe types by general value.
	 * @param value - string value.
	 * @return - return boolean value.
	 */
	private boolean checkSubscribeType(String value) {
		if(value.equals(cbSubscribeType.getItems().get(0))) {
			lblSubscribeTypeError.setText("Please choose subsrcibe type");
			setErrorImage(imgSubscribeType, "/images/error_icon.png");
			return false;
		}
		setErrorImage(imgSubscribeType, "/images/v_icon.png");
		return true; 
	}
	/**
	 * This method is responsible to check user name.
	 * @param userName - string value of user name.
	 * @return - return boolean value.
	 */
	private boolean checkUserName(String userName) {
		String errorMessage = "";
		if (userName.isEmpty()) {
			errorMessage = "Please fill field..";
		} else if (userIsAlreadyExist(userName)) {
			errorMessage = "This user name is already exist";
		} else {
			setErrorImage(imgUsernameError, "/images/v_icon.png");
			return true;
		}
		lblUserNameError.setText(errorMessage);
		setErrorImage(imgUsernameError, "/images/error_icon.png");
		return false;
	}
	/**
	 * This method is responsible to check if the user is exist.
	 * @param userName - string value of user name.
	 * @return - return boolean value.
	 */
	private boolean userIsAlreadyExist(String userName) {
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		Message message = new Message(MessageType.CHECK_IF_USER_EXIST,json.toString());
		ClientUI.accept(message);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("isExist").getAsBoolean();
	}
	/**
	 * This method is responsible to check is the password is legal.
	 * @param password - string value of password.
	 * @return - return boolean value.
	 */
	private boolean checkPassword(String password) {
		String errorMessage = "";
		if (password.isEmpty()) {
			errorMessage = "please fill field..";
		} else if(password.length() < 6) {
			errorMessage = "password must contain at least 6 letters";
		}else {
			setErrorImage(imgPasswordError, "/images/v_icon.png");
			return true;
		}
		lblPasswordError.setText(errorMessage);
		setErrorImage(imgPasswordError, "/images/error_icon.png");
		return false;
	}
	/**
	 * This method is responsible to check customer id value.
	 * @param customerID - string value of customer id.
	 * @return - return boolean value.
	 */
	private boolean checkCustomerID(String customerID) {
		String errorMessage = "";
		if (customerID.isEmpty()) {
			errorMessage = "please fill field..";
		} else if(!ObjectContainer.checkIfStringContainsOnlyNumbers(customerID)) {
			errorMessage = "Please insert only numbers..";
		} else if (customerIsExist(customerID)) {
			errorMessage = "customer ID is already exist..";
		} else if(customerID.length() < 6){
			errorMessage = "To short number..";
		}
		else {
			setErrorImage(imgCustomerIDError, "/images/v_icon.png");
			return true;
		}
		lblIDError.setText(errorMessage);
		setErrorImage(imgCustomerIDError, "/images/error_icon.png");
		return false;
	}
	/**
	 * This method is responsible to check if customer is exist.
	 * @param customerID - string value of customer id.
	 * @return - return boolean value.
	 */
	private boolean customerIsExist(String customerID) {
		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		Message message = new Message(MessageType.CHECK_IF_CUSTOMER_EXIST,json.toString());
		ClientUI.accept(message);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("isExist").getAsBoolean();
	}
	/**
	 * This method responsible to check the customer name.
	 * @param customerName - string value of customer name.
	 * @return - return boolean value.
	 */
	private boolean checkCustomerName(String customerName) {
		String errorMessage = "";
		if (customerName.isEmpty()) {
			errorMessage = "please fill field..";
		} else {
			setErrorImage(imgCustomerNameError, "/images/v_icon.png");
			return true;
		}
		lblCustomerNameError.setText(errorMessage);
		setErrorImage(imgCustomerNameError, "/images/error_icon.png");
		return false;
	}
	/**
	 * This method is responsible to check if the email is legal.
	 * @param email - string value of email.
	 * @return - return boolean value.
	 */
	private boolean checkEmail(String email) {
		String errorMessage = "";
		if (email.isEmpty()) {
			errorMessage = "please fill field..";
		} else {
			setErrorImage(imgEmailError, "/images/v_icon.png");
			return true;
		}
		lblEmailError.setText(errorMessage);
		setErrorImage(imgEmailError, "/images/error_icon.png");
		return false;
	}
	/**
	 * This method is responsible to check phone number.
	 * @param phoneNumber - string value of phone number.
	 * @return - return boolean value.
	 */
	private boolean checkPhoneNumber(String phoneNumber) {
		String errorMessage = "";
		if (phoneNumber.isEmpty()) {
			errorMessage = "please fill field..";
		} else if ((phoneNumber.length() != 10 || !ObjectContainer.checkIfStringContainsOnlyNumbers(phoneNumber))) {
			errorMessage = "Invalid input.. ";
		} else {
			setErrorImage(imgPhoneNumberError, "/images/v_icon.png");
			return true;
		}
		lblPhoneError.setText(errorMessage);
		setErrorImage(imgPhoneNumberError, "/images/error_icon.png");
		return false;
	}
	
	/****************************************** Check validation of stage 2 ***************************************/
	/**
	 * This method is responsible to check the credit card values.
	 * @return - return boolean value.
	 */
	private boolean checkInputValidationStageTwo() {
		initErrorLabels();
		boolean isValid = true;
		isValid = checkPurchaseModel(cbPurchaseModel.getValue().trim());
		isValid = checkCompanyNames() && isValid;
		isValid = checkPaymentMethod(cbPaymentMethod.getValue().trim()) && isValid;
		return isValid;
	}
	/**
	 * This method is responsible to check the payment method.
	 * @param paymentMethod - string value of payment method.
	 * @return - return boolean value.
	 */
	private boolean checkPaymentMethod(String paymentMethod) {
		if(paymentMethod.equals(cbPaymentMethod.getItems().get(0))) {
			lblPaymentMethodError.setText("Please choose payment method...");
			return false;
		}
		lblPaymentMethodError.setText("");
		if(paymentMethod.equals("Cash")) {
			return true;
		}
		//Credit Card
		String creditCardNumber = txtCardNumber.getText().trim();
		String cvv = txtCVV.getText().trim();
		String month = cbMonth.getValue();
		String year = cbYear.getValue();
		return checkCreditCardValues(creditCardNumber,cvv,month,year);
	}
	/**
	 * This method is responsible to check credit card values.
	 * @param creditCardNumber - string value of credit card number.
	 * @param cvv - string value of cvv number.
	 * @param month - string value of month number.
	 * @param year - string value of year number.
	 * @return - return boolean value.
	 */
	private boolean checkCreditCardValues(String creditCardNumber, String cvv, String month,String year) {
		boolean isValid = true;
		if(creditCardNumber.isEmpty() || creditCardNumber.length() < 8 || creditCardNumber.length() > 16) {
			lblCardNumberError.setText("Iligal card number, try again..");
			isValid = false;
		}
		if(cvv.isEmpty() || cvv.length() < 3 || cvv.length() > 4) {
			lblCvvError.setText("Enter 3/4 digits numbers");
			isValid = false;
		}
		if(month.equals(cbMonth.getItems().get(0)) || year.equals(cbYear.getItems().get(0))) {
			lblDateError.setText("Please insert date validation..");
			isValid = false;
		}
		return isValid;
	}
	/**
	 * This method is responsible to check companies name.
	 * @return - return boolean value.
	 */
	private boolean checkCompanyNames() {
		String comp1 = cbFuelCompany.getValue();
		String comp2 = cbFuelCompany2.getValue();
		String comp3 = cbFuelCompany3.getValue();
		String defaultValue = cbFuelCompany.getItems().get(0);
		
		if (cbFuelCompany2.isVisible() && cbFuelCompany3.isVisible()) { // 3 companies
			if (!comp1.equals(comp2) && !comp2.equals(comp3) && !comp1.equals(comp3) && !comp1.equals(defaultValue)
					&& !comp2.equals(defaultValue) && !comp3.equals(defaultValue)) {
				countFuelCompany = 3;
				return true;
			}
			lblFuelCompanyError.setText("invalid input..");
			return false;
			
		} else if (cbFuelCompany2.isVisible()) { // 2 compnies
			
			if (!comp1.equals(comp2) && !comp2.equals(defaultValue) && !comp1.equals(defaultValue)) {
				lblFuelCompanyError.setText("");
				countFuelCompany = 2;
				return true;
			}
			if (comp1.equals(comp2) && !comp1.equals(defaultValue)) {
				lblFuelCompanyError.setText("same companies..");
				return false;
			}
			lblFuelCompanyError.setText("invalid input..");
			return false;
			
		} else { // 1 company
			if(comp1.equals(defaultValue)) {
				lblFuelCompanyError.setText("invalid input..");
				return false;
			}
			countFuelCompany = 1;
			return true;
		}
	}
	/**
	 * This method is responsible to check purchase model.
	 * @param purchaseModel - string value of purchase model.
	 * @return - return boolean value.
	 */
	private boolean checkPurchaseModel(String purchaseModel) {
		if(purchaseModel.equals(cbPurchaseModel.getItems().get(0))) {
			lblPurchaseModelError.setText("Please choose purchase model..");
			return false;
		}
		return true;
	}
	
	/******************************************* Check validation of stage 3 **************************************/
	/**
	 * This method is responsible to check the input validation on pane 3.
	 * @return - return boolean value.
	 */
	private boolean checkInputValidationStageThree() {
		boolean isValid = true;
		if(customer.getVehicles().size() == 0) {
			ObjectContainer.showMessage("yes_no", "No Vehicle Added", "Are you sure you don't \nwant to add vehicle?");
			isValid = ObjectContainer.yesNoMessageResult;
		}else if(customer.getVehicles().size() < 2 && 
			customer.getSubscribeType().getSubscribeType().equals("Multiple Vehicle Monthly") ) {
			ObjectContainer.showMessage("Error", "Multiple vehicles", 
					"You have choosen multiple vehicle monthly subscribe.\nPlease add more vehicles");
			isValid = false;
		}else if(customer.getSubscribeType().getSubscribeType().contains("SINGLE") && customer.getVehicles().size() != 1) {
			ObjectContainer.showMessage("Error", "Single Purchase Model", 
					"You have choosen single vehicle subscribe.\nPlease add only 1 vehicle.");
			isValid = false;
		}
		return isValid;
	}
	/**
	 * This method is responsible to add new vehicle by dynamic pane.
	 * @param event - when we press on add vehicle.
	 */
	@FXML
	void addVehicle(ActionEvent event) {
		if(checkAddVehicleFields()) {
			Vehicle vehicle = new Vehicle(txtVehicleNumber.getText().trim(), 
					FuelType.stringToEnumVal(cbVehicleFuelType.getValue()), customer.getCustomerId());
			customer.getVehicles().add(vehicle);
			VehiclePane vehiclePane = new VehiclePane(vehicle);
			vehiclePanes.add(vehiclePane);
			updateVbChildren();
			lblVehicleError.setText("");
			cbVehicleFuelType.setValue(cbVehicleFuelType.getItems().get(0));
			txtVehicleNumber.setText("");
		}
	}
	/**
	 * This method is responsible to show in the gui the vehicle VBox.
	 */
	private void updateVbChildren() {
		
		vbVehicleContainer.getChildren().clear();
		for(int i = 0; i < vehiclePanes.size(); i++) {
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			vehiclePanes.get(i).setBackgroundColor(color);
			vbVehicleContainer.getChildren().add(vehiclePanes.get(i));
		}
	}
	/**
	 * This method is responsible to check the fields of the vehicle.
	 * @return - return boolean value.
	 */
	public boolean checkAddVehicleFields() {
		String vehicleNumber = txtVehicleNumber.getText().trim();
		String fuelType = cbVehicleFuelType.getValue().trim();
		
		if(vehicleNumber.isEmpty()) {
			lblVehicleError.setText("Invalid inputs..");
			return false;
		}
		if(vehicleNumber.length() < 6) {
			lblVehicleError.setText("To short number");
			return false;
		}
		if(!ObjectContainer.checkIfStringContainsOnlyNumbers(vehicleNumber)) {
			lblVehicleError.setText("Digits only");
			return false;
		}
		if(fuelType.equals(cbVehicleFuelType.getItems().get(0))) {
			lblVehicleError.setText("Fuel type empty");
			return false;
		}
		
		if(checkIfVehicleExistInNewVehicles(vehicleNumber)) {
			lblVehicleError.setText("Vehicle already added");
			return false;
		}
		
		JsonObject vehicle = new JsonObject();
		vehicle.addProperty("vehicleNumber", vehicleNumber);
		Message msg = new Message(MessageType.CHECK_IF_VEHICLE_EXIST,vehicle.toString());
		ClientUI.accept(msg);
		
		boolean isExist = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("isExist").getAsBoolean();
		if(isExist) {
			lblVehicleError.setText("Vehicle already exist");
		}
		return !isExist;
	}
	
	private boolean checkIfVehicleExistInNewVehicles(String vehicleNumber) {
		ArrayList<Vehicle> vehicles = customer.getVehicles();
		for(Vehicle v : vehicles) {
			if(v.getVehicleNumber().equals(vehicleNumber)) {
				return true;
			}
		}
		return false;
	}

	/************************************** LOGIC *******************************************/
	/**
	 * This method is responsible to change the stage.
	 * @param op - int value of operand, -1 for preview, 1 for next.
	 */
	private void changeStage(int op) {
		
		currentStage = currentStage + op;
		setErrorImage(imgStage, "/images/ScalaPart"+currentStage + ".png");
		switch (currentStage) {
		case 1:
			stageOne.setVisible(true);
			stageTwo.setVisible(false);
			stageThree.setVisible(false);
			btnBack.setVisible(false);
			btnNext.setText("Next");
			break;
		case 2:
			stageOne.setVisible(false);
			stageTwo.setVisible(true);
			stageThree.setVisible(false);
			btnBack.setVisible(true);
			btnNext.setText("Next");
			break;
		case 3:
			stageOne.setVisible(false);
			stageTwo.setVisible(false);
			stageThree.setVisible(true);
			btnBack.setVisible(true);
			btnNext.setText("Submit");
			break;
		default:
			break;
		}
	}
	/**
	 * This method is responsible to show the password.
	 * @param event - when we pressed on show password.
	 */
	@FXML
	void onShowPassword(ActionEvent event) {
		if(txtPassword.isVisible()) {
			txtShowPassword.setText(txtPassword.getText());
			txtPassword.setVisible(false);
			txtShowPassword.setVisible(true);
		}else {
			txtPassword.setVisible(true);
			txtPassword.setText(txtShowPassword.getText());
			txtShowPassword.setVisible(false);
		}
	}
	/**
	 * This method is responsible to load the 'xml' class and call 'initUI'
	 * @param changePane - Pane value with the current pane.
	 */
	public void load(Pane changePane) { // load pane to change pane.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CustomerRegistrationForm.fxml"));

		try {
			mainRegistrationPane = loader.load();
			changePane.getChildren().add(mainRegistrationPane);
			ObjectContainer.customerRegistrationController = loader.getController();
			ObjectContainer.customerRegistrationController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method is responsible to init buttons, texts and etc.
	 */
	private void initUI() {
		btnNext.setDefaultButton(true);
		customer = new Customer();
		vehiclePanes = new ArrayList<CustomerRegistrationController.VehiclePane>();
		vbVehicleContainer.setSpacing(5);
		currentStage = 1;
		creditCardPane.setVisible(false);
		changeStage(0);
		initErrorLabels();
		initChoiceBoxes();
		setButtonsImages("/images/eye_icon.png",btnShowPassword);
		txtShowPassword.setVisible(false);
		setBackgroundImage('+');
		limitTextFields();
		lblFuelCompanyError.setLayoutX(569);
		btnAddVehicle.setId("dark-blue");
		btnBack.setId("dark-blue");
		btnNext.setId("dark-blue");
		imgHelp.setVisible(false);
		setButtonsImages("/images/help_icon.png", btnHelp);
		btnHelp.hoverProperty().addListener((ov, oldValue, newValue) -> {
		    showHelp(newValue);
		});
	}	
	/**
	 * This method is responsible to show help to the user.
	 * @param isShow - boolaen value.
	 */
	public void showHelp(boolean isShow) {
		if(isShow) {
			lblCvvError.setText("");
			lblDateError.setText("");
			lblCardNumberError.setText("");
		}
		imgHelp.setVisible(isShow);
	}
	/**
	 * This method responsible to limit the text fields.
	 */
	private void limitTextFields() {
		ObjectContainer.setTextFieldLimit(txtUsername, 20);
		ObjectContainer.setTextFieldLimit(txtPassword, 20);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCustomerID, 9);
		ObjectContainer.setTextFieldLimit(txtCustomerName, 45);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtPhoneNumber, 10);
		ObjectContainer.setTextFieldLimit(txtEmail, 45);
		ObjectContainer.setTextFieldLimit(txtStreet, 45);
		ObjectContainer.setTextFieldLimit(txtCity, 30);
		ObjectContainer.setTextFieldLimit(txtStreet, 30);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCardNumber, 16);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCVV, 4);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtVehicleNumber, 8);
	}
	/**
	 * This method is responsible to set buttones with images.
	 * @param url - string value of the path image.
	 * @param btn - current button to set the icon.
	 */
	private void setButtonsImages(String url, Button btn) {		
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}
	/**
	 * This method is responsible to init choices box.
	 */
	private void initChoiceBoxes() {
		initPurchaseModels();
		initFuelCompaniesCB();
		initCustomerTypes();
		initFuelTypes();
		
		cbMonth.getItems().add("Month:");
		for(int i = 1; i <= 12; i++) {
			if(i < 10) cbMonth.getItems().add("0"+i);
			else cbMonth.getItems().add(""+i);
		}
		cbMonth.setValue(cbMonth.getItems().get(0));
		
		cbYear.getItems().add("Year:");
		for(int i = 2020; i <= 2030; i++) {
			cbYear.getItems().add(""+i);
		}
		cbYear.setValue(cbYear.getItems().get(0));
		
		cbCustomerType.getItems().clear();
		cbCustomerType.getItems().add("Choose customer type");
		cbCustomerType.getItems().add("Private");
		cbCustomerType.getItems().add("Company");
		cbCustomerType.setValue(cbCustomerType.getItems().get(0));
		
		String defualtValue = "Choose payment method";
		cbPaymentMethod.getItems().add(defualtValue);
		cbPaymentMethod.getItems().add("Credit Card");
		cbPaymentMethod.getItems().add("Cash");
		cbPaymentMethod.setValue(defualtValue);
		
		cbPaymentMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  boolean showCreditCardPane = cbPaymentMethod.getItems().get((Integer)number2).equals("Credit Card");
		    	  creditCardPane.setVisible(showCreditCardPane);
		      }
		    });
	}
	/**
	 * This method is responsible to init the fuel types on the choices box.
	 */
	private void initFuelTypes() {
		Message msg = new Message(MessageType.GET_FUEL_TYPES,"");
		ClientUI.accept(msg);
		
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		fuelTypes = response.get("fuelTypes").getAsJsonArray();
		
		cbVehicleFuelType.getItems().add("Choose type");
		for(int i = 0; i < fuelTypes.size();i++) {
			if(!fuelTypes.get(i).getAsString().equals("Home Heating Fuel")) {
				cbVehicleFuelType.getItems().add(fuelTypes.get(i).getAsString());				
			}
		}
		cbVehicleFuelType.setValue(cbVehicleFuelType.getItems().get(0));
	}
	/**
	 * This method is responsible to set an error icon.
	 * @param img - current image.
	 * @param url - string value path of the image.
	 */
	public void setErrorImage(ImageView img, String url) {
		Image image = new Image(getClass().getResource(url).toString());
		img.setImage(image);
	}
	/**
	 * This method is responsible to request from the server to get subscribe types.
	 * After that init the Json Array with the details and init the choices box.
	 */
	private void initCustomerTypes() {
		Message msg = new Message(MessageType.GET_SUBSCRIBE_TYPES, "");
		ClientUI.accept(msg);

		JsonObject json = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray array = json.get("subscribeTypes").getAsJsonArray();
		cbSubscribeType.getItems().clear();
		cbSubscribeType.getItems().add("Choose type");
		for (int i = 0; i < array.size(); i++) {
			cbSubscribeType.getItems().add(array.get(i).getAsString());
		}
		cbSubscribeType.setValue(cbSubscribeType.getItems().get(0));
	}
	/**
	 * This method is responsible to init fuel companies in the choices box.
	 */
	private void initFuelCompaniesCB() {
		JsonArray fuelCompanies = getAllFuelCompanies();
		
		String defualtValue = "Choose :";
		cbFuelCompany.getItems().add(defualtValue);
		cbFuelCompany2.getItems().add(defualtValue);
		cbFuelCompany3.getItems().add(defualtValue);
		
		for(int i = 0; i < fuelCompanies.size(); i++) {
			cbFuelCompany.getItems().add(fuelCompanies.get(i).getAsString());
			cbFuelCompany2.getItems().add(fuelCompanies.get(i).getAsString());
			cbFuelCompany3.getItems().add(fuelCompanies.get(i).getAsString());
		}
		
		cbFuelCompany.setValue(defualtValue);
		cbFuelCompany2.setValue(defualtValue);
		cbFuelCompany3.setValue(defualtValue);
		cbFuelCompany2.setVisible(false);
		cbFuelCompany3.setVisible(false);
		btnAddFuelCompany.setVisible(false);
	}
	/**
	 * This method is responsible to request from the server to get fuel companies names.
	 * @return - return Json array with the data.
	 */
	private JsonArray getAllFuelCompanies() {
		Message msg = new Message(MessageType.GET_FUEL_COMPANIES_NAMES,"");
		ClientUI.accept(msg);
		return ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies").getAsJsonArray();
	}
	/**
	 * This method is responsible to request from the server to get purchase models.
	 * After that init Json array with the data and init the choices box with the current details.
	 */
	private void initPurchaseModels() {
		Message msg = new Message(MessageType.GET_PURCHASE_MODELS, "");
		ClientUI.accept(msg);
		
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray types = response.get("purchaseModelTypes").getAsJsonArray();
		
		cbPurchaseModel.getItems().clear();
		cbPurchaseModel.getItems().add("Choose purchase model");
		for(int i = 0; i < types.size();i++) {
			cbPurchaseModel.getItems().add(types.get(i).getAsString());	
		}
		cbPurchaseModel.setValue(cbPurchaseModel.getItems().get(0));
		cbPurchaseModel.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  if((Integer)number2 != 0) {
		    		  lblPurchaseModelError.setText("");
		    		  lblFuelCompanyError.setText("");
		    	  }
		    	  if((Integer)number2 > 1) {
		    		  lblFuelCompanyError.setLayoutX(800);
		    	  }else {
		    		  lblFuelCompanyError.setLayoutX(569);
		    	  }
		    	  boolean showMoreCompaniesName = cbPurchaseModel.getItems().get((Integer)number2).equals(types.get(1).getAsString());
		    	  cbFuelCompany2.setVisible(showMoreCompaniesName);
		    	  if(showMoreCompaniesName == false) {
		    		cbFuelCompany3.setValue(cbFuelCompany3.getItems().get(0));
		    		btnAddFuelCompany.setLayoutX(600);
		  			cbFuelCompany3.setVisible(false);
		  			setBackgroundImage('+');
		    		  
		    	  }
		    	  cbFuelCompany2.setValue(cbFuelCompany2.getItems().get(0));
		    	  btnAddFuelCompany.setVisible(showMoreCompaniesName);
		      }
		    });
	}
	/**
	 * This method is responsible to init the error labels.
	 */
	private void initErrorLabels() {
		//stage 1
		lblUserNameError.setText("");
		lblPasswordError.setText("");
		lblIDError.setText("");
		lblCustomerNameError.setText("");
		lblCustomerTypeError.setText("");
		lblSubscribeTypeError.setText("");
		lblEmailError.setText("");
		lblPhoneError.setText("");
		lblCityError.setText("");
		lblStreetError.setText("");
		
		//stage 2
		lblCardNumberError.setText("");
		lblCvvError.setText("");
		lblDateError.setText("");
		lblPurchaseModelError.setText("");
		lblPaymentMethodError.setText("");
		lblFuelCompanyError.setText("");
		
		//stage 3
		lblVehicleError.setText("");
	}
	/**
	 * This class is responsible to create by dynamic pane new vehicle.
	 * @author oyomtov
	 * @version - Final
	 */
	class VehiclePane extends AnchorPane {
		public AnchorPane pane;
		public Button btnDeleteVehicle;
		public Label lblVehicleNumber,lblFuelType;
		public TextField txtVehicleNumber;
		public ChoiceBox<String> cbFuelType;
		
		public int width = 720;
		public int height = 60;
		
		public Vehicle vehicle;
		public VehiclePane(Vehicle vehicle) {
			this.vehicle = vehicle;
			initPane();
		}
		
		public void setBackgroundColor(String color) {
			this.setStyle("-fx-background-color:" + color + ";"
					+ "-fx-border-color:#77cde7;"
					+ "-fx-border-width:2px;");			
		}
		
		public void initPane() {
			this.setPrefSize(width, height);
			lblVehicleNumber = new Label();
			lblVehicleNumber.setMinSize(120, 20);
			lblVehicleNumber.relocate(15,15);
			lblVehicleNumber.setText("Vehicle Number : ");
			lblVehicleNumber.setStyle(""
					+ "-fx-text-fill:#ffffff;" 
					+ "-fx-font-weight: bold;"
					+ "-fx-font-size:14pt;"
					);
			this.getChildren().add(lblVehicleNumber);
			
			txtVehicleNumber = new TextField();
			txtVehicleNumber.setPrefSize(120, 30);
			txtVehicleNumber.relocate(175, 15);
			txtVehicleNumber.setText(vehicle.getVehicleNumber());
			this.getChildren().add(txtVehicleNumber);
			
			lblFuelType = new Label();
			lblFuelType.setMinSize(70, 20);
			lblFuelType.relocate(320,15);
			lblFuelType.setText("Fuel Type : ");
			lblFuelType.setStyle(""
					+ "-fx-text-fill:#ffffff;" 
					+ "-fx-font-weight: bold;"
					+ "-fx-font-size:14pt;"
					);
			this.getChildren().add(lblFuelType);
			
			cbFuelType = new ChoiceBox<String>();
			cbFuelType.setPrefSize(170, 30);
			cbFuelType.relocate(425, 15);
			for(int i = 0; i < fuelTypes.size(); i++) {
				if(!fuelTypes.get(i).getAsString().equals("Home Heating Fuel")) {
					cbFuelType.getItems().add(fuelTypes.get(i).getAsString());
				}
			}
			cbFuelType.setValue(cbVehicleFuelType.getValue());
			
			this.getChildren().add(cbFuelType);
			
			btnDeleteVehicle = new Button();
			btnDeleteVehicle.setPrefSize(30, 30);
			btnDeleteVehicle.relocate(690, 15);
			btnDeleteVehicle.setText("");
			setButtonsImages("/images/delete_icon.png", btnDeleteVehicle);
			btnDeleteVehicle.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					deleteVehicleFromList(VehiclePane.this.vehicle);
				}
			});
			this.getChildren().add(btnDeleteVehicle);
		}
		
	}
	
}
