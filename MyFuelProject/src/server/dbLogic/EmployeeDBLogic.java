package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class is responsible on send querys to the DB and get/insert/update/delete
 * Data from the data base and send it back to the server controller with the 
 * answer about the request that has been sent.
 * @author MyFuel Team
 *@version Final
 */
public class EmployeeDBLogic {

	/**
	 * this function get the string user name and send query to the DB to get the 
	 * employee role by the user name, and returns it as string
	 * @param userName - the string value of the username
	 * @return - employee role as string
	 */
	public String getEmployeeRoleByUsername(String userName) {
		String employeeRole = "";
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  employees " + 
						"WHERE userName ='" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					employeeRole = rs.getString("employeeRole");
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeeRole;
	}
	/**
	 * this function get the string username and send query to the DB
	 * and find the emloyee ID by the username and then returns the string value of
	 * the Employee ID
	 * @param userName - the string value of username
	 * @return employee ID- as a string
	 */
	public String getEmployeeIDByUsername(String userName) {
		String employeeID = "";
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  employees " + 
						"WHERE userName ='" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					employeeID = rs.getString("employeNumber");
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeeID;
	}
	/**
	 * this function get the string username and send query to the DB
	 * and find the station ID number by the username, then it returns
	 * it as a string value
	 * @param username - the string value of username 
	 * @return Station ID- as the string value of it
	 */
	public String getStationIDByUserName(String username){
		String stationID = "";
		
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT fuel_stations.stationID FROM  employees, fuel_stations " + 
						"WHERE employees.userName ='" + username + "' AND"
								+ " fuel_stations.managerID = employees.employeeNumber ;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					stationID = rs.getString("stationID");
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stationID;
	}
}
