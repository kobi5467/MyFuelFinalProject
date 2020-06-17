package client.gui.customer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import client.gui.marketingrepresentative.SaleTemplatePane;
import entitys.Customer;
import entitys.Message;
import entitys.enums.FuelType;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * This class is for Home heating fuel order.
 * The first screen contain all the process of the home heating fuel order.
 * The second screen contain the order details.
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
	public Boolean rememberCardPressed=false;
	public double discountrate = 0;
	public float pricePerLitter;
	public double shippingCost=10;
	// **************************************************Initialize  function**************************************************

	/**
	 * This function gets HomeHeatingFuelForm.fxml and load it to the main pain.
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
     * This function  initialize home heating fuel order form 
     */
	public void initUI() {
		initCreditCardFields();
		getFuelCompaniesByCustomerID();
		limitTextFields();
		panePurchaseSuccessful.setVisible(false);
		showCreditCardFields(false); 
		initOrderSummary();
		updatePaymentFormOnPaymentMethodClick();
		creditCard = getCreditCardByCustomerID();
	}

	/**
	 * This function activates the second screen of home heating fuel order and close the first screen.
	 * The second screen includes the details of the order:
	 * Order id,shipping date,Adders for shipping,Date supply.
	 */
	public void showPurchaseDetails() {
		System.out.println("in showPurchaseDetails");
		panePurchaseInputs.setVisible(false);
		panePurchaseSuccessful.setVisible(true);
		lblOrderNumberOnPurchasePain.setText(this.orderId);
		lblAdress.setText(this.city + "/" + this.street);
		lblDateSupplyInPurchasePane.setText(this.dateSuplly);
		lblPaymentMethodOnPurchasePane.setText(this.paymentMethod);
		lblTotalPriceOnPurchasePane.setText(this.totalPrice);
		lblUserFirstName.setText(this.customerName+",");
		setErrorImage(imgPurchaseSuccessful, "../../../images/purchase_successful.png");
	}

	/**
	 * This function displays the credit information fields if the user chosen
	 *  this payment option. Otherwise, this fields will be hidden.
	 * @param flag if the flag is true we will display the credit card fields, else the function hide this fields.
	 */
	private void showCreditCardFields(boolean flag) {
		txtCardNumber.setVisible(flag);
		txtCardNum.setVisible(flag);
		txtCVV.setVisible(flag);
		txtcvv.setVisible(flag);
		txtDateVal.setVisible(flag);
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
	 * This function initialize the payment method choice box and date validation choice box.
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
		lblShippingRate.setText(shippingCost+" $");

		lbltotalPriceAfterDiscount.setText("0.00 $");
		lblCurrentPricePerLitter.setText(String.valueOf(getFuelObjectByType("Home Heating Fuel"))+" $");
	}
	// **************************************************End Initialize  function********************************************

	// **************************************************On Click  function**************************************************
	/**
	 * This function check if all the fields on the first screen is correct and show message to the user 
	 * to approve his order.
	 * If one of the fields isn't correct this function show to the user error message.
	 * If all the fields is correct , the function send a request to the server controller for insert
	 * the order into our data base.
	 * @param event- while the user click on button 'submit' 
	 */
	@FXML
	void onSubmit(ActionEvent event) {
		Boolean flag = homeHeatingFuelFormTest();
		if(!flag) 
			ObjectContainer.showMessage("Error", "Error", "Please fill all the field currect!");
		else {
			ObjectContainer.showMessage("yes_no", "Place order", " Do you want to place your order?");
			flag = ObjectContainer.yesNoMessageResult;
		}
		if (flag) {
			JsonObject json=createJsonObjectForOrder();
			System.out.println(json.toString());
			System.out.println("credit card press:"+this.rememberCardPressed.toString());
			if(this.rememberCardPressed) 
				json.addProperty("savePaymentDetails",this.rememberCardPressed.toString());
			else
				json.addProperty("savePaymentDetails","false");
			if(!creditCard.get("creditCardNumber").isJsonNull()) {
				json.addProperty("updateExistsCreditCard","true");
				json.addProperty("customerID", this.customerId);
				json.addProperty("creditCard", this.cardNumber);
				json.addProperty("cvv",  this.cvv);
				json.addProperty("dateValidation", this.creditCardDateValidation);
			}
			Message msg = new Message(MessageType.SUBMIT_HOME_HEATING_FUEL_ORDER, json.toString());
			ClientUI.accept(msg);
			System.out.println("here after query");
			getOrderIdFromDB(json);
			showPurchaseDetails();
			}
		}
	
	/**
	 * This function put the current date in date supply datePicker field of home heating fuel order
	 * and disable the option to the user for select a date.
	 * Another click will give to the user to select date supply. 
	 * after click the function update the current price for the user.
	 * @param event while the user press on choice box of "Urgent order"
	 */
	@FXML
	void checkBoxPressed(ActionEvent event) {
		this.isPressed = !this.isPressed;
		if (this.isPressed) {
			datePickerDateSupply.setDisable(true);
			datePickerDateSupply.setValue(LocalDate.now());
			lblDiscountRate.setText(String.valueOf(this.discountrate));
		} else {
			datePickerDateSupply.setDisable(false);
			datePickerDateSupply.setValue(null);
			lblDiscountRate.setText(String.valueOf(this.discountrate));
		}
		calcTotalPrice(Float.parseFloat(txtAmount.getText().trim()));
	}
	
	/**
	 * This function change the value of "rememberCardPressed" variable from false to true
	 *  for saving the user credit  card details in data base.
	 * @param event while the user press on choice box for remember his credit card.
	 */
    @FXML
    void rememberCardPressed(ActionEvent event) {
    	this.rememberCardPressed =!this.rememberCardPressed;
    }

	/**
	 * This function update order summary labels.
	 * @param event after insert digit to amount field.
	 */
	@FXML
	void updateOrderSummary(KeyEvent event) {
		if (checkAmountField(false)) 
			calcTotalPrice(Float.parseFloat(txtAmount.getText().trim()));
	}
	
	/**
	 * This function displays the credit card fields  if the user choose to pay in credit card option.
	 * If the user register with credit card, we set his credit card details to the credit card field. 
	 */
	public void updatePaymentFormOnPaymentMethodClick() {
		cbPaymentMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = cbPaymentMethod.getItems().get((Integer) number2);
				System.out.println("we are in updatePaymentForm");
				if (value.equals("Choose type") || value.equals("Cash"))
					showCreditCardFields(false);
				else if (value.equals("Credit Card")) {
					if(creditCard.get("creditCardNumber")!=null) {
						txtCardNumber.setText(creditCard.get("creditCardNumber").getAsString());
						txtCVV.setText(creditCard.get("cvv").getAsString());
						cbCreditCardMonthValidation.setValue(creditCard.get("validationDate").getAsString().split("/")[0]);
						cbCreditCardYearValidation.setValue(creditCard.get("validationDate").getAsString().split("/")[1]);
						}
					showCreditCardFields(true);
				}
			}
		});

	}
	// **************************************************On Click  function**********************************************

	// **************************************************Test  function**************************************************
	/**
	 * This function coordinates all the fields tests.In case the user choose to pay in credit card,
	 * the function will check the fields that contain to credit card.
	 * @return if one of the tests fails the function will return false,, else the function will return true.
	 *   
	 */
	public Boolean homeHeatingFuelFormTest() {
		Boolean flag = true;
		flag = checkAmountField(true);
		flag = checkStreetField() && flag;
		flag = checkCityField() && flag;
		flag = checkDateSupplyField() && flag;
		flag = checkPaymentMethodField() && flag;
		flag=checkFuelCompanyField()&&flag;
		if (cbPaymentMethod.getValue().trim().equals("Credit Card")) {
			flag = checkCardNumberField() && flag;
			flag = checkCVVField() && flag;
			flag = checkDateValidationField() && flag;
		}
		return flag;

	}
	/**
	 * This function check the amount field.This function performs the following tests:
	 * -Field is empty.
	 * -Field contain string.
	 * -The user insert negative amount.
	 * @param showErrorMsg for showing error message to the user only after he click on submit button.
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkAmountField(Boolean showErrorMsg) {
		this.amount = txtAmount.getText().trim();
		if (txtAmount.getText().toString() == null || txtAmount.getText().isEmpty()) {
			lblSubTotalPriceBeforeDiscount.setText("0.00 $");
			lbltotalPriceAfterDiscount.setText("0.00 $");
			if(showErrorMsg) {
				setErrorImage(imgAmountError, "../../../images/error_icon.png");
				}
			return false;
			}
		if(!ObjectContainer.checkIfStringContainsOnlyNumbers(txtAmount.getText().toString())&&showErrorMsg){
			setErrorImage(imgAmountError, "../../../images/error_icon.png");
			return false;
			}
		float fuelAmount = Float.parseFloat(this.amount);
		if (fuelAmount <= 0&&showErrorMsg) {
			setErrorImage(imgAmountError, "../../../images/error_icon.png");
			return false;
			}
		if(showErrorMsg) 
			setErrorImage(imgAmountError, "../../../images/v_icon.png");
		return true;
	}
	/**
	 * This function check if street field is empty.
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkStreetField() {
		if (txtStreet.getText().toString() == null || txtStreet.getText().toString().isEmpty()) {
			setErrorImage(imgStreetError, "../../../images/error_icon.png");
			return false;
			}
		setErrorImage(imgStreetError, "../../../images/v_icon.png");
		return true;
	}

	/**
	 * This function check if city field is empty.
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkCityField() {
		System.out.println("checkCityField" + txtCity.getText());
		if (txtCity.getText() == null || txtCity.getText().toString().isEmpty()) {
			setErrorImage(imgCityError, "../../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgCityError, "../../../images/v_icon.png");
		return true;

	}
	
	/**
	 * This function check the date supply datePicker field.
	 * This function performs the following tests:
	 * -The user didn't choose date.
	 * -The selected date has already passed.
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkDateSupplyField() {
		if (datePickerDateSupply.getValue() == null || datePickerDateSupply.getValue().toString().trim().isEmpty()) {
			setErrorImage(imgDateSupllyError, "../../../images/error_icon.png");
			return false;
		}
		if (datePickerDateSupply.getValue().isBefore(LocalDate.now())) {
			setErrorImage(imgDateSupllyError, "../../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgDateSupllyError, "../../../images/v_icon.png");
		return true;
	}
	
	/**
	 * This function check if the user didn't select payment method in 'method payment' choice box .
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkPaymentMethodField() {
		if (cbPaymentMethod.getValue().trim().equals(cbPaymentMethod.getItems().get(0))) {
				setErrorImage(imgPaymentMethodError, "../../../images/error_icon.png");
			return false;
		}
			setErrorImage(imgPaymentMethodError, "../../../images/v_icon.png");
		return true;
	}
	
	/**
	 * This function check if the user didn't  select company in 'fuel company' choice box .
	 * @return true if this test passed, else the function return false.
	 */
	public Boolean checkFuelCompanyField() {
		if (cbfuelCompany.getValue().trim().equals(cbfuelCompany.getItems().get(0))) {
			setErrorImage(imgfuelCompanyErrMsg, "../../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgfuelCompanyErrMsg, "../../../images/v_icon.png");
		return true;
	}
	
	/**
	 * This function check the card number  field.
	 * This function performs the following tests:
	 * -This field is empty.
	 * -This field contain string.
	 * -The user entered an invalid credit card length.
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkCardNumberField() {
		if (txtCardNumber.getText().trim() == null || txtCardNumber.getText().trim().isEmpty()) {
			setErrorImage(imgCardNumberCVV, "../../../images/error_icon.png");
			return false;
		}
		if(!ObjectContainer.checkIfStringContainsOnlyNumbers(txtCardNumber.getText().toString())){
			setErrorImage(imgAmountError, "../../../images/error_icon.png");
		}
		System.out.println("lenght is:"+txtCardNumber.getText().trim().length());
 		if (txtCardNumber.getText().trim().length() > 16||txtCardNumber.getText().trim().length() < 8) {
			setErrorImage(imgCardNumberCVV, "../../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgCardNumberCVV, "../../../images/v_icon.png");
		return true;
	}

	/**
	 * This function check the cvv  field.
	 * This function performs the following tests:
	 * -This field is empty.
	 * -This field contain string.
	 * -The user entered an invalid cvv length.
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkCVVField() {
		System.out.println("in checkCVV");
		if (txtCVV.getText().toString() == null || txtCVV.getText().toString().isEmpty()) {
			setErrorImage(imgCVVError, "../../../images/error_icon.png");
			return false;
		}
		if (txtCVV.getText().trim().length()!=3) {
			setErrorImage(imgCVVError, "../../../images/error_icon.png");
			return false;
		}
		if(!ObjectContainer.checkIfStringContainsOnlyNumbers(txtCVV.getText().toString())){
			setErrorImage(imgAmountError, "../../../images/error_icon.png");
			return false;
		}
		setErrorImage(imgCVVError, "../../../images/v_icon.png");
		return true;

	}
	
	/**
	 * This function check the date validation datePicker field.
	 * This function performs the following tests:
	 * -The user didn't choose date validation.
	 * -The selected date has already passed.
	 * @return true if all the test passed, else the function return false.
	 */
	public Boolean checkDateValidationField() {
		if (cbCreditCardMonthValidation.getValue().trim().equals(cbCreditCardMonthValidation.getItems().get(0))) {
			setErrorImage(imgDateValidationError, "../../../images/error_icon.png");
			return false;
			}
		if(!ObjectContainer.checkIfStringContainsOnlyNumbers(cbCreditCardMonthValidation.getValue().toString())||
				!ObjectContainer.checkIfStringContainsOnlyNumbers(cbCreditCardYearValidation.getValue().toString())){
			setErrorImage(imgAmountError, "../../../images/error_icon.png");
			}
		if (cbCreditCardYearValidation.getValue().trim().equals(cbCreditCardYearValidation.getItems().get(0))) {
			setErrorImage(imgDateValidationError, "../../../images/error_icon.png");
			return false;
			}
		setErrorImage(imgDateValidationError, "../../../images/v_icon.png");
		return true;
	}
	// **************************************************End test  function**********************************************
	
	// **************************************************Data Base function**********************************************

	/**
	 * This function get fuel object(fuelType, pricePerLitter, maxPricePerLitter) 
	 * from data base and return the current price per litter.
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
		pricePerLitter = response.get("pricePerLitter").getAsFloat();
		System.out.println("price is:" + pricePerLitter);
		return pricePerLitter;
	}
	
	/**
	 * This function get the order id of the user from data base.
	 * In home_heating_fuel_orders table we generate order id in data base.
	 * @param json is the current user order.
	 */
	public void getOrderIdFromDB(JsonObject json) {
		// get last order id from db:
		Message msg = new Message(MessageType.GET_ORDER_ID, json.toString());
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		this.orderId = response.get("orderId").getAsString();
		this.orderId = response.get("orderId").getAsString();
	}

	/**
	 * This function get from DB the fuel companies that the user can purchase, 
	 * and displays them on fuel company choice box.
	 */
    private void getFuelCompaniesByCustomerID() {
    	String customerID = ((Customer)ObjectContainer.currentUserLogin).getCustomerId();
    	JsonObject json = new JsonObject();
    	json.addProperty("customerID", customerID);
    	Message msg = new Message(MessageType.GET_FUEL_COMPANIES_BY_CUSTOMER_ID,json.toString());
    	ClientUI.accept(msg);
    	String[] fuelCompanies = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies").getAsString().split(",");
    	cbfuelCompany.getItems().clear();
    	cbfuelCompany.getItems().add("Choose company:");
    	for(int i = 0; i < fuelCompanies.length; i++) {
    		cbfuelCompany.getItems().add(fuelCompanies[i]);
    	}
    	cbfuelCompany.setValue(cbfuelCompany.getItems().get(0));
    }
    
    /**
     * This function get user credit card and create jsonObect of his credit card details.
     * @return the credit card details.
     */
	private JsonObject getCreditCardByCustomerID() {
		JsonObject json = new JsonObject();
		Customer customer = (Customer) ObjectContainer.currentUserLogin;
		json.addProperty("customerID", customer.getCustomerId());
		Message msg = new Message(MessageType.GET_CREDIT_CARD_DETAILS_BY_ID, json.toString());
		ClientUI.accept(msg);
		return ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}
    
	// **************************************************End Data Base function*****************************************

	// **************************************************Sale function**************************************************

    /**
     * This function get the current sale details.
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
	 * @param response
	 * @return the current percentage discount
	 */
	private float getDiscount(JsonObject response) {
		JsonObject saleData = response.get("saleData").getAsJsonObject();
		JsonArray saleTypes = saleData.get("saleTypes").getAsJsonArray();
		float discountRate = 0;
		for (int i = 0; i < saleTypes.size(); i++) {
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_FUEL_TYPE)) {
				String fuel ="Home Heating Fuel";
				if (fuel.equals(saleData.get("fuelType").getAsString())) {
					discountRate = response.get("discountRate").getAsFloat();
					saleTemplateName = response.get("saleTemplateName").getAsString();
					break;
				}
			}
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_CUSTOMER_TYPE)) {
				Customer customer = (Customer) ObjectContainer.currentUserLogin;
				if (customer.getCustomerType().equals(saleData.get("customerType").getAsString())) {
					discountRate = response.get("discountRate").getAsFloat();
					saleTemplateName = response.get("saleTemplateName").getAsString();
					break;
				}
			}
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_CRETIAN_HOURS)) {
				if (checkTimes(saleData)) {
					discountRate = response.get("discountRate").getAsFloat();
					saleTemplateName = response.get("saleTemplateName").getAsString();
					break;
				}
			}
		}
		return discountRate;
	}
	/**
	 * This function check if the current sale is valid at time of placing home heating fuel order.
	 * @param saleData contain all the data of the current sale
	 * @return true if the sale is activate while placing an order, else returns false.
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
	// **************************************************End sale function**************************************************

	// **************************************************Generic function**************************************************
	/**
	 * This function create JsonObject with all the components of an home heating fuel order
	 * @return JsonObect that include all the details an home heating fuel order.
	 */
    private JsonObject createJsonObjectForOrder() {
    	this.saleTemplateName = "";
		this.amount = txtAmount.getText().toString().trim();
		this.street = txtStreet.getText().toString().trim();
		this.paymentMethod = cbPaymentMethod.getValue().toString().trim();
		this.dateSuplly = datePickerDateSupply.getValue().toString().trim();
		this.totalPrice = calcTotalPrice(Float.parseFloat(txtAmount.getText().trim()));
		this.orderDate = getCurrentDate();
		Customer customer = (Customer) ObjectContainer.currentUserLogin;
		this.customerId = customer.getCustomerId();
		this.city = txtCity.getText().trim();
		this.customerName=customer.getName();
		this.fuelCompany= cbfuelCompany.getValue().toString().trim();
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
		json.addProperty("updateExistsCreditCard","false");
		json.addProperty("savePaymentDetails","false");
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
    
	/**
	 * This function get the current date.
	 * @return the current date.
	 */
	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date).toString();
	}
	
	/**
	 * This function set image in the pane.
	 * @param img is the name of the image.
	 * @param url is the location of the image in the project.
	 * 
	 */
	public void setErrorImage(ImageView img, String url) {
		Image image = new Image(getClass().getResource(url).toString());
		img.setImage(image);
	}
	
	/**
	 * This function calculate the price of the order - before and after discount.
	 * After calculating, the function updates the Order Summary labels. 
	 * @param amountOfLitters is the amount of fuel that the user wants to buy.
	 * @return the total price of the order.
	 */
	public String calcTotalPrice(float amountOfLitters) {
		Boolean discountFlag = false;// If we have any discount change to true;
		double totalPriceAfterDiscount =0, subTotalPriceBeforeDiscount = 0, commissionForUrgentorder = 0;
		 this.discountrate=(double)getCurrentSaleDiscount();
		if (checkAmountField(false)) {
			if (this.isPressed) {
				commissionForUrgentorder = 2;
				discountFlag = true;
			}
			if (amountOfLitters >= 600 && amountOfLitters <= 800) {
				this.discountrate+= 3;
				discountFlag = true;
			} 
			else if (amountOfLitters >= 800) {
				this.discountrate+= 4;
				discountFlag = true;
			}
			lblDiscountRate.setText(String.valueOf(this.discountrate) + " %");
			lblShippingRate.setText(shippingCost+" $");
			subTotalPriceBeforeDiscount = (double) amountOfLitters * (double) pricePerLitter
					* (100 + commissionForUrgentorder) * 0.01;
			totalPriceAfterDiscount = (shippingCost+subTotalPriceBeforeDiscount) * (100 - this.discountrate) * 0.01;
			lblSubTotalPriceBeforeDiscount.setText(String.format("%.2f",subTotalPriceBeforeDiscount)+" $");
			lbltotalPriceAfterDiscount.setText(String.format("%.2f",totalPriceAfterDiscount) +" $");
			if (discountFlag)
				return String.format("%.2f",subTotalPriceBeforeDiscount);
			return String.format("%.2f",totalPriceAfterDiscount);
		}
		return null;
	}

	/**
	 * This function limits the amount of characters that the user can type.
	 */
	private void limitTextFields() {
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtAmount, 5);
		ObjectContainer.setTextFieldLimit(txtStreet, 20);
		ObjectContainer.setTextFieldToGetOnlyCharacterWithLimit(txtCity,15);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCardNumber, 16);
		ObjectContainer.setTextFieldToGetOnlyDigitsWithLimit(txtCVV, 4);		
	}
}
