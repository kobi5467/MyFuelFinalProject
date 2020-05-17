package entitys;

public class FuelCompany {

	private String companyId;
	private String companyName;
	
	public FuelCompany(String companyId, String companyName) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "FuelCompany [companyId=" + companyId + ", companyName="
				+ companyName + "]";
	}
	
}
