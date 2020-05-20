package entitys;

public class CreditCard {

	private String cardOnwerId;
	private String cardNumber;
	private String validationDate;
	private int cvvNumber;

	public CreditCard(String cardOnwerId, String cardNumber, String validationDate, int cvvNumber) {
		super();
		this.cardOnwerId = cardOnwerId;
		this.cardNumber = cardNumber;
		this.validationDate = validationDate;
		this.cvvNumber = cvvNumber;
	}

	public String getCardOnwerId() {
		return cardOnwerId;
	}

	public void setCardOnwerId(String cardOnwerId) {
		this.cardOnwerId = cardOnwerId;
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

	public int getCvvNumber() {
		return cvvNumber;
	}

	public void setCvvNumber(int cvvNumber) {
		this.cvvNumber = cvvNumber;
	}

	@Override
	public String toString() {
		return "CreditCard [cardOnwerId=" + cardOnwerId + ", cardNumber=" + cardNumber + ", validationDate="
				+ validationDate + ", cvvNumber=" + cvvNumber + "]";
	}

}
