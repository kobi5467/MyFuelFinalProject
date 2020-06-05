package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;

public class UserDBController {

	// User Table structure
	// (userName, password, userPermisson, name , email , phoneNumber ,
	// lastLoginTime, isLogin)
	// (varChar , varChar , varChar , varChar , varChar , varChar , long , boolean)

	public boolean checkIfUsernameExist(String userName) {
		boolean isExist = false;

		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  users " + "WHERE userName ='" + userName + "';";
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

	// get json object that contains the username and the password.
	public JsonObject checkLogin(String userName, String password) {
		JsonObject response = new JsonObject();
		String errorMessage = "";
		boolean isValid = false;
		String query = "";
		Statement stmt = null;

		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  users " + 
						"WHERE userName ='" + userName + "' AND password = '" + password + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					int isLogin = rs.getInt("isLogin");
					if (isLogin == 1) {
						errorMessage = "User is already logged in..";
					} else {
						isValid = true;
					}
				} else {
					errorMessage = "Invalid inputs..";
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

//		if(isValid) updateLoginFlag(userName, 1);		// TODO - SET THIS BACK !!!!!!!!!!! 
		response.addProperty("isValid", isValid);
		response.addProperty("errorMessage", errorMessage);
		return response;
	}

	public JsonObject getUserDetails(JsonObject json) {
		String query = "";

		String userName = json.get("userName").getAsString();
		JsonObject user = new JsonObject();

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  users " + 
						"WHERE userName ='" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					user.addProperty("name", rs.getString("name"));
					user.addProperty("userPermission", rs.getString("userPermission"));
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	// isLoginValue - 1 for login , 0 for logout.
	public void updateLoginFlag(String userName, int isLoginValue) {
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query = "UPDATE users " + "SET isLogin = " + isLoginValue + " WHERE userName = '" + userName + "';";
				stmt.executeUpdate(query);
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addUser(JsonObject user) {
		String userName = user.get("userName").getAsString();
		String password = user.get("password").getAsString();
		String name = user.get("name").getAsString(); 
		String email = user.get("email").getAsString();
		String phoneNumber = user.get("phoneNumber").getAsString();
		String userPermission = user.get("userPermission").getAsString();
		
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO users (userName, password, name, email, phoneNumber, userPermission, isLogin) " + 
						"VALUES ('" + userName + "','"+ password + "','" + name + "','" + email 
								+ "','" + phoneNumber + "','" + userPermission + "',0);";
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
}
