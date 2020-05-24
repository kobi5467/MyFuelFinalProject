package server.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
			case GET_FUEL_TYPES:
			case GET_FUEL_COMPANIES_NAMES:
				messageFromServer = handleFuelMessage(message);
				break;
			case GET_PURCHASE_MODELS:
				messageFromServer = handlePurchaseModelsMessage(message);
				break;
			case SUBMIT_HOME_HEATING_FUEL_ORDER:
				messageFromServer = handleOrderMessage(message);
				break;
			case CHECK_IF_CUSTOMER_EXIST:
			case GET_CUSTOMER_TYPES:
			case CHECK_IF_VEHICLE_EXIST:
				messageFromServer = handleCustomerMessage(message);
				break;
			default:
//				messageFromServer = new Message(MessageType.ERROR_TYPE_IS_UNSET, null);
				break;
			}

			client.sendToClient(messageFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Message handleOrderMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case SUBMIT_HOME_HEATING_FUEL_ORDER: {
			dbConnector.orderDBLogic.insertHomeHeatingFuelOrder(requestJson);
		}
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	private Message handleFuelMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case GET_FUEL_COMPANIES_NAMES: {
			JsonArray types = dbConnector.fuelDBLogic.getFuelCompanyNames();
			responseJson.add("fuelCompanies", types);
		}
			break;
		case GET_FUEL_BY_TYPE: {
			String fuelType = requestJson.get("fuelType").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);
			String response = new Gson().toJson(fuel);
			messageFromServer = new Message(MessageType.SERVER_RESPONSE, response);
		}
			break;

		case GET_FUEL_TYPES: {
			JsonArray fuelTypes = dbConnector.fuelDBLogic.getFuelTypes();
			responseJson.add("fuelTypes", fuelTypes);
			messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		}
			break;
		case UPDATE_FUEL: {
			String fuelType = requestJson.get("fuelType").getAsString();
			String newPrice = requestJson.get("pricePerLitter").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);
			dbConnector.fuelDBLogic.updateFuel(fuel, newPrice);
			messageFromServer = new Message(MessageType.SERVER_RESPONSE, "");
		}
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	private Message handlePurchaseModelsMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case GET_PURCHASE_MODELS: {
			JsonArray types = dbConnector.purchaseModelDBLogic.getPurchaseModelsTypes();
			responseJson.add("purchaseModelTypes", types);
		}
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	private Message handleCustomerMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case CHECK_IF_CUSTOMER_EXIST:
			boolean isExist = dbConnector.customerDBLogic.checkIfCustomerExist(requestJson.get("customerID").getAsString());
			responseJson.addProperty("isExist", isExist);
			break;
		case GET_CUSTOMER_TYPES:
			JsonArray types = dbConnector.customerDBLogic.getCustomerTypes();
			responseJson.add("customerTypes", types);
			break;
		case CHECK_IF_VEHICLE_EXIST:
			boolean vehicleIsExist = dbConnector.customerDBLogic.checkIfVehicleExist(requestJson);
			responseJson.addProperty("isExist", vehicleIsExist);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
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
		case CHECK_IF_USER_EXIST: {
			boolean isExist = dbConnector.userDBController
					.checkIfUsernameExist(messageJson.get("userName").getAsString());
			responseJson.addProperty("isExist", isExist);
		}
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
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

		case GET_FUEL_TYPES: {
			JsonArray fuelTypes = dbConnector.fuelDBLogic.getFuelTypes();
			responseJson.add("fuelTypes", fuelTypes);
			messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
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
