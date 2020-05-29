package srcTest;
import com.google.gson.JsonObject;

import server.dbLogic.DBConnector;

public class DBTester {

	
	public static void main(String[] args) {
		
		DBTester test = new DBTester();
		DBConnector dbConnector = new DBConnector();
		
		JsonObject creditCard = new JsonObject();
		creditCard.addProperty("creditCardNumber", "141412412412");
		creditCard.addProperty("cvv", "123");
		creditCard.addProperty("dateValidation", "12/2022");
		creditCard.addProperty("customerID", "12345");

		dbConnector.customerDBLogic.addCreditCard(creditCard);
	}

	
}
