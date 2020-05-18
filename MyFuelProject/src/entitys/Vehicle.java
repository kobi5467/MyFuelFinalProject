package entitys;

import enums.VehicleType;

public class Vehicle {

	private String vehicleNumber;
	private VehicleType vehicleType;
	private Fuel fuel;
	private Customer Owner;
	
	public Vehicle(String vehicleNumber, VehicleType vehicleType, Fuel fuel,
			Customer owner) {
		this.vehicleNumber = vehicleNumber;
		this.vehicleType = vehicleType;
		this.fuel = fuel;
		Owner = owner;
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

	public Fuel getFuel() {
		return fuel;
	}

	public void setFuel(Fuel fuel) {
		this.fuel = fuel;
	}

	public Customer getOwner() {
		return Owner;
	}

	public void setOwner(Customer owner) {
		Owner = owner;
	}

	@Override
	public String toString() {
		return "Vehicle [vehicleNumber=" + vehicleNumber + ", vehicleType="
				+ vehicleType + ", fuel=" + fuel + ", Owner=" + Owner + "]";
	}
	
	
}
