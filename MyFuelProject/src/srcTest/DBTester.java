package srcTest;
import java.util.Calendar;
import java.util.Date;

import server.dbLogic.DBConnector;

public class DBTester {

	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnector();
		
		System.out.println(dbConnector.fuelDBLogic.getFullFuelInventory());
	}
}
