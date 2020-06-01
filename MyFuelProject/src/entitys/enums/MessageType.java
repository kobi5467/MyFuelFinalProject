package entitys.enums;

public enum MessageType {

	// ************************** client messages **************************
	
	//LOGIN
	// User
	CHECK_LOGIN,
	CHECK_IF_USER_EXIST,
	GET_USER_DETAILS,
	
	// Customer
	CHECK_IF_CUSTOMER_EXIST,
	GET_SUBSCRIBE_TYPES,
	REGISTER_CUSTOMER,
	GET_CUSTOMER_DETAILS_BY_ID,
	GET_CUSTOMER_DETAILS_BY_USERNAME,
	UPDATE_CUSTOMER_DETAILS,
	
	GET_CUSTOMER_VEHICLES_BY_CUSTOMER_ID,		
	GET_STATION_NUMBERS_BY_FUEL_COMPANY,
	GET_FUEL_TYPE_BY_VEHICLE_NUMBER,
	GET_FUEL_COMPANIES_BY_CUSTOMER_ID,
	// Vehicle
	CHECK_IF_VEHICLE_EXIST,
	
	// Orders
	CHECK_IF_ORDER_EXISTS,
	GET_ORDER_BY_ID,
	GET_ORDERS_BY_STATIONID,
	
	// home heating fuel
	SUBMIT_HOME_HEATING_FUEL_ORDER,
	GET_HOME_HEATING_FUEL_ORDERS,
	
	// inventory orders
	GET_FUEL_INVENTORY_ORDERS,
	
	
	// fuel inventory per station
	GET_FUEL_INVENTORY_PER_STATION,
	
	// Purchase models
	GET_PURCHASE_MODELS,
	
	// Fuel
	GET_FUEL_BY_TYPE,
	GET_FUEL_TYPES,
	UPDATE_FUEL,
	GET_FUEL_COMPANIES_NAMES,
	
	// DetermineRates
	GET_RATES_REQUESTS,
	UPDATE_DECISION,
	SEND_RATE_REQUEST,
	
	// Sale templates
	GET_SALE_NAMES,
	GET_SALE_TEMPLATES,
	UPDATE_RUNNING_SALE,
	
	
	// Report
	
	
	// Credit Card
	
	
	LOGOUT, // LOGOUT Message
	
	// ************************** server messages **************************
	//LOGIN
	SERVER_RESPONSE
}
