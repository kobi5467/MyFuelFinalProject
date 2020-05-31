package entitys;

import entitys.enums.Status;

public class DeterminingRateRequests {

	private int requestID;
	private float currentPrice;
	private float newPrice;
	private Status requestStatus;
	private String fuelType;
	private String createTime;
	
	public DeterminingRateRequests(int requestID,float currentPrice,float newPrice,
			String fuelType ,String createTime	) {
		
		this.requestID=requestID;
		this.currentPrice=currentPrice;
		this.newPrice=newPrice;
		this.requestStatus=Status.WAITING_TO_APPROVE;
		this.fuelType=fuelType;
		this.createTime=createTime;
		
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public float getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(float newPrice) {
		this.newPrice = newPrice;
	}

	public Status getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Status requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
