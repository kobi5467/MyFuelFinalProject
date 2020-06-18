package entitys;

import entitys.enums.FuelType;
/**
 * This class is for vehicle objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class Vehicle {

	private String vehicleNumber;
	private FuelType fuelType;
	private String customerID;

	public Vehicle(String vehicleNumber, FuelType fuelType, String customerID) {
		this.vehicleNumber = vehicleNumber;
		this.fuelType = fuelType;
		this.customerID = customerID;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public FuelType getFuel() {
		return fuelType;
	}

	public void setFuel(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleNumber=" + vehicleNumber + ", fuel=" + fuelType.toString() + ", customerID=" + customerID
				+ "]";
	}

}
