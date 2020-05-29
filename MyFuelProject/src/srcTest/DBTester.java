package srcTest;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import entitys.CreditCard;
import entitys.Customer;
import entitys.FuelCompany;
import entitys.PurchaseModel;
import entitys.SubscribeType;
import entitys.Vehicle;
import entitys.enums.FuelType;
import entitys.enums.UserPermission;
import server.dbLogic.DBConnector;

public class DBTester {

	
	public static void main(String[] args) {
		
		DBTester test = new DBTester();
		DBConnector dbConnector = new DBConnector();
		// customerID, userName, city, street, customerType, purchaseModelType, subscribeType

		//userName, password, name, email, phoneNumber, userPermission, isLogin
		JsonObject user = new JsonObject();
		user.addProperty("userName", "kobi5467");
		user.addProperty("password", "123456");
		user.addProperty("name", "kobi malka");
		user.addProperty("email", "kobikobi@gmail.com");
		user.addProperty("phoneNumber", "0537553301");
		user.addProperty("userPermission", "CEO");
		
		
		Customer customer = new Customer();
		customer.setCustomerId("312239197");
		customer.setUsername("kobi5467");
		customer.setPassword("Kobi123123");
		customer.setName("Kobi Malka");
		customer.setEmail("kobi5467@gmail.com");
		customer.setPhoneNumber("0537553301");
		customer.setUsername("Customer");
		customer.setUserPermission(UserPermission.CUSTOMER);
		customer.setCity("Haifa");
		customer.setStreet("DummyStreet");
		customer.setCustomerType("private");
		
		ArrayList<FuelCompany> companies = new ArrayList<>();
		companies.add(new FuelCompany("Ten"));
		companies.add(new FuelCompany("Paz"));
		companies.add(new FuelCompany("Delek"));
		customer.setPurchaseModel(new PurchaseModel("dumyPurchaseModel", 0, companies));
		
		customer.setSubscribeType(new SubscribeType("dummySubscribeType", 0));
		
		customer.getVehicles().add(new Vehicle("12345678", FuelType.DIESEL, customer.getCustomerId()));
		customer.getVehicles().add(new Vehicle("33333333", FuelType.GASOLINE, customer.getCustomerId()));
		customer.getVehicles().add(new Vehicle("54765856", FuelType.SCOOTER_FUEL, customer.getCustomerId()));
		
		customer.setCreditCard(new CreditCard("312239197", "123456123456", "12/2020", "123"));
		
		String customerString = new Gson().toJson(customer);
		JsonObject cust = new Gson().fromJson(customerString, JsonObject.class);
//		JsonArray companiess = cust.get("purchaseModel").getAsJsonObject().get("fuelCompany").getAsJsonArray();
//		dbConnector.customerDBLogic.addFuelCompanies(customer.getCustomerId(),companiess);
	}

	public static void register(DBConnector dbConnector ,JsonObject requestJson) {
		dbConnector.userDBController.addUser(requestJson);
		dbConnector.customerDBLogic.addCustomer(requestJson);
		if(requestJson.get("creditCard") != null) {
			JsonObject creditCard = requestJson.get("creditCard").getAsJsonObject();
			dbConnector.customerDBLogic.addCreditCard(creditCard);
		}
		JsonArray vehicles = requestJson.get("vehicles").getAsJsonArray(); 
		for(int i = 0; i < vehicles.size(); i++) {
			JsonObject vehicle = vehicles.get(i).getAsJsonObject();
			dbConnector.customerDBLogic.addVehicle(vehicle);
		}
		
		JsonArray fuelCompanies = requestJson.get("purchaseModel").getAsJsonObject().get("fuelCompany").getAsJsonArray();
		dbConnector.customerDBLogic.addFuelCompanies(requestJson.get("customerID").getAsString(), fuelCompanies);
		
	}
	
}
