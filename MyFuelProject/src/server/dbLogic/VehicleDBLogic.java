package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;

public class VehicleDBLogic {

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
				System.out.println(query);
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
				System.out.println(query);
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
	
}
