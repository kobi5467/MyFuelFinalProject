package entitys.enums;
/**
 * This class is for User permission enums objects and methods 
 * @author Barak
 *
 */
public enum UserPermission {
	CUSTOMER,
	MARKETING_MANAGER,
	MARKETING_REPRESENTATIVE,
	STATION_MANAGER,
	SUPPLIER,
	CEO;
	/**
	 * *this method get string and return the corresponding String to the Enum
	 * @param permission - the string value of the permission
	 * @return the enum of permission
	 */
	public static UserPermission stringToEnumVal(String permission) {
		if(permission == null || permission.trim().equals("")) {
			return null;
		}
		if(permission.equals("CUSTOMER")) {
			return UserPermission.CUSTOMER;
		}
		if(permission.equals("MARKETING_MANAGER")) {
			return UserPermission.MARKETING_MANAGER;
		}
		if(permission.equals("MARKETING_REPRESENTATIVE")) {
			return UserPermission.MARKETING_REPRESENTATIVE;
		}
		if(permission.equals("STATION_MANAGER")) {
			return UserPermission.STATION_MANAGER;
		}
		if(permission.equals("SUPPLIER")) {
			return UserPermission.SUPPLIER;
		}
		if(permission.equals("CEO")) {
			return UserPermission.CEO;
		}
		return null;
	}
}
