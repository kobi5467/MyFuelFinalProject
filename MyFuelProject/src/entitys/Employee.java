package entitys;

import enums.OrganizationAffiliation;
import enums.UserPermission;

public class Employee extends User {
	
	private int employeeNumber;
	private String employeeRole;
	private OrganizationAffiliation organizationAffiliation;
	
	public Employee(String username, String password,
			UserPermission userPermission, String name, String email,
			String phoneNumber, int employeeNumber, String employeeRole,
			OrganizationAffiliation organizationAffiliation) {
		super(username, password, userPermission, name, email, phoneNumber);
		this.employeeNumber = employeeNumber;
		this.employeeRole = employeeRole;
		this.organizationAffiliation = organizationAffiliation;
	}

	public int getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(String employeeRole) {
		this.employeeRole = employeeRole;
	}

	public OrganizationAffiliation getOrganizationAffiliation() {
		return organizationAffiliation;
	}

	public void setOrganizationAffiliation(
			OrganizationAffiliation organizationAffiliation) {
		this.organizationAffiliation = organizationAffiliation;
	}

	@Override
	public String toString() {
		return "Employee [employeeNumber=" + employeeNumber + ", employeeRole="
				+ employeeRole + ", organizationAffiliation="
				+ organizationAffiliation + "]";
	}
	
	
	
	
	
	
}
