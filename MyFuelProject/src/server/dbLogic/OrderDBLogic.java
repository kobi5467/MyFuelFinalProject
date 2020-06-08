package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class OrderDBLogic {

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
					System.out.println(order.toString());
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
		String stationId = order.get("stationId").getAsString();
		String totalPrice = order.get("totalPrice").getAsString();
		String orderDate = order.get("orderDate").getAsString();
		String paymentMethod = order.get("paymentMethod").getAsString();
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO  home_heating_fuel_orders(customerID, amountOfLitters, city, street, dateSupply, urgentOrder, saleTemplateName, totalPrice, orderDate, paymentMethod, stationID)"
						+ "VALUES(" + "'" + customerId + "'" + "," + "'" + amount + "'" + "," + "'" + city + "'," + "'"
						+ Street + "'" + "," + "'" + dateSupply + "'" + "," + "'" + urgentOrder + "'" + "," + "'"
						+ saleTemplateName + "'" + ",'" + totalPrice + "'" + ",'" + orderDate + "'" + ",'"
						+ paymentMethod + "'" + ",'" + stationId + "'" + ");";
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

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

//get orders details for purchases report
	public JsonArray getOrdersDetailsByStationIdAndFuelType(String stationID, String fuelType) {
		JsonArray orders = new JsonArray();
		String query;
		if (fuelType.equals("Home heating fuel")) {
			query = "SELECT * FROM home_heating_fuel_orders WHERE " + "stationID = '" + stationID + "';";
		} else {
			query = "SELECT * FROM fast_fuel_orders WHERE " + "stationID = '" + stationID + "' AND fuelType='"
					+ fuelType + "';";
		}
		System.out.println(query);
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
					System.out.println(order.toString());
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

	public JsonArray getFastFuelOrdersByStationIdAndQuarter(String stationID, String quarter, String year) {
		JsonArray fastFuelOrders = new JsonArray();
		ArrayList<String> date = new ArrayList<>();
		date = createDate(quarter, year);
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT fuelType, sum(amountOfLitters) as totalAmountOfFuel,sum(totalPrice) as totalPriceOfFuel FROM fast_fuel_orders "
						+ "WHERE stationID='" + stationID + "' AND orderDate between '" + date.get(0) + "' and '"
						+ date.get(1) + "' group by fuelType;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("fuelType", rs.getString("fuelType"));
					order.addProperty("totalAmountOfFuel", rs.getString("totalAmountOfFuel"));
					order.addProperty("totalPriceOfFuel", rs.getString("totalPriceOfFuel"));
					System.out.println(order.toString());
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

	public JsonArray getHomeHeatingFuelOrdersByStationIdAndQuarter(String stationID, String quarter, String year) {
		JsonArray homeHeatingFuelOrders = new JsonArray();
		ArrayList<String> date = new ArrayList<>();
		date = createDate(quarter, year);
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT sum(amountOfLitters) as totalAmountOfFuel,sum(totalPrice) as totalPriceOfFuel FROM home_heating_fuel_orders "
						+ "WHERE stationID='" + stationID + "' AND orderDate between '" + date.get(0) + "' and '"
						+ date.get(1) + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("totalAmountOfFuel", rs.getString("totalAmountOfFuel"));
					order.addProperty("totalPriceOfFuel", rs.getString("totalPriceOfFuel"));
					System.out.println(order.toString());
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

	public JsonArray getHomeHeatingFuelOrdersByStationIdAndSaleName(String stationID, String saleName) {
		JsonArray homeHeatingFuelOrders = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {

				query = "SELECT customerID, count(orderID) as sumOfPurchase ,sum(totalPrice) as amountOfPayment FROM home_heating_fuel_orders "
						+ "WHERE stationID='" + stationID + "' AND saleTemplateName='" + saleName
						+ "' group by customerID;";
				System.out.println(query);
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("customerID", rs.getString("customerID"));
					order.addProperty("sumOfPurchase", rs.getString("sumOfPurchase"));
					order.addProperty("amountOfPayment", rs.getString("amountOfPayment"));
					System.out.println(order.toString());
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

	public JsonArray GetHomeHeatingFuelOrder(String username) {
		JsonArray HHFOrders = new JsonArray();

		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				// query = "SELECT * FROM home_heating_fuel_orders , ";
				query = "SELECT * FROM home_heating_fuel_orders, users,customer " + 
						"WHERE users.userName = '"+ username + "' AND "
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
					HHFOrder.addProperty("fuelAmount", rs.getString("fuelAmount"));
					HHFOrder.addProperty("totalPrice", rs.getString("totalPrice"));
					HHFOrder.addProperty("paymentMethod", rs.getString("paymentMethod"));
					HHFOrder.addProperty("dateSupply", rs.getString("dateSupply"));
					HHFOrder.addProperty("address", rs.getString("address"));
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

	public JsonArray getFastFuelOrdersByStationIdAndSaleName(String stationID, String saleName) {
		JsonArray fastFuelOrders = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {

				query = "SELECT customerID, count(orderID) as sumOfPurchase ,sum(totalPrice) as amountOfPayment FROM fast_fuel_orders "
						+ "WHERE stationID='" + stationID + "' AND saleTemplateName='" + saleName
						+ "' group by customerID;";
				System.out.println(query);
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject order = new JsonObject();
					order.addProperty("customerID", rs.getString("customerID"));
					order.addProperty("sumOfPurchase", rs.getString("sumOfPurchase"));
					order.addProperty("amountOfPayment", rs.getString("amountOfPayment"));
					System.out.println(order.toString());
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

	public String getOrderId(JsonObject order) {
		int orderID = 0;
		String customerId = order.get("customerId").getAsString();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM home_heating_fuel_orders"
						+ " WHERE customerID = '" + customerId + "';";
				System.out.println(query);
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					orderID = Math.max(rs.getInt("orderID"),orderID);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		System.out.println(orderID);
		return orderID + "";
	}

	
}
