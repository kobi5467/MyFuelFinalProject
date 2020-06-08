package server.dbLogic;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

	public boolean AddNewReport(JsonArray reportData, String reportType) {
		String query = "";
		String currentTime;
		Statement stmt = null;

		SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy ' ' HH:mm ");
		Date date = new Date(System.currentTimeMillis());
		currentTime=formatter.format(date);
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO reports (createDate, reportType, reportData) "
						+ "VALUES ('" + currentTime	+ "','"	+ reportType + "','" + reportData + "');";
				stmt = DBConnector.conn.createStatement();
				boolean rs = stmt.execute(query);
				System.out.println(rs);
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	public JsonArray getStationsID(String reportType) {
		JsonArray stationsID= new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"+reportType+"';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject json = new Gson().fromJson(rs.getString("reportData"),JsonObject.class);
					stationsID.add(json.get("stationID"));
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stationsID;
	}

	public JsonArray getCeateDates(String reportType) {
		JsonArray createDates = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"+reportType+"';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject json = new Gson().fromJson(rs.getString("reportData"),JsonObject.class);
					createDates.add(json.get("stationID"));
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return createDates;
	}
}
