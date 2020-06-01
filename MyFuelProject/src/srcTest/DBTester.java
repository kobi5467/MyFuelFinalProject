package srcTest;
import entitys.Customer;
import server.dbLogic.DBConnector;

public class DBTester {

	
	public static void main(String[] args) {
		
		DBTester test = new DBTester();
		DBConnector dbConnector = new DBConnector();
		// customerID, userName, city, street, customerType, purchaseModelType, subscribeType
		System.out.println(dbConnector.customerDBLogic.getSubscribeTypeDiscount().toString());
	}
	
}
