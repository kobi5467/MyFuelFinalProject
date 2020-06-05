package client.gui.marketingrepresentative;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import client.gui.customer.OrderPane;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class CustomerVehiclesController {

    @FXML
    private Pane vehiclePane;

    @FXML
    private TextField txtVehicle;

    @FXML
    private TextField txtFuelType;

    @FXML
    private Button btnRemoveVehicle;

    private JsonObject vehicle;
    @FXML
    void onRemoveVehicle(ActionEvent event) {
    	String vehicleNumber = vehicle.get("vehicleNumber").getAsString();
    	ObjectContainer.showMessage("yes_no", "Remove Vehicle", "Are you sure you want to remove\n vehicle number " + vehicleNumber);
    	if(ObjectContainer.yesNoMessageResult) {
    		JsonObject removeVehicle = new JsonObject();
    		removeVehicle.addProperty("vehicleNumber", vehicleNumber);
    		Message msg = new Message(MessageType.REMOVE_VEHICLE_FROM_DB, removeVehicle.toString());
    		ClientUI.accept(msg);
    		ObjectContainer.updateCustomerController.updateVehicles(vehicleNumber);
    	}
    }
    public Pane getVehiclePane(){
    	return vehiclePane;
    }
    
	public CustomerVehiclesController load(JsonObject vehicle, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("CustomerVehicles.fxml"));
		CustomerVehiclesController pane = null;
		try {
			vehiclePane = loader.load();
			pane = loader.getController();
			pane.initUI(vehicle, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	private void initUI(JsonObject vehicle, String color) {
		this.vehicle = vehicle;
		vehiclePane.setStyle("-fx-background-color:" + color + ";");
		txtVehicle.setText(vehicle.get("vehicleNumber").getAsString());
		txtFuelType.setText(vehicle.get("fuelType").getAsString());
	}

}