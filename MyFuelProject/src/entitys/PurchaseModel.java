package entitys;

import java.util.ArrayList;

public class PurchaseModel {

	private String purchaseModeltype;
	private float purchaseModelDiscount;
	private ArrayList<FuelCompany> fuelCompany = new ArrayList<FuelCompany>();
	
	public PurchaseModel(String purchaseModeltype, float purchaseModelDiscount,
			ArrayList<FuelCompany> fuelCompany) {
		super();
		this.purchaseModeltype = purchaseModeltype;
		this.purchaseModelDiscount = purchaseModelDiscount;
		this.fuelCompany = fuelCompany;
	}

	public String getPurchaseModeltype() {
		return purchaseModeltype;
	}

	public void setPurchaseModeltype(String purchaseModeltype) {
		this.purchaseModeltype = purchaseModeltype;
	}

	public float getPurchaseModelDiscount() {
		return purchaseModelDiscount;
	}

	public void setPurchaseModelDiscount(float purchaseModelDiscount) {
		this.purchaseModelDiscount = purchaseModelDiscount;
	}

	public ArrayList<FuelCompany> getFuelCompany() {
		return fuelCompany;
	}

	public void setFuelCompany(ArrayList<FuelCompany> fuelCompany) {
		this.fuelCompany = fuelCompany;
	}

	@Override
	public String toString() {
		return "PurchaseModel [purchaseModeltype=" + purchaseModeltype
				+ ", purchaseModelDiscount=" + purchaseModelDiscount
				+ ", fuelCompany=" + fuelCompany + "]";
	}
	
	
}
