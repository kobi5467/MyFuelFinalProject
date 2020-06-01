package client.gui.customer;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Customer;
import entitys.Message;
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

    // variables
    
    private float totalPrice = 0;
    private float pricePerLitter = 0;
    private String fuelType = "";
    
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
    	getFuelCompaniesByCustomerID();
    	getVehicleNumbersByCustomerID();
    }
    
    private void getVehicleNumbersByCustomerID() {
    	String customerID = ((Customer)ObjectContainer.currentUserLogin).getCustomerId();
    	
    	JsonObject json = new JsonObject();
    	json.addProperty("customerID", customerID);
    	Message msg = new Message(MessageType.GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID,json.toString());
    	ClientUI.accept(msg);
    	
    	JsonArray vehicles = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("vehicles").getAsJsonArray();
    	cbVehicleNumber.getItems().clear();
    	cbVehicleNumber.getItems().add("Choose vehicle:");
    	for(int i = 0; i < vehicles.size(); i++) {
    		cbVehicleNumber.getItems().add(vehicles.get(i).getAsJsonObject().get("vehicleNumber").getAsString());
    	}
    	cbVehicleNumber.setValue(cbVehicleNumber.getItems().get(0));
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
