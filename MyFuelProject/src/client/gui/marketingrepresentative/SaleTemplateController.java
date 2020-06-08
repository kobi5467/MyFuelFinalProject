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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SaleTemplateController {

    @FXML
    private Pane mainSalePane;

    @FXML
    private ScrollPane spSaleContainer;

    @FXML
    private VBox vbSaleContainer;

    @FXML
    private Button btnAddSale;

    public ArrayList<SaleTemplatePane> saleTemplatePanes;
    public JsonArray saleTemplates;
    
    public static boolean waitForSaveButton;
    @FXML
    void onAddSale(ActionEvent event) {
    	if(waitForSaveButton)
    		return;
    	waitForSaveButton = true;
    	SaleTemplatePane saleTemplatePane = new SaleTemplatePane();
    	saleTemplatePane = saleTemplatePane.load(null);
    	saleTemplatePanes.add(saleTemplatePane);
    	vbSaleContainer.getChildren().add(saleTemplatePane.getMainPane());
    }

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
	private void initSaleTemplates() {
		vbSaleContainer.getChildren().clear();
		for (int i = 0; i < saleTemplates.size(); i++) {
			SaleTemplatePane saleTemplatePane = new SaleTemplatePane();
	    	saleTemplatePane = saleTemplatePane.load(saleTemplates.get(i).getAsJsonObject());
	    	saleTemplatePanes.add(saleTemplatePane);
	    	vbSaleContainer.getChildren().add(saleTemplatePane.getMainPane());
		}
	}
	
	public void showAllSaleTemplates() {
		vbSaleContainer.getChildren().clear();
		for(int i = 0; i < saleTemplatePanes.size(); i++) {
			vbSaleContainer.getChildren().add(saleTemplatePanes.get(i).getMainPane());
		}
	}

	private void getSaleTemplates() {
		Message msg = new Message(MessageType.GET_SALE_TEMPLATES,new JsonObject().toString());
		ClientUI.accept(msg);
		
		saleTemplates = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().
				get("saleTemplates").getAsJsonArray();
	}
}
