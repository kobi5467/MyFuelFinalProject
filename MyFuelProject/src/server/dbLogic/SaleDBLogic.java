package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * This class is responsible on send querys to the DB and get/insert/update/delete
 * Data from the data base and send it back to the server controller with the 
 * answer about the request that has been sent.
 * @author MyFuel Team
 *@version Final
 */
public class SaleDBLogic {

	/**
	 * this function send query to the DB and get all of the sale names in the table
	 * @return Sale Names as a JsonArray with all the data
	 */
	public JsonArray getSaleNames() {
		JsonArray saleNames = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  sale_templates";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					saleNames.add(rs.getString("saleTemplateName"));
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saleNames;
	}

	/**
	 * this function send query to the DB and get all of the sale templates in the table
	 * @return Sale templates as a JsonArray with all the data
	 */
	public JsonArray getSaleTemplates() {
		JsonArray saleTemplates = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  sale_templates";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject saleTemplate = new JsonObject();
					saleTemplate.addProperty("saleTemplateName", rs.getString("saleTemplateName"));
					saleTemplate.addProperty("isRunning", rs.getInt("isRunning"));
					saleTemplate.addProperty("discountRate", rs.getInt("discountRate"));
					saleTemplate.addProperty("description", rs.getString("description"));
					String saleData = rs.getString("saleData");
					saleTemplate.add("saleData", new Gson().fromJson(saleData,JsonObject.class));
					saleTemplates.add(saleTemplate);
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saleTemplates;
	}

	/**
	 * this function get sale name and number 0/1 if its running or not, and update the status of it
	 * (running or not) by sending appropriate  query to the DB
	 * @param saleName - the sale name as string
	 * @param isRunning - int number of status (running or not)
	 */
	public void updateRunningSale(String saleName, int isRunning) {
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query =  "UPDATE sale_templates " + 
						 "SET isRunning = " + isRunning +  
						 " WHERE saleTemplateName = '" + saleName + "';";
				stmt.executeUpdate(query);
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	
	/**
	  * this function send query to the DB and get the Current running sales in the table 
	 * @return current running sale name as Json Objects
	 */
	public JsonObject getCurrentRunningSaleName() {
		JsonObject json = new JsonObject();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  sale_templates "
					  + "WHERE isRunning = " + 1 + ";";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					json.addProperty("saleTemplateName", rs.getString("saleTemplateName"));
					json.addProperty("discountRate", rs.getInt("discountRate"));
					JsonObject saleData = new Gson().fromJson(rs.getString("saleData"), JsonObject.class);
					json.add("saleData", saleData);
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	/**
	 *  * this function send query to the DB and add new sale template to the DB
	 * @param saleTemplate - as JsonObject with all the data
	 */
	public void addNewSaleTemplate(JsonObject saleTemplate) {
		//saleTemplateName, isRunning, discountRate, saleData, description
		String saleTemplateName = saleTemplate.get("saleTemplateName").getAsString();
		int discountRate = saleTemplate.get("discountRate").getAsInt();
		JsonObject saleData = saleTemplate.get("saleData").getAsJsonObject();
		String description = saleTemplate.get("description").getAsString();
		int isRunning = saleTemplate.get("isRunning").getAsInt();
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO sale_templates "
						+ "(saleTemplateName, isRunning, discountRate, saleData, description) "
					  + "VALUES ('" + saleTemplateName + "','"+ isRunning +"','" + discountRate + "','" 
						+ saleData + "','" + description + "');"; 
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
	 * this function get saleTemplate as JsonObject and send query to the DB to remove the sale template given from table
	 * @param saleTemplate - as a JsonObect with all the data inside
	 */
	public void removeSaleTemplate(JsonObject saleTemplate) {
		String saleTemplateName = saleTemplate.get("saleTemplateName").getAsString();
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "DELETE FROM sale_templates "
					  + "WHERE saleTemplateName = '" + saleTemplateName + "';"; 
				stmt = DBConnector.conn.createStatement();
				stmt.executeUpdate(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
