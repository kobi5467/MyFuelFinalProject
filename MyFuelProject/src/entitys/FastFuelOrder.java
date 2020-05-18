package entitys;

import enums.PaymentMethod;
import enums.Status;

public class FastFuelOrder extends Order{

	private PaymentMethod paymentMethod;
	private Customer customer;
	private FuelStation fuelStation;
	private Vehicle vehicle;
	
	public FastFuelOrder(String orderId, String orderDate, Status orderStatus,
			Fuel fuel, float fuelAmount, float totalPrice,
			SaleTemplate saleTemplate, PaymentMethod paymentMethod,
			Customer customer, FuelStation fuelStation, Vehicle vehicle) {
		super(orderId, orderDate, orderStatus, fuel, fuelAmount, totalPrice,
				saleTemplate);
		this.paymentMethod = paymentMethod;
		this.customer = customer;
		this.fuelStation = fuelStation;
		this.vehicle = vehicle;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public FuelStation getFuelStation() {
		return fuelStation;
	}

	public void setFuelStation(FuelStation fuelStation) {
		this.fuelStation = fuelStation;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	@Override
	public String toString() {
		return "FastFuelOrder [paymentMethod=" + paymentMethod + ", customer="
				+ customer + ", fuelStation=" + fuelStation + ", vehicle="
				+ vehicle + "]";
	}
	
}
