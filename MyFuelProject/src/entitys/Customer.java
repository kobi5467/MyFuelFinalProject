package entitys;

import java.util.ArrayList;

import entitys.enums.UserPermission;

public class Customer extends User {

	private String customerId;
	private String City;
	private String Street;
	private int customerRate;
	private CustomerType customerType;
	private PurchaseModel purchaseModel;
	private CreditCard creditCard;
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	public Customer(String username, String password, UserPermission userPermission, String name, String email,
			String phoneNumber, String customerId, String city, String street, int customerRate,
			CustomerType customerType, PurchaseModel purchaseModel, CreditCard creditCard,
			ArrayList<Vehicle> vehicles) {
		super(username, password, userPermission, name, email, phoneNumber);
		this.customerId = customerId;
		City = city;
		Street = street;
		this.customerRate = customerRate;
		this.customerType = customerType;
		this.purchaseModel = purchaseModel;
		this.creditCard = creditCard;
		this.vehicles = vehicles;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public int getCustomerRate() {
		return customerRate;
	}

	public void setCustomerRate(int customerRate) {
		this.customerRate = customerRate;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public PurchaseModel getPurchaseModelType() {
		return purchaseModel;
	}

	public void setPurchaseModelType(PurchaseModel purchaseModelType) {
		this.purchaseModel = purchaseModelType;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", City=" + City + ", Street=" + Street + ", customerRate="
				+ customerRate + ", customerType=" + customerType + ", purchaseModel=" + purchaseModel + ", creditCard="
				+ creditCard + "]";
	}

}
