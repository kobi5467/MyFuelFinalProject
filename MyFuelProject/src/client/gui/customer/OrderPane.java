package client.gui.customer;

import java.io.IOException;

import com.google.gson.JsonObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * This class is for only one sub-Order pane that will be loaded in the main OrderTrackingForm
 * every pane and its actions that we enable for the user to do will be handled in here
 * and how we want to present every single sub pane will handled in this class
 * @author Barak
 * @version final
 */

public class OrderPane {

	@FXML
	private AnchorPane mainOrderPane;

	@FXML
	private Pane viewPane;

	@FXML
	private Pane orderPane;
	@FXML
    private TextField txtOrderNumber;

    @FXML
    private TextField txtStatus;

    @FXML
    private TextField txtSupplyDate;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtTotalPrice;

    @FXML
    private TextField txtFuelCompany;

    @FXML
    private TextField txtCreateDate;

	/**
	 * This function gets an Home Heating Fuel order and load it to the main order Tracking Form.
	 * also gets the color of the pane. 
	 * @param  HHFOrder
	 * @param  color
	 * @return
	 */
	
	public OrderPane load(JsonObject HHFOrder, String color) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("OrderPane.fxml"));

		OrderPane pane = null;
		try {
			mainOrderPane = loader.load();
			pane = loader.getController();
			pane.initUI(HHFOrder, color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pane;
	}
	
	/**
	 *This function gets an Home Heating Fuel order and Initialize it.
	 * also gets the color of the pane. 	 * @param JsonObject HHFOrder
	 * @param  color
	 */

	public void initUI(JsonObject HHFOrder, String color) {
		mainOrderPane.setStyle(""
				+ "-fx-background-color:" + color + ";"
				+ "-fx-border-color:#77cde7;"
				+ "-fx-border-width:2px;");
		orderPane.setVisible(true);
		fillData(HHFOrder);
	}

	
	/**
	 * This function get Home heating fuel order and fill the data of the order
	 * in the pane fields.
	 * also makes it UnEditable.
	 * @param JsonObject HHFOrder
	 */
	private void fillData(JsonObject HHFOrder) {
		txtOrderNumber.setText(HHFOrder.get("orderID").getAsString());
		txtAmount.setText(HHFOrder.get("fuelAmount").getAsString());
		txtTotalPrice.setText(HHFOrder.get("totalPrice").getAsString());
		txtFuelCompany.setText(HHFOrder.get("fuelCompany").getAsString());
		txtSupplyDate.setText(HHFOrder.get("dateSupply").getAsString());
		txtCreateDate.setText(HHFOrder.get("orderDate").getAsString().split(" ")[0]);
		txtStatus.setText(HHFOrder.get("orderStatus").getAsString());
		
		String color = HHFOrder.get("orderStatus").getAsString().equals("OnGoing") ?
				"rgba(255,0,0,0.9)" : "rgba(0,255,0,0.9)";
		txtStatus.setStyle(""
				+ "-fx-background-color:#090a0c," + 
				"        radial-gradient(center 50% 0%, radius 100%,"+color+", rgba(255,255,255,0));" + 
				"	-fx-font-size: 10pt;" + 
				"	-fx-font-weight: bold;" + 
				"	-fx-text-fill:#ffffff;" + 
				"	-fx-border-color:#ffffff;" + 
				"	-fx-border-width:2px;");
		
		txtOrderNumber.setEditable(false);
		txtAmount.setEditable(false);
		txtTotalPrice.setEditable(false);
		txtFuelCompany.setEditable(false);
		txtStatus.setEditable(false);
		txtSupplyDate.setEditable(false);
		txtCreateDate.setEditable(false);
	}
	
	public AnchorPane getMainPane() {
		return mainOrderPane;
	}

}
