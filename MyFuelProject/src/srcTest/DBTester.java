package srcTest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
		
		JsonObject json = new JsonObject();
		json.addProperty("userName", "kobi");
		json.addProperty("password", "123");
		
		JsonArray array = new JsonArray();
		
		Point p = test.new Point(1, 2);
		Point p2 = test.new Point(4, 3);
		array.add(p.toString());
		array.add(p2.toString());
		json.add("Array", array);
		
		System.out.println(json.get("Array"));
		
		JsonArray new_array = json.get("Array").getAsJsonArray();
		System.out.println(new_array.get(0).getAsString());
		// use getasString to jsonObject to parse to object.
		Point p3 = new Gson().fromJson(new_array.get(0).getAsString(),Point.class);
		System.out.println(p3);
		
//		System.out.println("User exist ? -> " + db.userDBController.checkLogin(json));
//		System.out.println("UserPermission = " + db.userDBController.getUserPermission(json.get("userName").getAsString()));
//		System.out.println("IsExist ? -> " + db.userDBController.checkIfUsernameExist(json.get("userName").getAsString()));
//		System.out.println("IsExist ? -> " + db.userDBController.checkIfUsernameExist("blabla"));
	}

	
}
