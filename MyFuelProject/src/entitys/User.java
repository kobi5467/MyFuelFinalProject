package entitys;

import entitys.enums.UserPermission;
/**
 * This class is for User objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class User {

	private String userName;
	private String password;
	private UserPermission userPermission;
	
	private String name;
	private String email;
	private String phoneNumber;
	
	private long lastLoginTime;
	private boolean isLogin;
	
	public User() {
		
	}
	
	public User(String username, String password, UserPermission userPermission, String name, String email,
			String phoneNumber) {
		this.userName = username;
		this.password = password;
		this.userPermission = userPermission;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserPermission getUserPermission() {
		return userPermission;
	}

	public void setUserPermission(UserPermission userPermission) {
		this.userPermission = userPermission;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	@Override
	public String toString() {
		return "User [username=" + userName + ", password=" + password + ", userPermission=" + userPermission
				+ ", name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + ", lastLoginTime="
				+ lastLoginTime + ", isLogin=" + isLogin + "]";
	}	
}
