package server.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import server.gui.MainServerController;
/**
 * This method is responsible to run/stop the server.
 * @author oyomtov
 * @version - Final
 */
public class ServerUI extends Application {

	public static ServerController serverController;
	public static MainServerController mainServerController;
	public static Stage serverStage;
	
	public static void main(String[] args) {
		launch(args);
	}
	/**
	 * This method is responsible to start the process.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainServerController = new MainServerController();
		serverStage = primaryStage;
		mainServerController.start(primaryStage);
//		runServer("5555");
	}
	/**
	 * This method is responsible to run the server with the current port number.
	 * @param portNumber - string value of port number
	 */
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
	/**
	 * This method is responsible to stop the server.
	 */
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
