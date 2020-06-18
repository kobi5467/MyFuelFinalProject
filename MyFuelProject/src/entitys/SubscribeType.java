package entitys;
/**
 * This class is for Subscribe Type objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class SubscribeType { 
	
	private String subscribeType;
	private float discountRate;
	
	public SubscribeType(String subscribeType, float discountRate) {
		this.subscribeType = subscribeType;
		this.discountRate = discountRate;
	}

	public String getSubscribeType() {
		return subscribeType;
	}

	public void setSubscribeType(String subscribeType) {
		this.subscribeType = subscribeType;
	}

	public float getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(float discountRate) {
		this.discountRate = discountRate;
	}

	@Override
	public String toString() {
		return "SubscribeType [subscribeType=" + subscribeType + ", DiscountRate="
				+ discountRate + "]";
	}
	
	
}
