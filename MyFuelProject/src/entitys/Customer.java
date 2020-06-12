package entitys;

import java.util.ArrayList;

import entitys.enums.UserPermission;

public class Customer extends User {

	private String customerID;
	private String city;
	private String street;
	private int customerRate;
	private SubscribeType subscribeType;
	private String customerType;
	private PurchaseModel purchaseModel;
	private CreditCard creditCard;
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	private String paymentMethod;
	
	public Customer() {
		
	}
	
	public Customer(String username, String password, UserPermission userPermission, String name, String email,
			String phoneNumber, String customerId, String city, String street, int customerRate,
			SubscribeType subscribeType,String customerType, PurchaseModel purchaseModel, CreditCard creditCard,
			ArrayList<Vehicle> vehicles, String paymentMethod) {
		super(username, password, userPermission, name, email, phoneNumber);
		this.customerID = customerId;
		this.city = city;
		this.street = street;
		this.customerRate = customerRate;
		this.customerType = customerType;
		this.subscribeType = subscribeType;
		this.purchaseModel = purchaseModel;
		this.creditCard = creditCard;
		this.vehicles = vehicles;
		this.paymentMethod = paymentMethod;
		
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCustomerId() {
		return customerID;
	}

	public void setCustomerId(String customerId) {
		this.customerID = customerId;
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

	public int getCustomerRate() {
		return customerRate;
	}

	public void setCustomerRate(int customerRate) {
		this.customerRate = customerRate;
	}

	public SubscribeType getSubscribeType() {
		return subscribeType;
	}

	public void setSubscribeType(SubscribeType subscribeType) {
		this.subscribeType = subscribeType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public PurchaseModel getPurchaseModel() {
		return purchaseModel;
	}

	public void setPurchaseModel(PurchaseModel purchaseModel) {
		this.purchaseModel = purchaseModel;
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
		return "Customer [customerId=" + customerID + ", City=" + city + ", Street=" + street + ", customerRate="
				+ customerRate + ", subscribeType=" + subscribeType + ", customerType=" + customerType
				+ ", purchaseModel=" + purchaseModel + ", creditCard=" + creditCard + ", vehicles=" + vehicles + "]";
	}

}
