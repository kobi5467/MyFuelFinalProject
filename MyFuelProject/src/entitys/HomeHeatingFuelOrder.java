package entitys;

import entitys.enums.Status;
/**
 * This class is for Home Heating fuel order objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class HomeHeatingFuelOrder extends Order {

	private String paymentMethod;
	private String city;
	private String street;
	private String dateSupply;
	private boolean urgentOrder;
	private Customer customer;
	
	public HomeHeatingFuelOrder(String orderId, String orderDate,
			Status orderStatus, Fuel fuel, float fuelAmount, float totalPrice,
			SaleTemplate saleTemplate, String paymentMethod,
			String city, String street, String dateSupply, boolean urgentOrder,
			Customer customer) {
		super(orderId, orderDate, orderStatus, fuel, fuelAmount, totalPrice,
				saleTemplate);
		this.paymentMethod = paymentMethod;
		this.city = city;
		this.street = street;
		this.dateSupply = dateSupply;
		this.urgentOrder = urgentOrder;
		this.customer = customer;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDateSupply() {
		return dateSupply;
	}

	public void setDateSupply(String dateSupply) {
		this.dateSupply = dateSupply;
	}

	public boolean isUrgentOrder() {
		return urgentOrder;
	}

	public void setUrgentOrder(boolean urgentOrder) {
		this.urgentOrder = urgentOrder;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "HomeHeatingFuelOrder [paymentMethod=" + paymentMethod
				+ ", city=" + city + ", street=" + street + ", dateSupply="
				+ dateSupply + ", urgentOrder=" + urgentOrder + ", customer="
				+ customer + "]";
	}
	
}
