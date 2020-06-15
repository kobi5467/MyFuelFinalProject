package client.gui.supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SupplierController {


    @FXML
    private Pane paneSupplier;

    @FXML
    private Label lblOrderId;

    @FXML
    private TextField tfOrderIdInput;

    @FXML
    private Button btnSearch;

    @FXML
    private Label lblError;

    @FXML
    private Label lblShowOnlyOpenOrder;

    @FXML
    private ScrollPane spOrders;

    @FXML
    private VBox vbocOrdersPane;

    @FXML
    private CheckBox cbShowOpenOrder;

    @FXML
    private Text txtOrderId;

    @FXML
    private Text txtFuelType;

    @FXML
    private Text txtAmount;

    @FXML
    private Text txtStatus;

    @FXML
    private Text txtStationId;

    @FXML
    private Text txtTotalPrice;

    @FXML
    private Button btnShowOpenOrder;
    @FXML
    private Text txtAllOrdersSupplied;
    @FXML
    private Text txtDate;
    @FXML
    private Text txtApproveDecline;
	private ArrayList<SupllierOrderPane> order = new ArrayList<>();
    private String orderid=null; 
    private Boolean isPress=false;
    private String supplierId="123456";

	// **************************************************On press  function**********************************************q
    
	@FXML
	void onPressShowOpenOrder(ActionEvent event) {
		txtAllOrdersSupplied.setVisible(false);
		this.isPress = !this.isPress;
		if (this.isPress) 
			btnShowOpenOrder.setText("isPress");
		else
			btnShowOpenOrder.setText("isntPress");
		getDataBySupplierID(this.supplierId, this.isPress, this.orderid);

	}
    
	@FXML
	void OnSearch(ActionEvent event) {
		txtAllOrdersSupplied.setVisible(false);
		if (checkOrderId()) {
			this.orderid = tfOrderIdInput.getText();
			getDataBySupplierID(this.supplierId, this.isPress, this.orderid);
		}
		else
			getDataBySupplierID(this.supplierId, this.isPress,null);
	}
	
	// **************************************************End of on press  function***************************************

	
	
	// **************************************************Initialize  function********************************************

	private void initUI() {
		vbocOrdersPane.setSpacing(5);
		String supplierID = "123456";
		txtAllOrdersSupplied.setVisible(false);
		lblError.setText("");
		lblError.setVisible(true);
		getDataBySupplierID(supplierID, false, this.orderid);
		setTableStyle();
	}
	
	private void setTableStyle() {
	txtFuelType.setStyle("-fx-font-weight: bold");
	txtAmount.setStyle("-fx-font-weight: bold");
	txtStatus.setStyle("-fx-font-weight: bold");
	txtStationId.setStyle("-fx-font-weight: bold");
	txtTotalPrice.setStyle("-fx-font-weight: bold");
	txtDate.setStyle("-fx-font-weight: bold");
	txtOrderId.setStyle("-fx-font-weight: bold");
	txtApproveDecline.setStyle("-fx-font-weight: bold");
	lblOrderId.setStyle("-fx-font-weight: bold");
	
	
	}

	@FXML
	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SupplierForm.fxml"));
		try {
			paneSupplier = loader.load();
			changePane.getChildren().add(paneSupplier);
			ObjectContainer.supplierController = loader.getController();
			ObjectContainer.supplierController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initSupplierOrdersVBox(JsonArray orders) {
		for (int i = 0; i < orders.size(); i++) {
			SupllierOrderPane supplierOrder = new SupllierOrderPane();
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			order.add(supplierOrder.load(orders.get(i).getAsJsonObject(), color));
			vbocOrdersPane.getChildren().add(order.get(i).getOrdersPane());
		}
	}
	private void initOpenSupplierOrdersVBox(JsonArray orders) {
		Boolean openOrderExist=false;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getAsJsonObject().get("orderStatus").getAsString().equals("waiting")) {
				SupllierOrderPane supplierOrder = new SupllierOrderPane();
				String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
				order.add(supplierOrder.load(orders.get(i).getAsJsonObject(), color));
				vbocOrdersPane.getChildren().add(order.get(i).getOrdersPane());
				openOrderExist=true;
			}
		}
		if(!openOrderExist&&isPress) 
		   txtAllOrdersSupplied.setVisible(true);
	}

	private void initSupplierOrderByIdVBox(JsonArray orders, String orderId) {
		Boolean orderIdExist=false;
		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getAsJsonObject().get("orderID").getAsString().equals(orderId)&&!orderIdExist) {
				SupllierOrderPane supplierOrder = new SupllierOrderPane();
				String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
				order.add(supplierOrder.load(orders.get(i).getAsJsonObject(), color));
				vbocOrdersPane.getChildren().add(order.get(i).getOrdersPane());
				orderIdExist=true;				
			}
		}
		if(!orderIdExist) 
			lblError.setText("Order id didnt exist!");
		
			
		
	}
	// **************************************************End of initialize  function*************************************
    
	
    
	// **************************************************Test  function**************************************************
	public boolean checkOrderId() {
		if (tfOrderIdInput.getText().isEmpty()) {
			getDataBySupplierID(this.supplierId, false, this.orderid);
			lblError.setText("Please enter order id!");
			return false;
		}
		else if(!ObjectContainer.checkIfStringContainsOnlyNumbers(tfOrderIdInput.getText())) {
			lblError.setText("Please enter only numbers!");
			return false;	
		}
			lblError.setText("");
			return true;
		
	}
	
	// **************************************************End of test  function*******************************************

	
	
	
	// **************************************************Data base  function*********************************************
	private void getDataBySupplierID(String supplierID, Boolean showOpenOrder, String OrderId) {
		JsonObject order = new JsonObject();
		order.addProperty("supplierID", supplierID);
		Message msg = new Message(MessageType.GET_ORDERS_BY_SUPLLIER_ID, order.toString());
		ClientUI.accept(msg);
		JsonObject orders = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ordersArray = orders.get("orders").getAsJsonArray();
		ordersArray = sortOrderByOrderDate(ordersArray);
		vbocOrdersPane.getChildren().clear();
		if (showOpenOrder) {
			initOpenSupplierOrdersVBox(ordersArray);
		}
		else if (OrderId != null) {
			initSupplierOrderByIdVBox(ordersArray, OrderId);
			OrderId = null;
		} else {
			initSupplierOrdersVBox(ordersArray);
		}
	}
	// **************************************************End of data base  function**************************************
	private JsonArray sortOrderByOrderDate(JsonArray jsonArray)  {
		List<JsonObject> list = new ArrayList<>();
		JsonArray sortedJsonArray = new JsonArray();
		  for(int i = 0; i < jsonArray.size() ;i++) {
		         list.add(jsonArray.get(i).getAsJsonObject());
		      }
		  Collections.sort(list, new Comparator<JsonObject>() {
			  @Override
			  public int compare(JsonObject a, JsonObject b) {
		            String str1 = new String();
		            String str2 = new String();
		            try {
		               str1 = (String)a.get("orderDate").getAsString();
		               str2 = (String)b.get("orderDate").getAsString();
		            } catch(JsonIOException e) {
		               e.printStackTrace();
		            }
		            return str1.compareTo(str2);
		         }
	      });
	      for(int i = 0; i < jsonArray.size(); i++) {
	          sortedJsonArray.add(list.get(i));
	       }
	      return sortedJsonArray;
	}  


}
