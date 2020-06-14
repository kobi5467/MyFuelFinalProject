package client.gui.marketingrepresentative;

import com.google.gson.JsonArray;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class AnalyticSystemController {

    @FXML
    private Pane mainAnalyticPane;

    @FXML
    private TableView<String> tblDataView;

    @FXML
    private Button btnShowAll;

    @FXML
    private Button btnCustomerType;

    @FXML
    private Button btnCertainHours;

    @FXML
    private Button btnFuelType;

    @FXML
    private Button btnSort;

    @FXML
    private ChoiceBox<String> cbCertainHours;

    @FXML
    private ChoiceBox<String> cbCustomerType;

    @FXML
    private ChoiceBox<String> cbFuelType;

    @FXML
    private Label lblTitleOfPane;

    @FXML
    void onCertainHours(ActionEvent event) {

    }

    @FXML
    void onCustomerType(ActionEvent event) {

    }

    @FXML
    void onFuelType(ActionEvent event) {

    }

    @FXML
    void onShowAll(ActionEvent event) {

    }

    @FXML
    void onSort(ActionEvent event) {

    }
    
    public void load(Pane changePane) {
    	
    }
    
    public void initUI() {
    	initCB();
    }

    private void initCB() {
    	Message msg = new Message(MessageType.GET_FUEL_TYPES,"");
    	ClientUI.accept(msg);
    	JsonArray fuelTypes = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelTypes").getAsJsonArray();
    	
    	ObjectContainer.setChoiceOptionOfChoiceBox(cbFuelType, fuelTypes, "Choose:");
    	
    	JsonArray hours = new JsonArray();
    	hours.add("7:00 - 12:00");
    	hours.add("12:00 - 20:00");
    	hours.add("20:00 - 7:00");
    	ObjectContainer.setChoiceOptionOfChoiceBox(cbCertainHours, hours, "Choose");
    	
    	JsonArray customerTypes = new JsonArray();
    	customerTypes.add("Private");
    	customerTypes.add("Company");
    	ObjectContainer.setChoiceOptionOfChoiceBox(cbCustomerType, customerTypes, "Choose");
    	
    }
}
