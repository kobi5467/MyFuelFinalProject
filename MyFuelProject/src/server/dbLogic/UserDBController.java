package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;

import entitys.enums.UserPermission;

public class UserDBController {

	// User Table structure
	// (userName, password, userPermisson, name    , email   , phoneNumber , lastLoginTime, isLogin)
	// (varChar , varChar , varChar		 , varChar , varChar , varChar	   , long		  , boolean)
	
	
	public boolean checkIfUsernameExist(String userName) {
		boolean isExist = false;
		
		String query = "";
		Statement stmt = null;
		
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  users "
					  + "WHERE userName ='" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					isExist = true;
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return isExist;
	}
	
	
	//get json object that contains the username and the password.
	public boolean checkLogin(JsonObject json) {
		boolean isValid = false;
		String userName = json.get("userName").getAsString();
		String password = json.get("password").getAsString();
		
		String query = "";
		Statement stmt = null;
		
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  users "
					  + "WHERE userName ='" + userName + "' AND password = '" + password + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					isValid = true;
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return isValid;
	}
	
	public String getUserPermission(JsonObject json) {
		String permission = "";
		String query = "";
		
		String userName = json.get("userName").getAsString();
		
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  users "
					  + "WHERE userName ='" + userName + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					permission = rs.getString("userPermission");
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return permission;
	}
	
	
}
