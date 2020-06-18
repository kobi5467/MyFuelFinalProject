package entitys;
/**
 * This class is for Fuel Station objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class FuelStation {

	private String stationId;
	private FuelCompany fuelCompany;
	private StationManager stationManager;
	
	public FuelStation(String stationId, FuelCompany fuelCompany,StationManager stationManager) {
		this.stationId = stationId;
		this.fuelCompany = fuelCompany;
		this.stationManager=stationManager;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	

	public FuelCompany getFuelCompany() {
		return fuelCompany;
	}

	public void setFuelCompany(FuelCompany fuelCompany) {
		this.fuelCompany = fuelCompany;
	}

	public StationManager getStationManager() {
		return stationManager;
	}

	public void setStationManager(StationManager stationManager) {
		this.stationManager = stationManager;
	}

	@Override
	public String toString() {
		return "FuelStation [stationId=" + stationId + ", fuelCompany="
				+ fuelCompany + ", stationManager=" + stationManager + "]";
	}

	

}
