package client.gui;

import java.io.IOException;
import java.util.ArrayList;

import client.controller.ObjectContainer;
import entitys.Fuel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.dbLogic.DBConnector;

public class DeterminingFuelRatesController {

	@FXML
	private TextField txtCurrPrice;

	@FXML
	private TextField txtMaxPrice;

	@FXML
	private TextField txtNewPrice;

	@FXML
	private Button btnSubmit;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private Label lblErrorMessage;

	@FXML
	void onSubmit(ActionEvent event) {

		String currentPrice = txtCurrPrice.getText().trim();
		String maxPrice = txtMaxPrice.getText().trim();
		String newPrice = txtNewPrice.getText().trim();
		String errorMessage = "";

		if (newPrice.isEmpty() || cbFuelType.getValue().equals("Choose type")) {

			errorMessage = "Please fill all fields";
			lblErrorMessage.setText(errorMessage);
			// check if all fields are filled
		} else {
			checkFields(newPrice, currentPrice, maxPrice, errorMessage);
		}

	}

	public void checkFields(String newPrice, String currPrice, String maxPrice, String errorMessage) {
		int newPriceint = -1;
		int maxPriceint = -1;
		int currPriceint = -1;
		try {
			newPriceint = Integer.parseInt(newPrice);
			maxPriceint = Integer.parseInt(maxPrice);
			currPriceint = Integer.parseInt(currPrice);
			if (newPriceint > maxPriceint) {
				lblErrorMessage.setText("New price higher than the maximum price");
			} else {
				errorMessage = "";
				lblErrorMessage.setText(errorMessage);
			}
		} catch (Exception e) {
			lblErrorMessage.setText("Only digits!");
		}
	}

	public void showOptionOfFuelTypeChoiseBox() {
		ArrayList<String> fuelTypes= new ArrayList<>();
//		DBConnector db = new DBConnector();
//		fuelTypes=db.fuelDBLogic.getFuelTypes();
		
		cbFuelType.getItems().add("Choose type");
		int flag=0;
		for (String FuelType : fuelTypes) {
			
		
		cbFuelType.getItems().add(FuelType);
		flag=1;
		if(flag==1)
		continue;
		cbFuelType.getItems().add(FuelType);
		flag=2;
		if(flag==2)
		continue;
		cbFuelType.getItems().add(FuelType);
		flag=3;
		if (flag==3)
		continue;
		cbFuelType.getItems().add(FuelType);
		}
		cbFuelType.setValue(cbFuelType.getItems().get(0));
	}

	public void start(Stage primaryStage) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("DeterminingFuelRatesForm.fxml"));

		Pane root = loader.load();

		ObjectContainer.determiningFuelRatesController = loader.getController();
		ObjectContainer.determiningFuelRatesController.initUI();

		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//TestPanesBarak.controller.initUI();
	}
	
	private void afterChooseType() {
//		DBConnector db = new DBConnector();		//ArrayList<String> fuelTypes= new ArrayList<>();
//		Fuel fuel;
//		String type=cbFuelType.getValue();
//		System.out.println(type);
//		fuel = db.fuelDBLogic.getFuelObjectByType(cbFuelType.getValue());
//		txtCurrPrice.setText(fuel.getFuelType().toString());
	
	}

	private void initUI() {
		lblErrorMessage.setText("");
		showOptionOfFuelTypeChoiseBox();
		cbFuelType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//		        System.out.println(cbFuelType.getItems().get((Integer) number2));
		    	  afterChooseType();
		      }
		    });
		
		
	}

}
