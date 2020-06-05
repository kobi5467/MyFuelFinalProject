package client.gui.customer;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
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
import javafx.scene.layout.Pane;

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

    // variables
    
    private float totalPrice = 0;
    private float priceAfterDiscount = 0;
    private float pricePerLitter = 0;
    private String fuelType = "";
    private JsonArray vehicles;
    private float purchaseModelRate = 0;
    private JsonArray subscribeTypes;
    
    @FXML
    void onSubmit(ActionEvent event) {
    	
    }

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
    
    private void initUI() {
    	lblFuelType.setText("");
    	lblPricePerLitter.setText("");
    	lblTotalPrice.setText("");
    	lblPriceAfterDiscount.setText("");
    	getFuelCompaniesByCustomerID();
    	getVehicleNumbersByCustomerID();
    	getDiscountRate();
    	txtFuelAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            String stringAmount = txtFuelAmount.getText();
            try {
            	float amount = Float.parseFloat(stringAmount);
            	totalPrice = amount * pricePerLitter;
            	lblTotalPrice.setText(String.format("%.2f", totalPrice));
            	priceAfterDiscount = calcTotalPrice();
            	lblPriceAfterDiscount.setText(String.format("%.2f", priceAfterDiscount));
            }catch(Exception e) {
            	System.out.println("NOT FLAOT INPUT");
            }
        });
    }
    
    private void getVehicleNumbersByCustomerID() {
    	String customerID = ((Customer)ObjectContainer.currentUserLogin).getCustomerId();
    	
    	JsonObject json = new JsonObject();
    	json.addProperty("customerID", customerID);
    	Message msg = new Message(MessageType.GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID,json.toString());
    	ClientUI.accept(msg);
    	
    	vehicles = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("vehicles").getAsJsonArray();
    	cbVehicleNumber.getItems().clear();
    	cbVehicleNumber.getItems().add("Choose vehicle:");
    	for(int i = 0; i < vehicles.size(); i++) {
    		cbVehicleNumber.getItems().add(vehicles.get(i).getAsJsonObject().get("vehicleNumber").getAsString());
    	}
    	cbVehicleNumber.setValue(cbVehicleNumber.getItems().get(0));
    	cbVehicleNumber.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  if((Integer)number2 > 0) {
		    		  fuelType = vehicles.get((Integer)number2 - 1).getAsJsonObject().get("fuelType").getAsString();
		    		  lblFuelType.setText(FuelType.enumToString(FuelType.stringToEnumVal(fuelType)));
		    		  getPricePerLitterByFuelType(fuelType);
		    		  lblPricePerLitter.setText(pricePerLitter + "");
		    	  }else {
		    		  pricePerLitter = 0;
		    	  }
		      }
    	});
    }
    
    private void getDiscountRate() {
    	String subscribeType = ((Customer)ObjectContainer.currentUserLogin).getSubscribeType().getSubscribeType();
    	String purchaseModel = ((Customer)ObjectContainer.currentUserLogin).getPurchaseModel().getPurchaseModeltype();
    	
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
    
    private float calcTotalPrice() {
    	Customer customer = (Customer)ObjectContainer.currentUserLogin;
    	String subscribeType = customer.getSubscribeType().getSubscribeType();
    	float discountFromSale = getCurrentSaleDiscount();
    	float price = totalPrice * ((purchaseModelRate + 100) / 100);
    	if(subscribeType.equals("REFULING_MIZDAMEN")) {
    		return price;
    	}else if(subscribeType.equals("SINGLE_VEHICLE_MONTHLY")) {
    		return (float)(price * (getDiscountBySubscribeType(subscribeType) / 100.0));
    	}else if(subscribeType.equals("MULTIPLE_VEHICLE_MONTHLY")) {
    		return (float)(price * (getDiscountBySubscribeType("SINGLE_VEHICLE_MONTHLY") * vehicles.size() + 
    				getDiscountBySubscribeType("MULTIPLE_VEHICLE_MONTHLY") / 100.0));
    	}else if(subscribeType.equals("SIGNLE_VEHICLE_FULL_MONTHLY")) {
    		return getPreviousMonthFuelAmount() * (getDiscountBySubscribeType(subscribeType) / 100);
    	}
    	return 0;
    }
    
    private float getCurrentSaleDiscount() {
    	Message msg = new Message(MessageType.GET_CURRENT_SALE_TEMPLATE,"");
    	ClientUI.accept(msg);
    	
    	JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
    	String saleTemplateName = response.get("saleTemplateName").getAsString();
    	System.out.println(saleTemplateName);
    	return 0;
	}

	private float getPreviousMonthFuelAmount() {
    	// add here the previusMonth fuel
    	// create method that get the amount of the previus month and calculate the new price.
    	JsonObject json = new JsonObject();
    	String customerID = ((Customer)(ObjectContainer.currentUserLogin)).getCustomerId();
    	json.addProperty("customerID", customerID);
    	Message msg = new Message(MessageType.GET_PREVIOUS_AMOUNT_FAST_FUEL_ORDER, json.toString());
    	ClientUI.accept(msg);
    	
    	float amount = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("amount").getAsFloat();
    	return amount;
    }
    
    private float getDiscountBySubscribeType(String type) {
    	for(int i = 0; i < subscribeTypes.size(); i++) {
    		JsonObject json = subscribeTypes.get(i).getAsJsonObject();
    		if(json.get("subscribeType").getAsString().equals(type)) {
    			return json.get("discountRate").getAsFloat();
    		}
    	}
    	return 0;
    }
    
    private void getFuelCompaniesByCustomerID() {
    	String customerID = ((Customer)ObjectContainer.currentUserLogin).getCustomerId();
    	JsonObject json = new JsonObject();
    	json.addProperty("customerID", customerID);
    	Message msg = new Message(MessageType.GET_FUEL_COMPANIES_BY_CUSTOMER_ID,json.toString());
    	ClientUI.accept(msg);
    	
    	String[] fuelCompanies = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelCompanies").getAsString().split(",");
    	cbFuelCompany.getItems().clear();
    	cbFuelCompany.getItems().add("Choose company:");
    	for(int i = 0; i < fuelCompanies.length; i++) {
    		cbFuelCompany.getItems().add(fuelCompanies[i]);
    	}
    	cbFuelCompany.setValue(cbFuelCompany.getItems().get(0));
    	cbFuelCompany.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  if((Integer)number2 > 0) {
		    		  getStationNumbersByFuelCompany(cbFuelCompany.getItems().get((Integer)number2));		    		  
		    	  }else {
		    		  cbStationNumber.getItems().clear();
		    		  cbStationNumber.getItems().add("Choose station:");
		    	  }
		      }
    	});
    }
    
    public void getPricePerLitterByFuelType(String fuelType) {
    	JsonObject json = new JsonObject();
    	json.addProperty("fuelType", fuelType);
    	Message msg = new Message(MessageType.GET_FUEL_BY_TYPE,json.toString());
    	ClientUI.accept(msg);
    	
    	JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		pricePerLitter = response.get("pricePerLitter").getAsFloat();	
    }
    
    public void getStationNumbersByFuelCompany(String fuelCompany) {
    	JsonObject json = new JsonObject();
    	json.addProperty("fuelCompany", fuelCompany);
    	Message msg = new Message(MessageType.GET_STATION_NUMBERS_BY_FUEL_COMPANY,json.toString());
    	ClientUI.accept(msg);
    	
    	JsonArray stations = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("stations").getAsJsonArray();
    	cbStationNumber.getItems().clear();
    	cbStationNumber.getItems().add("Choose station:");
    	for (int i = 0; i < stations.size(); i++) {
			cbStationNumber.getItems().add(stations.get(i).getAsString());
		}
    	cbStationNumber.setValue(cbStationNumber.getItems().get(0));
    }
}
