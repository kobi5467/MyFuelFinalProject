package entitys.enums;

public enum MessageType {

	// ************************** client messages **************************
	
	//LOGIN
	CHECK_LOGIN,
	
	
	
	
	// ************************** server messages **************************
	//LOGIN
	LOGIN_RESPONSE,	// can return null if the user doesn't exist or return the user.
	ERROR_TYPE_IS_UNSET
}
