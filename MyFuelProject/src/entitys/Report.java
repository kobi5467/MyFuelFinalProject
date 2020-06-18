package entitys;
/**
 * This class is for Report objects, constructor setters, getters, and variables
 * @author MyFuel Team
 *
 */
public class Report {

	private String reportType;
	private long createDate;
	
	private String reportDate;
	public Report(String reportType, long createDate, String reportDate) {
		super();
		this.reportType = reportType;
		this.createDate = createDate;
		this.reportDate = reportDate;
	}
	
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
	@Override
	public String toString() {
		return "Report [reportType=" + reportType + ", createDate="
				+ createDate + ", reportDate=" + reportDate + "]";
	}
	
	
}
