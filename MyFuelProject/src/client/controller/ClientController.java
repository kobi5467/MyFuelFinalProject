package client.controller;

import java.io.IOException;

import entitys.Message;
import ocsf.client.AbstractClient;

public class ClientController extends AbstractClient {

	private volatile boolean waitingForResponse = false;
	
	public ClientController(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		waitingForResponse = false;
		Message message = (Message) msg;
		ObjectContainer.currentMessageFromServer = message;
	}

	/**
	 * This method send message to client.
	 */
	public void handleMessageFromClient(Object msg) {
		try {
			waitingForResponse = true;
			openConnection();
			sendToServer(msg);		
			while(waitingForResponse){
				try {
					Thread.sleep(100);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			ObjectContainer.showMessage("connection", "Connection Problem", "Connection Refused\nTry again later..");
			quit();
		}
	}
	
	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
