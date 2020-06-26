package unittest;

import java.util.Random;

import com.google.gson.JsonObject;

import client.gui.customer.HomeHeatingFuelController;
import client.gui.customer.IHomeHeatingFuelDBManager;
import server.dbLogic.DBConnector;

public class SetUpTest {

	
	public Random rnd = new Random();
	
	public DBConnector dbConnector;
	public HomeHeatingFuelController homeHeatingFuelController;
	public IHomeHeatingFuelDBManager iHomeHeatingFuelDBManager;
	
	public float pricePerLitter = 0;
	public float saleDiscount = 0;
	
	public SetUpTest() {
		dbConnector = new DBConnector();
		homeHeatingFuelController = new HomeHeatingFuelController();
		iHomeHeatingFuelDBManager = new IHomeHeatingFuelDBManager() {
			
			@Override
			public boolean submitHomeHeatingFuelOrder(JsonObject json) {
				//regular submit order.
				return dbConnector.orderDBLogic.insertHomeHeatingFuelOrder(json);
			}
			
			@Override
			public float getPricePerLitter() {
//				return 6.5F; // price for test
				return pricePerLitter;
			}
			
			@Override
			public int getLastOrderID(JsonObject json) {
				return 10; // last id
			}
			
			@Override
			public String[] getFuelCompaniesByCustomerID(String customerID) {
				return new String[] { "Paz", "Dor Alon", "Ten" }; // fuel companies for test.
			}
			
			@Override
			public float getCurrentRunningSaleTemplateDiscount() {
				return saleDiscount; // fake sale discount.
			}
			
			@Override
			public JsonObject getCreditCardByCustomerID() {
				// fake credit card details.
				JsonObject json = new JsonObject();
				json.addProperty("creditCardNumber", "123456789");
				json.addProperty("cvv", "333");
				json.addProperty("validationDate", "09/2024");
				return json;
			}
		};
	}
	
}
