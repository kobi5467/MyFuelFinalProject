package client.gui;

import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import srcTest.TestPanes;

public class HomeHeatingFuelController {

	@FXML
	private TextField txtAmount;

	@FXML
	private TextField txtDateSupply;

	@FXML
	private TextField txtStreet;

	@FXML
	private Button btnSubmit;

	@FXML
	private CheckBox boxUrgentOrder;

	@FXML
	private ChoiceBox<String> cbPaymentMethod;

	@FXML
	private TextField txtCardNumber;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtDateValidation;

	@FXML
	private TextField txtPaymentMethod;
	@FXML
	private Pane mainPane;

	// @FXML
	// private Label lblAmountError;
	@FXML
	private Label lblAmountErrorMsg;

	@FXML
	private Label lblStreetErrorMsg;

	@FXML
	private Label lblPaymentErrorMsg;

	@FXML
	private Label lblCardNumberErrorMsg;

	@FXML
	private Label lblCVVErrorMsg;

	@FXML
	private Label dateValidationError;

	@FXML
	private Label dateErrorMsg;
	@FXML
	private Text txtcvv;

	@FXML
	private Text txtCardNum;

	@FXML
	private Text txtDateVal;

	@FXML
	private DatePicker datePickerDateSupply;

	@FXML
	private DatePicker datePickerDateValidation;

	@FXML
	public void start(Stage stage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeHeatingFuelForm.fxml"));
		Pane root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TestPanes.controller = loader.getController();
		TestPanes.controller.initUI();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	void onSubmit(ActionEvent event) {
		Boolean flag=false;
		String amount = txtAmount.getText().trim();
		String street = txtStreet.getText().trim();
		Boolean isUrgentOrder = boxUrgentOrder.isSelected();
		String cardNubmer = txtCardNumber.getText().trim();
		String cvv = txtCVV.getText().trim();
		LocalDate dateSupplay = datePickerDateSupply.getValue();
		// System.out.println(supplayDate);
		String paymentMethod = cbPaymentMethod.getValue().trim();
		LocalDate suplayValidation = datePickerDateValidation.getValue();
		setErorLablesToNull();// Clean back Error message
		flag=homeHeatingFuelFormTest(amount, street, dateSupplay,isUrgentOrder, paymentMethod,cardNubmer, cvv,suplayValidation);

	}

	public void initUI() {
		showOptionOfCreditCardChoiseBox();
		setErorLablesToNull();
		showCreditCardFields(false);// Hide the credit card fields in initialize
		initDateValidation();
		updatePaymentFormOnPaymentMethodClick();
	
	}

	public Boolean homeHeatingFuelFormTest(String amount, String street, LocalDate dateSupplay,Boolean isUrgentOrder,String paymentMethod, String cardNumber,
			String CVV,LocalDate suplayValidation) {
		Boolean f1=true,f2=true,f3=true,f4=true,f5=true,f6=true;//flags
		f1=checkAmountField(amount);
		f2=checkStreetField(street);
		f3=checkDateSupplyField(dateSupplay);//need to check this method
		if(paymentMethod.equals("Credit Card")){
		f4=checkCardNumberField(cardNumber);
		f5=checkCVVField(CVV);
		//f6=
		}
		System.out.println((f1&&f2&&f4));
		return (f1&&f2&&f4);
		
		//checkCVVField(CVV);
	}
	//**************************************************Test fun**************************************************
	public Boolean checkAmountField(String amount) {
		int fuelAmount = -1;
		Boolean flag=true;
		try {
			fuelAmount = Integer.parseInt(amount);
			// Check positive amount:
			if (fuelAmount <= 0)
				lblAmountErrorMsg.setText("Invalide amount");
				flag=false;
		}
		catch (Exception e) {
			// Check if this field is empty:
			if (amount.isEmpty()) {
				lblAmountErrorMsg.setText("Please fill amount");
				flag=false;
			}
			// If the user put string in this filed:
			else {
				lblAmountErrorMsg.setText("only digits");
				flag=false;
				}
		}
		return flag;

	}

	public Boolean checkStreetField(String street) {
		Boolean flag=true;
		if (street.isEmpty())
			lblStreetErrorMsg.setText("Please fill street");
			flag=false;
		return flag;

	}

	public Boolean checkDateSupplyField(LocalDate dateSupplay) {
		Boolean flag=true;
			if(datePickerDateSupply.getValue()==null)
			dateErrorMsg.setText("Please fill date supply");
			flag=false;
		return flag;
	}
	public Boolean checkPaymentMethodField(String paymentMethod) {
		Boolean flag=true;
		if(paymentMethod.equals("Choose type"))
			lblPaymentErrorMsg.setText("Please fill date");
			flag=false;
		return flag;
	}
	public Boolean checkCardNumberField(String CardNumber) {
		int cardNum=-1;		
		Boolean flag=true;
			try {
				cardNum = Integer.parseInt(CardNumber);
				if (CardNumber.isEmpty()) {
					lblCardNumberErrorMsg.setText("Please fill card number");
					flag=false;
				}
				if( CardNumber.length()<=17&&CardNumber.length()>1) {
					lblCardNumberErrorMsg.setText("Please enter the full card number");
					flag=false;
				}
			} catch (Exception e) {								
				lblCardNumberErrorMsg.setText("only digits");
				flag=false;
				}
			return flag;
	}
	public Boolean checkCVVField(String CVV) {

		Boolean flag=true;
			try {
				//CVVuserInput = Integer.parseInt(CVV);
				if (CVV.isEmpty()) {
					lblCVVErrorMsg.setText("Please fill CVV");
					flag=false;
				}
				else if( CVV.length()<=3&&CVV.length()>=1) {
					lblCVVErrorMsg.setText("Please enter the CVV number");
					flag=false;
					
				}
			} catch (Exception e) {								
				lblCVVErrorMsg.setText("Only digits");
				flag=false;
				}
			return flag;

	}
	public Boolean checkDateValidationField(LocalDate suplayValidation) {
		Boolean flag=true;
			if(datePickerDateSupply.getValue()==null)
			dateErrorMsg.setText("Please fill date supply");
			flag=false;
		return flag;
	}
	
	
	//**************************************************End test fun**************************************************
	
	
	//**************************************************Initialize function**************************************************
	
	public void initDateValidation() {
		datePickerDateValidation.setShowWeekNumbers(false);
	}

	private void showCreditCardFields(boolean flag) {
		txtCardNumber.setVisible(flag);
		txtCardNum.setVisible(flag);
		txtCVV.setVisible(flag);
		txtcvv.setVisible(flag);
		datePickerDateValidation.setVisible(flag);
		txtDateVal.setVisible(flag);
	}

	public void showOptionOfCreditCardChoiseBox() {
		cbPaymentMethod.getItems().add("Choose type");
		cbPaymentMethod.getItems().add("Cash");
		cbPaymentMethod.getItems().add("Credit Card");
		cbPaymentMethod.setValue(cbPaymentMethod.getItems().get(0));
	}
	public void updatePaymentFormOnPaymentMethodClick() {
		cbPaymentMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = cbPaymentMethod.getItems().get((Integer) number2);

				if (value.equals("Choose type") || value.equals("Cash"))
					showCreditCardFields(false);
				else if (value.equals("Credit Card"))
					showCreditCardFields(true);
			}
		});

	}
	
	public void updateCheckBoxFormOnPaymentMethodClick() {
		
		
		
	}
	
	

	public void setErorLablesToNull() {
		lblAmountErrorMsg.setText(" ");
		lblStreetErrorMsg.setText(" ");
		dateErrorMsg.setText(" ");
		lblPaymentErrorMsg.setText("");
		lblCardNumberErrorMsg.setText("");
		lblCVVErrorMsg.setText("");
		dateValidationError.setText("");
		//lblCardNumberErrorMsg.setVisible(true);
	}

//**************************************************End Initialize function**************************************************

	public boolean setHomeHeatingFuelOrderInDB(String amount, String street, String dateSupplay,String isUrgentOrder,
						String paymentMethod, String cardNumber,String CVV,String dateValidaton) {
		
		JsonObject json = new JsonObject();
		json.addProperty("amount", amount);
		json.addProperty("street", street);
		json.addProperty("dateSupplay", dateSupplay);
		json.addProperty("isUrgentOrder", isUrgentOrder);
		//json.addProperty("paymentMethod", paymentMethod);
		//json.addProperty("cardNumber", cardNumber);
		//json.addProperty("CVV", CVV);
		//json.addProperty("dateValidaton", dateValidaton);

		Message msg = new Message(MessageType.ADD_HOME_HEATING_FUEL_ORDER,json.toString());
		ClientUI.clientController.handleMessageFromClient(msg);
		
		Message response = ObjectContainer.currentMessageFromServer;
		JsonObject responseJson = response.getMessageAsJsonObject();
		
		if(responseJson.get("isValid").getAsBoolean()) {
			isCorrect = true;
			userPermission = UserPermission.stringToEnumVal(responseJson.get("permission").getAsString());
		}
		return isCorrect;
	}
	
	
}
	
	
	