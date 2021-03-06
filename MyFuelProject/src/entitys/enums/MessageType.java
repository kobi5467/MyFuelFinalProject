package entitys.enums;
/**
 * This class is for message enums objects
 * @author MyFuel Team
 *
 */
public enum MessageType {

	// ************************** client messages **************************
	
	//LOGIN
	// User
	CHECK_LOGIN,
	CHECK_IF_USER_EXIST,
	GET_USER_DETAILS,
	GET_USER_ID_BY_USERNAME,
	
	// Customer
	GET_CUSTOMERS_ID,
	CHECK_IF_CUSTOMER_EXIST,
	GET_SUBSCRIBE_TYPES,
	REGISTER_CUSTOMER,
	GET_CUSTOMER_DETAILS_BY_ID,
	GET_CREDIT_CARD_DETAILS_BY_ID,
	GET_CUSTOMER_DETAILS_BY_USERNAME,
	UPDATE_CUSTOMER_DETAILS,
	UPDATE_CREDIT_CARD_DETAILS,
	INSERT_CREDIT_CARD_DETAILS,
	GET_CUSTOMER_FUEL_TYPE,
	
	GET_DICOUNT_RATES_BY_TYPES,
	GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID,		
	GET_STATION_NUMBERS_BY_FUEL_COMPANY,
	GET_FUEL_TYPE_BY_VEHICLE_NUMBER,
	GET_FUEL_COMPANIES_BY_CUSTOMER_ID,
	GET_PREVIOUS_AMOUNT_FAST_FUEL_ORDER,
	UPDATE_PURCHASE_MODEL_IN_DB,

	// Vehicle
	CHECK_IF_VEHICLE_EXIST,
	REMOVE_VEHICLE_FROM_DB,
	UPDATE_VEHICLES_IN_DB,
	
	// Orders
	CHECK_IF_ORDER_EXISTS,
	GET_ORDER_BY_ID,
	GET_ORDERS_BY_STATIONID,
	GET_INVENTORY_ORDERS_FROM_DB_FOR_MARKETING_MANAGER,
	REMOVE_ORDER_FROM_DB,
	UPDATE_ORDER_IN_DB,
	GET_ORDERS_BY_DATES,	

	GET_ORDERS_BY_SUPLLIER_ID,
	UPDATE_FUEL_AMOUNT_INVENTORY,
	
	
	GET_ORDERS_BY_STATIONID_AND_QUARTER,
	GET_ORDERS_BY_STATIONID_AND_FUEL_TYPE,
	GET_ORDERS_BY_STATIONID_AND_SALE_NAME,
	

	// fast fuel orders
	ADD_FAST_FUEL_ORDER,
	
	// home heating fuel
	SUBMIT_HOME_HEATING_FUEL_ORDER,
	GET_HOME_HEATING_FUEL_ORDERS,
	GET_ORDER_ID,
	
	// inventory orders
	GET_FUEL_INVENTORY_ORDERS,
	GET_FUEL_INVENTORY_BY_USER_NAME,
	
	// fuel inventory per station
	GET_FUEL_INVENTORY_PER_STATION,
	GET_CURRENT_FUEL_AMOUNT_BY_FUEL_TYPE,
	
	// Purchase models
	GET_PURCHASE_MODELS,
	
	// Fuel
	GET_FUEL_BY_TYPE,
	GET_FUEL_TYPES,
	UPDATE_FUEL,
	GET_FUEL_COMPANIES_NAMES,
	GET_STATION_BY_MANAGERID,
	UPDATE_FUEL_STATION_INVENTORY,
	GET_SUBSCRIBE_RATE,
	SEND_SUBSCRIBE_RATE_REQUEST,
	UPDATE_DISCOUNT_RATE,
	UPDATE_DISCOUNT_DECISION,
	GET_DISCOUNT_REQUESTS,
	
	// DetermineRates
	GET_RATES_REQUESTS,
	UPDATE_DECISION,
	SEND_RATE_REQUEST,
	
	// Sale templates
	GET_SALE_NAMES,
	GET_SALE_TEMPLATES,
	UPDATE_RUNNING_SALE,
	GET_CURRENT_SALE_TEMPLATE,
	ADD_NEW_SALE_TEMPLATE,
	REMOVE_SALE_TEMPLATE,
	
	// Report
	ADD_NEW_REPORT,
	GET_CREAT_DATES_BY_REPORT_TYPE,
	GET_CREAT_DATES_BY_STATION_ID_AND_REPORT_TYPE,
	GET_STATION_ID_BY_REPORT_TYPE,
	GET_STATIONS_REPORTS,
	GET_MARKETING_MANAGER_REPORTS,
	// Credit Card
	
	//employee
	GET_STATION_ID_BY_USER_NAME,
	
	LOGOUT, // LOGOUT Message
	
	// activity tracking
	GET_ACTIVITY_TRACKING_DATA, 
	GET_CUSTOMER_RANKS,
	// ************************** server messages **************************
	//LOGIN
	SERVER_RESPONSE
}
