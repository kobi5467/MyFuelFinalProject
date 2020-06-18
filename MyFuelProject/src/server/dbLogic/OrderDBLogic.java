package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This class is responsible on send query to the DB and
 * get/insert/update/delete order data from the data base and send it back to
 * the server controller with the answer about the request that has been sent.
 * 
 * @author Or Maman
 * @version Final
 */
public class OrderDBLogic {
	/**
	 * This function get all fuel inventory orders by specific supplier .
	 * 
	 * @param supplierID is the supplier that need to place orders.
	 * @return all the fuel inventory orders by supplierId.
	 */
	public JsonArray getFuelInventoryOrders(String supplierID) {
		JsonArray fuelInventoryOrders = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  fuel_inventory_orders " + "WHERE supplierID=" + supplierID;
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("orderID", rs.getString("orderID"));
					order.addProperty("stationID", rs.getString("stationID"));
					order.addProperty("orderDate", rs.getString("orderDate"));
					order.addProperty("orderStatus", rs.getString("orderStatus"));
					order.addProperty("fuelAmount", rs.getString("fuelAmount"));
					order.addProperty("totalPrice", rs.getString("totalPrice"));
					fuelInventoryOrders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fuelInventoryOrders;

	}

	/**
	 * This function get single home heating fuel order from the user and insert him
	 * into home_heating_fuel_orders table.
	 * 
	 * @param order is the data of a single order that made by the user.
	 */
	public void insertHomeHeatingFuelOrder(JsonObject order) {

		String query = " ";
		Statement stmt = null;
		String customerId = order.get("customerId").getAsString();
		String amount = order.get("amount").getAsString();
		String city = order.get("city").getAsString();
		String Street = order.get("street").getAsString();
		String dateSupply = order.get("dateSupplay").getAsString();
		String urgentOrder = order.get("isUrgentOrder").getAsString();
		String saleTemplateName = order.get("saleTemplateName").getAsString();
		String fuelCompany = order.get("fuelCompany").getAsString();
		String totalPrice = order.get("totalPrice").getAsString();
		String orderDate = order.get("orderDate").getAsString();
		String paymentMethod = order.get("paymentMethod").getAsString();
		String orderStatus = "WaitToApprove";
		String customerType = CustomerDBLogic.getCustomerTypeByCustomerID(customerId);
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO  home_heating_fuel_orders(customerID, amountOfLitters, city, street, dateSupply, urgentOrder, saleTemplateName, totalPrice, orderStatus, orderDate, paymentMethod, fuelCompany, customerType)"
						+ "VALUES('" + customerId + "','" + amount + "','" + city + "','" + Street + "','" + dateSupply
						+ "','" + urgentOrder + "','" + saleTemplateName + "','" + totalPrice + "','" + orderStatus + "','" + orderDate
						+ "','" + paymentMethod + "','" + fuelCompany + "','" + customerType + "');";
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
	 * This function get fast fuel orders by station id.
	 * 
	 * @param stationID is the station id for fast fuel orders station.
	 */
	public JsonArray getFastFuelOrdersByStationID(String stationID) {
		JsonArray fastFuelOrders = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fast_fuel_orders " + "WHERE stationID='" + stationID + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("orderID", rs.getString("orderID"));
					order.addProperty("orderDate", rs.getString("orderDate"));
					order.addProperty("customerID", rs.getString("customerID"));
					order.addProperty("fuelAmount", rs.getString("fuelAmount"));
					order.addProperty("totalPrice", rs.getString("totalPrice"));
					fastFuelOrders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fastFuelOrders;

	}

	/**
	 * This function get home heating fuel orders details for purchases report.
	 * 
	 * @param stationID is the station from which to take the purchase details.
	 * @param fuelType  is the fuel type that we want for the purchases report.
	 * @return the orders details
	 */

	public JsonArray getOrdersDetailsByStationIdAndFuelType(String stationID, String fuelType) {
		JsonArray orders = new JsonArray();
		String query;
		if (fuelType.equals("Home heating fuel")) {
			query = "SELECT * FROM home_heating_fuel_orders WHERE " + "stationID = '" + stationID + "';";
		} else {
			query = "SELECT * FROM fast_fuel_orders WHERE " + "stationID = '" + stationID + "' AND fuelType='"
					+ fuelType + "';";
		}
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("orderID", rs.getString("orderID"));
					order.addProperty("orderDate", rs.getString("orderDate"));
					order.addProperty("customerId", rs.getString("customerId"));
					order.addProperty("amountOfLitters", rs.getString("amountOfLitters"));
					order.addProperty("totalPrice", rs.getString("totalPrice"));
					orders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orders;
	}

	/**
	 * This function get fast fuel orders from DB for station quarterly income
	 * report.
	 * 
	 * @param stationID is the station that the user want to take the orders
	 *                  details.
	 * @quarter is the time in the year that we want to generate the report.
	 * @param year is when the fuel orders were made.
	 */

	public JsonArray getFastFuelOrdersByStationIdAndQuarter(String stationID, String quarter, String year) {
		JsonArray fastFuelOrders = new JsonArray();
		ArrayList<String> date = new ArrayList<>();
		date = createDate(quarter, year);
		String totalPriceOfFuel = "";
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT fuelType, sum(amountOfLitters) as totalAmountOfFuel,sum(totalPrice) as totalPriceOfFuel FROM fast_fuel_orders "
						+ "WHERE stationID='" + stationID + "' AND orderDate between '" + date.get(0)
						+ " 00:00:00' and '" + date.get(1) + " 23:59:59' group by fuelType;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("fuelType", rs.getString("fuelType"));
					order.addProperty("totalAmountOfFuel", rs.getString("totalAmountOfFuel"));
					totalPriceOfFuel = String.format("%.3f", Double.parseDouble(rs.getString("totalPriceOfFuel")));
					order.addProperty("totalPriceOfFuel", totalPriceOfFuel);
					fastFuelOrders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fastFuelOrders;

	}

	/**
	 * This function get home heating fuel orders from DB for station quarterly
	 * income report.
	 * 
	 * @param stationID is the station that the user want to take the orders
	 *                  details.
	 * @quarter is the time of year that we want to take orders for generate the
	 *          report.
	 * @param year is when the fuel orders were made.
	 */

	public JsonArray getHomeHeatingFuelOrdersByStationIdAndQuarter(String stationID, String quarter, String year) {
		JsonArray homeHeatingFuelOrders = new JsonArray();
		ArrayList<String> date = new ArrayList<>();
		date = createDate(quarter, year);
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT sum(amountOfLitters) as totalAmountOfFuel,sum(totalPrice) as totalPriceOfFuel FROM home_heating_fuel_orders "
						+ "WHERE stationID='" + stationID + "' AND orderDate between '" + date.get(0)
						+ " 00:00:00' and '" + date.get(1) + " 23:59:59';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("fuelType", "Home heating fuel");
					order.addProperty("totalAmountOfFuel", rs.getString("totalAmountOfFuel"));
					order.addProperty("totalPriceOfFuel", rs.getString("totalPriceOfFuel"));
					homeHeatingFuelOrders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return homeHeatingFuelOrders;

	}

	/**
	 * This function get home heating fuel orders from DB for comments report for
	 * sale Campaign.
	 * 
	 * @param saleName is the parameter that we want to generate the report on him.
	 * @return home heating fuel orders that participate in the sale.
	 */
	public JsonArray getHomeHeatingFuelOrdersBySaleName(String saleName) {
		JsonArray homeHeatingFuelOrders = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {

				query = "SELECT customerID, count(orderID) as sumOfPurchase ,sum(totalPrice) as amountOfPayment FROM home_heating_fuel_orders "
						+ "WHERE saleTemplateName='" + saleName + "' group by customerID;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("customerID", rs.getString("customerID"));
					order.addProperty("sumOfPurchase", rs.getString("sumOfPurchase"));
					order.addProperty("amountOfPayment", rs.getString("amountOfPayment"));
					homeHeatingFuelOrders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return homeHeatingFuelOrders;

	}

	/**
	 * This function get from the DB all of the Home Heating Fuel Orders of the
	 * given userName. it used the result of the query and insert it to an
	 * jsonObject and then insert it into JsonArray and returns it.
	 * 
	 * @param String username
	 * @return JsonArray HHFOrders
	 */
	public JsonArray GetHomeHeatingFuelOrder(String username) {
		JsonArray HHFOrders = new JsonArray();

		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				// query = "SELECT * FROM home_heating_fuel_orders , ";
				query = "SELECT * FROM home_heating_fuel_orders, users,customer " + "WHERE users.userName = '"
						+ username + "' AND "
						+ "customer.userName = users.userName AND   home_heating_fuel_orders.customerID ="
						+ " customer.customerID; ";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject HHFOrder = new JsonObject();
					HHFOrder.addProperty("orderID", rs.getString("orderID"));
					HHFOrder.addProperty("customerID", rs.getString("customerID"));
					HHFOrder.addProperty("saleTemplateName", rs.getString("saleTemplateName"));
					HHFOrder.addProperty("orderDate", rs.getString("orderDate"));
					HHFOrder.addProperty("orderStatus", rs.getString("orderStatus"));
					HHFOrder.addProperty("fuelAmount", rs.getString("amountOfLitters"));
					HHFOrder.addProperty("totalPrice", rs.getString("totalPrice"));
					HHFOrder.addProperty("paymentMethod", rs.getString("paymentMethod"));
					HHFOrder.addProperty("dateSupply", rs.getString("dateSupply"));
					HHFOrder.addProperty("city", rs.getString("city"));
					HHFOrder.addProperty("street", rs.getString("street"));
					HHFOrder.addProperty("urgentOrder", rs.getString("urgentOrder"));
					HHFOrders.add(HHFOrder);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return HHFOrders;
	}

	/**
	 * This function get fast fuel orders from DB for comments report for sale
	 * Campaign.
	 * 
	 * @param saleName is the parameter that we want to generate the report on him.
	 * @return fast fuel orders that participate in the sale.
	 */

	public JsonArray getFastFuelOrdersBySaleName(String saleName) {
		JsonArray fastFuelOrders = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {

				query = "SELECT customerID, count(orderID) as sumOfPurchase ,sum(totalPrice) as amountOfPayment FROM fast_fuel_orders "
						+ "WHERE saleTemplateName='" + saleName + "' group by customerID;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("customerID", rs.getString("customerID"));
					order.addProperty("sumOfPurchase", rs.getString("sumOfPurchase"));
					order.addProperty("amountOfPayment", rs.getString("amountOfPayment"));
					fastFuelOrders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fastFuelOrders;

	}

	/**
	 * This function generate date range for quarterly income report.
	 * 
	 * @param quarter is the quarter that the user chosen for generate this report.
	 * @param year    is the year that the user chosen for generate this report.
	 * @return All the dates that participant in the sale.
	 */
	public ArrayList<String> createDate(String quarter, String year) {
		ArrayList<String> date = new ArrayList<>();
		String startDate = "";
		String endDate = "";
		if (quarter.equals("January - March")) {
			startDate = year + "-01-01";
			endDate = year + "-03-31";
		} else if (quarter.equals("April - June")) {
			startDate = year + "-04-01";
			endDate = year + "-06-30";
		} else if (quarter.equals("July - September")) {
			startDate = year + "-07-01";
			endDate = year + "-09-30";
		} else {
			startDate = year + "-10-01";
			endDate = year + "-12-31";
		}
		date.add(startDate);
		date.add(endDate);
		return date;
	}

	/**
	 * This function get inventory order from DB.
	 * 
	 * @return all the inventory orders.
	 */
	public JsonArray getInvertoryOrdersFromDB(JsonObject requestJson) {
		JsonArray orders = new JsonArray();
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fuel_inventory_orders "
						+ "WHERE orderStatus = 'SENT_TO_SUPPLIER' AND orderStatus != 'DeniedByStationManager'"
						+ " AND orderStatus != 'Supplied' AND orderStatus != 'DeniedBySupplier';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("orderID", rs.getString("orderID"));
					order.addProperty("fuelType", rs.getString("fuelType"));
					order.addProperty("fuelAmount", rs.getString("fuelAmount"));
					order.addProperty("supplierID", rs.getString("supplierID"));
					orders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orders;

	}

	/**
	 * this function gets Json Object of order(with the Customer ID) and then go to
	 * the DB and find the Order ID number and returns it
	 * 
	 * @param order - JsonObect with the details about the customer ID
	 * @return Order ID number
	 */

	public String getOrderId(JsonObject order) {
		int orderID = 0;
		String customerId = order.get("customerId").getAsString();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM home_heating_fuel_orders" + " WHERE customerID = '" + customerId + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					orderID = Math.max(rs.getInt("orderID"), orderID);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return orderID + "";
	}

	/**
	 * This function get inventory order by specific supplier from DB.
	 * 
	 * @return all the inventory orders.
	 */
	public JsonArray getInvertoryOrdersByID(String supplierID) {
		JsonArray orders = new JsonArray();
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fuel_inventory_orders" + " WHERE supplierID = '" + supplierID
						+ "' AND orderStatus != 'DeniedByStationManager'" + " AND orderStatus != '';";
				stmt = DBConnector.conn.createStatement();
				stmt.executeQuery(query);
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("orderID", rs.getString("orderID"));
					order.addProperty("fuelType", rs.getString("fuelType"));
					order.addProperty("fuelAmount", rs.getString("fuelAmount"));
					order.addProperty("supplierID", rs.getString("supplierID"));
					order.addProperty("stationID", rs.getString("stationID"));
					order.addProperty("orderStatus", rs.getString("orderStatus"));
					order.addProperty("reason", rs.getString("reason"));
					order.addProperty("totalPrice", rs.getString("totalPrice"));
					order.addProperty("orderDate", rs.getString("orderDate"));
					orders.add(order);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public void removeOrderFromDB(JsonObject requestJson) {
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "DELETE FROM fuel_inventory_orders " + "WHERE orderID = '"
						+ requestJson.get("orderID").getAsString() + "';";
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
	 * This function update the status of fuel order details from 'Send to supplier'
	 * until 'Order supplied'.
	 * 
	 * @param orderStatus is the data of the order that we want to update.
	 * @return the updated order.
	 */
	public JsonObject updateOrderDetails(JsonObject orderStatus) {
		String orderID = orderStatus.get("orderID").getAsString();
		String status = orderStatus.get("orderStatus").getAsString();
		String reason = orderStatus.get("reason").getAsString();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query = "UPDATE fuel_inventory_orders " + "SET orderStatus = '" + status + "'" + ", reason = '" + reason
						+ "'" + " WHERE orderID = '" + orderID + "';";
				stmt.executeUpdate(query);

			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return orderStatus;
	}

	/**
	 * This function get fast heating fuel order and insert him into
	 * fast_fuel_orders table.
	 * 
	 * @param order is the data of a single fast fuel order.
	 */
	public void addFastFuelOrder(JsonObject order) {
		String query = " ";
		Statement stmt = null;

		String customerID = order.get("customerID").getAsString();
		String vehicleNumber = order.get("vehicleNumber").getAsString();
		String saleTemplateName = order.get("saleTemplateName").getAsString();
		String stationID = order.get("stationID").getAsString();
		String orderDate = order.get("orderDate").getAsString();
		String orderStatus = order.get("orderStatus").getAsString();
		String fuelType = order.get("fuelType").getAsString();
		float amountOfLitters = order.get("amountOfLitters").getAsFloat();
		float totalPrice = order.get("totalPrice").getAsFloat();
		String paymentMethod = order.get("paymentMethod").getAsString();
		String pumpNumber = order.get("pumpNumber").getAsString();
		String customerType = CustomerDBLogic.getCustomerTypeByCustomerID(customerID);
		
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO  fast_fuel_orders(customerID, vehicleNumber, saleTemplateName, stationID, orderDate, "
						+ "orderStatus, fuelType, amountOfLitters, totalPrice, paymentMethod, pumpNumber, customerType) " + "VALUES('"
						+ customerID + "','" + vehicleNumber + "','" + saleTemplateName + "','" + stationID + "','"
						+ orderDate + "','" + orderStatus + "','" + fuelType + "'," + amountOfLitters + "," + totalPrice
						+ ",'" + paymentMethod + "','" + pumpNumber + "','"+ customerType + "');";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public float getTotalPriceByCustomerIdAndStationIdFromFastFuelOrders(String customerId, String stationId,
			String startDate, String endDate) {

		float totalPrice = 0;
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT SUM(totalPrice) as totalPrice from fast_fuel_orders" + " WHERE customerID ='"
						+ customerId + "' AND stationID='" + stationId + "' AND orderDate between '" + startDate
						+ " 00:00:00' AND '" + endDate + " 23:59:59' ;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					totalPrice += rs.getInt("totalPrice");
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return totalPrice;
	}

	public float getTotalPriceByCustomerIdAndStationIdFromHomeHeatingFuelOrders(String customerId, String companyName,
			String startDate, String endDate) {

		float totalPrice = 0;
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT SUM(totalPrice) as totalPrice from home_heating_fuel_orders" + " WHERE customerID ='"
						+ customerId + "' AND fuelCompany='" + companyName + "' AND orderDate between '"
						+ startDate + " 00:00:00' AND '" + endDate + " 23:59:59' ;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					totalPrice += rs.getInt("totalPrice");
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return totalPrice;
	}

	public float getTotalPriceByCustomerIdAndStaions(String customerId, JsonArray stations, String startDate,
			String endDate) {
		float totalPrice = 0;
		for (int i = 0; i < stations.size(); i++) {
			totalPrice += getTotalPriceByCustomerIdAndStationIdFromFastFuelOrders(customerId,
					stations.get(i).getAsString(), startDate, endDate);
		}
		return totalPrice;
	}

	public JsonObject getTotalPriceByCustomerId(String customerId, String startDate, String endDate) {
		float total = 0;
		JsonObject totalOfCustomer = new JsonObject();
		float totalPrice = 0;
		totalOfCustomer.addProperty("customerID", customerId);
		JsonArray companies = FuelDBLogic.getFuelCompanyNames();
		for (int i = 0; i < companies.size(); i++) {
			JsonArray stations = CustomerDBLogic.getFuelStationsByCompanyName(companies.get(i).getAsString());
			totalPrice = getTotalPriceByCustomerIdAndStaions(customerId, stations, startDate, endDate);
			// add the total price of home heating fuel by fuelCompany
			totalPrice += getTotalPriceByCustomerIdAndStationIdFromHomeHeatingFuelOrders(customerId,
					companies.get(i).getAsString(), startDate, endDate);
			;

			totalOfCustomer.addProperty(companies.get(i).getAsString(), totalPrice);
			total += totalPrice;

		}
		totalOfCustomer.addProperty("total", total);
		return totalOfCustomer;
	}

	public JsonArray getTotalPriceForAllCustomers(String startDate, String endDate) {
		JsonArray customers = new JsonArray();
		JsonArray arrayCustomer = new JsonArray();
		customers = CustomerDBLogic.getAllCustomerId();
		for (int i = 0; i < customers.size(); i++) {
			arrayCustomer.add(getTotalPriceByCustomerId(customers.get(i).getAsString(), startDate, endDate));
		}
		arrayCustomer = sortReportDetails(arrayCustomer);
		return arrayCustomer;
	}

	private JsonArray sortReportDetails(JsonArray jsonArray) {
		ArrayList<JsonObject> list = new ArrayList<>();
		JsonArray sortedJsonArray = new JsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			list.add(jsonArray.get(i).getAsJsonObject());
		}
		Collections.sort(list, new Comparator<JsonObject>() {
			@Override
			public int compare(JsonObject a, JsonObject b) {
				try {
					float num1 = a.get("total").getAsFloat();
					float num2 = b.get("total").getAsFloat();
					System.out.println(num1 + "  " + num2);
					if (num1 > num2) {
						return -1;
					} else if (num1 == num2) {
						return 0;
					}
					return 1;
				} catch (NumberFormatException e) {
					return 0;
				}
			}
		});
		for (int i = 0; i < jsonArray.size(); i++) {
			sortedJsonArray.add(list.get(i));
		}
		for (int i = 0; i < sortedJsonArray.size(); i++) {
			System.out.println(sortedJsonArray.get(i).toString());
		}
		return sortedJsonArray;
	}
}
