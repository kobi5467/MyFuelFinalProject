package server.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import entitys.Fuel;
import entitys.Message;
import entitys.enums.MessageType;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server.dbLogic.DBConnector;

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
			switch (message.getMessageType()) {
			case CHECK_LOGIN:
			case CHECK_IF_USER_EXIST:
				messageFromServer = handleUserMessage(message);
				break;
			case GET_FUEL_BY_TYPE:
				messageFromServer = handleMarketingManagerMessage(message);
				break;
				
			case CHECK_IF_CUSTOMER_EXIST:{
				messageFromServer = handleCustomerMessage(message);
				break;
			}
			default:
				messageFromServer = new Message(MessageType.ERROR_TYPE_IS_UNSET, null);
				break;
			}

			client.sendToClient(messageFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Message handleCustomerMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case CHECK_IF_CUSTOMER_EXIST:{
			boolean isExist = dbConnector.customerDBLogic.checkIfCustomerExist(requestJson.get("customerID").getAsString());
			responseJson.addProperty("isExist", isExist);
		}break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.LOGIN_RESPONSE, responseJson.toString());
		return messageFromServer;

	}

	public Message handleUserMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject messageJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case CHECK_LOGIN: {
			boolean isValid = dbConnector.userDBController.checkLogin(messageJson);
			responseJson.addProperty("isValid", isValid);
			if (isValid) {
				String permission = dbConnector.userDBController.getUserPermission(messageJson);
				responseJson.addProperty("permission", permission);
			}
		}
			break;
		case CHECK_IF_USER_EXIST:{
			boolean isExist = dbConnector.userDBController.checkIfUsernameExist(messageJson.get("userName").getAsString());
			responseJson.addProperty("isExist", isExist);
		}
		default:
			break;

		}
		messageFromServer = new Message(MessageType.LOGIN_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	public Message handleMarketingManagerMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject messageJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case GET_FUEL_BY_TYPE: {
			String fuelType = messageJson.get("fuelType").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);
			String response = new Gson().toJson(fuel);
			messageFromServer = new Message(MessageType.SERVER_RESPONSE, response);
		}
			break;
		default:
			break;

		}
		return messageFromServer;
	}

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}
}
