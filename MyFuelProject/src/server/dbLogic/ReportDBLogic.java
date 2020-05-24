package server.dbLogic;

public class ReportDBLogic {

	
	/*public String generateReportByType(JsonObject reportDetails){
		String query = "";
		Statement stmt = null;
		
		String reportType = reportDetails.get("reportType").getAsString();
		if(reportType.equals("Comments")){
			String saleName =reportDetails.get("saleName").getAsString();
			int countCustomer=0,sumPurchase=0;
			try {
				if(DBConnector.conn != null) {
					query = "SELECT * FROM Fast_fuel_orders"
							+ " WHERE saleTemplateName="+saleName;
					stmt = DBConnector.conn.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while(rs.next()) {
						 sumPurchase+=rs.getInt("totalPrice");
					}
				}else {
					System.out.println("Conn is null");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}*/
	
}
