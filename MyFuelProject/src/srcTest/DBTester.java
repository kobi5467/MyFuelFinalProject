package srcTest;
import com.google.gson.JsonObject;

import server.dbLogic.DBConnector;

public class DBTester {

	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnector();
		
		String code = "200";
		System.out.println(dbConnector.customerDBLogic.getDataByCode(code).toString());
	}
}
