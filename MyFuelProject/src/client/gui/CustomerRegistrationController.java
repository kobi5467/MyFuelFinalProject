package client.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Customer;
import entitys.Message;
import entitys.Vehicle;
import entitys.enums.FuelType;
import entitys.enums.MessageType;
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
	private TextField txtDateValidation;

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
	private Button btnBack;

	@FXML
	private Button btnNext;

	/* Local variables */
	
	private int currentStage;
	private Customer customer;
	private JsonArray fuelTypes;
	private ArrayList<VehiclePane> vehiclePanes;
	
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

	private void setBackgroundImage(char c) {
		String url = "../../images/";
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

	@FXML
	void onBack(ActionEvent event) {
		//
		changeStage(-1);
	}

	@FXML
	void onNext(ActionEvent event) {
		boolean isValid = false;
		switch (currentStage) {
		case 1:
			// next1
			isValid = checkInputValidationStageOne();
			isValid = true;
			if(isValid) {
				changeStage(1);
			}
			break;
		case 2:
			//next2
			isValid = checkInputValidationStageTwo();
			isValid = true;
			if(isValid)
				changeStage(1);
			break;
			
		case 3:
			//submit
			break;
		
		
		default:
			break;
		}
	}
	
	private void updateObjectDetails() {
		customer.setUsername(txtUsername.getText());
		customer.setPassword(txtPassword.getText());
		customer.setName(txtCustomerName.getText());
		customer.setEmail(txtEmail.getText());
		customer.setPhoneNumber(txtPhoneNumber.getText());
		customer.setCity(txtCity.getText());
		customer.setStreet(txtStreet.getText());
		customer.setCustomerId(txtCustomerID.getText());
	}

	/************************************** Check validation of stage 1 **************************************/
	
	private boolean checkInputValidationStageOne() {
		initErrorLabels();
		boolean isValid = true;
		isValid = checkUserName(txtUsername.getText().trim());
		isValid = checkPassword(txtPassword.getText().trim()) && isValid;
		isValid = checkCustomerID(txtCustomerID.getText().trim()) && isValid;
		isValid = checkCustomerName(txtCustomerName.getText().trim()) && isValid;
		isValid = checkCustomerType(cbCustomerType.getValue().trim()) && isValid;
		isValid = checkEmail(txtEmail.getText().trim()) && isValid;
		isValid = checkPhoneNumber(txtPhoneNumber.getText().trim()) && isValid;
		isValid = checkCity(txtCity.getText().trim()) && isValid;
		isValid = checkStreet(txtStreet.getText().trim()) && isValid;
		
		return isValid;
	}

	private boolean checkStreet(String street) {
		if(street.isEmpty()) {
			lblStreetError.setText("Please fill street name and number");
			setErrorImage(imgStreetError, "../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgStreetError, "../../images/v_icon.png");
		return true;
	}

	private boolean checkCity(String city) {
		if(city.isEmpty()) {
			lblCityError.setText("Please fill city name");
			setErrorImage(imgCityError, "../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgCityError, "../../images/v_icon.png");
		return true;
	}

	private boolean checkCustomerType(String value) {
		if(value.equals(cbCustomerType.getItems().get(0))) {
			lblCustomerTypeError.setText("Please choose customer type");
			setErrorImage(imgCustomerTypeError, "../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgCustomerTypeError, "../../images/v_icon.png");
		return true; 
	}

	private boolean checkUserName(String userName) {
		String errorMessage = "";
		if (userName.isEmpty()) {
			errorMessage = "Please fill field..";
		} else if (userIsAlreadyExist(userName)) {
			errorMessage = "This user name is already exist";
		} else {
			setErrorImage(imgUsernameError, "../../images/v_icon.png");
			return true;
		}
		lblUserNameError.setText(errorMessage);
		setErrorImage(imgUsernameError, "../../images/error_icon.png");
		return false;
	}

	private boolean userIsAlreadyExist(String userName) {
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		Message message = new Message(MessageType.CHECK_IF_USER_EXIST,json.toString());
		ClientUI.accept(message);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("isExist").getAsBoolean();
	}

	private boolean checkPassword(String password) {
		String errorMessage = "";
		if (password.isEmpty()) {
			errorMessage = "please fill field..";
		} else if(password.length() < 6) {
			errorMessage = "password must contain at least 6 letters";
		}else {
			setErrorImage(imgPasswordError, "../../images/v_icon.png");
			return true;
		}
		lblPasswordError.setText(errorMessage);
		setErrorImage(imgPasswordError, "../../images/error_icon.png");
		return false;
	}

	private boolean checkCustomerID(String customerID) {
		String errorMessage = "";
		if (customerID.isEmpty()) {
			errorMessage = "please fill field..";
		} else if (customerIsExist(customerID)) {
			errorMessage = "customer ID is already exist";
		} else {
			setErrorImage(imgCustomerIDError, "../../images/v_icon.png");
			return true;
		}
		lblIDError.setText(errorMessage);
		setErrorImage(imgCustomerIDError, "../../images/error_icon.png");
		return false;
	}

	private boolean customerIsExist(String customerID) {
		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		Message message = new Message(MessageType.CHECK_IF_CUSTOMER_EXIST,json.toString());
		ClientUI.accept(message);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return response.get("isExist").getAsBoolean();
	}

	private boolean checkCustomerName(String customerName) {
		String errorMessage = "";
		if (customerName.isEmpty()) {
			errorMessage = "please fill field..";
		} else {
			setErrorImage(imgCustomerNameError, "../../images/v_icon.png");
			return true;
		}
		lblCustomerNameError.setText(errorMessage);
		setErrorImage(imgCustomerNameError, "../../images/error_icon.png");
		return false;
	}

	private boolean checkEmail(String email) {
		String errorMessage = "";
		if (email.isEmpty()) {
			errorMessage = "please fill field..";
		} else {
			setErrorImage(imgEmailError, "../../images/v_icon.png");
			return true;
		}
		lblEmailError.setText(errorMessage);
		setErrorImage(imgEmailError, "../../images/error_icon.png");
		return false;
	}

	private boolean checkPhoneNumber(String phoneNumber) {
		String errorMessage = "";
		if (phoneNumber.isEmpty()) {
			errorMessage = "please fill field..";
		} else if ((phoneNumber.length() != 10 || !ObjectContainer.checkIfStringContainsOnlyNumbers(phoneNumber))) {
			errorMessage = "Invalid input.. ";
		} else {
			setErrorImage(imgPhoneNumberError, "../../images/v_icon.png");
			return true;
		}
		lblPhoneError.setText(errorMessage);
		setErrorImage(imgPhoneNumberError, "../../images/error_icon.png");
		return false;
	}
	
	/****************************************** Check validation of stage 2 ***************************************/
	
	private boolean checkInputValidationStageTwo() {
		initErrorLabels();
		boolean isValid = true;
		isValid = checkPurchaseModel(cbPurchaseModel.getValue().trim());
		isValid = checkCompanyNames() && isValid;
		isValid = checkPaymentMethod(cbPaymentMethod.getValue().trim()) && isValid;
		return isValid;
	}
	
	private boolean checkPaymentMethod(String paymentMethod) {
		if(paymentMethod.equals(cbPaymentMethod.getItems().get(0))) {
			return false;
		}
		if(paymentMethod.equals("Cash")) {
			return true;
		}
		//Credit Card
		String creditCardNumber = txtCardNumber.getText().trim();
		String cvv = txtCVV.getText().trim();
		String dateValidation = txtDateValidation.getText().trim();
		return checkCreditCardValues(creditCardNumber,cvv,dateValidation);
	}

	private boolean checkCreditCardValues(String creditCardNumber, String cvv, String dateValidation) {
		if(creditCardNumber.isEmpty() || creditCardNumber.length() < 8 || creditCardNumber.length() > 16) {
			return false;
		}
		if(cvv.isEmpty() || cvv.length() < 3 || cvv.length() > 4) {
			return false;
		}
		if(dateValidation.isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean checkCompanyNames() {
		String comp1 = cbFuelCompany.getValue();
		String comp2 = cbFuelCompany2.getValue();
		String comp3 = cbFuelCompany3.getValue();
		String defaultValue = cbFuelCompany.getItems().get(0);
		if(cbPurchaseModel.getValue().equals(cbPurchaseModel.getItems().get(1))) {
			// only 1 company name
			return !comp1.equals(defaultValue);
		}
		if(cbFuelCompany3.isVisible()) {
			// 3 companies
			if(!comp1.equals(comp2) && !comp2.equals(comp3) && !comp1.equals(comp3) && 
				!comp1.equals(defaultValue) && !comp2.equals(defaultValue) && !comp3.equals(defaultValue)){
				return true;
			}
			return false;
		}
		// 2 companies
		if(!comp1.equals(comp2) && !comp2.equals(defaultValue) && !comp1.equals(defaultValue)) 
				return true;
		return false;
	}

	private boolean checkPurchaseModel(String purchaseModel) {
		return !purchaseModel.equals(cbPurchaseModel.getItems().get(0));
	}
	
	/******************************************* Check validation of stage 3 **************************************/
	
	private boolean checkInputValidationStageThree() {
		boolean isValid = true;
		if(customer.getVehicles().size() == 0) {
			isValid = false;
		}
		return isValid;
	}
	
	@FXML
	void addVehicle(ActionEvent event) {
		if(checkAddVehicleFields()) {
			Vehicle vehicle = new Vehicle(txtVehicleNumber.getText().trim(), 
					FuelType.stringToEnumVal(cbVehicleFuelType.getValue()), customer.getCustomerId());
			customer.getVehicles().add(vehicle);
			VehiclePane vehiclePane = new VehiclePane(vehicle);
			vehiclePanes.add(vehiclePane);
			updateVbChildren();
		}
	}

	private void updateVbChildren() {
		
		vbVehicleContainer.getChildren().clear();
		for(int i = 0; i < vehiclePanes.size(); i++) {
			String color = i % 2 == 0 ? "#0240FF" : "#024079";
			vehiclePanes.get(i).setBackgroundColor(color);
			vbVehicleContainer.getChildren().add(vehiclePanes.get(i));
		}
		
	}
	
	public boolean checkAddVehicleFields() {
		String vehicleNumber = txtVehicleNumber.getText().trim();
		String fuelType = cbVehicleFuelType.getValue().trim();
		
		if(vehicleNumber.isEmpty() || fuelType.equals(cbVehicleFuelType.getItems().get(0)) || 
				vehicleNumber.length() < 6) {
			System.out.println("Invalid inputs..");
			return false;
		}
		
		JsonObject vehicle = new JsonObject();
		vehicle.addProperty("vehicleNumber", vehicleNumber);
		Message msg = new Message(MessageType.CHECK_IF_VEHICLE_EXIST,vehicle.toString());
		ClientUI.accept(msg);
		
		boolean isExist = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("isExist").getAsBoolean();
		if(isExist) {
			System.out.println("This vehicle are already exist..");
		}
		return !isExist;
	}
	
	private void changeStage(int op) {
		
		currentStage = currentStage + op;
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

	private void initUI() {
		customer = new Customer();
		vehiclePanes = new ArrayList<CustomerRegistrationController.VehiclePane>();
		
		currentStage = 1;
		creditCardPane.setVisible(false);
		changeStage(0);
		initErrorLabels();
		initChoiceBoxes();
		setButtonsImages("../../images/eye_icon.png",btnShowPassword);
		txtShowPassword.setVisible(false);
		setBackgroundImage('+');
	}	

	private void setButtonsImages(String url, Button btn) {		
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}
	
	private void initChoiceBoxes() {
		initPurchaseModels();
		initFuelCompaniesCB();
		initCustomerTypes();
		initFuelTypes();
		
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

	public void setErrorImage(ImageView img, String url) {
		Image image = new Image(getClass().getResource(url).toString());
		img.setImage(image);
	}
	
	private void initCustomerTypes() {
		Message msg = new Message(MessageType.GET_CUSTOMER_TYPES,"");
		ClientUI.accept(msg);
		
		JsonObject json = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray array = json.get("customerTypes").getAsJsonArray();
		cbCustomerType.getItems().clear();
		cbCustomerType.getItems().add("Choose type");
		for(int i = 0; i < array.size();i++) {
			cbCustomerType.getItems().add(array.get(i).getAsString());
		}
		cbCustomerType.setValue(cbCustomerType.getItems().get(0));
	}

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

	private JsonArray getAllFuelCompanies() {
		
		Message msg = new Message(MessageType.GET_FUEL_COMPANIES_NAMES,"");
		ClientUI.accept(msg);
		return ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies").getAsJsonArray();
	}

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
		    	  boolean showMoreCompaniesName = cbPurchaseModel.getItems().get((Integer)number2).equals(types.get(1).getAsString());
		    	  cbFuelCompany2.setVisible(showMoreCompaniesName);
		    	  cbFuelCompany2.setValue(cbFuelCompany2.getItems().get(0));
		    	  btnAddFuelCompany.setVisible(showMoreCompaniesName);
		      }
		    });
	}

	private void initErrorLabels() {
		//stage 1
		lblUserNameError.setText("");
		lblPasswordError.setText("");
		lblIDError.setText("");
		lblCustomerNameError.setText("");
		lblCustomerTypeError.setText("");
		lblEmailError.setText("");
		lblPhoneError.setText("");
		lblCityError.setText("");
		lblStreetError.setText("");
		
		//stage 2
		lblCardNumberError.setText("");
		lblCvvError.setText("");
		lblDateError.setText("");
	}
	
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
			this.setStyle("-fx-background-color:" + color + ";");			
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
			setButtonsImages("../../images/delete_icon.png", btnDeleteVehicle);
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
