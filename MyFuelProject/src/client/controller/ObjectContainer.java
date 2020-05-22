package client.controller;

import client.gui.CustomerRegistrationController;
import client.gui.DeterminingFuelRatesController;
import client.gui.LoginController;
import client.gui.MainFormController;
import entitys.Message;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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
	
	// MARKETING MANAGER
	public static DeterminingFuelRatesController determiningFuelRatesController;
	
	// CUSTOMER
	
	// STATION MANAGER

	// SUPPLIER
	
	
	// ****************************************     global variables 	****************************************
	
	public static Message currentMessageFromServer;
	
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
