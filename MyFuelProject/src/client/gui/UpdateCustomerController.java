package client.gui;

import java.io.IOException;

import client.controller.ObjectContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UpdateCustomerController {

	/******************************* Main Pane ********************************/
	@FXML
	private TextField txtEnterYourCustomerID;

	@FXML
	private Button btnSubmitCustomerID;

	@FXML
	private Label lblErrorText;

	/******************************* Details Pane ******************************/
	@FXML
	private Pane Pane2;

	@FXML
	private Pane UpdateCustomer;

	@FXML
	private TextField txtUserNameUpdate;

	@FXML
	private TextField txtCustomerNameUpdate;

	@FXML
	private TextField txtEmailUpdate;

	@FXML
	private TextField txtPhoneUpdate;

	@FXML
	private TextField txtAddressUpdate;

	@FXML
	private Button btnAddVehicle;

	@FXML
	private Button btnEditDetails;

	@FXML
	private Button btnSaveDetails;

	@FXML
	private Label lblUserName;

	@FXML
	private Label lblCustomerName;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblPhoneNumber;

	@FXML
	private Label lblAddress;

	/******************************* CreditCard Pane ******************************/
	@FXML
	private Pane Pane3;

	@FXML
	private Button btnUpdateCreditCard;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private TextField btnCVV;

	@FXML
	private TextField btnDateValidation;

	@FXML
	private Button btnViewCreditCard;

	@FXML
	private Button btnAddCreditCard;

	/******************************* Implements ********************************/

	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("UpdateCustomerForm.fxml"));

		UpdateCustomer = loader.load();
		ObjectContainer.updateCustomerController = loader.getController();
		ObjectContainer.updateCustomerController.initUI();

		Scene scene = new Scene(UpdateCustomer);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void initUI() {

		Pane2.setVisible(false);
		Pane3.setVisible(false);
		txtUserNameUpdate.setDisable(true);
		txtCustomerNameUpdate.setDisable(true);
		txtEmailUpdate.setDisable(true);
		txtPhoneUpdate.setDisable(true);
		txtAddressUpdate.setDisable(true);
		lblErrorText.setText("");
		lblAddress.setText("");
		lblCustomerName.setText("");
		lblEmail.setText("");
		lblPhoneNumber.setText("");
		lblUserName.setText("");
	}

	@FXML
	void onSubmit(ActionEvent event) {
		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(txtEnterYourCustomerID.getText()))
			lblErrorText.setText("Please insert only digits, try Again");
		if (checkInputValidationMainPane()) {
			Pane2.setVisible(true);
			lblErrorText.setText("");
			txtEnterYourCustomerID.setText("");
		} else {
			lblErrorText.setText("The Customer ID not exist in the DB, try Again");
		}
	}

	private boolean checkInputValidationMainPane() {
		boolean isValid = true;
		// Check from DB
		// isValid = checkCustomerID(txtEnterYourCustomerID.getText().trim());
		return isValid;
	}

	private boolean checkCustomerID() {
		// Check from DB
		return true;
	}

	@FXML
	void onEditDetails(ActionEvent event) {
		txtUserNameUpdate.setDisable(false);
		txtCustomerNameUpdate.setDisable(false);
		txtEmailUpdate.setDisable(false);
		txtPhoneUpdate.setDisable(false);
		txtAddressUpdate.setDisable(false);
	}

	@FXML
	void onSaveDetails(ActionEvent event) {
		int count = 5;
		// checkUserNameExistFromDB - KOBI
		if (!checkCustomerName(txtCustomerNameUpdate.getText()) || !checkEmpty(txtCustomerNameUpdate.getText())) {
			count--;
			lblCustomerName.setText("Enter only characters");
		}
		if (!checkEmpty(txtEmailUpdate.getText())) {
			lblEmail.setText("Please Enter Email");
			count--;
		}
		if (!checkPhoneNumber(txtPhoneUpdate.getText()) || !checkEmpty(txtPhoneUpdate.getText())) {
			lblPhoneNumber.setText("Illegal phone number, try again");
			count--;
		}
		if (!checkEmpty(txtAddressUpdate.getText())) {
			lblAddress.setText("Fill Address please");
			count--;
		}
		if (count == 5) {
			lblErrorText.setText("");
			lblAddress.setText("");
			lblCustomerName.setText("");
			lblEmail.setText("");
			lblPhoneNumber.setText("");
			lblUserName.setText("");
			txtUserNameUpdate.setDisable(true);
			txtCustomerNameUpdate.setDisable(true);
			txtEmailUpdate.setDisable(true);
			txtPhoneUpdate.setDisable(true);
			txtAddressUpdate.setDisable(true);
			//Change the Strings in the DB
		}
	}

	private boolean checkEmpty(String text) {
		return (!text.isEmpty());
	}

	private boolean checkPhoneNumber(String phoneNumber) {
		if (phoneNumber.length() < 9 || phoneNumber.length() > 10 || phoneNumber.isEmpty())
			return false;
		return true;
	}

	private boolean checkCustomerName(String customerName) {
		for (int i = 0; i < customerName.length(); i++) {
			if (!Character.isAlphabetic(customerName.charAt(i)))
				return false;
		}
		return true;
	}

}
