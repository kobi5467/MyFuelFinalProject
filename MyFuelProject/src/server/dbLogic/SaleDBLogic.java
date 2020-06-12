package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SaleDBLogic {

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
