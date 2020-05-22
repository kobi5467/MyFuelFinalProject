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
				
		System.out.println("Customer Exist ? -> " + db.customerDBLogic.checkIfCustomerExist("312239197"));
		System.out.println("Customer Exist ? -> " + db.customerDBLogic.checkIfCustomerExist("123456789"));
		
		//		System.out.println("User exist ? -> " + db.userDBController.checkLogin(json));
//		System.out.println("UserPermission = " + db.userDBController.getUserPermission(json.get("userName").getAsString()));
//		System.out.println("IsExist ? -> " + db.userDBController.checkIfUsernameExist(json.get("userName").getAsString()));
//		System.out.println("IsExist ? -> " + db.userDBController.checkIfUsernameExist("blabla"));
	}

	
}
