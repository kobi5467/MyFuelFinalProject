package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;

public class SaleDBLogic {
	
	public JsonArray getSaleNames(){
		JsonArray saleNames = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  sale_templates";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					 saleNames.add(rs.getString("saleName"));
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return saleNames;
	}
}
