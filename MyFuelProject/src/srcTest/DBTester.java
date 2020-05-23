package srcTest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import entitys.User;
import server.dbLogic.DBConnector;

public class DBTester {

	
	public class Point{
		int x;
		int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public String toString() {
			return "{\"x\":\""+x + "\",\"y\":\""+y+"\"}";
		}
	}
	
	public static void main(String[] args) {
		
		DBTester test = new DBTester();
		DBConnector db = new DBConnector();

	}

	
}
