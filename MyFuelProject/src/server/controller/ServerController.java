package server.controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
import entitys.DeterminingRateRequests;
import entitys.Fuel;
import entitys.Message;
import entitys.enums.MessageType;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server.dbLogic.DBConnector;


/**
 * this class is the server controller class, the class connects between the client controllers
 * to the DB logic classes and to the DB.
 * there's many handlers functions each with its purpose.
 * @author MyFuel Team
 * @version Final
 *
 */
public class ServerController extends AbstractServer {

	public static DBConnector dbConnector;

	public static long intervalForRankAnalysis = 24 * 60 * 60 * 1000;
	public ServerController(int port) {
		super(port);
		dbConnector = new DBConnector();
//		customerRanksAnalysis();
	}

	public void customerRanksAnalysis() {
		new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						System.out.println("Start generate ranks");
						dbConnector.customerDBLogic.generateRanks();
						Thread.sleep(intervalForRankAnalysis);						
					}catch (Exception e) {
						continue;
					}
				}
			}
		}.start();
	}
	
	/**
	 * this function handle the messages from the client, it gets a message from the client
	 * and with the right case of message it sends the request to the appropriate handler.
	 */
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
			case GET_STATION_ID_BY_USER_NAME:
				messageFromServer = handleEmployeeMessage(message);
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
			case GET_CURRENT_FUEL_AMOUNT_BY_FUEL_TYPE:
				messageFromServer = handleFuelMessage(message);
				break;
			case GET_PURCHASE_MODELS:
				messageFromServer = handlePurchaseModelsMessage(message);
				break;
			case SUBMIT_HOME_HEATING_FUEL_ORDER:
			case GET_HOME_HEATING_FUEL_ORDERS:
			case GET_ORDERS_BY_SUPLLIER_ID:
			case UPDATE_FUEL_AMOUNT_INVENTORY:
			case GET_ORDERS_BY_STATIONID_AND_FUEL_TYPE:
			case GET_ORDERS_BY_STATIONID_AND_QUARTER:
			case GET_ORDERS_BY_STATIONID_AND_SALE_NAME:
			case GET_ORDER_ID:
			case GET_ORDERS_FROM_DB:
			case REMOVE_ORDER_FROM_DB:
			case UPDATE_ORDER_IN_DB:
			case ADD_FAST_FUEL_ORDER:
			case GET_ORDERS_BY_DATES:
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
			case GET_CREDIT_CARD_DETAILS_BY_ID:
			case UPDATE_CREDIT_CARD_DETAILS:
			case INSERT_CREDIT_CARD_DETAILS:
			case GET_CUSTOMER_FUEL_TYPE:
			case GET_PREVIOUS_AMOUNT_FAST_FUEL_ORDER:
				messageFromServer = handleCustomerMessage(message);
				break;
			case GET_SALE_TEMPLATES:
			case UPDATE_RUNNING_SALE:
			case GET_SALE_NAMES:
			case GET_CURRENT_SALE_TEMPLATE:
			case ADD_NEW_SALE_TEMPLATE:
			case REMOVE_SALE_TEMPLATE:
				messageFromServer = handleSaleTemplateMessage(message);
				break;
			case ADD_NEW_REPORT:
			case GET_STATION_ID_BY_REPORT_TYPE:
			case GET_CREAT_DATES_BY_STATION_ID_AND_REPORT_TYPE:
			case GET_CREAT_DATES_BY_REPORT_TYPE:
			case GET_STATIONS_REPORTS:
			case GET_MARKETING_MANAGER_REPORTS:
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

	/**
	 * this function handle messages from the client that related to employees methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
	private Message handleEmployeeMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case GET_STATION_ID_BY_USER_NAME:
			String userName = requestJson.get("userName").getAsString();
			String stationID = dbConnector.employeeDBLogic.getStationIDByUserName(userName);
			responseJson.addProperty("stationID", stationID);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	/**
	 * this function handle messages from the client that related to Sale Templates methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
	private Message handleSaleTemplateMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case GET_SALE_TEMPLATES:
			JsonArray saleTemplates = dbConnector.saleDBLOgic.getSaleTemplates();
			responseJson.add("saleTemplates", saleTemplates);
			break;
		case UPDATE_RUNNING_SALE: {
			String saleName = requestJson.get("saleTemplateName").getAsString();
			int isRunning = requestJson.get("isRunning").getAsInt();
			dbConnector.saleDBLOgic.updateRunningSale(saleName, isRunning);
		}
			break;
		case GET_SALE_NAMES: {
			JsonArray saleName = dbConnector.saleDBLOgic.getSaleNames();
			responseJson.add("saleNames", saleName);
		}
			break;
		case GET_CURRENT_SALE_TEMPLATE:
			responseJson = dbConnector.saleDBLOgic.getCurrentRunningSaleName();
			break;
		case ADD_NEW_SALE_TEMPLATE:
			dbConnector.saleDBLOgic.addNewSaleTemplate(requestJson);
			break;
		case REMOVE_SALE_TEMPLATE:
			dbConnector.saleDBLOgic.removeSaleTemplate(requestJson);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	/**
	 * this function handle messages from the client that related to Orders methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
	private Message handleOrderMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case SUBMIT_HOME_HEATING_FUEL_ORDER: {
			dbConnector.orderDBLogic.insertHomeHeatingFuelOrder(requestJson);
		}
			break;
		case GET_HOME_HEATING_FUEL_ORDERS: { // check with or haim , about changes here.
			String userName = requestJson.get("userName").getAsString();
			JsonArray HHFOrders = dbConnector.orderDBLogic.GetHomeHeatingFuelOrder(userName);
			responseJson.add("HHFOrders", HHFOrders);
		}
			break;

		case GET_ORDERS_BY_STATIONID_AND_FUEL_TYPE: {
			JsonArray orders;
			String stationID = requestJson.get("stationID").getAsString();
			String fuelType = requestJson.get("fuelType").getAsString();
			orders = dbConnector.orderDBLogic.getOrdersDetailsByStationIdAndFuelType(stationID, fuelType);
			responseJson.add("orders", orders);
		}
			break;
		case GET_ORDERS_BY_STATIONID_AND_QUARTER: {
			JsonArray orders;
			String stationID = requestJson.get("stationID").getAsString();
			String quarter = requestJson.get("quarter").getAsString();
			String year = requestJson.get("year").getAsString();

			orders = dbConnector.orderDBLogic.getFastFuelOrdersByStationIdAndQuarter(stationID, quarter, year);
			responseJson.add("fastFuelOrders", orders);

		}
			break;
		case GET_ORDERS_BY_STATIONID_AND_SALE_NAME: {
			JsonArray orders;
			String saleName = requestJson.get("saleName").getAsString();

			orders = dbConnector.orderDBLogic.getHomeHeatingFuelOrdersBySaleName(saleName);
			responseJson.add("homeHeatingFuelOrders", orders);

			orders = dbConnector.orderDBLogic.getFastFuelOrdersBySaleName(saleName);
			responseJson.add("fastFuelOrders", orders);
		}
			break;
		case GET_ORDERS_BY_DATES: {
			JsonArray customerDetails;
			String startDate = requestJson.get("startDate").getAsString();
			String endDate = requestJson.get("endDate").getAsString();
			customerDetails = dbConnector.orderDBLogic.getTotalPriceForAllCustomers(startDate, endDate);
			responseJson.add("customerDetails", customerDetails);
		}
			break;
		case GET_ORDER_ID: {
			String orderIdResponse = dbConnector.orderDBLogic.getOrderId(requestJson);
			responseJson.addProperty("orderId", orderIdResponse);
		}
			break;
		case GET_ORDERS_BY_SUPLLIER_ID:{
			String supplierID = requestJson.get("supplierID").getAsString();
			JsonArray orders = dbConnector.orderDBLogic.getInvertoryOrdersByID(supplierID);
			responseJson.add("orders", orders);
		}	break;
		case GET_ORDERS_FROM_DB:{
			JsonArray orders = dbConnector.orderDBLogic.getInvertoryOrdersFromDB(requestJson);
			responseJson.add("orders", orders);
		}	break;
		case REMOVE_ORDER_FROM_DB:
			dbConnector.orderDBLogic.removeOrderFromDB(requestJson);
			break;
		case UPDATE_ORDER_IN_DB:
			responseJson = dbConnector.orderDBLogic.updateOrderDetails(requestJson);
			break;
		case ADD_FAST_FUEL_ORDER:
			String stationID = requestJson.get("stationID").getAsString();
			String fuelType = requestJson.get("fuelType").getAsString();
			float amount = requestJson.get("amountOfLitters").getAsFloat();
			dbConnector.orderDBLogic.addFastFuelOrder(requestJson);
			dbConnector.fuelDBLogic.updateFuelAmountByStationIDFuelTypeAndAmount(fuelType, stationID, amount);
			boolean createNewOrder = dbConnector.fuelDBLogic.checkIfNeedToCreateInventoryOrder(stationID, fuelType);
			if(createNewOrder) {
				dbConnector.fuelDBLogic.createInventoryOrderByFuelTypeAndStationID(stationID, fuelType);
			}
			break;
		case UPDATE_FUEL_AMOUNT_INVENTORY:
			dbConnector.fuelDBLogic.updateFuelAmountByStationIDFuelTypeAndAmount(requestJson.get("fuelType").getAsString(),
					requestJson.get("stationID").getAsString(), (-1) * requestJson.get("fuelAmount").getAsFloat());
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	/**
	 * this function handle messages from the client that related to Reports methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
	private Message handleReportMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();

		switch (msg.getMessageType()) {
		case ADD_NEW_REPORT: {
			boolean add = dbConnector.reportDBLogic.AddNewReport(requestJson.get("reportData").getAsJsonObject(),
					requestJson.get("reportType").getAsString());
			responseJson.addProperty("Add new report", "" + add);
		}
			break;
		case GET_STATION_ID_BY_REPORT_TYPE: {
			JsonArray stationsID = new JsonArray();
			stationsID = dbConnector.reportDBLogic.getStationsIDByReportType(requestJson.get("reportType").getAsString());
			responseJson.add("stations", stationsID);
		}
			break;
		case GET_CREAT_DATES_BY_STATION_ID_AND_REPORT_TYPE: {
			JsonArray createDates = new JsonArray();
			createDates = dbConnector.reportDBLogic.getCreateDatesByStationsIdAndReportType(
					requestJson.get("reportType").getAsString(), requestJson.get("stationID").getAsString());
			responseJson.add("createDates", createDates);
		}
			break;
		case GET_CREAT_DATES_BY_REPORT_TYPE: {
			JsonArray createDates = new JsonArray();
			createDates = dbConnector.reportDBLogic
					.getCreateDatesByReportType(requestJson.get("reportType").getAsString());
			responseJson.add("createDates", createDates);
		}
			break;
		case GET_STATIONS_REPORTS: {
			responseJson = dbConnector.reportDBLogic.getStationReports(requestJson.get("reportType").getAsString(),
							requestJson.get("stationId").getAsString(), requestJson.get("date").getAsString());
		}
			break;
		case GET_MARKETING_MANAGER_REPORTS: {
			responseJson = dbConnector.reportDBLogic.getMarketingManagerReports(
					requestJson.get("reportType").getAsString(), requestJson.get("date").getAsString());
		}
			break;
		default:
			break;
		}

		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}
	/**
	 * this function handle messages from the client that related to Fuel methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */

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
			String fuelType = requestJson.get("fuelType").getAsString();
			String newPrice = requestJson.get("pricePerLitter").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);
			dbConnector.fuelDBLogic.updateFuel(fuel, newPrice);
		}
			break;
		case GET_FUEL_INVENTORY_PER_STATION: {
			JsonArray fuelInventory = dbConnector.fuelDBLogic
					.getFuelInventoryPerStation(requestJson.get("stationID").getAsString());
			responseJson.add("fuelInventory", fuelInventory);
		}
			break;
		case SEND_RATE_REQUEST: {
			String fuelType = requestJson.get("fuelType").getAsString();
			String newPrice = requestJson.get("pricePerLitter").getAsString();
			Fuel fuel = dbConnector.fuelDBLogic.getFuelObjectByType(fuelType);

			String createTime;
			createTime = ObjectContainer.getCurrentDate();
			DeterminingRateRequests detRequest = new DeterminingRateRequests(-1, fuel.getPricePerLitter(),
					Float.parseFloat(newPrice), fuelType, createTime);
			dbConnector.fuelDBLogic.SendRateRequest(detRequest, newPrice);

		}
			break;
		case GET_RATES_REQUESTS: {
			JsonArray RateRequests = dbConnector.fuelDBLogic.getRateRequests();
			responseJson.add("RateRequests", RateRequests);

		}
			break;
		case UPDATE_DECISION: {
			String decline = requestJson.get("reasonOfDecline").getAsString();
			boolean decision = requestJson.get("decision").getAsBoolean();
			String ID = requestJson.get("requestID").getAsString();
			dbConnector.fuelDBLogic.UpdateDecline(decline, decision, ID);
		}
			break;
		case GET_STATION_BY_MANAGERID: {
			String managerID = requestJson.get("employeNumber").getAsString();
			String stationID = dbConnector.fuelDBLogic.getStationIDbyManagerID(managerID);
			responseJson.addProperty("stationID", stationID);

		}
			break;
		case UPDATE_FUEL_STATION_INVENTORY: {
			String userName=requestJson.get("userName").getAsString();
			String threshold = requestJson.get("thresholdAmount").getAsString();
			String maxAmount = requestJson.get("maxFuelAmount").getAsString();
			String fuelType = requestJson.get("fuelType").getAsString();
			dbConnector.fuelDBLogic.updateFuelInventory(threshold, maxAmount, fuelType,userName);
		}
			break;
		case GET_FUEL_INVENTORY_BY_USER_NAME: {
			JsonArray array = dbConnector.fuelDBLogic
					.getFuelInventoryByUserName(requestJson.get("userName").getAsString());
			responseJson.add("fuelInventories", array);
		}
			break;
			
		case GET_CURRENT_FUEL_AMOUNT_BY_FUEL_TYPE:
			float available = dbConnector.fuelDBLogic.getAvailableAmountOfFuelByTypeAndStationID(
					requestJson.get("fuelType").getAsString(), requestJson.get("stationID").getAsString());
			responseJson.addProperty("availableAmount", available);;
			break;

		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;
	}

	/**
	 * this function handle messages from the client that related to Purchase Models methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
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

	/**
	 * this function handle messages from the client that related to Customer methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
	private Message handleCustomerMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case CHECK_IF_CUSTOMER_EXIST:
			boolean isExist = dbConnector.customerDBLogic
					.checkIfCustomerExist(requestJson.get("customerID").getAsString());
			responseJson.addProperty("isExist", isExist);
			break;

		case GET_CUSTOMER_DETAILS_BY_USERNAME:
			responseJson = dbConnector.customerDBLogic
					.getCustomerDetailsByUsername(requestJson.get("userName").getAsString());
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
		case GET_CREDIT_CARD_DETAILS_BY_ID:
			String customerid = requestJson.get("customerID").getAsString();
			responseJson = dbConnector.customerDBLogic.getCreditCardDetails(customerid);
			break;
		case UPDATE_CUSTOMER_DETAILS:
			responseJson = dbConnector.customerDBLogic.updateCustomerDetails(requestJson);
			break;
		case UPDATE_CREDIT_CARD_DETAILS:
			responseJson = dbConnector.customerDBLogic.updateCreditCardDetails(requestJson);
			break;
		case UPDATE_VEHICLES_IN_DB:
			responseJson = dbConnector.customerDBLogic.updateVehicleInDB(requestJson);
			break;
		case GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID:
			JsonArray vehicles = dbConnector.customerDBLogic
					.getVehiclesByCustomerID(requestJson.get("customerID").getAsString());
			responseJson.add("vehicles", vehicles);
			break;
		case GET_FUEL_COMPANIES_BY_CUSTOMER_ID:
			String companies = dbConnector.customerDBLogic
					.getFuelCompaniesByCustomerID(requestJson.get("customerID").getAsString());
			responseJson.addProperty("fuelCompanies", companies);
			break;
		case GET_STATION_NUMBERS_BY_FUEL_COMPANY:
			JsonArray stations = dbConnector.customerDBLogic
					.getFuelStationsByCompanyName(requestJson.get("fuelCompany").getAsString());
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
			float previosAmount = dbConnector.customerDBLogic
					.getPreviousFastFuelOrdersAmount(requestJson.get("customerID").getAsString());
			responseJson.addProperty("amount", previosAmount);

			break;
		case REMOVE_VEHICLE_FROM_DB:
			dbConnector.customerDBLogic.removeVehicleFromDB(requestJson);
			break;
		case UPDATE_PURCHASE_MODEL_IN_DB:
			dbConnector.customerDBLogic.updatePurchaseModelByID(requestJson);
			break;
		case INSERT_CREDIT_CARD_DETAILS:
			dbConnector.customerDBLogic.insertCreditCard(requestJson);
		case GET_CUSTOMER_FUEL_TYPE:
			responseJson = dbConnector.customerDBLogic.getFuelCompaniesByID(requestJson);
			break;
		default:
			break;
		}
		messageFromServer = new Message(MessageType.SERVER_RESPONSE, responseJson.toString());
		return messageFromServer;

	}
	/**
	 * this function handle request to register a customer to the system
	 * it gets the request JsonObject with the details and calls to the customerDBLogic methods
	 * @param requestJson - the request as JsonObject with all the data
	 */
	public void register(JsonObject requestJson) {
		dbConnector.userDBController.addUser(requestJson);
		dbConnector.customerDBLogic.addCustomer(requestJson);
		if (requestJson.get("creditCard") != null) {
			JsonObject creditCard = requestJson.get("creditCard").getAsJsonObject();
			dbConnector.customerDBLogic.addCreditCard(creditCard);
		}
		JsonArray vehicles = requestJson.get("vehicles").getAsJsonArray();
		for (int i = 0; i < vehicles.size(); i++) {
			JsonObject vehicle = vehicles.get(i).getAsJsonObject();
			dbConnector.customerDBLogic.addVehicle(vehicle);
		}

		JsonArray fuelCompanies = requestJson.get("purchaseModel").getAsJsonObject().get("fuelCompany")
				.getAsJsonArray();
		dbConnector.customerDBLogic.addFuelCompanies(requestJson.get("customerID").getAsString(), fuelCompanies);
	}

	/**
	 * this function handle messages from the client that related to User methods.
	 * it goes to the right case and activate the right function of DBLogic.
	 * @param msg - the message from the client
	 * @return - response message from the server to the client
	 */
	public Message handleUserMessage(Message msg) {
		Message messageFromServer = null;
		JsonObject requestJson = msg.getMessageAsJsonObject();
		JsonObject responseJson = new JsonObject();
		switch (msg.getMessageType()) {
		case CHECK_LOGIN: {
			String userName = requestJson.get("userName").getAsString();
			String password = requestJson.get("password").getAsString();

			responseJson = dbConnector.userDBController.checkLogin(userName, password);
		}
			break;
		case CHECK_IF_USER_EXIST:
			boolean isExist = dbConnector.userDBController
					.checkIfUsernameExist(requestJson.get("userName").getAsString());
			responseJson.addProperty("isExist", isExist);
			break;

		case GET_USER_DETAILS:
			responseJson = dbConnector.userDBController.getUserDetails(requestJson);
			String employeeRole = dbConnector.employeeDBLogic
					.getEmployeeRoleByUsername(requestJson.get("userName").getAsString());
			responseJson.addProperty("employeeRole", employeeRole);
			break;
		case GET_USER_ID_BY_USERNAME:
			String userName = requestJson.get("userName").getAsString();
			String employeeID = dbConnector.employeeDBLogic.getEmployeeIDByUsername(userName);
			// String employeeID =
			// dbConnector.employeeDBLogic.getEmployeeRoleByUsername(requestJson.get("userName").getAsString());
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

	/**
	 * this function prints that the server is started and listening for connections
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
	}
}
