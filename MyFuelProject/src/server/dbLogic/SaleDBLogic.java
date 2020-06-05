package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SaleDBLogic {

	// saleName, saleType, isRunning, startSaleTime, endSaleTime, lastRunningDate,
	// discountRate, saleDescription

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
					saleTemplate.addProperty("saleName", rs.getString("saleTemplateName"));
					saleTemplate.addProperty("saleType", rs.getString("saleType"));
					saleTemplate.addProperty("isRunning", rs.getInt("isRunning"));
					saleTemplate.addProperty("startSaleTime", rs.getString("startSaleTime"));
					saleTemplate.addProperty("endSaleTime", rs.getString("endSaleTime"));
					saleTemplate.addProperty("lastRunningDate", rs.getString("lastRunningDate"));
					saleTemplate.addProperty("discountRate", rs.getString("discountRate"));
					saleTemplate.addProperty("saleDescription", rs.getString("saleDescription"));
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

	public String getCurrentRunningSaleName() {
		String saleName = "";
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  sale_templates "
					  + "WHERE isRunning = " + 1 + ";";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					saleName = rs.getString("saleTemplateName");
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return saleName;
	}
}
