package entitys;

import entitys.enums.CustomerTypes;

public class CustomerType { 
	
	private CustomerTypes customerType;
	private float DiscountRate;
	
	public CustomerType(CustomerTypes customerType, float discountRate) {
		this.customerType = customerType;
		DiscountRate = discountRate;
	}

	public CustomerTypes getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerTypes customerType) {
		this.customerType = customerType;
	}

	public float getDiscountRate() {
		return DiscountRate;
	}

	public void setDiscountRate(float discountRate) {
		DiscountRate = discountRate;
	}

	@Override
	public String toString() {
		return "CustomerType [customerType=" + customerType + ", DiscountRate="
				+ DiscountRate + "]";
	}
	
	
}
