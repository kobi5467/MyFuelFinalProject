package entitys;

import entitys.enums.Status;
/**
 * This class is for Order objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class Order {

	private String orderId;
	private String orderDate;
	private Status orderStatus;
	private Fuel fuel;
	private float fuelAmount;
	private float totalPrice;
	//private OrderType orderType; // Its unnecessary 
	private SaleTemplate saleTemplate; // Can be null 
	
	public Order(String orderId, String orderDate, Status orderStatus,
			Fuel fuel, float fuelAmount, float totalPrice,
			SaleTemplate saleTemplate) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.fuel = fuel;
		this.fuelAmount = fuelAmount;
		this.totalPrice = totalPrice;
		this.saleTemplate = saleTemplate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Status getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Status orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Fuel getFuel() {
		return fuel;
	}

	public void setFuel(Fuel fuel) {
		this.fuel = fuel;
	}

	public float getFuelAmount() {
		return fuelAmount;
	}

	public void setFuelAmount(float fuelAmount) {
		this.fuelAmount = fuelAmount;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public SaleTemplate getSaleTemplate() {
		return saleTemplate;
	}

	public void setSaleTemplate(SaleTemplate saleTemplate) {
		this.saleTemplate = saleTemplate;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderDate=" + orderDate
				+ ", orderStatus=" + orderStatus + ", fuel=" + fuel
				+ ", fuelAmount=" + fuelAmount + ", totalPrice=" + totalPrice
				+ ", saleTemplate=" + saleTemplate + "]";
	}
	
	
}
