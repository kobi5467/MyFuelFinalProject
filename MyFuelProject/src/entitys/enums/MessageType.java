package entitys.enums;

public enum MessageType {

	// ************************** client messages **************************
	
	//LOGIN
	CHECK_LOGIN,
	
	// MARKETING REPRESENTATIVE
	CHECK_IF_USER_EXIST,
	CHECK_IF_CUSTOMER_EXIST,
	
	// MARKETING MANAGER
	GET_FUEL_BY_TYPE,
	
	GET_PURCHASE_MODELS,
	GET_FUEL_COMPANIES_NAMES,
	GET_CUSTOMER_TYPES,
	
	// ************************** server messages **************************
	//LOGIN
	LOGIN_RESPONSE,	// can return null if the user doesn't exist or return the user.
	ERROR_TYPE_IS_UNSET, SERVER_RESPONSE
	
}
