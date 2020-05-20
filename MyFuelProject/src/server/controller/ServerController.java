package server.controller;

import java.io.IOException;

import com.google.gson.JsonObject;

import entitys.Message;
import entitys.enums.MessageType;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server.dbLogic.DBConnector;
import server.dbLogic.UserDBController;

public class ServerController extends AbstractServer {

	public static DBConnector dbConnector;
	
	public ServerController(int port) {
		super(port);
		dbConnector = new DBConnector();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

		try {
			Message message = (Message) msg;
			Message messageFromServer = null;
			JsonObject messageJson = message.getMessageAsJsonObject();
			JsonObject responseJson = new JsonObject();
			switch (message.getMessageType()) {
			case CHECK_LOGIN:{
				boolean isValid = UserDBController.checkLogin(messageJson);
				responseJson.addProperty("isValid", isValid);
				if(isValid) {
					String permission = UserDBController.getUserPermission(messageJson);
					responseJson.addProperty("permission", permission);
				}
				messageFromServer = new Message(MessageType.LOGIN_RESPONSE, responseJson.toString());
			}break;
			
			default:{
				messageFromServer = new Message(MessageType.ERROR_TYPE_IS_UNSET,null);
			}break;
			}

			client.sendToClient(messageFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}
}
