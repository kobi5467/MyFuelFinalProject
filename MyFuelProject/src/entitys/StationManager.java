package entitys;

public class StationManager extends Employee {

	private String managerId;
	private FuelStation fuelStation;
	
	public StationManager(String username, String password,
			UserPermission userPermission, String name, String email,
			String phoneNumber, int employeeNumber, String employeeRole,
			OrganizationAffiliation organizationAffiliation, String managerId,
			FuelStation fuelStation) {
		super(username, password, userPermission, name, email, phoneNumber,
				employeeNumber, employeeRole, organizationAffiliation);
		this.managerId = managerId;
		this.fuelStation = fuelStation;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public FuelStation getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}

	@Override
	public String toString() {
		return "StationManager [managerId=" + managerId + ", fuelStation="
				+ fuelStation + "]";
	}
	
	
	
}
