package entitys;

import entitys.enums.FuelType;

public class Fuel {

	private FuelType fuelType;
	private float pricePerLitter;
	private float maxPricePerLitter;
	
	public Fuel(FuelType fuelType, float pricePerLitter, float maxPricePerLitter) {
		this.fuelType = fuelType;
		this.pricePerLitter = pricePerLitter;
		this.maxPricePerLitter = maxPricePerLitter;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public float getPricePerLitter() {
		return pricePerLitter;
	}

	public void setPricePerLitter(float pricePerLitter) {
		this.pricePerLitter = pricePerLitter;
	}

	public float getMaxPricePerLitter() {
		return maxPricePerLitter;
	}

	public void setMaxPricePerLitter(float maxPricePerLitter) {
		this.maxPricePerLitter = maxPricePerLitter;
	}

	@Override
	public String toString() {
		return "Fuel [fuelType=" + fuelType + ", pricePerLitter="
				+ pricePerLitter + ", maxPricePerLitter=" + maxPricePerLitter
				+ "]";
	}
	
	
	
	
	
	
	
}
