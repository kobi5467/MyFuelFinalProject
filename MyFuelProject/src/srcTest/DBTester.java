package srcTest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import server.dbLogic.DBConnector;

public class DBTester {

	public static void main(String[] args) {
		DBConnector dbConnector = new DBConnector();
		
		JsonObject sale = new JsonObject();
		sale.addProperty("saleTemplateName", "Happy Hour");
		sale.addProperty("description", "All fuel types for 20% discount between 2-4pm");
		
		JsonObject saleData = new JsonObject();
		
		JsonArray saleTypes = new JsonArray();
		saleTypes.add("BY_FUEL_TYPE");
		saleTypes.add("BY_CRETIAN_HOURS");
		saleData.add("saleTypes", saleTypes);
		
		saleData.addProperty("fuelType", "Gasoline");
		saleData.addProperty("from", "14:00");
		saleData.addProperty("to", "16:00");
		
		sale.addProperty("isRunning", 0);
		sale.addProperty("discountRate", 20);
		sale.add("saleData",saleData);
		
		dbConnector.customerDBLogic.getPreviousFastFuelOrdersAmount("312239197");
	}
}
