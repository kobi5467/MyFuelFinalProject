package client.gui.marketingrepresentative;

import java.io.IOException;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class UpdateCustomerController {

	/******************************* Main Pane ********************************/
	@FXML
	private TextField txtEnterYourCustomerID;

	@FXML
	private Button btnSubmitCustomerID;

	@FXML
	private Label lblErrorText;
	
    @FXML
    private Label lblSucc2;

    @FXML
    private Label lblSucc1;

	/******************************* Details Pane ******************************/
	@FXML
	private Pane Pane2;

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

    @FXML
    private Label lblCity;

	/******************************* CreditCard Pane ******************************/
	@FXML
	private Pane Pane3;

	@FXML
	private Button btnUpdateCreditCard;

	@FXML
	private TextField txtCreditCard;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtDateValidation;
	
    @FXML
    private Label lblCreditCardNumber;

    @FXML
    private Label lblDateValidition;

    @FXML
    private Label lblCVV;

	@FXML
	private Button btnAddCreditCard;
	
	/************************ Edit Vehicles *************************************/

    @FXML
    private ScrollPane VehicleScrollPane;

	/******************************* Implements ********************************/

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

		Pane2.setVisible(false);
		Pane3.setVisible(false);
		txtUserNameUpdate.setDisable(true);
		txtCustomerNameUpdate.setDisable(true);
		txtEmailUpdate.setDisable(true);
		txtPhoneUpdate.setDisable(true);
		txtAddressUpdate.setDisable(true);
		txtCity.setDisable(true);
		lblErrorText.setText("");
		lblAddress.setText("");
		lblCity.setText("");
		lblCustomerName.setText("");
		lblEmail.setText("");
		lblPhoneNumber.setText("");
		lblUserName.setText("");
		lblSucc1.setText("");
		lblSucc2.setText("");
	}

	@FXML
	void onSubmit(ActionEvent event) {
		
		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(txtEnterYourCustomerID.getText()))
			lblErrorText.setText("Please insert only digits, try Again");
		if (customerIsExist(txtEnterYourCustomerID.getText())) {
			takeDataFromDB(txtEnterYourCustomerID.getText());
			Pane2.setVisible(true);
			lblErrorText.setText("");
		} else {
			lblErrorText.setText("The Customer ID not exist in the DB, try again");
			
		}
	}
	
	

	private void takeDataFromDB(String customerID) {
		txtUserNameUpdate.setText("");
		JsonObject customer = new JsonObject();
		customer.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_CUSTOMER_DETAILS_BY_ID,customer.toString());
		ClientUI.accept(msg);
		JsonObject customerDetails = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		txtUserNameUpdate.setText(customerDetails.get("userName").getAsString());
		txtCustomerNameUpdate.setText(customerDetails.get("name").getAsString());
		txtPhoneUpdate.setText(customerDetails.get("phoneNumber").getAsString());
		txtCity.setText(customerDetails.get("city").getAsString());
		txtAddressUpdate.setText(customerDetails.get("street").getAsString());
		txtEmailUpdate.setText(customerDetails.get("email").getAsString());
		txtCreditCard.setText(customerDetails.get("creditCardNumber").getAsString());
		txtCVV.setText(customerDetails.get("cvv").getAsString());
		txtDateValidation.setText(customerDetails.get("validationDate").getAsString());
	}

	private boolean customerIsExist(String customerID) {
		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		Message message = new Message(MessageType.CHECK_IF_CUSTOMER_EXIST,json.toString());
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
		lblSucc1.setText("");
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
		if(!checkEmpty(txtCity.getText())) {
			lblCity.setText("Fill City please");
			count--;
		}
		
		if (count == 4) {
			lblErrorText.setText("");
			lblAddress.setText("");
			lblCustomerName.setText("");
			lblEmail.setText("");
			lblPhoneNumber.setText("");
			lblUserName.setText("");
			lblCity.setText("");
			txtEmailUpdate.setDisable(true);
			txtPhoneUpdate.setDisable(true);
			txtAddressUpdate.setDisable(true);
			txtCity.setDisable(true);
			lblSucc1.setText("Customer Details Saved!");
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
		json.addProperty("creditCard", txtCreditCard.getText());
		json.addProperty("cvv", txtCVV.getText());
		json.addProperty("dateValidation", txtDateValidation.getText());
		Message msg = new Message(MessageType.UPDATE_CUSTOMER_DETAILS, json.toString());
		ClientUI.accept(msg);
		JsonObject customerUpdate = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		//System.out.println(customerUpdate.toString());

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
    	lblCreditCardNumber.setText("");
    	lblCVV.setText("");
    	lblDateValidition.setText("");
    	lblSucc2.setText("");
    	Pane3.setVisible(true);
    }
    
    @FXML
    void onUpdateCreditCard(ActionEvent event) {
    	 if(checkCreditCardValues(txtCreditCard.getText(),txtCVV.getText(),txtDateValidation.getText())) {
    		 	lblCreditCardNumber.setText("");
    	    	lblCVV.setText("");
    	    	lblDateValidition.setText("");
    	    	Pane3.setVisible(false);
    			lblSucc2.setText("Credit Card Details Updated!");
    	    	
    	 }
    }

    private boolean checkCreditCardValues(String creditCardNumber, String cvv, String dateValidation) {
		if(creditCardNumber.isEmpty() || creditCardNumber.length() < 8 || creditCardNumber.length() > 16) {
			lblCreditCardNumber.setText("Please enter legal CreditCard Number");
			return false;
		}
		if(cvv.isEmpty() || cvv.length() < 3 || cvv.length() > 4) {
			lblCVV.setText("Please enter legal CVV");
			return false;
		}
		if(dateValidation.isEmpty()) {
			lblDateValidition.setText("Please enter date validation");
			return false;
		}
		return true;
	}
    
    @FXML
    void onEditVehicle(ActionEvent event) {

    }

}
