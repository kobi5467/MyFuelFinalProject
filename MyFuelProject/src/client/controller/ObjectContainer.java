package client.controller;

import client.gui.allusers.LoginController;
import client.gui.allusers.MainFormController;
import client.gui.allusers.MessageController;
import client.gui.ceo.RatesToApproveController;
import client.gui.customer.FastFuelController;
import client.gui.customer.HomeHeatingFuelController;
import client.gui.customer.OrderTrackingController;
import client.gui.marketingmanager.DeterminingFuelRatesController;
import client.gui.marketingmanager.ReportController;
import client.gui.marketingmanager.RunningSalesController;
import client.gui.marketingrepresentative.CustomerRegistrationController;
import client.gui.marketingrepresentative.UpdateCustomerController;
import client.gui.stationmanager.InventoryController;
import client.gui.supplier.SupplierController;
import entitys.Message;
import entitys.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

public class ObjectContainer {

	
	// **************************************** 		stages 			****************************************
	
	public static Stage loginStage;
	public static Stage mainStage;
	public static Stage messageStage;
	
	
	// **************************************** 	   controllers 		****************************************
	
	public static LoginController loginController;
	public static MainFormController mainFormController;
	
	// **************************************** 	   pane controllers	****************************************
	
	// MARKETING REPRESENTATIVE
	public static CustomerRegistrationController customerRegistrationController;
	public static UpdateCustomerController updateCustomerController;
	
	// MARKETING MANAGER
	public static DeterminingFuelRatesController determiningFuelRatesController;
	public static RunningSalesController runningSalesController;
	
	
	// CUSTOMER
	public static HomeHeatingFuelController homeHeatingFuelController;
	public static OrderTrackingController orderTrackingController;
	public static FastFuelController fastFuelController;
	
	// STATION MANAGER
	public static ReportController reportController;
	public static InventoryController inventoryController;
	// SUPPLIER
	public static SupplierController supplierController;
	
	//CEO
	public static RatesToApproveController ratesToApproveController;
	
	public static MessageController messageController;
	
	
	
	// ****************************************     global variables 	****************************************
	
	public static Message currentMessageFromServer;
	public static User currentUserLogin;
	public static boolean yesNoMessageResult = false;
	
	// ****************************************     global methods  	****************************************
		
	public static void showMessage(String type,String title, String msg) {
		if(ObjectContainer.messageController == null) {
			ObjectContainer.messageController = new MessageController();
		}
		ObjectContainer.messageController.start(type,title,msg);
	}
	
	public static boolean checkIfStringContainsOnlyNumbers(String val) {
		boolean flag = true;
		for (int i = 0; i < val.length(); i++) {
			if (!Character.isDigit(val.charAt(i))) {
				flag = false;
				break;
			}
		}
		return flag;
	}
	public static boolean checkIfStringContainsOnlyNumbersFloatType(String val) {
		boolean flag = true;
		for (int i = 0; i < val.length(); i++) {
			if (!Character.isDigit(val.charAt(i))) {
				if(val.charAt(i)=='.') {
					flag=true;
					continue;
				}
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	public static boolean checkIfStringContainsOnlyChar(String val) {
		for(int i = 0; i<val.length(); i++) {
			if(!Character.isAlphabetic(val.charAt(i)))
				return false;
		}
		return true;
	}
	
	
	public static void setTextFieldLimit(TextField txt, int limit) {	// set length limit for textfields.
		txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(txt.getText().length() > limit) {
            	txt.setText(txt.getText().substring(0, limit - 1));
            }
        });
	}
	
	// **************************************** Allow to drag the window **************************************** 
	
	private static double mouseDragDeltaX = 0;
    private static double mouseDragDeltaY = 0;
    private static EventHandler<MouseEvent> mousePressedHandler;
    private static EventHandler<MouseEvent> mouseDraggedHandler;
    private static WeakEventHandler<MouseEvent> weakMousePressedHandler;
    private static WeakEventHandler<MouseEvent> weakMouseDraggedHandler;
	public static void allowDrag(Node node, Stage stage) {
        mousePressedHandler = (MouseEvent event) -> {
            mouseDragDeltaX = node.getLayoutX() - event.getSceneX();
            mouseDragDeltaY = node.getLayoutY() - event.getSceneY();
        };
        weakMousePressedHandler = new WeakEventHandler<>(mousePressedHandler);
        node.setOnMousePressed(weakMousePressedHandler);
 
        mouseDraggedHandler = (MouseEvent event) -> {
            stage.setX(event.getScreenX() + mouseDragDeltaX);
            stage.setY(event.getScreenY() + mouseDragDeltaY);
        };
        weakMouseDraggedHandler = new WeakEventHandler<>(mouseDraggedHandler);
        node.setOnMouseDragged(weakMouseDraggedHandler);
    }

}
