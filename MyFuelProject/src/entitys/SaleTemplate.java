package entitys;

import entitys.enums.FuelType;
import entitys.enums.SaleTemplateType;
/**
 * This class is for Sale Template objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class SaleTemplate {

	private String saleTemplateName;
	private SaleTemplateType saleTemplateType;
	private boolean isRuuning;
	private long startSaleTime;
	private long endSaleTime;
	private String lastRunningDate;
	private float discountRate;
	private FuelType fuelType; // Maybe need to be array (for sales type of customer and hours)
	private String saleDescription;
	
	public SaleTemplate(String saleTemplateName,
			SaleTemplateType saleTemplateType, boolean isRuuning,
			long startSaleTime, long endSaleTime, String lastRunningDate,
			float discountRate, FuelType fuelType, String saleDescription) {
		this.saleTemplateName = saleTemplateName;
		this.saleTemplateType = saleTemplateType;
		this.isRuuning = isRuuning;
		this.startSaleTime = startSaleTime;
		this.endSaleTime = endSaleTime;
		this.lastRunningDate = lastRunningDate;
		this.discountRate = discountRate;
		this.fuelType = fuelType;
		this.saleDescription = saleDescription;
	}

	public String getSaleTemplateName() {
		return saleTemplateName;
	}

	public void setSaleTemplateName(String saleTemplateName) {
		this.saleTemplateName = saleTemplateName;
	}

	public SaleTemplateType getSaleTemplateType() {
		return saleTemplateType;
	}

	public void setSaleTemplateType(SaleTemplateType saleTemplateType) {
		this.saleTemplateType = saleTemplateType;
	}

	public boolean isRuuning() {
		return isRuuning;
	}

	public void setRuuning(boolean isRuuning) {
		this.isRuuning = isRuuning;
	}

	public long getStartSaleTime() {
		return startSaleTime;
	}

	public void setStartSaleTime(long startSaleTime) {
		this.startSaleTime = startSaleTime;
	}

	public long getEndSaleTime() {
		return endSaleTime;
	}

	public void setEndSaleTime(long endSaleTime) {
		this.endSaleTime = endSaleTime;
	}

	public String getLastRunningDate() {
		return lastRunningDate;
	}

	public void setLastRunningDate(String lastRunningDate) {
		this.lastRunningDate = lastRunningDate;
	}

	public float getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(float discountRate) {
		this.discountRate = discountRate;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuel(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public String getSaleDescription() {
		return saleDescription;
	}

	public void setSaleDescription(String saleDescription) {
		this.saleDescription = saleDescription;
	}

	@Override
	public String toString() {
		return "SaleTemplate [saleTemplateName=" + saleTemplateName
				+ ", saleTemplateType=" + saleTemplateType + ", isRuuning="
				+ isRuuning + ", startSaleTime=" + startSaleTime
				+ ", endSaleTime=" + endSaleTime + ", lastRunningDate="
				+ lastRunningDate + ", discountRate=" + discountRate
				+ ", fuelType=" + fuelType + ", saleDescription=" + saleDescription
				+ "]";
	}
		
	
}
