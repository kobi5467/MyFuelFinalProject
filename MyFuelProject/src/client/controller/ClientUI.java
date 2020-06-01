package client.controller;

import client.gui.allusers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.controller.ServerUI;

public class ClientUI extends Application{

	public static ClientController clientController;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		clientController = new ClientController("localhost", 5555);
		ObjectContainer.loginStage = primaryStage;
		ObjectContainer.loginStage.initStyle(StageStyle.UNDECORATED);
		ObjectContainer.loginController = new LoginController();
		ObjectContainer.loginController.start(primaryStage);
	}
	
	public static void accept(Object msg) {
		clientController.handleMessageFromClient(msg);			
	}
	
}