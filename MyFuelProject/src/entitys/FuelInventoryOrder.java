package entitys;

import enums.Status;

public class FuelInventoryOrder extends Order{

	private Supplier supplier;
	private FuelStation fuelStation;
	
	public FuelInventoryOrder(String orderId, String orderDate,
			Status orderStatus, Fuel fuel, float fuelAmount, float totalPrice,
			SaleTemplate saleTemplate, Supplier supplier,
			FuelStation fuelStation) {
		super(orderId, orderDate, orderStatus, fuel, fuelAmount, totalPrice,
				saleTemplate);
		this.supplier = supplier;
		this.fuelStation = fuelStation;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public FuelStation getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}

	@Override
	public String toString() {
		return "FuelInventoryOrder [supplier=" + supplier + ", fuelStation="
				+ fuelStation + "]";
	}

	
	
}
