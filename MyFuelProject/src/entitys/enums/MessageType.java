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
	GET_CUSTOMER_TYPES,
	
	
	// Vehicle
	CHECK_IF_VEHICLE_EXIST,
	
	
	// Orders
	CHECK_IF_ORDER_EXISTS,
	// home heating fuel
	SUBMIT_HOME_HEATING_FUEL_ORDER,
	
	// inventory orders
	GET_FUEL_INVENTORY_ORDERS,
	
	
	// Purchase models
	GET_PURCHASE_MODELS,
	
	// Fuel
	GET_FUEL_BY_TYPE,
	GET_FUEL_TYPES,
	UPDATE_FUEL,
	GET_FUEL_COMPANIES_NAMES,
	
	// Sale templates
	GET_SALE_NAMES,
	GET_SALE_TEMPLATES,
	
	// Report
	
	
	// Credit Card
	
	
	LOGOUT, // LOGOUT Message
	
	// ************************** server messages **************************
	//LOGIN
	SERVER_RESPONSE
}
