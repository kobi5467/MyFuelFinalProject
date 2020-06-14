package server.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import server.gui.MainServerController;

public class ServerUI extends Application {

	public static ServerController serverController;
	public static MainServerController mainServerController;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		mainServerController = new MainServerController();
//		mainServerController.start(primaryStage);
		runServer("5555");
	}
	
	public static void runServer(String portNumber) {
		int port = 0;
		
		try {
			port = Integer.parseInt(portNumber);
		}catch (Throwable e) {
			return;
		}
		
		System.out.println(System.currentTimeMillis() + " START SERVER !!");
		serverController = new ServerController(port);
		
		try {
			serverController.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void stopServer() {
		try {
			serverController.stopListening();
			serverController.close();
			System.out.println("Server stop listening..");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
