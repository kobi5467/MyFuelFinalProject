package entitys;
/**
 * This class is for credit cards objects, setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class CreditCard {

	private String customerID;
	private String cardNumber;
	private String validationDate;
	private String cvvNumber;

	public CreditCard(String customerID, String cardNumber, String validationDate, String cvvNumber) {
		super();
		this.customerID = customerID;
		this.cardNumber = cardNumber;
		this.validationDate = validationDate;
		this.cvvNumber = cvvNumber;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCardCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(String validationDate) {
		this.validationDate = validationDate;
	}

	public String getCvvNumber() {
		return cvvNumber;
	}

	public void setCvvNumber(String cvvNumber) {
		this.cvvNumber = cvvNumber;
	}

	@Override
	public String toString() {
		return "CreditCard [customerID=" + customerID + ", cardNumber=" + cardNumber + ", validationDate="
				+ validationDate + ", cvvNumber=" + cvvNumber + "]";
	}

}
