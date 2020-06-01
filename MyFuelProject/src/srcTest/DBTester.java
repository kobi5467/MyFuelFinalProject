package srcTest;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import entitys.CreditCard;
import entitys.Customer;
import entitys.FuelCompany;
import entitys.PurchaseModel;
import entitys.SubscribeType;
import entitys.Vehicle;
import entitys.enums.FuelType;
import entitys.enums.UserPermission;
import server.dbLogic.DBConnector;

public class DBTester {

	
	public static void main(String[] args) {
		
		DBTester test = new DBTester();
		DBConnector dbConnector = new DBConnector();
		// customerID, userName, city, street, customerType, purchaseModelType, subscribeType
		JsonObject customer = dbConnector.customerDBLogic.getCustomerDetailsByUsername("kobi");
		System.out.println(customer.toString());
	}
	
}
