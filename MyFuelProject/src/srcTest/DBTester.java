package srcTest;
import java.time.LocalDate;

import com.google.gson.JsonObject;

import server.dbLogic.DBConnector;

public class DBTester {

	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnector();
		LocalDate date = LocalDate.now();
		int currentMonth = date.getMonth().getValue();
		int currentYear = date.getYear();
		String code = "200";
	}
}
