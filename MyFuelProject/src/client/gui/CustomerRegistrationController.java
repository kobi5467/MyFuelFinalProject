package client.gui;

import java.io.IOException;

import client.controller.ObjectContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerRegistrationController {

	// ************************************************Main**********************************************
	@FXML
	private Pane mainRegistrationPane;

	// ************************************************Stage 1*******************************************
	@FXML
	private Pane stageOne;

	@FXML
	private TextField txtUsername;

	@FXML
	private TextField txtPassword;

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
	private Button btnNext1;

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

	// ************************************************Stage 2*******************************************
	@FXML
	private Pane stageTwo;

	@FXML
	private Button btnPlus;

	@FXML
	private ChoiceBox<?> choicePurchase;

	@FXML
	private ChoiceBox<?> choicePayment;

	@FXML
	private TextField txtCardNumber;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtDateValidation;

	@FXML
	private Button btnHelp;

	@FXML
	private Button btnNext2;

	@FXML
	private Button btnBack1;

	@FXML
	private ChoiceBox<?> choiceFuelCompany;

	@FXML
	private Label lblCvvError;
	
    @FXML
    private Label lblCardNumberError;

    @FXML
    private Label lblDateError;

	// ************************************************Stage 3*******************************************
	@FXML
	private Pane stageThree;

	@FXML
	private Button btnAddVehicle3;

	@FXML
	private Button btnRemoveVehicle3;

	@FXML
	private Button btnRegister3;

	@FXML
	private Button btnBack2;

	@FXML
	private TextField txtVehicleNumber3;

	@FXML
	private TextField txtFuelType3;

	@FXML
	private TextField textVehicleNumber3;

	@FXML
	private ChoiceBox<?> choiceFuelType3;

	
	// ************************************************************Implements*************************************************
	@FXML
	public void start(Stage stage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CustomerRegistration.fxml"));
		Pane root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void onNext1(ActionEvent event) {
		String userName = txtUsername.getText();
		String password = txtPassword.getText();
		String customerID = txtCustomerID.getText();
		String customerName = txtCustomerName.getText();
		String email = txtEmail.getText();
		String phoneNumber = txtPhoneNumber.getText();
		String city = txtCity.getText();
		String streetC = txtStreet.getText();

		//checkUserName(userName);
		checkPassword(password);
		checkCustomerID(customerID);
		//checkCustomerName(customerName);
		if(!checkEmail(email))
			lblEmailError.setText("Invalid Email, Enter valid email");
		checkPhoneNumber(phoneNumber);
	}

	public void checkPassword(String password) {
		if (password.length() < 8)
			lblPasswordError.setText("Enter 8 or more char/digits");
	}
	
	public void checkCustomerID(String customerID) {
		if (customerID.length() != 9)
			lblIDError.setText("Invalid ID, Enter valid ID");
	}
	
	public boolean checkEmail(String email) {
		boolean flag = false;
		for(int i=0; i<email.length(); i++) {
			if(email.charAt(i) == '@')
				flag = true;
		}
		return flag;
	}
	
	public void checkPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() != 10)
			lblPhoneError.setText("Invalid Phone Number, Enter valid Phone Number");
	}

	
	
	// ************************************************************Stage 2*************************************************
	@FXML
	void onNext2(ActionEvent event) {
		// Add all choice models
		String cardNubmerC = txtCardNumber.getText();
		String cvvC = txtCVV.getText();
		String dateValidationC = txtDateValidation.getText();

		if(!checkCardNumber(cardNubmerC))
			lblCardNumberError.setText("Iilegal Card Number, try again");
		checkCVV(cvvC);
		if(!checkDateValidition(dateValidationC))
			lblDateError.setText("Press Date Like that - mm:yy");
	}

	public void initUI() {
		// showCreditCardFields(false);
		// showOptionOfCreditCardChoiseBox();
	}

	/*
	 * public void showOptionOfCreditCardChoiseBox() {
	 * cbPaymentMethod.getItems().add("Choose type");
	 * cbPaymentMethod.getItems().add("Cash");
	 * cbPaymentMethod.getItems().add("Credit Card"); //
	 * cbPaymentMethod.setValue(cbPaymentMethod.getItems().get(0)); }
	 */
	private void showCreditCardFields(boolean flag) {
		txtCardNumber.setVisible(flag);
		txtCVV.setVisible(flag);
		txtDateValidation.setVisible(flag);

	}

	public boolean checkCardNumber(String cardNumberC) {
		boolean flag = true;
		if (cardNumberC.length() < 8 || cardNumberC.length() > 16) {
			return !flag;
		} else {
			for (int i = 0; i < cardNumberC.length(); i++) {
				if (cardNumberC.charAt(i) < '0' || cardNumberC.charAt(i) > '9') {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	public void checkCVV(String cvvC) {
		int cvvInt = Integer.parseInt(cvvC);
		if (cvvInt < 100 || cvvInt > 999)
			lblCvvError.setText("Only 3 digits numbers");
	}

	public boolean checkDateValidition(String dateValidationC) {
		if (dateValidationC.length() != 5)
			return false;
		if(dateValidationC.charAt(2) != ':')
			return false;
		return true;
	}

	@FXML
	void checkPaymentMethod(InputMethodEvent event) {

	}

	void onNext3(ActionEvent event) {
		// Add all choice models
		String vehicleNumber = txtVehicleNumber3.getText();
		String fuelType = txtFuelType3.getText();

	}

	public Pane getMainPane() {
		return mainRegistrationPane;
	}

	public void load(Pane pane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CustomerRegistrationForm.fxml"));

		try {
			mainRegistrationPane = loader.load();
			pane.getChildren().add(mainRegistrationPane);
			ObjectContainer.customerRegistrationController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
