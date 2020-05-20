package entitys;

public class FuelInventory {

	private FuelStation fuelStation;
	private Fuel fuel;
	private float currentFuelAmount;
	private float thresholdAmount;
	private float maxFuelAmount;

	public FuelInventory(FuelStation fuelStation, Fuel fuel, float currentFuelAmount, float thresholdAmount, float maxFuelAmount) {
		this.fuelStation = fuelStation;
		this.fuel = fuel;
		this.currentFuelAmount = currentFuelAmount;
		this.thresholdAmount = thresholdAmount;
		this.maxFuelAmount = maxFuelAmount;
	}

	public FuelStation getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}

	public Fuel getFuel() {
		return fuel;
	}

	public void setFuel(Fuel fuel) {
		this.fuel = fuel;
	}

	public float getCurrentFuelAmount() {
		return currentFuelAmount;
	}

	public void setCurrentFuelAmount(float currentFuelAmount) {
		this.currentFuelAmount = currentFuelAmount;
	}

	public float getThresholdAmount() {
		return thresholdAmount;
	}

	public void setThresholdAmount(float thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public float getMaxFuelAmount() {
		return maxFuelAmount;
	}

	public void setMaxFuelAmount(float maxFuelAmount) {
		this.maxFuelAmount = maxFuelAmount;
	}

	@Override
	public String toString() {
		return "FuelInventory [fuelStation=" + fuelStation + ", fuel=" + fuel + ", currentFuelAmount="
				+ currentFuelAmount + ", thresholdAmount=" + thresholdAmount + ", maxFuelAmount=" + maxFuelAmount + "]";
	}

}
