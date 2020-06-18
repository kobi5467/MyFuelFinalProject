package client.gui.customer;

import java.io.IOException;
import java.util.Calendar;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * This class responsible to manage the fast fuel orders.
 * @author Kobi Malka
 * @version - Final
 */
public class FastFuelController {

	@FXML
	private Pane fastFuelPane;

	@FXML
	private ChoiceBox<String> cbFuelCompany;

	@FXML
	private ChoiceBox<String> cbVehicleNumber;

	@FXML
	private ChoiceBox<String> cbStationNumber;

	@FXML
	private Button btnSubmitOrder;

	@FXML
	private TextField txtFuelAmount;

	@FXML
	private Label lblFuelType;

	@FXML
	private Label lblPricePerLitter;

	@FXML
	private Label lblTotalPrice;

	@FXML
	private Label lblPriceAfterDiscount;

	@FXML
	private ChoiceBox<String> cbPaymentMethod;

	@FXML
	private Pane creditCardViewPane;

	@FXML
	private ChoiceBox<String> cbYear;

	@FXML
	private TextField txtCreditCardNumber;

	@FXML
	private TextField txtCvv;

	@FXML
	private ChoiceBox<String> cbMonth;

	@FXML
	private ImageView imgCompany;

	@FXML
	private ImageView imgVehicle;

	@FXML
	private ImageView imgPaymentMethod;

	@FXML
	private ImageView imgCreditCardNumber;

	@FXML
	private ImageView imgCVV;

	@FXML
	private ImageView imgDateValidation;

	@FXML
	private ImageView imgAmount;

	@FXML
	private ImageView imgStationNumber;

	@FXML
    private ChoiceBox<String> cbPumpNumber;

    @FXML
    private ImageView imgPumpNumber;
	
	// variables
	private float amount = 0;
	private float totalPrice = 0;
	private float priceAfterDiscount = 0;
	private float pricePerLitter = 0;
	private String fuelType = "";
	private JsonArray vehicles;
	private float purchaseModelRate = 0;
	private JsonArray subscribeTypes;
	private JsonObject creditCard;
	private String saleTemplateName = "";
	private float availableAmount = -1;
	
	@FXML
	void onSubmit(ActionEvent event) {
		if (checkValidOrder()) {
			createOrder();
			ObjectContainer.showMessage("Error", "Order Success", "Your order success.\nYou ordered " + amount
					+ " litters of " + fuelType + "\nTotal cost: " + priceAfterDiscount + "$");
			ObjectContainer.mainFormController.setPane("Home");
		}
	}
	/**
	 * This method responsible to request the data from DB.
	 */
	private void createOrder() {
		JsonObject json = new JsonObject();
		json.addProperty("customerID", ((Customer) ObjectContainer.currentUserLogin).getCustomerId());
		json.addProperty("vehicleNumber", cbVehicleNumber.getValue());
		json.addProperty("saleTemplateName", saleTemplateName);
		json.addProperty("stationID", cbStationNumber.getValue());
		json.addProperty("orderDate", ObjectContainer.getCurrentDate());
		json.addProperty("orderStatus", "Finished");
		json.addProperty("fuelType", lblFuelType.getText());
		json.addProperty("amountOfLitters", amount);
		json.addProperty("totalPrice", priceAfterDiscount);
		json.addProperty("paymentMethod", cbPaymentMethod.getValue());
		json.addProperty("pumpNumber", cbPumpNumber.getValue());
		
		Message msg = new Message(MessageType.ADD_FAST_FUEL_ORDER, json.toString());
		ClientUI.accept(msg);

	}
	/**
	 * This method responsible to check for valid order.
	 * @return - return boolean value.
	 */
	private boolean checkValidOrder() {
		boolean flag = true;
		if (cbVehicleNumber.getValue().equals(cbVehicleNumber.getItems().get(0))) {
			setErrorImage(imgVehicle, "/images/error_icon.png");
			flag = false;
		} else {
			setErrorImage(imgVehicle, "/images/v_icon.png");
		}

		if (cbFuelCompany.getValue().equals(cbFuelCompany.getItems().get(0))) {
			setErrorImage(imgCompany, "/images/error_icon.png");
			flag = false;
		} else {
			setErrorImage(imgCompany, "/images/v_icon.png");
		}

		if (cbStationNumber.getValue().equals(cbStationNumber.getItems().get(0))) {
			setErrorImage(imgStationNumber, "/images/error_icon.png");
			flag = false;
		} else {
			setErrorImage(imgStationNumber, "/images/v_icon.png");
		}

		if (cbPaymentMethod.getValue().equals(cbPaymentMethod.getItems().get(0))) {
			setErrorImage(imgPaymentMethod, "/images/error_icon.png");
			flag = false;
		} else {
			setErrorImage(imgPaymentMethod, "/images/v_icon.png");
		}

		if (txtFuelAmount.getText().isEmpty()) {
			setErrorImage(imgAmount, "/images/error_icon.png");
			flag = false;
		} else {
			setErrorImage(imgAmount, "/images/v_icon.png");
		}

		try {
			Float.parseFloat(txtFuelAmount.getText());
			setErrorImage(imgAmount, "/images/v_icon.png");
		} catch (NumberFormatException e) {
			setErrorImage(imgAmount, "/images/error_icon.png");
			flag = false;
		}

		if (cbPaymentMethod.getValue().equals(cbPaymentMethod.getItems().get(2))) {
			// check credit card details.
			if (txtCreditCardNumber.getText().isEmpty()
					|| !ObjectContainer.checkIfStringContainsOnlyNumbers(txtCreditCardNumber.getText())
					|| txtCreditCardNumber.getText().length() > 16 || txtCreditCardNumber.getText().length() < 8) {
				setErrorImage(imgCreditCardNumber, "/images/error_icon.png");
				flag = false;
			} else {
				setErrorImage(imgCreditCardNumber, "/images/v_icon.png");
			}

			if (txtCvv.getText().length() > 4 || txtCvv.getText().length() < 3
					|| !ObjectContainer.checkIfStringContainsOnlyNumbers(txtCvv.getText())) {
				setErrorImage(imgCVV, "/images/error_icon.png");
				flag = false;
			} else {
				setErrorImage(imgCVV, "/images/v_icon.png");
			}

			if (cbYear.getValue().equals(cbYear.getItems().get(0))
					|| cbMonth.getValue().equals(cbYear.getItems().get(0))) {
				setErrorImage(imgDateValidation, "/images/error_icon.png");
				flag = false;
			} else {
				setErrorImage(imgDateValidation, "/images/v_icon.png");
			}
		}
		
		if(cbPumpNumber.getValue().equals(cbPumpNumber.getItems().get(0))) {
			setErrorImage(imgPumpNumber, "/images/error_icon.png");
			flag = false;
		} else {
			setErrorImage(imgPumpNumber, "/images/v_icon.png");
		}
		
		if(flag && checkIfFuelAmountAvailable(fuelType)) {
			flag = true;
		}else {
			if(flag) {
				ObjectContainer.showMessage("Error", "Fast Fuel Order", 
						"We are sorry\nWe only have " + availableAmount + " litters of " + fuelType + "\nPlease try less..");
				flag = false;				
			}
		}

		return flag;
	}
	/**
	 * This method responsible to check if the fuel amount is available.
	 * @param fuelType - string value of fuel type.
	 * @return - boolean value.
	 */
	public boolean checkIfFuelAmountAvailable(String fuelType) {
		
		boolean isValid = true;
		JsonObject json = new JsonObject();
		json.addProperty("fuelType", fuelType);
		json.addProperty("stationID", cbStationNumber.getValue());
		Message msg = new Message(MessageType.GET_CURRENT_FUEL_AMOUNT_BY_FUEL_TYPE, json.toString());
		ClientUI.accept(msg);
		
		availableAmount = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject()
				.get("availableAmount").getAsFloat();
		if(amount > availableAmount) isValid = false;
		return isValid;
	}
	/**
	 * This method responsible to set error image.
	 * @param img - the node to set image.
	 * @param url - string value of path image.
	 */
	public void setErrorImage(ImageView img, String url) {
		Image image = new Image(getClass().getResource(url).toString());
		img.setImage(image);
	}
	/**
	 * This method responsible to load the 'xml' class and go to init.
	 * @param changePane
	 */
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("FastFuelForm.fxml"));
		try {
			fastFuelPane = loader.load();
			changePane.getChildren().add(fastFuelPane);
			ObjectContainer.fastFuelController = loader.getController();
			ObjectContainer.fastFuelController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method is responsible to init the lbl, text and etc.
	 */
	private void initUI() {
		lblFuelType.setText("");
		lblPricePerLitter.setText("");
		lblTotalPrice.setText("");
		lblPriceAfterDiscount.setText("");
		btnSubmitOrder.setText("Submit");
		btnSubmitOrder.setId("dark-blue");
		getFuelCompaniesByCustomerID();
		getVehicleNumbersByCustomerID();
		getDiscountRate();
		creditCard = getCreditCardByCustomerID();
		initCB();
		txtCreditCardNumber.setText(creditCard.get("creditCardNumber").getAsString());
		txtCvv.setText(creditCard.get("cvv").getAsString());
		cbMonth.setValue(creditCard.get("validationDate").getAsString().split("/")[0]);
		cbYear.setValue(creditCard.get("validationDate").getAsString().split("/")[1]);

		creditCardViewPane.setVisible(false);
		txtFuelAmount.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.isEmpty()) {
				setErrorImage(imgAmount, "/images/error_icon.png");
			}
			calcPrice();
		});
	}
	/**
	 * This method responsible to calculation the price.
	 */
	public void calcPrice() {
		String stringAmount = txtFuelAmount.getText();
		try {
			if (stringAmount.length() == 0) {
				lblTotalPrice.setText("0");
				lblPriceAfterDiscount.setText("0");
			} else {
				amount = Float.parseFloat(stringAmount);
				totalPrice = amount * pricePerLitter;
				lblTotalPrice.setText(String.format("%.2f$", totalPrice));
				priceAfterDiscount = calcTotalPrice();
				lblPriceAfterDiscount.setText(String.format("%.2f$", priceAfterDiscount));
				setErrorImage(imgAmount, "/images/v_icon.png");
			}
		} catch (Exception e) {
			setErrorImage(imgAmount, "/images/error_icon.png");
		}

	}
	/**
	 * This method is responsible to init all the choice boxes.
	 */
	private void initCB() {
		cbStationNumber.getItems().clear();
		cbStationNumber.getItems().add("Choose company first");
		cbStationNumber.setValue(cbStationNumber.getItems().get(0));
		cbStationNumber.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (oldValue.equals(newValue))
					return;
				if ((Integer) newValue > 0) {
					setErrorImage(imgStationNumber, "/images/v_icon.png");
				} else {
					setErrorImage(imgStationNumber, "/images/error_icon.png");
				}
			}
		});

		cbYear.getItems().add("Choose");
		for (int i = 2020; i < 2031; i++) {
			cbYear.getItems().add("" + i);
		}
		cbYear.setValue(cbYear.getItems().get(0));

		cbMonth.getItems().add("Choose");
		for (int i = 1; i < 13; i++) {
			if (i < 10)
				cbMonth.getItems().add("0" + i);
			else
				cbMonth.getItems().add("" + i);
		}
		cbMonth.setValue(cbMonth.getItems().get(0));

		cbPaymentMethod.getItems().add("Choose");
		cbPaymentMethod.getItems().add("Cash");
		cbPaymentMethod.getItems().add("Credit Card");
		cbPaymentMethod.setValue(cbPaymentMethod.getItems().get(0));
		cbPaymentMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				if ((Integer) number2 > 0) {
					setErrorImage(imgPaymentMethod, "/images/v_icon.png");
				} else {
					setErrorImage(imgPaymentMethod, "/images/error_icon.png");
				}
				if (cbPaymentMethod.getItems().get((Integer) number2).equals("Credit Card")) {
					creditCardViewPane.setVisible(true);
					imgCreditCardNumber.setVisible(true);
					imgCVV.setVisible(true);
					imgDateValidation.setVisible(true);
				} else {
					creditCardViewPane.setVisible(false);
					imgCreditCardNumber.setVisible(false);
					imgCVV.setVisible(false);
					imgDateValidation.setVisible(false);
				}
			}
		});
		
		cbPumpNumber.getItems().clear();
		cbPumpNumber.getItems().add("Choose pump:");
		for(int i = 1; i < 5; i++) {
			cbPumpNumber.getItems().add(""+i);
		}
		cbPumpNumber.setValue(cbPumpNumber.getItems().get(0));
		cbPumpNumber.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				if ((Integer) number2 > 0) {
					setErrorImage(imgPumpNumber, "/images/v_icon.png");
				} else {
					setErrorImage(imgPumpNumber, "/images/error_icon.png");
				}
			}
		});
	}
	/**
	 * This method is responsible to request from the server the credit card details by id
	 * @return - return The object with the details.
	 */
	private JsonObject getCreditCardByCustomerID() {
		JsonObject json = new JsonObject();
		Customer customer = (Customer) ObjectContainer.currentUserLogin;
		json.addProperty("customerID", customer.getCustomerId());

		Message msg = new Message(MessageType.GET_CREDIT_CARD_DETAILS_BY_ID, json.toString());
		ClientUI.accept(msg);

		return ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}
	/**
	 * This method is responsible to request from the server the vehicles by customer id
	 */
	private void getVehicleNumbersByCustomerID() {
		String customerID = ((Customer) ObjectContainer.currentUserLogin).getCustomerId();

		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID, json.toString());
		ClientUI.accept(msg);

		vehicles = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("vehicles").getAsJsonArray();
		cbVehicleNumber.getItems().clear();
		cbVehicleNumber.getItems().add("Choose vehicle:");
		for (int i = 0; i < vehicles.size(); i++) {
			cbVehicleNumber.getItems().add(vehicles.get(i).getAsJsonObject().get("vehicleNumber").getAsString());
		}
		cbVehicleNumber.setValue(cbVehicleNumber.getItems().get(0));
		cbVehicleNumber.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				if ((Integer) number2 > 0) {
					fuelType = vehicles.get((Integer) number2 - 1).getAsJsonObject().get("fuelType").getAsString();
					lblFuelType.setText(FuelType.enumToString(FuelType.stringToEnumVal(fuelType)));
					getPricePerLitterByFuelType(fuelType);
					lblPricePerLitter.setText(pricePerLitter + "");
					calcPrice();
					setErrorImage(imgVehicle, "/images/v_icon.png");
				} else {
					setErrorImage(imgVehicle, "/images/error_icon.png");
					pricePerLitter = 0;
				}
			}
		});
	}
	/**
	 * This method is responsible to request from the server the discount rates.
	 */
	private void getDiscountRate() {
		String subscribeType = ((Customer) ObjectContainer.currentUserLogin).getSubscribeType().getSubscribeType();
		String purchaseModel = ((Customer) ObjectContainer.currentUserLogin).getPurchaseModel().getPurchaseModeltype();

		JsonObject json = new JsonObject();
		json.addProperty("subscribeType", subscribeType);
		json.addProperty("purchaseModel", purchaseModel);
		json.addProperty("fuelType", fuelType);

		Message msg = new Message(MessageType.GET_DICOUNT_RATES_BY_TYPES, json.toString());
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		purchaseModelRate = response.get("purchaseModelRate").getAsFloat();
		subscribeTypes = response.get("subscribeTypes").getAsJsonArray();
	}
	/**
	 * This method is responsible to calculate the total price.
	 * @return - return the price.
	 */
	private float calcTotalPrice() {
		Customer customer = (Customer) ObjectContainer.currentUserLogin;
		String subscribeType = customer.getSubscribeType().getSubscribeType();
		float discountFromSale = getCurrentSaleDiscount();
		float totalDiscount = discountFromSale;
		float price = totalPrice;
		if (subscribeType.equals("REFULING_MIZDAMEN")) {
			// DO NOTHING
		} else if (subscribeType.equals("SINGLE_VEHICLE_MONTHLY")) {
			totalDiscount += getDiscountBySubscribeType(subscribeType);
		} else if (subscribeType.equals("MULTIPLE_VEHICLE_MONTHLY")) {
			totalDiscount += getDiscountBySubscribeType("SINGLE_VEHICLE_MONTHLY")
					+ getDiscountBySubscribeType("MULTIPLE_VEHICLE_MONTHLY");
		} else if (subscribeType.equals("SIGNLE_VEHICLE_FULL_MONTHLY")) {
			float prevPrice = getPreviousMonthFuelAmount();
			if(prevPrice > 0) {
				return prevPrice * ((100 - getDiscountBySubscribeType(subscribeType)) / 100);
			}
		}
		price = totalPrice * ((100 - totalDiscount) / 100);
		return price;
	}
	/**
	 * This method is responsible to request from the server the current sale template.
	 * @return - return the discount with the object details.
	 */
	private float getCurrentSaleDiscount() {
		Message msg = new Message(MessageType.GET_CURRENT_SALE_TEMPLATE, "");
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();

		return getDiscount(response);
	}

	private float getDiscount(JsonObject response) {
		if(response.get("saleData") == null) {
			return 0;
		}
		JsonObject saleData = response.get("saleData").getAsJsonObject();
		JsonArray saleTypes = saleData.get("saleTypes").getAsJsonArray();
		float discountRate = 0;
		for (int i = 0; i < saleTypes.size(); i++) {
			if (saleTypes.get(i).getAsString().equals(SaleTemplatePane.BY_FUEL_TYPE)) {
				String fuel = FuelType.enumToString(FuelType.stringToEnumVal(fuelType));
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
	 * This method is responsible to check the times.
	 * @param saleData - Json Object value
	 * @return - return boolean value.
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
	 * This method is responsible to request from the server to get previous amount fast fuel order.
	 * @return - return amount.
	 */
	private float getPreviousMonthFuelAmount() {
		JsonObject json = new JsonObject();
		String customerID = ((Customer) (ObjectContainer.currentUserLogin)).getCustomerId();
		json.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_PREVIOUS_AMOUNT_FAST_FUEL_ORDER, json.toString());
		ClientUI.accept(msg);

		float amount = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("amount").getAsFloat();
		return amount;
	}
	/**
	 * This method is responsible to get discount by subscribe type.
	 * @param type - string value of type.
	 * @return - return the discount number or 0 if not exist.
	 */
	private float getDiscountBySubscribeType(String type) {
		for (int i = 0; i < subscribeTypes.size(); i++) {
			JsonObject json = subscribeTypes.get(i).getAsJsonObject();
			if (json.get("subscribeType").getAsString().equals(type)) {
				return json.get("discountRate").getAsFloat();
			}
		}
		return 0;
	}
	/**
	 * This method is responsible to request from the server the fuel companies by customer id.
	 */
	private void getFuelCompaniesByCustomerID() {
		String customerID = ((Customer) ObjectContainer.currentUserLogin).getCustomerId();
		JsonObject json = new JsonObject();
		json.addProperty("customerID", customerID);
		Message msg = new Message(MessageType.GET_FUEL_COMPANIES_BY_CUSTOMER_ID, json.toString());
		ClientUI.accept(msg);

		String[] fuelCompanies = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies")
				.getAsString().split(",");
		cbFuelCompany.getItems().clear();
		cbFuelCompany.getItems().add("Choose company:");
		for (int i = 0; i < fuelCompanies.length; i++) {
			cbFuelCompany.getItems().add(fuelCompanies[i]);
		}

		cbFuelCompany.setValue(cbFuelCompany.getItems().get(0));
		cbFuelCompany.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				if ((Integer) number2 > 0) {
					getStationNumbersByFuelCompany(cbFuelCompany.getItems().get((Integer) number2));
					setErrorImage(imgCompany, "/images/v_icon.png");
				} else {
					setErrorImage(imgCompany, "/images/error_icon.png");
					cbStationNumber.getItems().clear();
					cbStationNumber.getItems().add("Choose company first");
					cbStationNumber.setValue(cbStationNumber.getItems().get(0));

				}
			}
		});
	}
	/**
	 * This method is responsible to get price per litter by fuel type.
	 * @param fuelType - string value of fuel type.
	 */
	public void getPricePerLitterByFuelType(String fuelType) {
		JsonObject json = new JsonObject();
		json.addProperty("fuelType", fuelType);
		Message msg = new Message(MessageType.GET_FUEL_BY_TYPE, json.toString());
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		pricePerLitter = response.get("pricePerLitter").getAsFloat();
	}
	/**
	 * This method is responsible to get station number by fuel company.
	 * @param fuelCompany - string value of fuel company.
	 */
	public void getStationNumbersByFuelCompany(String fuelCompany) {
		JsonObject json = new JsonObject();
		json.addProperty("fuelCompany", fuelCompany);
		Message msg = new Message(MessageType.GET_STATION_NUMBERS_BY_FUEL_COMPANY, json.toString());
		ClientUI.accept(msg);

		JsonArray stations = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("stations")
				.getAsJsonArray();
		cbStationNumber.getItems().clear();
		cbStationNumber.getItems().add("Choose station:");
		for (int i = 0; i < stations.size(); i++) {
			cbStationNumber.getItems().add(stations.get(i).getAsString());
		}
		cbStationNumber.setValue(cbStationNumber.getItems().get(0));
	}
}
