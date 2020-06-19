package client.gui.marketingrepresentative;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
/**
 * This class create a new pane for dynamic vehicles to show them on the gui in "UpdateCustomerController" class.
 * @author Or Yom Tov
 * @version - Final
 */
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
    
    /**
     * This method responsible to remove the vehicle from the DB by request that from the server.
     * @param event - ActionEvent from the gui when we press on remove vehicle button.
     */
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
    /**
     * This method responsible to return the main pane of this controller for show it on the GUI.
     * @return - Return pane.
     */
    public Pane getVehiclePane(){
    	return vehiclePane;
    }
    /**
     * This method responsible to load all the data from the other controller,
     * Include the json of the vehicle and the current color to show it,
     * After that call "initUI" method.
     * @param vehicle - Json of vehicle with all the details.
     * @param color - color for the background.
     * @return - Return this current vehicle.
     */
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
	/**
	 * This method responsible to init the vehicle details on the pane and call "setColor" method.
	 * @param vehicle - vehicle Json with all the details.
	 * @param color - color for the background.
	 */
	private void initUI(JsonObject vehicle, String color) {
		this.vehicle = vehicle;
		setButtonImage("../../../images/error_icon_30px.png", btnRemoveVehicle);
		btnRemoveVehicle.setText("");

		setColor(color);
		txtVehicle.setText(vehicle.get("vehicleNumber").getAsString());
		txtFuelType.setText(vehicle.get("fuelType").getAsString());
	}
	/**
	 * This method responsible to set the currenct color on the pane background.
	 * @param color - color for the backgroung
	 */
	public void setColor(String color) {
		vehiclePane.setStyle("-fx-background-color:" + color + ";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:3px;");
	}
	
    /**
     * this function set image to the button in the pane
     * @param url - the path
     * @param btn - the button
     */
    public void setButtonImage(String url, Button btn) {
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}	


}