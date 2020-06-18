package entitys;
/**
 * This class is for Fuel Company objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class FuelCompany {

	private String companyName;

	public FuelCompany(String companyName) {
		super();
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "FuelCompany [companyName=" + companyName + "]";
	}
}
