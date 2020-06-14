package client.gui.stationmanager;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class InventoryController {

   @FXML
    private Pane inventoryPane;

    @FXML
    private VBox vbInventoryContainer;

    private ArrayList<InventoryPane> inventoryPanes; 
    
	public void initUI() {
		inventoryPanes = new ArrayList<>();
		JsonArray array = getInventoryOfStation();
		for (int i = 0; i < array.size(); i++) {
			InventoryPane pane = new InventoryPane();
			inventoryPanes.add(pane.load(array.get(i).getAsJsonObject()));
		}
		showPanes();
	}
	
	private void showPanes() {
		vbInventoryContainer.getChildren().clear();
		for (int i = 0; i < inventoryPanes.size(); i++) {
			vbInventoryContainer.getChildren().add(inventoryPanes.get(i).getMainPane());
		}
	}

	public void updateFuelInventory(String threshold, String maxAmount,String fuelType) {
		JsonObject json = new JsonObject();
		json.addProperty("thresholdAmount", threshold);
		json.addProperty("maxFuelAmount", maxAmount);
		json.addProperty("fuelType", fuelType);

		Message msg = new Message(MessageType.UPDATE_FUEL_STATION_INVENTORY, json.toString());
		ClientUI.accept(msg);
	}
	
	public JsonArray getInventoryOfStation(){
		
		JsonObject json = new JsonObject();
		String userName =ObjectContainer.currentUserLogin.getUsername();
		json.addProperty("userName", userName);
		
		Message msg = new Message(MessageType.GET_FUEL_INVENTORY_BY_USER_NAME, json.toString());
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray fuelInventory = response.get("fuelInventories").getAsJsonArray();
		return fuelInventory;
	}
      
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("InventoryForm.fxml"));

		try {
			inventoryPane = loader.load();
			changePane.getChildren().add(inventoryPane);
			ObjectContainer.inventoryController = loader.getController();
			ObjectContainer.inventoryController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

