package server.dbLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

	public static Connection conn;	//
	
	private String url = "jdbc:mysql://localhost/myfuel?serverTimezone=IST";
	private String username = "root";
	private String password = "12345678";
	
	public UserDBController userDBController;
	
	public DBConnector() {
		createConnection();
		userDBController = new UserDBController();
	}
	
	private void createConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
		}
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public void closeConnection() {
		try {
			if(!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
