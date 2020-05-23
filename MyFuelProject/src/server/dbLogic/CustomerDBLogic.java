package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;

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
	
	public JsonArray getCustomerTypes() {
		JsonArray types = new JsonArray();
		
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  customer_type ";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					types.add(rs.getString("customerType"));
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return types;
		
	}
}
