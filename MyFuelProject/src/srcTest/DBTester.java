package srcTest;
import server.dbLogic.DBConnector;

public class DBTester {

	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnector();
		
		System.out.println(dbConnector.fuelDBLogic.checkIfInventoryOrderAlreadyCreatedForNewOrders("13", "Gasoline"));
	}
}
