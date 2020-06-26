package client.gui.customer;

import com.google.gson.JsonObject;

public interface IHomeHeatingFuelDBManager {

	public boolean submitHomeHeatingFuelOrder(JsonObject json);
	
	public float getPricePerLitter();
	
	public int getLastOrderID(JsonObject json);
	
	public String[] getFuelCompaniesByCustomerID(String customerID);
	
	public JsonObject getCreditCardByCustomerID();
	
	public float getCurrentRunningSaleTemplateDiscount();
	
}
