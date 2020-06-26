package client.gui.customer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import client.gui.marketingrepresentative.SaleTemplatePane;
import entitys.Customer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * This class is for Home heating fuel order. The first screen contain all the
 * process of the home heating fuel order. The second screen contain the order
 * details.
 * 
 * @author OrMamamn
 * @version final
 */
public class HomeHeatingFuelController {
	@FXML
	private Text txtSlash;
	@FXML
	private Text txtFuelCompany;
	@FXML
	private Pane mainPane;
	@FXML
	private Pane panePurchaseSuccessful;
	@FXML
	private Text txtOrderNumber;
	@FXML
	private Text txtDeliveryDetails;
	@FXML
	private Text txtDateSupplyOnPurchaseDetails;
	@FXML
	private Text paymentMethodOnPurchaseDetails;
	@FXML
	private Text totalPriceOnPurchaseDetails;
	@FXML
	private Label lblOrderNumberOnPurchasePain;
	@FXML
	private Label lblDateSupplyInPurchasePane;
	@FXML
	private Label lblPaymentMethodOnPurchasePane;
	@FXML
	private Label lblTotalPriceOnPurchasePane;
	@FXML
	private Text txtAdress;
	@FXML
	private Label lblAdress;
	@FXML
	private Text txtPurchaseSuccessful;
	@FXML
	private Label lblUserFirstName;
	@FXML
	private Text txtPurchaseSuccessful2;
	@FXML
	private ImageView imgPurchaseSuccessful;
	@FXML
	private Pane panePurchaseInputs;
	@FXML
	private TextField txtAmount;
	@FXML
	private TextField txtStreet;
	@FXML
	private Button btnSubmit;
	@FXML
	private CheckBox boxUrgentOrder;
	@FXML
	private ChoiceBox<String> cbPaymentMethod;
	@FXML
	private TextField txtCVV;
	@FXML
	private Text txtcvv;
	@FXML
	private Text txtCardNum;
	@FXML
	private Text txtDateVal;
	@FXML
	private CheckBox cbRemeberCreditCard;
	@FXML
	private Text txtRemeberCreditCard;
	@FXML
	private DatePicker datePickerDateSupply;
	@FXML
	private ChoiceBox<String> cbCreditCardMonthValidation;
	@FXML
	private ChoiceBox<String> cbCreditCardYearValidation;
	@FXML
	private Text txtTotalPrice;
	@FXML
	private Label lbltotalPriceAfterDiscount;
	@FXML
	private Label lblSubTotalPriceBeforeDiscount;
	@FXML
	private Label lblShippingRate;
	@FXML
	private Label lblDiscountRate;
	@FXML
	private Label lblCurrentPricePerLitter;
	@FXML
	private TextField txtCity;
	@FXML
	private ImageView imgAmountError;
	@FXML
	private ImageView imgStreetError;
	@FXML
	private ImageView imgCityError;
	@FXML
	private ImageView imgDateSupllyError;
	@FXML
	private ImageView imgPaymentMethodError;
	@FXML
	private ImageView imgCVVError;
	@FXML
	private ImageView imgCardNumberCVV;
	@FXML
	private ImageView imgDateValidationError;
	@FXML
	private ImageView imgfuelCompanyErrMsg;
	@FXML
	private TextField txtCardNumber;
	@FXML
	private Text txtOrderDetailsTitle;
	@FXML
	private ChoiceBox<String> cbfuelCompany;
	@FXML
	private Text txtOrderSummaryTitle;
	@FXML
	private Text textUrgentOrder;
	@FXML
	private Label lblUrgentOrder;
    @FXML
    private Button btnHelp;
    @FXML
    private ImageView imgHelpCVV;
	
	public Boolean isPressed = false;
	public String amount;
	public String street;
	public String isUrgentOrder;
	public String dateSuplly;
	public String paymentMethod;
	public String cardNumber;
	public String cvv;
	public String creditCardDateValidation;
	public String city;
	public String customerId;
	public String orderId;
	public String totalPrice;
	public String orderDate;
	public String saleTemplateName;
	public String customerName;
	public String fuelCompany;
	public JsonObject creditCard;
	public Boolean rememberCardPressed = false;
	public double discountrate = 0;
	public float pricePerLitter;
	public double shippingCost = 10;

	
	public IHomeHeatingFuelDBManager iHomeHeatingFuelDBManager;
	
	public HomeHeatingFuelController() {
		iHomeHeatingFuelDBManager = new IHomeHeatingFuelDBManager() {
			
			@Override
			public boolean submitHomeHeatingFuelOrder(JsonObject json) {
				Message msg = new Message(MessageType.SUBMIT_HOME_HEATING_FUEL_ORDER, json.toString());
				ClientUI.accept(msg);
				return true;
			}
			
			@Override
			public int getLastOrderID(JsonObject json) {
				return getOrderIdFromDB(json);
			}
			
			@Override
			public float getPricePerLitter() {
				return getFuelObjectByType("Home Heating Fuel");
			}
			
			@Override
			public String[] getFuelCompaniesByCustomerID(String customerID) {
				JsonObject json = new JsonObject();
				json.addProperty("customerID", customerID);
				Message msg = new Message(MessageType.GET_FUEL_COMPANIES_BY_CUSTOMER_ID, json.toString());
				ClientUI.accept(msg);
				String[] fuelCompanies = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies")
						.getAsString().split(",");
				return fuelCompanies;
			}
			
			@Override
			public float getCurrentRunningSaleTemplateDiscount() {
				return getCurrentSaleDiscount();
			}
			
			@Override
			public JsonObject getCreditCardByCustomerID() {
				return getCreditCard();
			}
		};
	}
	
	
	
	/**
	 * This function gets HomeHeatingFuelForm.fxml and load it to the main pain.
	 * 
	 * @param changePane - change the exist pain to HomeHeatingFuelForm.fxml
	 */
	@FXML
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeHeatingFuelForm.fxml"));
		try {
			mainPane = loader.load();
			changePane.getChildren().add(mainPane);
			ObjectContainer.homeHeatingFuelController = loader.getController();
			ObjectContainer.homeHeatingFuelController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This function initialize home heating fuel order form
	 */
	public void initUI() {
		ObjectContainer.setButtonImage("/images/help_icon.png", btnHelp);
		ObjectContainer.setImageBackground("/images/cvvHelp.png", imgHelpCVV);
		imgHelpCVV.setVisible(false);
		btnHelp.hoverProperty().addListener((ov, oldValue, newValue) -> {
		    showHelp(newValue);
		});
		btnHelp.setVisible(false);
		textUrgentOrder.setVisible(false);
		lblUrgentOrder.setVisible(false);
		btnSubmit.setId("dark-blue");
		initCreditCardFields();
		getFuelCompaniesByCustomerID();
		limitTextFields();
		panePurchaseSuccessful.setVisible(false);
		showCreditCardFields(false);
		initOrderSummary();
		updatePaymentFormOnPaymentMethodClick();
		creditCard = iHomeHeatingFuelDBManager.getCreditCardByCustomerID();
	}

	/**
	 * This method is responsible to show help to the user.
	 * @param isShow - boolaen value.
	 */
	public void showHelp(boolean isShow) {
		if(isShow) {
			imgCVVError.setVisible(false);
		}
		imgHelpCVV.setVisible(isShow);
	}
	
	/**
	 * This function activates the second screen of home heating fuel order and
	 * close the first screen. The second screen includes the details of the order:
	 * Order id,shipping date,Adders for shipping,Date supply.
	 */
	public void showPurchaseDetails() {
		panePurchaseInputs.setVisible(false);
		panePurchaseSuccessful.setVisible(true);
		lblOrderNumberOnPurchasePain.setText(this.orderId);
		lblAdress.setText(this.city + "/" + this.street);
		lblDateSupplyInPurchasePane.setText(this.dateSuplly);
		lblPaymentMethodOnPurchasePane.setText(this.paymentMethod);
		lblTotalPriceOnPurchasePane.setText(this.totalPrice);
		lblUserFirstName.setText(this.customerName);
//		setErrorImage(imgPurchaseSuccessful, "/images/purchase_successful.png");
		ObjectContainer.setImageBackground("/images/purchase_successful.png", imgPurchaseSuccessful);
	}

	/**
	 * This function displays the credit information fields if the user chosen this
	 * payment option. Otherwise, this fields will be hidden.
	 * 
	 * @param flag if the flag is true we will display the credit card fields, else
	 *             the function hide this fields.
	 */
	private void showCreditCardFields(boolean flag) {
		txtCardNumber.setVisible(flag);
		txtCardNum.setVisible(flag);
		txtCVV.setVisible(flag);
		txtcvv.setVisible(flag);
		txtDateVal.setVisible(flag);
		btnHelp.setVisible(flag);
		cbCreditCardMonthValidation.setVisible(flag);
		cbCreditCardYearValidation.setVisible(flag);
		imgCardNumberCVV.setVisible(flag);
		imgCVVError.setVisible(flag);
		imgDateValidationError.setVisible(flag);
		txtSlash.setVisible(flag);
		txtRemeberCreditCard.setVisible(flag);
		cbRemeberCreditCard.setVisible(flag);
	}

	/**
	 * This function initialize the payment method choice box and date validation
	 * choice box.
	 */
	public void initCreditCardFields() {
		cbPaymentMethod.getItems().add("Choose type");
		cbPaymentMethod.getItems().add("Cash");
		cbPaymentMethod.getItems().add("Credit Card");
		cbPaymentMethod.setValue(cbPaymentMethod.getItems().get(0));
		cbCreditCardMonthValidation.getItems().add("Month:");
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				cbCreditCardMonthValidation.getItems().add("0" + i);
			else
				cbCreditCardMonthValidation.getItems().add("" + i);
		}
		cbCreditCardMonthValidation.setValue(cbCreditCardMonthValidation.getItems().get(0));
		cbCreditCardYearValidation.getItems().add("Year:");
		for (int i = 2020; i <= 2030; i++) {
			cbCreditCardYearValidation.getItems().add("" + i);
		}
		cbCreditCardYearValidation.setValue(cbCreditCardYearValidation.getItems().get(0));
	}

	/**
	 * This function initialize the Order Summary fields.
	 */
	public void initOrderSummary() {
		lblSubTotalPriceBeforeDiscount.setText("0.00 $");
		lblShippingRate.setText("0.00 $");
		lblDiscountRate.setText("0.00 %");
		lblShippingRate.setText(shippingCost + " $");
		lbltotalPriceAfterDiscount.setText("0.00 $");
		pricePerLitter = iHomeHeatingFuelDBManager.getPricePerLitter();
		lblCurrentPricePerLitter.setText(String.valueOf(pricePerLitter) + " $");
	}

	/**
	 * This function check if all the fields on the first screen is correct and show
	 * message to the user to approve his order. If one of the fields isn't correct
	 * this function show to the user error message. If all the fields is correct ,
	 * the function send a request to the server controller for insert the order
	 * into our data base.
	 * 
	 * @param event- while the user click on button 'submit'
	 */
	@FXML
	void onSubmit(ActionEvent event) {
		Boolean flag = homeHeatingFuelFormTest();
		if (!flag)
			ObjectContainer.showMessage("Error", "Error", "Please fill all the field currect!");
		else {
			ObjectContainer.showMessage("yes_no", "Place order", " Do you want to place your order?");
			flag = ObjectContainer.yesNoMessageResult;
		}
		if (flag) {
			JsonObject json = createJsonObjectForOrder();

			if (paymentMethod.equals("Credit Card")) {
				json.addProperty("updateExistsCreditCard", "true");
				json.addProperty("customerID", this.customerId);
				json.addProperty("creditCard", this.cardNumber);
				json.addProperty("cvv", this.cvv);
				json.addProperty("dateValidation", this.creditCardDateValidation);
				json.addProperty("updateExistsCreditCard", "false");
				json.addProperty("savePaymentDetails", "false");
				if (creditCard.get("creditCardNumber") == null && this.rememberCardPressed) {
					json.addProperty("savePaymentDetails", "true");
					json.addProperty("updateExistsCreditCard", "false");
				} else if (this.rememberCardPressed) {
					json.addProperty("updateExistsCreditCard", "true");
					json.addProperty("savePaymentDetails", "false");
				}
			}
//			Message msg = new Message(MessageType.SUBMIT_HOME_HEATING_FUEL_ORDER, json.toString());
//			ClientUI.accept(msg);
			iHomeHeatingFuelDBManager.submitHomeHeatingFuelOrder(json);
			iHomeHeatingFuelDBManager.getLastOrderID(json);
			showPurchaseDetails();
		}
	}

	/**
	 * This function put the current date in date supply datePicker field of home
	 * heating fuel order and disable the option to the user for select a date.
	 * Another click will give to the user to select date supply. after click the
	 * function update the current price for the user.
	 * 
	 * @param event while the user press on choice box of "Urgent order"
	 */
	@FXML
	void checkBoxPressed(ActionEvent event) {
		this.isPressed = !this.isPressed;
		if (this.isPressed) {
			datePickerDateSupply.setEditable(true);
			datePickerDateSupply.setValue(LocalDate.now());
			lblDiscountRate.setText(String.valueOf(this.discountrate));
		} else {
			datePickerDateSupply.setEditable(false);
			datePickerDateSupply.setValue(null);
			lblDiscountRate.setText(String.valueOf(this.discountrate));
		}
		if (txtAmount.getText().isEmpty())
			return;
		calcTotalPrice(Float.parseFloat(txtAmount.getText().trim()));
	}

	/**
	 * This function change the value of "rememberCardPressed" variable from false
	 * to true for saving the user credit card details in data base.
	 * 
	 * @param event while the user press on choice box for remember his credit card.
	 */
	@FXML
	void rememberCardPressed(ActionEvent event) {
		this.rememberCardPressed = !this.rememberCardPressed;
	}

	/**
	 * This function update order summary labels.
	 * 
	 * @param event after insert digit to amount field.
	 */
	@FXML
	void updateOrderSummary(KeyEvent event) {
		if (checkAmountField(false,txtAmount.getText().trim()))
			calcTotalPrice(Float.parseFloat(txtAmount.getText().trim()));
	}

	/**
	 * This function displays the credit card fields if the user choose to pay in
	 * credit card option. If the user register with credit card, we set his credit
	 * card details to the credit card field.
	 */
	public void updatePaymentFormOnPaymentMethodClick() {
		cbPaymentMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = cbPaymentMethod.getItems().get((Integer) number2);
				if (value.equals("Choose type") || value.equals("Cash"))
					showCreditCardFields(false);
				else if (value.equals("Credit Card")) {
					if (creditCard.get("creditCardNumber") != null) {
						txtCardNumber.setText(creditCard.get("creditCardNumber").getAsString());
						txtCVV.setText(creditCard.get("cvv").getAsString());
						cbCreditCardMonthValidation
								.setValue(creditCard.get("validationDate").getAsString().split("/")[0]);
						cbCreditCardYearValidation
								.setValue(creditCard.get("validationDate").getAsString().split("/")[1]);
					}
					showCreditCardFields(true);
				}
			}
		});

	}

	/**
	 * This function coordinates all the fields tests.In case the user choose to pay
	 * in credit card, the function will check the fields that contain to credit
	 * card.
	 * 
	 * @return if one of the tests fails the function will return false,, else the
	 *         function will return true.
	 * 
	 */
	public Boolean homeHeatingFuelFormTest() {
		Boolean flag = true;
		flag = checkAmountField(true,txtAmount.getText().trim());
		flag = checkStreetField(txtStreet.getText()) && flag;
		flag = checkCityField(txtCity.getText()) && flag;
		flag = checkDateSupplyField(datePickerDateSupply.getValue()) && flag;
		flag = checkPaymentMethodField(cbPaymentMethod.getValue()) && flag;
		flag = checkFuelCompanyField(cbfuelCompany.getValue().trim()) && flag;
		if (cbPaymentMethod.getValue().trim().equals("Credit Card")) {
			flag = checkCardNumberField(txtCardNumber.getText().trim()) && flag;
			flag = checkCVVField(txtCVV.getText()) && flag;
			flag = checkDateValidationField(cbCreditCardMonthValidation.getValue().trim(),cbCreditCardYearValidation.getValue().trim()) && flag;
		}
		return flag;

	}
	
	
	public boolean checkFuelAmount(String fuelAmount) {
		if(fuelAmount == null || fuelAmount.isEmpty()) {
			return false;
		}
		if(!ObjectContainer.checkIfStringContainsOnlyNumbersFloatType(fuelAmount)) {
			return false;
		}
		float amount = -1;
		try {
			amount = Float.parseFloat(fuelAmount);
		}catch(NumberFormatException e) {
			return false;
		}
		if(amount <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * This function check the amount field.This function performs the following
	 * tests: -Field is empty. -Field contain string. -The user insert negative
	 * amount.
	 * 
	 * @param showErrorMsg for showing error message to the user only after he click
	 *                     on submit button.
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkAmountField(Boolean showErrorMsg, String fuelAmount) {
		this.amount = fuelAmount;
		if (fuelAmount == null || fuelAmount.isEmpty()) {
			lblSubTotalPriceBeforeDiscount.setText("0.00 $");
			lbltotalPriceAfterDiscount.setText("0.00 $");
			if (showErrorMsg) {
//				setErrorImage(imgAmountError, "/images/error_icon.png");
				ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgAmountError);
			}
			return false;
		}
		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(fuelAmount) && showErrorMsg) {
//			setErrorImage(imgAmountError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgAmountError);
			return false;
		}
		float fuelAmountFloat = Float.parseFloat(this.amount);
		if (fuelAmountFloat <= 0 && showErrorMsg) {
//			setErrorImage(imgAmountError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgAmountError);
			return false;
		}
		if (showErrorMsg)
//			setErrorImage(imgAmountError, "/images/v_icon.png");
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgAmountError);
		return true;
	}

	public boolean checkStreet(String street) {
		if (street == null || street.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * This function check if street field is empty.
	 * 
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkStreetField(String street) {
		if (checkStreet(street)) {
			ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgStreetError);
			return true;
		}
		ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgStreetError);
		return false;
	}

	/**
	 * This function check if city field is empty.
	 * 
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkCityField(String city) {
		if (city == null || city.isEmpty()) {
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCityError);
			return false;
		}
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgCityError);
		return true;

	}

	public boolean checkIfDateSupplyIsValid(String date) {
		if (date == null || date.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public boolean checkIfDateSupplyIsPassed(LocalDate date) {
		if(date.isBefore(LocalDate.now())) {
			return false;
		}
		return true;
	}
	
	/**
	 * This function check the date supply datePicker field. This function performs
	 * the following tests: -The user didn't choose date. -The selected date has
	 * already passed.
	 * 
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkDateSupplyField(LocalDate date) {
		if (!checkIfDateSupplyIsValid(date.toString())){
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgDateSupllyError);
			return false;
		}
		if (!checkIfDateSupplyIsPassed(date)) {
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgDateSupllyError);
			return false;
		}
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgDateSupllyError);
		return true;
	}

	public boolean checkIfSelectedPaymentMethod(String paymentMethod, String defualtValue) {
		if(paymentMethod.equals(defualtValue)) {
			return false;
		}
		return true;
	}
	
	/**
	 * This function check if the user didn't select payment method in 'method
	 * payment' choice box .
	 * 
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkPaymentMethodField(String paymentMethod) {
		if (!checkIfSelectedPaymentMethod(paymentMethod,cbPaymentMethod.getItems().get(0))) {
//			setErrorImage(imgPaymentMethodError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgPaymentMethodError);
			return false;
		}
//		setErrorImage(imgPaymentMethodError, "/images/v_icon.png");
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgPaymentMethodError);
		return true;
	}

	/**
	 * This function check if the user didn't select company in 'fuel company'
	 * choice box .
	 * 
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkFuelCompanyField(String fuelCompany) {
		if (fuelCompany.equals(cbfuelCompany.getItems().get(0))) {
//			setErrorImage(imgfuelCompanyErrMsg, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgfuelCompanyErrMsg);
			return false;
		}
//		setErrorImage(imgfuelCompanyErrMsg, "/images/v_icon.png");
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgfuelCompanyErrMsg);
		return true;
	}

	/**
	 * This function check the card number field. This function performs the
	 * following tests: -This field is empty. -This field contain string. -The user
	 * entered an invalid credit card length.
	 * 
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkCardNumberField(String cardNumber) {
		if (cardNumber == null || cardNumber.isEmpty()) {
//			setErrorImage(imgCardNumberCVV, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCardNumberCVV);
			return false;
		}
		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(cardNumber)) {
//			setErrorImage(imgAmountError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCardNumberCVV);
		}
		if (cardNumber.length() > 16 || cardNumber.length() < 8) {
//			setErrorImage(imgCardNumberCVV, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCardNumberCVV);
			return false;
		}
//		setErrorImage(imgCardNumberCVV, "/images/v_icon.png");
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgCardNumberCVV);
		return true;
	}

	/**
	 * This function check the cvv field. This function performs the following
	 * tests: -This field is empty. -This field contain string. -The user entered an
	 * invalid cvv length.
	 * 
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkCVVField(String cvv) {
		if (cvv.isEmpty()) {
//			setErrorImage(imgCVVError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCVVError);
			return false;
		}
		if (cvv.length() < 3 || cvv.length() > 4) {
//			setErrorImage(imgCVVError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCVVError);
			return false;
		}
		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(cvv)) {
//			setErrorImage(imgAmountError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgCVVError);
			return false;
		}
//		setErrorImage(imgCVVError, "/images/v_icon.png");
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgCVVError);
		return true;
	}

	/**
	 * This function check the date validation datePicker field. This function
	 * performs the following tests: -The user didn't choose date validation. -The
	 * selected date has already passed.
	 * 
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkDateValidationField(String month, String year) {
		if (month.equals(cbCreditCardMonthValidation.getItems().get(0))) {
//			setErrorImage(imgDateValidationError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgDateValidationError);
			return false;
		}
		if (!ObjectContainer.checkIfStringContainsOnlyNumbers(month)
				|| !ObjectContainer
						.checkIfStringContainsOnlyNumbers(year)) {
//			setErrorImage(imgDateValidationError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgDateValidationError);
		}
		if (year.equals(cbCreditCardYearValidation.getItems().get(0))) {
//			setErrorImage(imgDateValidationError, "/images/error_icon.png");
			ObjectContainer.setImageBackground(ObjectContainer.errorIcon, imgDateValidationError);
			return false;
		}
//		setErrorImage(imgDateValidationError, "/images/v_icon.png");
		ObjectContainer.setImageBackground(ObjectContainer.vIcon, imgDateValidationError);
		return true;
	}

	/**
	 * This function get fuel object(fuelType, pricePerLitter, maxPricePerLitter)
	 * from data base and return the current price per litter.
	 * 
	 * @param fuelType will be home heating fuel
	 * @return the current price per litter.
	 */
	public float getFuelObjectByType(String fuelType) {
		JsonObject json = new JsonObject();
		json.addProperty("fuelType", fuelType);
		Message msg = new Message(MessageType.GET_FUEL_BY_TYPE, json.toString());
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		fuelType = response.get("fuelType").getAsString();
		float price = response.get("pricePerLitter").getAsFloat();
		return price;
	}

	/**
	 * This function get the order id of the user from data base. In
	 * home_heating_fuel_orders table we generate order id in data base.
	 * 
	 * @param json is the current user order.
	 */
	public int getOrderIdFromDB(JsonObject json) {
		// get last order id from db:
		Message msg = new Message(MessageType.GET_ORDER_ID, json.toString());
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		this.orderId = response.get("orderId").getAsString();
		return Integer.parseInt(this.orderId);
	}

	/**
	 * This function get from DB the fuel companies that the user can purchase, and
	 * displays them on fuel company choice box.
	 */
	private void getFuelCompaniesByCustomerID() {
		String customerID = ((Customer) ObjectContainer.currentUserLogin).getCustomerId();
//		JsonObject json = new JsonObject();
//		json.addProperty("customerID", customerID);
//		Message msg = new Message(MessageType.GET_FUEL_COMPANIES_BY_CUSTOMER_ID, json.toString());
//		ClientUI.accept(msg);
//		String[] fuelCompanies = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies")
//				.getAsString().split(",");
		String[] fuelCompanies = iHomeHeatingFuelDBManager.getFuelCompaniesByCustomerID(customerID);
		cbfuelCompany.getItems().clear();
		cbfuelCompany.getItems().add("Choose company:");
		for (int i = 0; i < fuelCompanies.length; i++) {
			cbfuelCompany.getItems().add(fuelCompanies[i]);
		}
		cbfuelCompany.setValue(cbfuelCompany.getItems().get(0));
	}

	/**
	 * This function get user credit card and create jsonObect of his credit card
	 * details.
	 * 
	 * @return the credit card details.
	 */
	private JsonObject getCreditCard() {
		JsonObject json = new JsonObject();
		Customer customer = (Customer) ObjectContainer.currentUserLogin;
		json.addProperty("customerID", customer.getCustomerId());
		Message msg = new Message(MessageType.GET_CREDIT_CARD_DETAILS_BY_ID, json.toString());
		ClientUI.accept(msg);
		return ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}

	/**
	 * This function get the current sale details.
	 * 
	 * @return the percentage discount for current sale.
	 */
	private float getCurrentSaleDiscount() {
		Message msg = new Message(MessageType.GET_CURRENT_SALE_TEMPLATE, "");
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		return getDiscount(response);
	}

	/**
	 * This function get the data of discount.
	 * 
	 * @param response
	 * @return the current percentage discount
	 */
	private float getDiscount(JsonObject response) {
		if (response.get("saleData") == null) {
			return 0;
		}
		JsonObject saleData = response.get("saleData").getAsJsonObject();
		JsonArray saleTypes = saleData.get("saleTypes").getAsJsonArray();
		float discountRate = 0;
		int count = 0;

		for (int i = 0; i < saleTypes.size(); i++) {
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_FUEL_TYPE)) {
				String fuel = "Home Heating Fuel";
				if (fuel.equals(saleData.get("fuelType").getAsString())) {
					count++;
				}
			}
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_CUSTOMER_TYPE)) {
				Customer customer = (Customer) ObjectContainer.currentUserLogin;

				if (customer.getCustomerType().equals(saleData.get("customerType").getAsString())) {
					count++;
				}
			}
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_CRETIAN_HOURS)) {
				if (checkTimes(saleData)) {
					count++;
				}
			}
		}
		
		if(count == saleTypes.size()) {
			discountRate = response.get("discountRate").getAsFloat();
			saleTemplateName = response.get("saleTemplateName").getAsString();
		}
		return discountRate;
	}
	
	/**
	 * This function check if the current sale is valid at time of placing home
	 * heating fuel order.
	 * 
	 * @param saleData contain all the data of the current sale
	 * @return true if the sale is activate while placing an order, else returns
	 *         false.
	 */
	private boolean checkTimes(JsonObject saleData) {
		String from = saleData.get("from").getAsString();
		String to = saleData.get("to").getAsString();
		Calendar c = Calendar.getInstance();
		int start = Integer.parseInt(from.split(":")[0]) * 60 + Integer.parseInt(from.split(":")[1]);
		int end = Integer.parseInt(to.split(":")[0]) * 60 + Integer.parseInt(to.split(":")[1]);
		int currentTime = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
		return start >= currentTime && currentTime <= end;
	}

	/**
	 * This function create JsonObject with all the components of an home heating
	 * fuel order
	 * 
	 * @return JsonObect that include all the details an home heating fuel order.
	 */
	private JsonObject createJsonObjectForOrder() {
		this.saleTemplateName = "";
		this.amount = txtAmount.getText().toString().trim();
		this.street = txtStreet.getText().toString().trim();
		this.paymentMethod = cbPaymentMethod.getValue().toString().trim();
		this.dateSuplly = datePickerDateSupply.getValue().toString().trim();
		this.totalPrice = calcTotalPrice(Float.parseFloat(txtAmount.getText().trim()));
		this.orderDate = ObjectContainer.getCurrentDate();
		Customer customer = (Customer) ObjectContainer.currentUserLogin;
		this.customerId = customer.getCustomerId();
		this.city = txtCity.getText().trim();
		this.customerName = customer.getName();
		this.fuelCompany = cbfuelCompany.getValue().toString().trim();
		JsonObject json = new JsonObject();
		json.addProperty("orderID", this.orderId);
		json.addProperty("customerId", this.customerId);
		json.addProperty("amount", this.amount);
		json.addProperty("street", this.street);
		json.addProperty("isUrgentOrder", this.isPressed.toString());
		json.addProperty("paymentMethod", this.paymentMethod);
		json.addProperty("dateSupplay", this.dateSuplly);
		json.addProperty("totalPrice", this.totalPrice);
		json.addProperty("paymentMethod", this.paymentMethod);
		json.addProperty("orderDate", this.orderDate);
		json.addProperty("saleTemplateName", this.saleTemplateName);
		json.addProperty("city", this.city);
		json.addProperty("fuelCompany", this.fuelCompany);
		json.addProperty("updateExistsCreditCard", "false");
		json.addProperty("savePaymentDetails", "false");
		json.addProperty("customerType", customer.getCustomerType());
		if (paymentMethod.equals("Credit Card")) {
			String month = cbCreditCardMonthValidation.getValue();
			String year = cbCreditCardYearValidation.getValue();
			this.creditCardDateValidation = month + "/" + year;
			this.cardNumber = txtCardNumber.getText().trim();
			this.cvv = txtCVV.getText().trim();
			json.addProperty("cardNumber", this.cardNumber);
			json.addProperty("cvvNumber", this.cvv);
			json.addProperty("validationDate", this.creditCardDateValidation);
			json.addProperty("customerID", this.customerId);
		}
		return json;
	}
	
	
	public float calcTotalPrice(float amountOfLitters, boolean isUrgentOrder) {
		float discount = 0;
		float price = 0;
		float shippingCost = 10;
		
		pricePerLitter = iHomeHeatingFuelDBManager.getPricePerLitter();
		discount = iHomeHeatingFuelDBManager.getCurrentRunningSaleTemplateDiscount();
		
		if(isUrgentOrder) {
			discount -= 2;
		}
		
		if(amountOfLitters >= 600 && amountOfLitters <= 800) {
			discount += 3;
		}else if(amountOfLitters > 800){
			discount += 4;
		}
		
		price = (float) ((amountOfLitters * pricePerLitter * (100 - discount) * 0.01) + shippingCost);  
		return price;
	}
	
	/**
	 * This function calculate the price of the order - before and after discount.
	 * After calculating, the function updates the Order Summary labels.
	 * 
	 * @param amountOfLitters is the amount of fuel that the user wants to buy.
	 * @return the total price of the order.
	 */
	public String calcTotalPrice(float amountOfLitters) {
		Boolean discountFlag = false;// If we have any discount change to true;
		double totalPriceAfterDiscount = 0, subTotalPriceBeforeDiscount = 0, commissionForUrgentorder = 0,
				totalCommision = 0;
		this.discountrate = (double) iHomeHeatingFuelDBManager.getCurrentRunningSaleTemplateDiscount();
		if (checkAmountField(false,txtAmount.getText().trim())) {
			if (this.isPressed) {
				commissionForUrgentorder = 2;
				discountFlag = true;
//				totalCommision = (double) amountOfLitters * commissionForUrgentorder * 0.01;
				totalCommision = (double) pricePerLitter * amountOfLitters * commissionForUrgentorder * 0.01;
				textUrgentOrder.setVisible(true);
				lblUrgentOrder.setVisible(true);
				lblUrgentOrder.setText(String.format("%.2f", totalCommision) + " $");
			} else {
				textUrgentOrder.setVisible(false);
				lblUrgentOrder.setVisible(false);
			}

			if (amountOfLitters >= 600 && amountOfLitters <= 800) {
				this.discountrate += 3;
				discountFlag = true;
			} else if (amountOfLitters >= 800) {
				this.discountrate += 4;
				discountFlag = true;
			}
			lblDiscountRate.setText(String.valueOf(this.discountrate) + " %");
			lblShippingRate.setText(shippingCost + " $");
			subTotalPriceBeforeDiscount = (double) amountOfLitters * (double) pricePerLitter;
			totalPriceAfterDiscount = shippingCost
					+ (subTotalPriceBeforeDiscount) * (100 - this.discountrate + commissionForUrgentorder) * 0.01;
			lblSubTotalPriceBeforeDiscount.setText(String.format("%.2f", subTotalPriceBeforeDiscount) + " $");
			lbltotalPriceAfterDiscount.setText(String.format("%.2f", totalPriceAfterDiscount) + " $");
			if (discountFlag)
				return String.format("%.2f", subTotalPriceBeforeDiscount);
			return String.format("%.2f", totalPriceAfterDiscount);
		}
		return null;
	}

	/**
	 * This function limits the amount of characters that the user can type.
	 */
	private void limitTextFields() {
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtAmount, 5);
		ObjectContainer.setTextFieldLimit(txtStreet, 20);
		ObjectContainer.setTextFieldLimit(txtCity, 20);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCardNumber, 16);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCVV, 4);
	}


	public void setHomeHeatingFuelInterface(IHomeHeatingFuelDBManager iHomeHeatingFuelDBManager) {
		this.iHomeHeatingFuelDBManager = iHomeHeatingFuelDBManager;
	}
}
