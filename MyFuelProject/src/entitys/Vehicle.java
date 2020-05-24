package entitys;

import entitys.enums.FuelType;
import entitys.enums.VehicleType;

public class Vehicle {

	private String vehicleNumber;
	private VehicleType vehicleType;
	private FuelType fuelType;
	private String ownerID;
	
	public Vehicle(String vehicleNumber, VehicleType vehicleType, FuelType fuelType,
			String ownerID) {
		this.vehicleNumber = vehicleNumber;
		this.vehicleType = vehicleType;
		this.fuelType = fuelType;
		this.ownerID = ownerID;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public FuelType getFuel() {
		return fuelType;
	}

	public void setFuel(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public String getOwner() {
		return ownerID;
	}

	public void setOwner(String ownerID) {
		this.ownerID = ownerID;
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleNumber=" + vehicleNumber + ", vehicleType="
				+ vehicleType + ", fuel=" + fuelType.toString() + ", ownerID=" + ownerID + "]";
	}
	
	
}
