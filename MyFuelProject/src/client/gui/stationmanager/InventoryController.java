package client.gui.stationmanager;

import java.io.IOException;

import client.controller.ObjectContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InventoryController {

	@FXML
	private Pane inventoryPane;
	
    @FXML
    private Label lblFuelType;

    @FXML
    private Label lblAmount;

    @FXML
    private Label lblThreshold;

    @FXML
    private Label lblMaxAmount;

    @FXML
    private TextField txtFuelType1;

    @FXML
    private TextField txtAmount1;

    @FXML
    private TextField txtThreshold1;

    @FXML
    private TextField txtMaxAmount1;

    @FXML
    private TextField txtFuelType2;

    @FXML
    private TextField txtAmount2;

    @FXML
    private TextField txtThreshold2;

    @FXML
    private TextField txtMaxAmount2;

    @FXML
    private TextField txtFuelType3;

    @FXML
    private TextField txtAmount3;

    @FXML
    private TextField txtThreshold3;

    @FXML
    private TextField txtMaxAmount3;

    @FXML
    private Button btnUpdate1;

    @FXML
    private Button btnUpdate2;

    @FXML
    private Button btnUpdate3;

    @FXML
    private Label lblErrorMessage1;

    @FXML
    private Label lblErrorMessage2;

    @FXML
    private Label lblErrorMessage3;

    @FXML
    void onUpdate1(ActionEvent event) {
    	String errorMessage = "";
    	String threshold1 = txtThreshold1.getText().trim();
    	String maxAmount1 = txtMaxAmount1.getText().trim();
    	if(!checkFields(txtThreshold1.getText(), txtMaxAmount1.getText())) {
    		errorMessage="Only Numbers!";
    		lblErrorMessage1.setText(errorMessage);
    	}
    	if(threshold1.isEmpty() || maxAmount1.isEmpty()) {
    		errorMessage="Please Fill All Fields!";
    		lblErrorMessage1.setText(errorMessage);
    	}
    	errorMessage="";
		lblErrorMessage1.setText(errorMessage);
    	
    	
    }
    
    public boolean checkFields(String Threshold, String maxAmount) {
    	boolean flag1= ObjectContainer.checkIfStringContainsOnlyNumbers(Threshold);
    	boolean flag2=ObjectContainer.checkIfStringContainsOnlyNumbers(maxAmount);
    	if(flag1 && flag2){
    		return true;
    	}
    	return false;
    }

    @FXML
    void onUpdate2(ActionEvent event) {
    	
    	String errorMessage = "";
    	String threshold2 = txtThreshold2.getText().trim();
    	String maxAmount2 = txtMaxAmount2.getText().trim();
    	if(!checkFields(txtThreshold2.getText(), txtMaxAmount2.getText())) {
    		errorMessage="Only Numbers!";
    		lblErrorMessage1.setText(errorMessage);
    	}
    	if(threshold2.isEmpty() || maxAmount2.isEmpty()) {
    		errorMessage="Please Fill All Fields!";
    		lblErrorMessage1.setText(errorMessage);
    	}
    	errorMessage="";
		lblErrorMessage1.setText(errorMessage);
    }

    @FXML
    void onUpdate3(ActionEvent event) {
    	String errorMessage = "";
    	String threshold3 = txtThreshold3.getText().trim();
    	String maxAmount3 = txtMaxAmount3.getText().trim();
    	if(!checkFields(txtThreshold3.getText(), txtMaxAmount3.getText())) {
    		errorMessage="Only Numbers!";
    		lblErrorMessage1.setText(errorMessage);
    	}
    	if(threshold3.isEmpty() || maxAmount3.isEmpty()) {
    		errorMessage="Please Fill All Fields!";
    		lblErrorMessage1.setText(errorMessage);
    	}
    	errorMessage="";
		lblErrorMessage1.setText(errorMessage);
    }
    
	public void initUI() {
		lblErrorMessage1.setText("");
		lblErrorMessage2.setText("");
		lblErrorMessage3.setText("");

		
		txtAmount1.setEditable(false);
		txtAmount2.setEditable(false);
		txtAmount3.setEditable(false);
		txtFuelType1.setEditable(false);
		txtFuelType2.setEditable(false);
		txtFuelType3.setEditable(false);
	}
	
	public void start(Stage primaryStage) throws IOException {

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

