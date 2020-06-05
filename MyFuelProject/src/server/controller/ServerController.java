package server.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import entitys.DeterminingRateRequests;
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
			case GET_USER_DETAILS:
			case GET_USER_ID_BY_USERNAME:	
			case LOGOUT:
				messageFromServer = handleUserMessage(message);
				break;
			case GET_FUEL_BY_TYPE:
			case GET_FUEL_TYPES:
			case GET_FUEL_COMPANIES_NAMES:
			case UPDATE_FUEL:
			case SEND_RATE_REQUEST:
			case GET_RATES_REQUESTS:
			case UPDATE_DECISION:
			case GET_FUEL_INVENTORY_PER_STATION:
			case GET_STATION_BY_MANAGERID:	
			case UPDATE_FUEL_STATION_INVENTORY:	
			case GET_FUEL_INVENTORY_BY_USER_NAME:
				messageFromServer = handleFuelMessage(message);
				break;
			case GET_PURCHASE_MODELS:
				messageFromServer = handlePurchaseModelsMessage(message);
				break;
			case SUBMIT_HOME_HEATING_FUEL_ORDER:
			case GET_HOME_HEATING_FUEL_ORDERS:	
			case GET_ORDERS_BY_STATIONID_AND_FUEL_TYPE:
			case GET_ORDERS_BY_STATIONID_AND_QUARTER:
			case GET_ORDERS_BY_STATIONID_AND_SALE_NAME:
			case GET_ORDER_ID: //check with maman
				messageFromServer = handleOrderMessage(message);
				break;
			case CHECK_IF_CUSTOMER_EXIST:
			case GET_CUSTOMER_DETAILS_BY_USERNAME:
			case GET_SUBSCRIBE_TYPES:
			case CHECK_IF_VEHICLE_EXIST:
			case REGISTER_CUSTOMER:
			case GET_CUSTOMER_DETAILS_BY_ID:
			case UPDATE_CUSTOMER_DETAILS:
			case GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID:
			case GET_FUEL_COMPANIES_BY_CUSTOMER_ID:
			case GET_STATION_NUMBERS_BY_FUEL_COMPANY:
			case GET_DICOUNT_RATES_BY_TYPES:
			case REMOVE_VEHICLE_FROM_DB:
			case UPDATE_VEHICLES_IN_DB:
			case UPDATE_PURCHASE_MODEL_IN_DB:
				messageFromServer = handleCustomerMessage(message);
				break;
			case GET_SALE_TEMPLATES:
			case UPDATE_RUNNING_SALE:
			case GET_SALE_NAMES:
			case GET_CURRENT_SALE_TEMPLATE:
				messageFromServer = handleSaleTemplateMessage(message);
				break;
			case ADD_NEW_REPORT:
				messageFromServer = handleReportMessage(message);
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

	private Message handleSaleTemplateMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case GET_SALE_TEMPLATES: 
				JsonArray saleTemplates = dbConnector.saleDBLOgic.getSaleTemplates();
				responseJson.add("saleTemplates", saleTemplates);
			break;
		case UPDATE_RUNNING_SALE:{
				String saleName = requestJson.get("saleName").getAsString();
				int isRunning = requestJson.get("isRunning").getAsInt();
				dbConnector.saleDBLOgic.updateRunningSale(saleName, isRunning);
		}	break;
		case GET_SALE_NAMES:{
			JsonArray saleName = dbConnector.saleDBLOgic.getSaleNames();
			responseJson.add("saleNames", saleName);
		}	break;
		case GET_CURRENT_SALE_TEMPLATE:
			String saleTemplateName = dbConnector.saleDBLOgic.getCurrentRunningSaleName();
			responseJson.addProperty("saleTemplateName", saleTemplateName);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
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
		case GET_HOME_HEATING_FUEL_ORDERS: {
			String userName = requestJson.get("userName").getAsString();
			JsonArray HHFOrders = dbConnector.orderDBLogic.GetHomeHeatingFuelOrder(userName);
			responseJson.add("HHFOrders", HHFOrders);	
		}break;

		case GET_ORDERS_BY_STATIONID_AND_FUEL_TYPE:{
			JsonArray orders;
			String stationID=requestJson.get("stationID").getAsString();
			String fuelType=requestJson.get("fuelType").getAsString();
			orders = dbConnector.orderDBLogic.getOrdersDetailsByStationIdAndFuelType(stationID, fuelType);
			responseJson.add("orders", orders);
		}
		break;
		case GET_ORDERS_BY_STATIONID_AND_QUARTER: {
			JsonArray orders;
			String stationID = requestJson.get("stationID").getAsString();
			String quarter = requestJson.get("quarter").getAsString();
			String year=requestJson.get("year").getAsString();
			
			orders = dbConnector.orderDBLogic.getFastFuelOrdersByStationIdAndQuarter(stationID, quarter, year);
			responseJson.add("fastFuelOrders", orders);
			
			orders = dbConnector.orderDBLogic.getHomeHeatingFuelOrdersByStationIdAndQuarter(stationID, quarter, year);
			responseJson.add("homeHeatingFuelOrders", orders);
			
		
		}
			break;
		case GET_ORDERS_BY_STATIONID_AND_SALE_NAME:{
			JsonArray orders;
			String stationID = requestJson.get("stationID").getAsString();
			String saleName = requestJson.get("saleName").getAsString();
			
			orders = dbConnector.orderDBLogic.getHomeHeatingFuelOrdersByStationIdAndSaleName(stationID, saleName);
			responseJson.add("homeHeatingFuelOrders", orders);
			
			orders = dbConnector.orderDBLogic.getFastFuelOrdersByStationIdAndSaleName(stationID, saleName);
			responseJson.add("fastFuelOrders", orders);
		}break;

		case GET_ORDER_ID: {
			String orderIdResponse = dbConnector.orderDBLogic.getOrderId(requestJson);
			responseJson.addProperty("orderId",orderIdResponse);
		}break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	private Message handleReportMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();

		switch (msg.getMessageType()) {
		case ADD_NEW_REPORT: {
			boolean add= dbConnector.reportDBLogic.AddNewReport(requestJson
					.get("reportData").getAsJsonArray(),
					requestJson.get("reportType").getAsString());
			responseJson.addProperty("Add new report", ""+add);
		}
			break;
		case GET_ALL_REPORT: {

		}
			break;
		default:
			break;
		}

		messageFromServer = new Message(MessageType.SERVER_RESPONSE,
				responseJson.toString());
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
			responseJson = new Gson().fromJson(response, JsonObject.class);
		}
			break;
		case GET_FUEL_TYPES: {
			JsonArray fuelTypes = dbConnector.fuelDBLogic.getFuelTypes();
			responseJson.add("fuelTypes", fuelTypes);
		}
			break;
		case UPDATE_FUEL: {
			System.out.println("UPDATE_FULE !!");
			String fuelType = requestJson.get("fuelType").getAsString();
			String newPrice = requestJson.get("pricePerLitter").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);
			dbConnector.fuelDBLogic.updateFuel(fuel, newPrice);
		}
			break;
		case GET_FUEL_INVENTORY_PER_STATION: {
			JsonArray fuelInventory = dbConnector.fuelDBLogic
					.getFuelInventoryPerStation(requestJson.get("stationID")
							.getAsString());
			responseJson.add("fuelInventory", fuelInventory);
		}break;
		case SEND_RATE_REQUEST: {
			String fuelType = requestJson.get("fuelType").getAsString();
			String newPrice = requestJson.get("pricePerLitter").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);
			
			String createTime;
			SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy ' ' HH:mm ");
			Date date = new Date(System.currentTimeMillis());
			createTime=formatter.format(date);
			System.out.println(createTime);
			DeterminingRateRequests detRequest = new DeterminingRateRequests(-1 ,
					 fuel.getPricePerLitter(), Float.parseFloat(newPrice),
			 fuelType ,createTime);
			dbConnector.fuelDBLogic.SendRateRequest(detRequest, newPrice);

		}
		break;
		case GET_RATES_REQUESTS:{
			JsonArray RateRequests = dbConnector.fuelDBLogic.getRateRequests();
			responseJson.add("RateRequests", RateRequests);
			
		}
		break;
		case UPDATE_DECISION:{
			String decline = requestJson.get("reasonOfDecline").getAsString();
			boolean decision=requestJson.get("decision").getAsBoolean();
			String ID=requestJson.get("requestID").getAsString();
			dbConnector.fuelDBLogic.UpdateDecline(decline,decision,ID);
		}
		break;
		case GET_STATION_BY_MANAGERID:{
			String managerID = requestJson.get("employeNumber").getAsString();
			String stationID= dbConnector.fuelDBLogic.getStationIDbyManagerID(managerID);
			responseJson.addProperty("stationID", stationID);

		}
		break;
		case UPDATE_FUEL_STATION_INVENTORY:{
			String threshold = requestJson.get("thresholdAmount").getAsString();
			String maxAmount = requestJson.get("maxFuelAmount").getAsString();
			String fuelType = requestJson.get("fuelType").getAsString();
			dbConnector.fuelDBLogic.updateFuelInventory(threshold, maxAmount,fuelType);
		}
		break;
		case GET_FUEL_INVENTORY_BY_USER_NAME:{
			JsonArray array = dbConnector.fuelDBLogic.getFuelInventoryByUserName(requestJson.get("userName").getAsString());
			responseJson.add("fuelInventories", array);
		}break;

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
			
		case GET_CUSTOMER_DETAILS_BY_USERNAME:
			String customerDetails = dbConnector.customerDBLogic.getCustomerDetailsByUsername(requestJson.get("userName").getAsString());
			responseJson.addProperty("customerDetails", customerDetails);
			break;
		case GET_SUBSCRIBE_TYPES:
			JsonArray types = dbConnector.customerDBLogic.getSubscribeTypes();
			responseJson.add("subscribeTypes", types);
			break;
		case CHECK_IF_VEHICLE_EXIST:
			boolean vehicleIsExist = dbConnector.customerDBLogic.checkIfVehicleExist(requestJson);
			responseJson.addProperty("isExist", vehicleIsExist);
			break;
		case REGISTER_CUSTOMER:
				register(requestJson);
			break;
		case GET_CUSTOMER_DETAILS_BY_ID:
			String customerID = requestJson.get("customerID").getAsString();
			responseJson = dbConnector.customerDBLogic.getCustomerDetails(customerID);
			break;
		case UPDATE_CUSTOMER_DETAILS:
			responseJson = dbConnector.customerDBLogic.updateCustomerDetails(requestJson);
			break;
		case UPDATE_VEHICLES_IN_DB:
			responseJson = dbConnector.customerDBLogic.updateVehicleInDB(requestJson);
			break;
		case GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID:
			JsonArray vehicles = dbConnector.customerDBLogic.getVehiclesByCustomerID(requestJson.get("customerID").getAsString());
			responseJson.add("vehicles", vehicles);
			break;
		case GET_FUEL_COMPANIES_BY_CUSTOMER_ID:
			String companies = dbConnector.customerDBLogic.getFuelCompaniesByCustomerID(requestJson.get("customerID").getAsString());
			responseJson.addProperty("fuelCompanies", companies);
			break;
		case GET_STATION_NUMBERS_BY_FUEL_COMPANY:
			JsonArray stations = dbConnector.customerDBLogic.getFuelStationsByCompanyName(requestJson.get("fuelCompany").getAsString());
			responseJson.add("stations", stations);
			break;
		case GET_DICOUNT_RATES_BY_TYPES:
			String purchaseModel = requestJson.get("purchaseModel").getAsString();
			String fuelType = requestJson.get("fuelType").getAsString();

			float purchaseModelRate = dbConnector.purchaseModelDBLogic.getPurchaseModelDiscountByType(purchaseModel);
			JsonArray subscribeTypes = dbConnector.customerDBLogic.getSubscribeTypeDiscount();
			responseJson.add("subscribeTypes", subscribeTypes);
			responseJson.addProperty("purchaseModelRate", purchaseModelRate);
			break;
		case GET_PREVIOUS_AMOUNT_FAST_FUEL_ORDER:
			float previosAmount = dbConnector.customerDBLogic.getPreviousFastFuelOrdersAmount(requestJson.get("customerID").getAsString());
			responseJson.addProperty("amount", previosAmount);

			break;
		case REMOVE_VEHICLE_FROM_DB:
			dbConnector.customerDBLogic.removeVehicleFromDB(requestJson);
			break;
		case UPDATE_PURCHASE_MODEL_IN_DB:
			dbConnector.customerDBLogic.updatePurchaseModelByID(requestJson);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;

	}

	public void register(JsonObject requestJson) {
		dbConnector.userDBController.addUser(requestJson);
		dbConnector.customerDBLogic.addCustomer(requestJson);
		if(requestJson.get("creditCard") != null) {
			JsonObject creditCard = requestJson.get("creditCard").getAsJsonObject();
			dbConnector.customerDBLogic.addCreditCard(creditCard);
		}
		JsonArray vehicles = requestJson.get("vehicles").getAsJsonArray(); 
		for(int i = 0; i < vehicles.size(); i++) {
			JsonObject vehicle = vehicles.get(i).getAsJsonObject();
			dbConnector.customerDBLogic.addVehicle(vehicle);
		}
		
		JsonArray fuelCompanies = requestJson.get("purchaseModel").getAsJsonObject().get("fuelCompany").getAsJsonArray();
		dbConnector.customerDBLogic.addFuelCompanies(requestJson.get("customerID").getAsString(), fuelCompanies);
	}
	
	public Message handleUserMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case CHECK_LOGIN: {
			String userName = requestJson.get("userName").getAsString();
			String password = requestJson.get("password").getAsString();
			
			responseJson = dbConnector.userDBController.checkLogin(userName,password);
		}
			break;
		case CHECK_IF_USER_EXIST: 
			boolean isExist = dbConnector.userDBController
					.checkIfUsernameExist(requestJson.get("userName").getAsString());
			responseJson.addProperty("isExist", isExist);
			break;
		
		case GET_USER_DETAILS:
			responseJson = dbConnector.userDBController.getUserDetails(requestJson);
			String employeeRole = dbConnector.employeeDBLogic.getEmployeeRoleByUsername(requestJson.get("userName").getAsString());
			responseJson.addProperty("employeeRole", employeeRole);
			break;
		case GET_USER_ID_BY_USERNAME:
			String userName=requestJson.get("userName").getAsString();
			String employeeID = dbConnector.employeeDBLogic.getEmployeeIDByUsername(userName);
			//String employeeID = dbConnector.employeeDBLogic.getEmployeeRoleByUsername(requestJson.get("userName").getAsString());
			responseJson.addProperty("employeNumber", employeeID);
			break;
		case LOGOUT:
			dbConnector.userDBController.updateLoginFlag(requestJson.get("userName").getAsString(), 0);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}
}
