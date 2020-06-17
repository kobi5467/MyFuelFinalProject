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

/**
 * This class is the gui controller class of Inventory form.
 * this class will display the inventory of the current station of the manager logged in
 * it will allow him to set new threshold and max amount of the fuel types in his/her station. 
 * @author Barak
 * @version final
 */

public class InventoryController {

   @FXML
    private Pane inventoryPane;

    @FXML
    private VBox vbInventoryContainer;

    private ArrayList<InventoryPane> inventoryPanes; 
    
    
    /**
     * this function will init the form and display all of the fuels inventory panes in this 
     * main pane of the form.
     */
	public void initUI() {
		inventoryPanes = new ArrayList<>();
		JsonArray array = getInventoryOfStation();
		for (int i = 0; i < array.size(); i++) {
			InventoryPane pane = new InventoryPane();
			inventoryPanes.add(pane.load(array.get(i).getAsJsonObject()));
		}
		showPanes();
	}
	
	
	/**
	 * this fucntion get the seperated fuel types inventory panes into the VBOX
	 * and show them.
	 */
	private void showPanes() {
		vbInventoryContainer.getChildren().clear();
		for (int i = 0; i < inventoryPanes.size(); i++) {
			vbInventoryContainer.getChildren().add(inventoryPanes.get(i).getMainPane());
		}
	}

	/**
	 * this function get the new threshold, max amount, fuel type and user name
	 * then it send a message to the server with the request to update the 
	 * specific fuel type inventory.
	 * @param threshold - the new threshold to update (can be set as the same threshold as before)
	 * @param maxAmount - the new max amount to update (can be set as the same threshold as before)
	 * @param fuelType - the specific fuel type we want to update 
	 * @param userName - the user name of the station manager so the server, next
	 * in the DB logic, could find by it the manager ID and his/her station ID
	 */
	public void updateFuelInventory(String threshold, String maxAmount,String fuelType, String userName) {
		JsonObject json = new JsonObject();
		
		json.addProperty("userName", userName);
		json.addProperty("thresholdAmount", threshold);
		json.addProperty("maxFuelAmount", maxAmount);
		json.addProperty("fuelType", fuelType);

		Message msg = new Message(MessageType.UPDATE_FUEL_STATION_INVENTORY, json.toString());
		ClientUI.accept(msg);
	}
	
	/**
	 * this function send message to the server with the request to bring
	 * the inventory of specific station. the inventory of the station returned in 
	 * a JsonArray with all the details.
	 * @return JsonArray fuelInventory - array with the inventory of the station by types.
	 */
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
      /**
       * this funciton loads the main pane inventory controller.
       * @param changePane
       */
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

