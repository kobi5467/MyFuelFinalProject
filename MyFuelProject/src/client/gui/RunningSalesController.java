package client.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.SaleTemplate;
import entitys.enums.FuelType;
import entitys.enums.MessageType;
import entitys.enums.SaleTemplateType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class RunningSalesController {

    @FXML
    private Pane runningSalesPane;

    @FXML
    private ScrollPane spSaleContainer;

    @FXML
    private VBox vbSaleContainer;

    private ArrayList<AnchorPane> salePanes;
    private JsonArray saleTemplates;
    
    public void load(Pane changePane) {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("RunningSalesForm.fxml"));

		try {
			runningSalesPane = loader.load();
			changePane.getChildren().add(runningSalesPane);
			ObjectContainer.runningSalesController = loader.getController();
			ObjectContainer.runningSalesController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void initUI() {
		saleTemplates = getSaleTemplatesArray();
		salePanes = new ArrayList<>();
		for(int i = 0; i < saleTemplates.size(); i++) {
			SalePane salePane = new SalePane();
			String color = i % 2 == 0 ? "#0240FF" : "#024079";
			AnchorPane pane = salePane.load(saleTemplates.get(i).getAsJsonObject(),color);
			salePanes.add(pane);
		}
		showSales();
	}
    
	public JsonArray getSaleTemplatesArray() {
		Message msg = new Message(MessageType.GET_SALE_TEMPLATES,"");
		ClientUI.accept(msg);
		
		JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray saleTemplates = responseJson.get("saleTemplates").getAsJsonArray();
		return saleTemplates;
	}
	
	public void showSales() {
		vbSaleContainer.getChildren().clear();
		for(int i = 0; i < salePanes.size(); i++) {
			vbSaleContainer.getChildren().add(salePanes.get(i));
		}
	}
}
