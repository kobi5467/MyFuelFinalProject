package client.gui;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.User;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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

	/******************************* STAGE 2 *******************************/
	
	@FXML
	private Pane stageTwo;

	@FXML
	private ChoiceBox<String> choicePurchase;

	@FXML
	private ChoiceBox<String> choicePayment;

	@FXML
	private TextField txtCardNumber;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtDateValidation;

	@FXML
	private Button btnHelp;

	@FXML
	private ChoiceBox<String> choiceFuelCompany;

	@FXML
	private Label lblCvvError;

	@FXML
	private Label lblCardNumberError;

	@FXML
	private Label lblDateError;

	@FXML
	private ChoiceBox<String> cbFuelCompany2;

	@FXML
	private ChoiceBox<String> cbFuelCompany3;

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
	private ChoiceBox<String> cbVehicleFulType;

	@FXML
	private TextField txtDeleteVehicleNumber;

	@FXML
	private Button btnDeleteVehicle;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnNext;

	/* Local variables */
	
	private int currentStage;
	private User user = new User();
	
	@FXML
	void addFuelCompanyCB(ActionEvent event) {

	}

	@FXML
	void addVehicle(ActionEvent event) {

	}

	@FXML
	void deleteVehicle(ActionEvent event) {

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
			isValid = checkInputValidationStageOne();
			if(isValid) {
				changeStage(1);
			}
			break;
		default:
			break;
		}
	}
	
	private boolean checkInputValidationStageOne() {
		initErrorLabels();
		boolean isValid = true;
		isValid = checkUserName(txtUsername.getText().trim()) && isValid;
		isValid = checkPassword(txtPassword.getText().trim()) && isValid;
		isValid = checkCustomerID(txtCustomerID.getText().trim()) && isValid;
		isValid = checkCustomerName(txtCustomerName.getText().trim()) && isValid;
		isValid = checkEmail(txtEmail.getText().trim()) && isValid;
		isValid = checkPhoneNumber(txtPhoneNumber.getText().trim()) && isValid;
		return isValid;
	}

	private boolean checkUserName(String userName) {
		String errorMessage = "";
		if (userName.isEmpty()) {
			errorMessage = "Please fill field..";
		} else if (userIsAlreadyExist(userName)) {
			errorMessage = "This user name is already exist";
		} else {
			return true;
		}
		lblUserNameError.setText(errorMessage);
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
			return true;
		}
		lblPasswordError.setText(errorMessage);
		return false;
	}

	private boolean checkCustomerID(String customerID) {
		String errorMessage = "";
		if (customerID.isEmpty()) {
			errorMessage = "please fill field..";
		} else if (customerIsExist(customerID)) {
			errorMessage = "customer ID is already exist";
		} else {
			return true;
		}
		lblIDError.setText(errorMessage);
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
			return true;
		}
		lblCustomerNameError.setText(errorMessage);
		return false;
	}

	private boolean checkEmail(String email) {
		String errorMessage = "";
		if (email.isEmpty()) {
			errorMessage = "please fill field..";
		} else {
			return true;
		}
		lblEmailError.setText(errorMessage);
		return false;
	}

	private boolean checkPhoneNumber(String phoneNumber) {
		String errorMessage = "";
		if (phoneNumber.isEmpty()) {
			errorMessage = "please fill field..";
		} else if ((phoneNumber.length() != 10 || !ObjectContainer.checkIfStringContainsOnlyNumbers(phoneNumber))) {
			errorMessage = "Invalid input.. ";
		} else {
			return true;
		}
		lblPhoneError.setText(errorMessage);
		return false;
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
			currentStage = currentStage - op;
			changeStage(0);
			break;
		}
		
		
	}

	@FXML
	void onShowPassword(ActionEvent event) {

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
		currentStage = 1;
		initErrorLabels();
		btnBack.setVisible(false);
	}
	
	private void initErrorLabels() {
		//stage 1
		lblUserNameError.setText("");
		lblPasswordError.setText("");
		lblIDError.setText("");
		lblCustomerNameError.setText("");
		lblEmailError.setText("");
		lblPhoneError.setText("");
		//stage 2
		lblCardNumberError.setText("");
		lblCvvError.setText("");
		lblDateError.setText("");
	}
	
	
}
