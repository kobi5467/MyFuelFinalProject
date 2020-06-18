package entitys;

import entitys.enums.UserPermission;
/**
 * This class is for Supplier objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class Supplier extends User {

	private String SupplierId;

	public Supplier(String username, String password,
			UserPermission userPermission, String name, String email,
			String phoneNumber, String supplierId) {
		super(username, password, userPermission, name, email, phoneNumber);
		SupplierId = supplierId;
	}

	public String getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(String supplierId) {
		SupplierId = supplierId;
	}

	@Override
	public String toString() {
		return "Supplier [SupplierId=" + SupplierId + "]";
	}
	
	
}
