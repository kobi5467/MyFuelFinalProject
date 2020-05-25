package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDBLogic {

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

}
