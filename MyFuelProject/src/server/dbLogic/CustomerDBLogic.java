package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import entitys.Customer;
import entitys.PurchaseModel;
import entitys.SubscribeType;
import entitys.enums.UserPermission;

public class CustomerDBLogic {

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
	
	public String getCustomerDetailsByUsername(String userName) {
		Customer customer = new Customer();
		float discountRate = 0;
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM customer,users "
					  + "WHERE customer.userName = users.userName AND customer.userName = '" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					customer.setUsername(userName);
					customer.setCustomerId(rs.getString("customerID"));
					customer.setName(rs.getString("name"));
					customer.setEmail(rs.getString("email"));
					customer.setPhoneNumber(rs.getString("phoneNumber"));
					customer.setCustomerType(rs.getString("customerType"));
					customer.setSubscribeType(new SubscribeType(rs.getString("subscribeType"),discountRate));
					customer.setPurchaseModel(new PurchaseModel(rs.getString("purchaseModelType"), 0, null));
					customer.setCity(rs.getString("city"));
					customer.setStreet(rs.getString("street"));
					customer.setUserPermission(UserPermission.stringToEnumVal(rs.getString("userPermission")));
					
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new Gson().toJson(customer);
	}
	
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

	public void addCustomer(JsonObject customer) {
		String customerID = customer.get("customerID").getAsString();
		String userName = customer.get("userName").getAsString();
		String city = customer.get("city").getAsString();
		String street = customer.get("street").getAsString();
		String customerType = customer.get("customerType").getAsString();
		String purchaseModelType = customer.get("purchaseModel").getAsJsonObject().get("purchaseModeltype").getAsString();
		String subscribeType = customer.get("subscribeType").getAsJsonObject().get("subscribeType").getAsString();
		
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO customer (customerID, userName, city, street, customerType, purchaseModelType, subscribeType) " + 
						"VALUES ('" + customerID + "','"+ userName + "','" + city + "','" + street 
								+ "','" + customerType + "','" + purchaseModelType + "','" + subscribeType + "');";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(customer.toString());
		return customer;
	}

	public JsonObject updateCustomerDetails(JsonObject customerUpdate) {
		String city = customerUpdate.get("city").getAsString();
		String street = customerUpdate.get("street").getAsString();
		String phoneNumber = customerUpdate.get("phoneNumber").getAsString();
		String email = customerUpdate.get("email").getAsString();
		String creditCard = customerUpdate.get("creditCard").getAsString();
		String validationDate = customerUpdate.get("dateValidation").getAsString();
		String cvv = customerUpdate.get("cvv").getAsString();
		String userName = customerUpdate.get("userName").getAsString();
		String customerID = customerUpdate.get("customerID").getAsString();
		
		System.out.println(customerUpdate.toString());
		
		String queryCustomer = "";
		String queryUser = "";
		String queryCreditCard = "";
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
				
				System.out.println(customerID);
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
		
		return customerUpdate;
	}

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
	
	
	public JsonArray getFuelStationsByCompanyName(String companyName) {
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

	public void removeVehicleFromDB(JsonObject removeVehicle) {
		String query = "";
		System.out.println(removeVehicle.toString());
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

	public float getPreviousFastFuelOrdersAmount(String customerID) {
		float lastMonthPrice = 0;
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fast_fuel_orders;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					System.out.println(rs);
				}				
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lastMonthPrice;
	}

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

	public void updatePurchaseModelByID(JsonObject purchaseModelJson) {
		String customerID = purchaseModelJson.get("customerID").getAsString();
		String purchaseModelType = purchaseModelJson.get("purchaseModelType").getAsString();
		String fuelCompanies = purchaseModelJson.get("fuelCompanies").getAsString();
		System.out.println(purchaseModelJson.toString());
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
}
