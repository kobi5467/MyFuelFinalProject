package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * This class responsible to rule on all the customer DB Logic with all the requests from the server.
 * @author Or Yom Tov & Kobi Malka.
 * @version - Final
 */
public class CustomerDBLogic {
	/**
	 * @author Kobi Malka
	 * This method responsible to check if customer is already exist in the DB.
	 * @param customerID - string value of customer id
	 * @return - return boolean value - 'True' if exist, else 'False'
	 */
	public boolean checkIfCustomerExist(String customerID) {

		boolean isExist = false;

		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  customer " +
						"WHERE customerID ='" + customerID + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					isExist = true;
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isExist;
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to get all the customer details by user name.
	 * @param userName - string value of user name
	 * @return - return Json object of customer with all his details.
	 */
	public JsonObject getCustomerDetailsByUsername(String userName) {
		JsonObject customer = new JsonObject();
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM customer,users "
					  + "WHERE customer.userName = users.userName AND customer.userName = '" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					customer.addProperty("userName", userName);
					customer.addProperty("customerID", rs.getString("customerID"));
					customer.addProperty("name", rs.getString("name"));
					customer.addProperty("userName", rs.getString("userName"));
					customer.addProperty("email", rs.getString("email"));
					customer.addProperty("phoneNumber", rs.getString("phoneNumber"));
					customer.addProperty("customerType", rs.getString("customerType"));
					customer.addProperty("subscribeType", rs.getString("subscribeType"));
					customer.addProperty("purchaseModelType", rs.getString("purchaseModelType"));
					customer.addProperty("city", rs.getString("city"));
					customer.addProperty("street", rs.getString("street"));
					customer.addProperty("userPermission", rs.getString("userPermission"));
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customer;
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to get all the subscribe types.
	 * @return - return Json array of types.
	 */
	public JsonArray getSubscribeTypes() {
		JsonArray types = new JsonArray();
		
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  subscribe_type ";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					types.add(rs.getString("subscribeType"));
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return types;
		
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to add new customer in DB by all his data.
	 * @param customer - Json object with all his details.
	 */
	public void addCustomer(JsonObject customer) {
		String customerID = customer.get("customerID").getAsString();
		String userName = customer.get("userName").getAsString();
		String city = customer.get("city").getAsString();
		String street = customer.get("street").getAsString();
		String customerType = customer.get("customerType").getAsString();
		String purchaseModelType = customer.get("purchaseModel").getAsJsonObject().get("purchaseModeltype").getAsString();
		String subscribeType = customer.get("subscribeType").getAsJsonObject().get("subscribeType").getAsString();
		String paymentMethod = customer.get("paymentMethod").getAsString();
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO customer (customerID, userName, city, street, customerType, purchaseModelType, subscribeType, paymentMethod) " + 
						"VALUES ('" + customerID + "','"+ userName + "','" + city + "','" + street 
								+ "','" + customerType + "','" + purchaseModelType + "','" + subscribeType + "','" + paymentMethod + "');";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to add new vehicle to DB.
	 * @param vehicle - Json object with vehicle data.
	 */
	public void addVehicle(JsonObject vehicle) {
		String vehicleNumber = vehicle.get("vehicleNumber").getAsString();
		String fuelType = vehicle.get("fuelType").getAsString();
		String customerID = vehicle.get("customerID").getAsString();
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO vehicles (vehicleNumber, fuelType, customerID) " + 
						"VALUES ('" + vehicleNumber + "','"+ fuelType + "','" + customerID + "');";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to check if the vehicle is already exists.
	 * @param vehicle - Json object of vehicle data.
	 * @return - return 'True' if exist, else 'False'.
	 */
	public boolean checkIfVehicleExist(JsonObject vehicle) {
		String vehicleNumber = vehicle.get("vehicleNumber").getAsString();
		boolean isExist = false;
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM vehicles "
					  + "WHERE vehicleNumber = '" + vehicleNumber + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					isExist = true;
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isExist;
	}	
	/**
	 * @author Kobi Malka
	 * This method responsible to add new credit card to DB.
	 * @param creditCard - Json object with credit card data.
	 */
	public void addCreditCard(JsonObject creditCard) {
		String creditCardNumber = creditCard.get("cardNumber").getAsString();
		String cvv = creditCard.get("cvvNumber").getAsString();
		String dateValidation = creditCard.get("validationDate").getAsString();
		String customerID = creditCard.get("customerID").getAsString();

		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO credit_card (cardNumber, customerID, validationDate, cvvNumber) " + 
						"VALUES ('" + creditCardNumber + "','"+ customerID + "','"+ dateValidation + "','" + cvv + "');";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to add the fuel companies to the DB.
	 * @param customerID - string value of customer id
	 * @param companies - Json array with the companies.
	 */
	public void addFuelCompanies(String customerID, JsonArray companies) {
		String companyNames = "";
		for(int i = 0; i < companies.size(); i++) {
			if (i > 0) {
				companyNames += ",";
			}
			companyNames += companies.get(i).getAsJsonObject().get("companyName").getAsString();
		}
		
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO customer_fuel_companies (customerID, fuelCompanies) " + 
						"VALUES ('" + customerID + "','"+ companyNames + "');";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to get all the data from 'customer', 'users' and 'credit_card' tables.
	 * @param customerID - string value of customer ID
	 * @return - return Json object with all the data of customer, user and credit card tables.
	 */
	public JsonObject getCustomerDetails(String customerID) {

		JsonObject customer = new JsonObject();
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM customer, users, credit_card "
					  + "WHERE customer.customerID = '" + customerID + "'"
					  		+ " AND users.userName = customer.userName AND customer.customerID = credit_card.customerID;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					customer.addProperty("customerID", customerID);
					customer.addProperty("name", rs.getString("name"));
					customer.addProperty("userName", rs.getString("userName"));
					customer.addProperty("email", rs.getString("email"));
					customer.addProperty("phoneNumber", rs.getString("phoneNumber"));
					customer.addProperty("city", rs.getString("city"));
					customer.addProperty("street", rs.getString("street"));
					customer.addProperty("creditCardNumber", rs.getString("cardNumber"));
					customer.addProperty("validationDate", rs.getString("validationDate"));
					customer.addProperty("cvv", rs.getString("cvvNumber"));
					customer.addProperty("customerType", rs.getString("customerType"));
					customer.addProperty("subscribeType", rs.getString("subscribeType"));
					customer.addProperty("purchaseModelType", rs.getString("purchaseModelType"));
					customer.addProperty("paymentMethod", rs.getString("paymentMethod"));
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to update in the DB all the customer details in 'customer' and 'users' tables.
	 * @param customerUpdate - Json object with customer details.
	 * @return - return Json Object with all the customer data.
	 */
	public JsonObject updateCustomerDetails(JsonObject customerUpdate) {
		String city = customerUpdate.get("city").getAsString();
		String street = customerUpdate.get("street").getAsString();
		String phoneNumber = customerUpdate.get("phoneNumber").getAsString();
		String email = customerUpdate.get("email").getAsString();
		String userName = customerUpdate.get("userName").getAsString();
		String customerID = customerUpdate.get("customerID").getAsString();
		
		String queryCustomer = "";
		String queryUser = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				queryCustomer =  "UPDATE customer " + 
						 "SET city = '" + city  + "'" + ", street= '" + street + "'" +
						 " WHERE customerID = '" + customerID + "';";
				stmt.executeUpdate(queryCustomer);
				
				queryUser = "UPDATE users " +
						"SET email  = '" + email + "'" + ", phoneNumber= '" + phoneNumber + "'" +
						" WHERE userName = '" + userName + "';";
				stmt.executeUpdate(queryUser);				 
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerUpdate;
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to get all the vehicles data by customer id from the DB.
	 * @param customerID - string value of cutsomerI id
	 * @return - return Json array with all the vehicles.
	 */
	public JsonArray getVehiclesByCustomerID(String customerID) {
		
		JsonArray vehicles = new JsonArray();
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM vehicles "
					  + "WHERE customerID = '" + customerID + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					JsonObject vehicle = new JsonObject();
					vehicle.addProperty("customerID", customerID);
					vehicle.addProperty("vehicleNumber", rs.getString("vehicleNumber"));
					vehicle.addProperty("fuelType", rs.getString("fuelType"));
					vehicles.add(vehicle);
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vehicles;
	}
	
	/**
	 * @author Or Yom Tov
	 * This method responsible to get all the fuel station in the current company by company name
	 * @param companyName - string value of companyName
	 * @return - return Json array with all the fuel station.
	 */
	public static JsonArray getFuelStationsByCompanyName(String companyName) {
		JsonArray stations = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fuel_stations "
					  + "WHERE companyName = '" + companyName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					stations.add(rs.getString("stationID"));
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stations;
	}
	
	/**
	 * @author Or Yom Tov
	 * This method responsible to get all the fuel companies by customer ID.
	 * @param customerID - string value of customer id
	 * @return - return all the companies.
	 */
	public String getFuelCompaniesByCustomerID(String customerID) {
		String companies = "";
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM customer_fuel_companies "
					  + "WHERE customerID = '" + customerID + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					companies = rs.getString("fuelCompanies");
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return companies;
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to get all the subscribe types.
	 * @return  - return Json array of subscribe types.
	 */
	public JsonArray getSubscribeTypeDiscount() {
		JsonArray subscribeTypes = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM subscribe_type;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					JsonObject json = new JsonObject();
					json.addProperty("subscribeType", rs.getString("subscribeType"));
					json.addProperty("discountRate", rs.getFloat("discountRate"));
					subscribeTypes.add(json);
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return subscribeTypes;
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to remove vehicle from the DB.
	 * @param removeVehicle - Json object of vehicle to remove with all his data.
	 */
	public void removeVehicleFromDB(JsonObject removeVehicle) {
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "DELETE FROM vehicles " +
						"WHERE vehicleNumber = '" + removeVehicle.get("vehicleNumber").getAsString() + "';";
				stmt = DBConnector.conn.createStatement();
				stmt.executeUpdate(query);
			
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Kobi Malka
	 * This method responsible to get previous fast fuel orders amount from DB.
	 * @param customerID - string value of customer id
	 * @return - return last mount price.
	 */
	public float getPreviousFastFuelOrdersAmount(String customerID) {
		float lastMonthPrice = 0;
		String query = "";
		Statement stmt = null;
		
		LocalDate now = LocalDate.now();
		YearMonth yearMonth	= YearMonth.of(now.getYear(), now.getMonth().getValue() - 1);
		LocalDate startDate = yearMonth.atDay(1);
		LocalDate endDate = yearMonth.atEndOfMonth();
		
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fast_fuel_orders "
					+   "WHERE customerID = '" + customerID + "' AND "
							+ "orderDate between '" + startDate + "' and '" + endDate + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					lastMonthPrice += rs.getFloat("totalPrice");
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lastMonthPrice;
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to update the current vehicle in DB
	 * @param newVehicle - Json object of new vehicle
	 * @return - return the Json object of the vehicle.
	 */
	public JsonObject updateVehicleInDB(JsonObject newVehicle) {
		
		String customerid = newVehicle.get("customerID").getAsString();
		String fueltype = newVehicle.get("fuelType").getAsString();
		String vehiclenumber = newVehicle.get("vehicleNumber").getAsString();
		
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query =  "INSERT INTO vehicles"
				        + " VALUES ('" + vehiclenumber + "', '" + fueltype + "', '" + customerid + "');";
				stmt.executeUpdate(query);
				
				 
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return newVehicle;
		
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to update all the purchase models in DB
	 * @param purchaseModelJson - Json object of purchase model.
	 */
	public void updatePurchaseModelByID(JsonObject purchaseModelJson) {
		String customerID = purchaseModelJson.get("customerID").getAsString();
		String purchaseModelType = purchaseModelJson.get("purchaseModelType").getAsString();
		String fuelCompanies = purchaseModelJson.get("fuelCompanies").getAsString();
		String queryPurchase = "";
		String queryfuelCompanies = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				queryPurchase =  "UPDATE customer_fuel_companies "
				        + "SET fuelCompanies =  '" + fuelCompanies + "'" +
				         " WHERE customerID = '" + customerID + "';";
				stmt.executeUpdate(queryPurchase);
				
				queryfuelCompanies = "UPDATE customer "
						+ "SET purchaseModelType = '" + purchaseModelType + "'" +
						" WHERE customerID = '" + customerID + "';";
				stmt.executeUpdate(queryfuelCompanies);
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to update the credit card details in the DB.
	 * @param creditCardUpdate - Json object of credit card
	 * @return - return credit card Json
	 */
	public JsonObject updateCreditCardDetails(JsonObject creditCardUpdate) {
		String creditCard = creditCardUpdate.get("creditCard").getAsString();
		String validationDate = creditCardUpdate.get("dateValidation").getAsString();
		String cvv = creditCardUpdate.get("cvv").getAsString();
		String customerID = creditCardUpdate.get("customerID").getAsString();

		String queryCreditCard = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				queryCreditCard = "UPDATE credit_card " +
						"SET cardNumber = '" + creditCard + "'" + ", validationDate = '" + validationDate + "'" + ", cvvNumber = '" + cvv + "'" +
						" WHERE customerID = '" + customerID + "';";
				stmt.executeUpdate(queryCreditCard);
				 
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return creditCardUpdate;
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to insert new credit card in the DB.
	 * @param creditCard - Json object with credit card details.
	 */
	public void insertCreditCard(JsonObject creditCard) {
		
		String customerID = creditCard.get("customerID").getAsString();
		String cardNumber = creditCard.get("creditCard").getAsString();
		String cvv = creditCard.get("cvv").getAsString();
		String dateValidation = creditCard.get("dateValidation").getAsString();
		
		String query = "";
		String updatePayment = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query =  "INSERT INTO credit_card"
				        + " VALUES ('" + cardNumber + "', '" + customerID + "', '" + dateValidation + "', '" + cvv + "');";
				stmt.executeUpdate(query);
				
				updatePayment = "UPDATE customer " +
							"SET paymentMethod = 'creditCard'" +
							" WHERE customerID = '" + customerID + "';";
				stmt.executeUpdate(updatePayment);
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Or Yom Tov
	 * This method is responsible to get the credit card details by customer id from the DB.
	 * @param customerID - string value of customer id
	 * @return - return Json object with credit card details.
	 */
	public JsonObject getCreditCardDetails(String customerID) {
		JsonObject customer = new JsonObject();
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM credit_card "
					  + "WHERE customerID = '" + customerID + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					customer.addProperty("creditCardNumber", rs.getString("cardNumber"));
					customer.addProperty("validationDate", rs.getString("validationDate"));
					customer.addProperty("cvv", rs.getString("cvvNumber"));
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customer;
	}
	/**
	 * @author Or Yom Tov
	 * This method responsible to get all the fuel companis by customer id.
	 * @param requestJson - Json object with customer details.
	 * @return - return Json object with customer details.
	 */
	public JsonObject getFuelCompaniesByID(JsonObject requestJson) {
		String customerID = requestJson.get("customerID").getAsString();
		JsonObject customer = new JsonObject();
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM customer_fuel_companies "
					  + "WHERE customerID = '" + customerID + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					customer.addProperty("fuelCompanies", rs.getString("fuelCompanies"));
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}
	
	public static JsonArray getAllCustomerId() {
		JsonArray customersId = new JsonArray();
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT customerID FROM customer;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					customersId.add( rs.getString("customerID"));
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customersId;
	}
	
	
	public void generateRanks() {
		JsonArray customers = getAllCustomerId();
		customers.toString();
		
		for(int i = 0; i < customers.size(); i++) {
			JsonArray customerOrders = getCustomerOrders(customers.get(i).getAsString());
			for(int j = 0; j < customerOrders.size(); j++) {
				
			}
		}
	}
	
	public float getRankByOrder(JsonObject order) {
		return 0;
	}
	
	private JsonArray getCustomerOrders(String customerID) {
		
		return null;
	}
}
