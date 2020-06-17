package srcTest;
import client.controller.ObjectContainer;
import server.dbLogic.DBConnector;

public class DBTester {

	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnector();
		
		String date = ObjectContainer.getCurrentDate();
		System.out.println(date);
		String[] times = date.trim().split(" ")[1].split(":");
		int time = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
	}
}
