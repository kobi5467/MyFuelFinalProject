package client.gui.marketingmanager;

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
    public JsonArray saleTemplates;
    
    public ArrayList<SalePane> salePanesControllers;
    
    private String color1 = "#36D1DC";//"#00B4DB";
    private String color2 = "#5B86E5";//"#0083B0";
    
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
		salePanesControllers = new ArrayList<>();
		for(int i = 0; i < saleTemplates.size(); i++) {
			SalePane salePane = new SalePane();
			salePanesControllers.add(salePane);
			String color = i % 2 == 0 ? color1 : color2;
			AnchorPane pane = salePane.load(color,i);
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

	public boolean onChangeSaleStatus(boolean isRunning, int index) {
		if(isRunning) {
			saleTemplates.get(index).getAsJsonObject().addProperty("isRunning", 0);
			Message msg = new Message(MessageType.UPDATE_RUNNING_SALE,saleTemplates.get(index).toString());
			ClientUI.accept(msg);
			return true;
		}
		
		if(checkIfCanStartSale(index)) {
			saleTemplates.get(index).getAsJsonObject().addProperty("isRunning", 1);
			Message msg = new Message(MessageType.UPDATE_RUNNING_SALE,saleTemplates.get(index).toString());
			ClientUI.accept(msg);
			return true;
		}
		return false;
	}
	
	public boolean checkIfCanStartSale(int index) {
		boolean isValid = true;
		
		for(int i = 0; i < saleTemplates.size(); i++) {
			if(i != index && saleTemplates.get(i).getAsJsonObject().get("isRunning").getAsInt() == 1) {
				isValid = false;
			}
		}
		
		return isValid;
	}
	
}
