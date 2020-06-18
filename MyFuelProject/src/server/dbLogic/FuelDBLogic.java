package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
import entitys.DeterminingRateRequests;
import entitys.Fuel;
import entitys.enums.FuelType;


/**
 * This class is responsible on send querys to the DB and get/insert/update/delete
 * Data from the data base and send it back to the server controller with the 
 * answer about the request that has been sent.
 * @author Barak
 *@version Final
 */
public class FuelDBLogic {

	
	/**
	 * this function will get from the DB the fuel types inventory by the user name
	 * of the station manager and will insert every fuel type inventory status and details
	 * into an array, and then returns it.
	 * @param  userName - the user name of the client in the system
	 * @return  array - Json array of the fuel inventory
	 */
	public JsonArray getFuelInventoryByUserName(String userName) {
		JsonArray array = new JsonArray();
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				//stationID, fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount
				query = "SELECT fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount "
					+   "FROM  fuel_inventorys, employees, fuel_stations "
					+   "WHERE employees.userName = '"+userName +"' AND "
							+ "employees.employeeNumber = fuel_stations.managerID AND "
							+ "fuel_stations.stationID = fuel_inventorys.stationID;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject json = new JsonObject();
					json.addProperty("fuelType", rs.getString("fuelType"));
					json.addProperty("currentFuelAmount", rs.getFloat("currentFuelAmount"));
					json.addProperty("thresholdAmount", rs.getFloat("thresholdAmount"));
					json.addProperty("maxFuelAmount", rs.getFloat("maxFuelAmount"));
					array.add(json);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	/**
	 * This function get all of the fuel types in the DB and insert them into 
	 * JsonArray, and then returns it.
	 * @return JsonArray fuelTypes
	 */
	public static JsonArray getFuelTypes() {
		JsonArray fuelTypes = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  fuel ";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					fuelTypes.add(rs.getString("fuelType"));
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fuelTypes;

	}
	
	
	/**
	 * this function get a Fuel Type name than go to the DB and gets from the Fuel table
	 * the data of the fuel type, like current price and max price.
	 * then it insert it into Fuel object and returns it.
	 * @param  fuelType name
	 * @return  fuel object
	 */
	public Fuel getFuelObjectByType(String fuelType) {
		Fuel fuel = null;

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  fuel " + "WHERE fuelType ='" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					float pricePerLitter = rs.getFloat("pricePerLitter");
					float maxPricePerLitter = rs.getFloat("maxPricePerLitter");
					fuel = new Fuel(FuelType.stringToEnumVal(fuelType), pricePerLitter, maxPricePerLitter);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fuel;
	}
	/**
	 * This function send query to the DB and get the subscribe discount rate from the table.
	 * @param subscribeType - as string type.
	 * @return
	 */
	public String getSubscribeRate(String subscribeType) {
		
		String rate="";
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  subscribe_type " + "WHERE subscribeType ='" + subscribeType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					
					rate = rs.getString("discountRate");
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rate;
	}
	/**
	 * This method is responsible to update the subscribe rates on the DB.
	 * @param subscribeType - String value of subscribe type.
	 * @param newRate - String value of new rate.
	 */
	public void updateSubscribeRate(String subscribeType, String newRate) {
		float rateToUpdate = 0;
		rateToUpdate = Float.parseFloat(newRate);
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query = "UPDATE subscribe_type " + "SET discountRate = " + rateToUpdate + " WHERE subscribeType = '" + subscribeType
						+ "';";
				stmt.executeUpdate(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * this function gets fuel object and a new price and then update the new price in the DB
	 * by the fuel type.
	 * @param  fuel (exact fuel object of some type)
	 * @param  newPrice - the new price that we want to update
	 */
	public void updateFuel(Fuel fuel, String newPrice) {

		float PriceToUpdate = 0;
		PriceToUpdate = Float.parseFloat(newPrice);
		//String fueltype = fuel.getFuelType().toString();
		String fueltype = fuel.getFuelType().enumToString(fuel.getFuelType());
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query = "UPDATE fuel " + "SET pricePerLitter = " + PriceToUpdate + " WHERE fuelType = '" + fueltype
						+ "';";
				stmt.executeUpdate(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * this function get a request to determine rate of the fuel type price and the new price to be updated.
	 * then it insert to the DB in the "determining_rate_requests" table the most updated rate request.
	 * @param request - a request that contains the fuel type, current price, status and create time
	 * @param newPrice - new price to be updated as a new rate request.
	 */
	public void SendRateRequest(DeterminingRateRequests request, String newPrice) {

		float PriceToUpdate = 0;
		PriceToUpdate = Float.parseFloat(newPrice);
		float currentPrice = request.getCurrentPrice();
		String fueltype = request.getFuelType();
		String status = request.getRequestStatus().enumToString(request.getRequestStatus());
		String createTime = request.getCreateTime();
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query="DELETE FROM determining_rate_requests WHERE fuelType = '" + fueltype + "';";
				stmt.execute(query);

				query = "INSERT INTO determining_rate_requests ( currentPrice, newPrice, requestStatus, fuelType, createTime) "
						+ "VALUES ('" + currentPrice + "','" + PriceToUpdate + "','" + status + "','" + fueltype + "','"
						+ createTime + "');";
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
	 * this function get a request to determine rate of the Subscribe type discount and the new discount to be updated.
	 * then it insert to the DB in the "discounts_requests" table the most updated rate request.
	 * @param request - a request that contains the subscribe type, current discount, status and create time
	 * @param newDiscount - new discount to be updated as a new rate request.
	 */
	
	public void SendSubscribeRequest(DeterminingRateRequests request, Float newDiscount) {

		//float DiscountToUpdate = 0;
		//PriceToUpdate = Float.parseFloat(newPrice);
		float currentDiscount = request.getCurrentPrice();
		String subscribetype = request.getFuelType();
		String status = request.getRequestStatus().enumToString(request.getRequestStatus());
		String createTime = request.getCreateTime();
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query="DELETE FROM discounts_requests WHERE SubscribeType = '" + subscribetype + "';";
				stmt.execute(query);

				query = "INSERT INTO discounts_requests ( currentDiscount, newDiscount, requestStatus, SubscribeType, createTime) "
						+ "VALUES ('" + currentDiscount + "','" + newDiscount + "','" + status + "','" + subscribetype + "','"
						+ createTime + "');";
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
	 * this functon go to the DB and get from the table
	 * all of the Company names we work with.
	 * it insert it into an Json array and returns it
	 * @return JsonArray CompanyNames -  all of the Companies in the DB
	 */
	
	public static JsonArray getFuelCompanyNames() {
		JsonArray companyNames = new JsonArray();

		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fuel_company;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					companyNames.add(rs.getString("companyName"));
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companyNames;
	}

	/**
	 * this function get all of the current rate requests from the DB and insert it
	 * into an Json Array.
	 * it gets only the requests that their current status is waiting to approve.
	 * @return rateRequests - JsonArray of the current waiting to approve requests.
	 */
	
	public JsonArray getRateRequests() {
		JsonArray rateRequests = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  determining_rate_requests " + " WHERE requestStatus ='Waiting To Approve';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject rateRequest = new JsonObject();
					rateRequest.addProperty("requestID", rs.getString("requestID"));
					rateRequest.addProperty("currentPrice", rs.getString("currentPrice"));
					rateRequest.addProperty("newPrice", rs.getString("newPrice"));
					rateRequest.addProperty("requestStatus", rs.getString("requestStatus"));
					rateRequest.addProperty("fuelType", rs.getString("fuelType"));
					rateRequest.addProperty("createTime", rs.getString("createTime"));

					rateRequests.add(rateRequest);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rateRequests;
	}
	
	/**
	 * this function get all of the current discount requests from the DB and insert it
	 * into an Json Array.
	 * it gets only the requests that their current status is waiting to approve.
	 * @return rateRequests - JsonArray of the current waiting to approve requests.
	 */
	
	public JsonArray getDiscountRequests() {
		JsonArray discountRequests = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  discounts_requests " + " WHERE requestStatus ='Waiting To Approve';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject discountRequest = new JsonObject();
					discountRequest.addProperty("requestID", rs.getString("requestID"));
					discountRequest.addProperty("currentDiscount", rs.getString("currentDiscount"));
					discountRequest.addProperty("newDiscount", rs.getString("newDiscount"));
					discountRequest.addProperty("requestStatus", rs.getString("requestStatus"));
					discountRequest.addProperty("SubscribeType", rs.getString("SubscribeType"));
					discountRequest.addProperty("createTime", rs.getString("createTime"));

					discountRequests.add(discountRequest);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return discountRequests;
	}
	
	/**
	 * this function update the decision about the rate request if it gets declined.
	 * the function will insert the reason of decline and the new status by its decision
	 * for the specific request ID.
	 * @param decline - the string of the reason to decline the request.
	 * @param decision - boolean variable, true for approve, or false for decline.
	 * @param ID  - the specific request ID.
	 */
	public void UpdateDecline(String decline, boolean decision, String ID) {

		String query = "";
		Statement stmt = null;
		try {

			if (DBConnector.conn != null) {
				if (decision == false) {
					stmt = DBConnector.conn.createStatement();
					query = "UPDATE determining_rate_requests " + "SET reasonOfDecline = '" + decline
							+ "', requestStatus = 'Not Approved' " + " WHERE requestID = '" + ID + "';";

					stmt.executeUpdate(query);
				} else if (decision == true) {
					stmt = DBConnector.conn.createStatement();
					query = "UPDATE determining_rate_requests SET requestStatus = 'Approved' " + " WHERE requestID = '"
							+ ID + "';";

					stmt.executeUpdate(query);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this function update the decision about the rate request if it gets declined.
	 * the function will insert the reason of decline and the new status by its decision
	 * for the specific request ID.
	 * @param decline - the string of the reason to decline the request.
	 * @param decision - boolean variable, true for approve, or false for decline.
	 * @param ID  - the specific request ID.
	 */
	public void UpdateDiscountDecline(String decline, boolean decision, String ID) {

		String query = "";
		Statement stmt = null;
		try {

			if (DBConnector.conn != null) {
				if (decision == false) {
					stmt = DBConnector.conn.createStatement();
					query = "UPDATE  discounts_requests " + "SET reasonOfDecline = '" + decline
							+ "', requestStatus = 'Not Approved' " + " WHERE requestID = '" + ID + "';";

					stmt.executeUpdate(query);
				} else if (decision == true) {
					stmt = DBConnector.conn.createStatement();
					query = "UPDATE  discounts_requests SET requestStatus = 'Approved' " + " WHERE requestID = '"
							+ ID + "';";

					stmt.executeUpdate(query);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this function gets the manager ID and go to the DB and find the station ID
	 * by the manager id that is logged in.
	 * it returns the station ID number.
	 * @param managerID - the number of ID of the manager logged in
	 * @return Station ID - the station ID number in string form.
	 */
	
	public String getStationIDbyManagerID(String managerID) {
		String query = "";
		Statement stmt = null;
		String stationID="";
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_stations "
						+ "WHERE managerID='"+managerID+"';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					
					stationID=rs.getString("stationID");
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return stationID;
	}
	
	/**
	 * this function get the fuel inventory of specific station.
	 * it gets the station ID and then return a JsonArray that contains 
	 * fuel type, current fuel amount, threshold and Max fuel amount.
	 * @param stationID - specific Station id number
	 * @return fuelInventories - JsonArray the contains the data of the inventory
	 */
	public JsonArray getFuelInventoryPerStation(String stationID){
		JsonArray fuelInventories = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_inventorys "
						+ "WHERE stationID='"+stationID+"';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					JsonObject fuelInventory = new JsonObject();
					fuelInventory.addProperty("stationID", rs.getString("stationID"));
					fuelInventory.addProperty("fuelType", rs.getString("fuelType"));
					fuelInventory.addProperty("currentFuelAmount", rs.getString("currentFuelAmount"));
					fuelInventory.addProperty("thresholdAmount", rs.getString("thresholdAmount"));
					fuelInventory.addProperty("maxFuelAmount", rs.getString("maxFuelAmount"));
					fuelInventories.add(fuelInventory);
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return fuelInventories;
	}
	/**
	 * this function get the fuel inventory of specific fuel type and station ID number 
	 * and return specific JsonObject  of specific fuel type in station.
	 * @param stationID - the specific statiod id number
	 * @param fuelType - specific fuel type
	 * @return JsonObject inventory - current inventory of specific fuel type in the station given.
	 */
	public JsonObject getFuelInventoryByStationIDAndFuelType(String stationID, String fuelType) {

		JsonObject inventory = new JsonObject();
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_inventorys "
						+ "WHERE stationID='"+stationID+"' AND fuelType = '" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					inventory.addProperty("stationID", rs.getString("stationID"));
					inventory.addProperty("fuelType", rs.getString("fuelType"));
					inventory.addProperty("currentFuelAmount", rs.getFloat("currentFuelAmount"));
					inventory.addProperty("thresholdAmount", rs.getFloat("thresholdAmount"));
					inventory.addProperty("maxFuelAmount", rs.getFloat("maxFuelAmount"));
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return inventory;
	}
	
	
	/**
	 * This function update in the DB the new threshold and/or max amount 
	 * of the fuel type in the station inventory of the station manager logged in
	 * @param threshold - the threshold of specific fuel type inventory
	 * @param maxAmount - the max amount of fuel type in the inventory
	 * @param fuelType - the specific fuel type we want to change its inventory
	 * @param userName - the user name of the user logged in, so we can find his/her ID
	 * and then the station ID by the query
	 */
	public void updateFuelInventory(String threshold, String maxAmount,String fuelType ,String userName) {

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query = "UPDATE fuel_inventorys, employees, fuel_stations " + "SET fuel_inventorys.thresholdAmount = '" + threshold +
						"', fuel_inventorys.maxFuelAmount = '"+ maxAmount+"' "
						+	"WHERE employees.userName = '" +userName + "' AND " + 
						"employees.employeeNumber = fuel_stations.managerID AND " + 
						" fuel_inventorys.fuelType = '" + fuelType + "' AND" + 
						" fuel_stations.stationID = fuel_inventorys.stationID;";
				stmt.executeUpdate(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	//TODO - MAYBE TO DELETE!!! I CANT SEE ANY USE IN IT (THIS IS NOT BY STATION)
	public JsonArray getFullFuelInventory() {

		JsonArray fuelInventory = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_inventorys;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("stationID", rs.getString("stationID"));
					order.addProperty("fuelType", rs.getString("fuelType"));
					order.addProperty("currentFuelAmount", rs.getFloat("currentFuelAmount"));
					order.addProperty("thresholdAmount", rs.getFloat("thresholdAmount"));
					order.addProperty("maxFuelAmount", rs.getFloat("maxFuelAmount"));
					fuelInventory.add(order);
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return fuelInventory;
	}
	
	/**
	 * this function get the fuel type, and the station ID number and bring
	 * from the DB the current amount of fuel available.
	 * @param fuelType - specific fuel type to search
	 * @param stationID - specific ID number of station
	 * @return the float number that represent the amount available
	 */
	public float getAvailableAmountOfFuelByTypeAndStationID(String fuelType, String stationID) {
		float availableAmount = 0;
		
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_inventorys "
					  + "WHERE stationID = '" + stationID + "' AND "
					  		+ "fuelType = '" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					availableAmount = rs.getFloat("currentFuelAmount");
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return availableAmount;
	}

	/**
	 * This function update the CURRENT fuel amount of specific fuel type
	 * by the station id. this function will go to the DB and update 
	 * the data in it.
	 * @param fuelType - the specific fuel type
	 * @param stationID - the specific station ID
	 * @param amount - the amount we want to update
	 */
	
	public void updateFuelAmountByStationIDFuelTypeAndAmount(String fuelType, String stationID, float amount) {
		float newAmount = getAvailableAmountOfFuelByTypeAndStationID(fuelType, stationID) - amount;
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "UPDATE fuel_inventorys "
						+ "SET currentFuelAmount = " + newAmount + " "
					  + "WHERE stationID = '" + stationID + "' AND "
					  		+ "fuelType = '" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				stmt.executeUpdate(query);
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * just get the supplier ID for all the stations
	 * @return the ID number
	 */
	public String getSupplierID() {
		return "777";
	}
	
	/**
	 * this function check if we need to create new inventory order
	 * if the current fuel amount is smaller than threshold , we have to create an order
	 * it gets the station ID and the fuel type we have to check
	 * @param stationID - the specific station ID
	 * @param fuelType - the specific fuel type
	 * @return boolean variable true - to create, false to not create
	 */
	public boolean checkIfNeedToCreateInventoryOrder(String stationID, String fuelType) {
		// return true if need to create inventory order.
		boolean isValid = false;
		float currentFuelAmount = 0;
		float thresholdAmount = -1;
		//stationID, fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_inventorys "
					  + "WHERE stationID = '" + stationID + "' AND "
					  		+ "fuelType = '" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					currentFuelAmount = rs.getFloat("currentFuelAmount");
					thresholdAmount = rs.getFloat("thresholdAmount");
					if(currentFuelAmount < thresholdAmount)
						isValid = true;
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		// doing this to ignore from create another order for the same fuel type.
		isValid = isValid && checkIfInventoryOrderAlreadyCreatedForNewOrders(stationID, fuelType);
		return isValid;
	}
	
	
	/**
	 * check if there's an order that already created for some 
	 * fuel type and station ID. we use this to check it in the function up above
	 * we don't want to create multiple orders for the same fuel type so this
	 * function help us to prevent this situation  
	 * @param stationID - the specific station ID number
	 * @param fuelType - the specific fuel type we want to check
	 * @return - boolean variable that tell us the answer
	 */
	public boolean checkIfInventoryOrderAlreadyCreatedForNewOrders(String stationID, String fuelType) {
		boolean isValid = true;
		
		String query = "";
		Statement stmt = null;
		
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM myfuel.fuel_inventory_orders "
						+ "WHERE stationID = '" + stationID + "' AND fuelType = '" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					isValid = false;
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	
	/**
	 * this function create new inventory order and insert it to the the table in the DB
	 * it send an order for specific fuel type  and specific station ID
	 * the amount of the fuel calculate by MAX FUEL AMOUNT - CURRENT FUEL AMOUNT
	 * @param stationID - specific station ID we want to send the order
	 * @param fuelType - specific fuel type
	 */
	public void createInventoryOrderByFuelTypeAndStationID(String stationID, String fuelType) {
		JsonObject json = getFuelInventoryByStationIDAndFuelType(stationID, fuelType);
		//stationID, fuelType, currentFuelAmount, thresholdAmount, maxFuelAmount
		String orderDate = ObjectContainer.getCurrentDate();
		String orderStatus = "SENT_TO_STATION_MANAGER";
		String supplierID = getSupplierID();
		float fuelAmount = json.get("maxFuelAmount").getAsFloat() - json.get("currentFuelAmount").getAsFloat();
		
		float pricePerLitter = getFuelObjectByType(fuelType).getPricePerLitter();
		float totalPrice = fuelAmount * pricePerLitter;
		
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query = "INSERT INTO fuel_inventory_orders (stationID, supplierID, orderDate, orderStatus, fuelType, fuelAmount, totalPrice) "
						+ "VALUES ('" + stationID + "','" + supplierID + "','" + orderDate + "','"
						+ orderStatus + "','" + fuelType + "'," + fuelAmount + "," + totalPrice + ");";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
