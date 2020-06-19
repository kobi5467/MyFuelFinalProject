package client.gui.marketingrepresentative;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
/**
 * This class is responsible to create new sale template and manage to sales.
 * @author Kobi Malka
 * @version - Final
 */
public class SaleTemplateController {

    @FXML
    private Pane mainSalePane;

    @FXML
    private ScrollPane spSaleContainer;

    @FXML
    private VBox vbSaleContainer;

    @FXML
    private Button btnAddSale;

    @FXML
    private Label lblNoSales;
    
    public ArrayList<SaleTemplatePane> saleTemplatePanes;
    public JsonArray saleTemplates;
    
    public static boolean waitForSaveButton;
    /**
     * This method is responsible to add new sale.
     * @param event - when we press on add sale button.
     */
    @FXML
    void onAddSale(ActionEvent event) {
    	if(waitForSaveButton)
    		return;
    	waitForSaveButton = true;
    	SaleTemplatePane saleTemplatePane = new SaleTemplatePane();
    	saleTemplatePane = saleTemplatePane.load(null);
    	saleTemplatePanes.add(saleTemplatePane);
    	vbSaleContainer.getChildren().add(saleTemplatePane.getMainPane());
    	lblNoSales.setVisible(false);
    }
    /**
     * This method is responsible to load 'xml' class and call to 'initUI'
     * @param changePane - Pane value with the current pane.
     */
    public void load(Pane changePane) {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SaleTemplateController.fxml"));

		try {
			Pane pane = loader.load();
			changePane.getChildren().add(pane);
			ObjectContainer.saleTemplateController = loader.getController();
			ObjectContainer.saleTemplateController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * This method is responsible to init the buttons, texts and etc.
     */
    public void initUI() {
    	waitForSaveButton = false;
    	saleTemplatePanes = new ArrayList<>();
    	vbSaleContainer.setSpacing(5);
    	btnAddSale.setVisible(ObjectContainer.currentUserLogin.getUserPermission()  == UserPermission.MARKETING_REPRESENTATIVE);
    	if(btnAddSale.isVisible()) {
    		btnAddSale.setId("dark-blue");
    	}
    	getSaleTemplates();
    	initSaleTemplates();
    }
    /**
     * This method is responsible to check if can run sale by sale template name.
     * @param saleTemplateName - string value of sale template name.
     * @return - return boolean value.
     */
    public boolean checkIfCanRunSale(String saleTemplateName) {
    	for (int i = 0; i < saleTemplates.size(); i++) {
			JsonObject currentSale = saleTemplates.get(i).getAsJsonObject();
			if(!currentSale.get("saleTemplateName").getAsString().equals(saleTemplateName) &&
					currentSale.get("isRunning").getAsInt() == 1) {
				return false;
			}
		}
    	return true;
    }
    /**
     * This method is responsible to init the sale template by dynamic pane.
     */
	private void initSaleTemplates() {
		vbSaleContainer.getChildren().clear();
		for (int i = 0; i < saleTemplates.size(); i++) {
			SaleTemplatePane saleTemplatePane = new SaleTemplatePane();
	    	saleTemplatePane = saleTemplatePane.load(saleTemplates.get(i).getAsJsonObject());
	    	saleTemplatePanes.add(saleTemplatePane);
	    	vbSaleContainer.getChildren().add(saleTemplatePane.getMainPane());
		}
		
		lblNoSales.setVisible(saleTemplates.size() == 0);
	}
	/**
	 * This method is responsible to show all the sale templates.
	 */
	public void showAllSaleTemplates() {
		vbSaleContainer.getChildren().clear();
		for(int i = 0; i < saleTemplatePanes.size(); i++) {
			vbSaleContainer.getChildren().add(saleTemplatePanes.get(i).getMainPane());
		}
		lblNoSales.setVisible(saleTemplatePanes.size() == 0);
	}
	/**
	 * This method is responsible to request from the server to get the sale template from DB.
	 */
	private void getSaleTemplates() {
		Message msg = new Message(MessageType.GET_SALE_TEMPLATES,new JsonObject().toString());
		ClientUI.accept(msg);
		
		saleTemplates = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().
				get("saleTemplates").getAsJsonArray();
	}
}
