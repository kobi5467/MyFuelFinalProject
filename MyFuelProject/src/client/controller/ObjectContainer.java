package client.controller;

import client.gui.CustomerRegistrationController;
import client.gui.DeterminingFuelRatesController;
import client.gui.HomeHeatingFuelController;
import client.gui.LoginController;
import client.gui.MainFormController;
import client.gui.OrderTrackingController;
import client.gui.ReportControler;
import client.gui.RunningSalesController;
import client.gui.UpdateCustomerController;
import entitys.Message;
import entitys.User;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
	public static Stage errorStage;
	
	
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
	
	// STATION MANAGER
	public static ReportControler reportController;

	// SUPPLIER
	
	
	// ****************************************     global variables 	****************************************
	
	public static Message currentMessageFromServer;
	public static User currentUserLogin;
	
	// ****************************************     global methods  	****************************************
		
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
	
	public static boolean checkIfStringContainsOnlyChar(String val) {
		for(int i = 0; i<val.length(); i++) {
			if(!Character.isAlphabetic(val.charAt(i)))
				return false;
		}
		return true;
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
