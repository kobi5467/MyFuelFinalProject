package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
public class ReportDBLogic {

	public boolean AddNewReport(JsonObject reportData, String reportType) {
		String query = "";
		String currentTime;
		Statement stmt = null;

		currentTime = ObjectContainer.getCurrentDate();
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO reports (createDate, reportType, reportData) "
						+ "VALUES ('" + currentTime	+ "','"	+ reportType + "','" + reportData + "');";
				stmt = DBConnector.conn.createStatement();
				boolean rs = stmt.execute(query);
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public JsonArray getStationsIDByReportType(String reportType) {
		JsonArray stationsID = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"
						+ reportType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject json = new Gson().fromJson(
							rs.getString("reportData"), JsonObject.class);
					if (!stationsID.contains(json.get("stationID")))
						stationsID.add(json.get("stationID").getAsString());
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stationsID;
	}

	public JsonArray getCreateDatesByStationsIdAndReportType(String reportType,
			String stationId) {
		JsonArray createDates = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports WHERE reportType='" + reportType
						+ "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					JsonObject json = new Gson().fromJson(
							rs.getString("reportData"), JsonObject.class);
					if (json.get("stationID").getAsString().equals(stationId)) {
						createDates.add(rs.getString("createDate"));
					}
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return createDates;
	}

	public JsonArray getCreateDatesByReportType(String reportType) {
		JsonArray createDates = new JsonArray();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"
						+ reportType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					createDates.add(rs.getString("createDate"));
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return createDates;
	}

	public JsonObject getStationReports(String reportType, String stationId,
			String date) {
		JsonObject json =  new JsonObject();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"
						+ reportType + "' AND createDate='" + date + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					json = new Gson().fromJson(
							rs.getString("reportData"), JsonObject.class);
					if (json.get("stationID").getAsString().equals(stationId)) {
						return json;
					}
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public JsonObject getMarketingManagerReports(String reportType,String date) {
		JsonObject json =  new JsonObject();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"
						+ reportType + "' AND createDate='" + date + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					json = new Gson().fromJson(rs.getString("reportData"), JsonObject.class);
						return json;
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
