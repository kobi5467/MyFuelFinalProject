package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ObjectContainer;

/**
 * This class is responsible on send queries to the DB and
 * get/insert/update/delete Data from the data base and send it back to the
 * server controller with the answer about the request that has been sent.
 * 
 * @author Or Haim
 *
 */
public class ReportDBLogic {

	/**
	 * This function receives a request to add a new report to database. It adds
	 * the report to the "reports" table.
	 * 
	 * @param reportData
	 *            - contain all report data required to be added.
	 * @param reportType
	 *            - contains the report type required to be added.
	 * @return boolean response, true - successfully added the report, false -
	 *         failed to add report.
	 */
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

	/**
	 * This function receives from the database all the stations ID matching to
	 * the required report type.
	 * 
	 * @param reportType
	 *            - contains the report type of the selected report.
	 * @return JsonArray that contains the results of the request(all the
	 *         stations ID match).
	 */
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

	/**
	 * This function receives from the database all the report creation dates
	 * matching to report type and station ID required.
	 * 
	 * @param reportType
	 *            - contains the report type of the selected report.
	 * @param stationId
	 *            - contains the station ID of the selected report.
	 * @return JsonArray that contains the results of the request(all the report
	 *         creation dates match).
	 */
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

	/**
	 * This function receives from the database all the report creation dates
	 * matching to required report type.
	 * 
	 * @param reportType
	 *            - contains the report type of the selected report.
	 * @return JsonArray that contains the results of the request(all the report
	 *         creation dates match).
	 */
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

	/**
	 * This function receives a request to fetch the required station manager
	 * report from the database by report type, station ID and date.
	 * 
	 * @param reportType
	 *            - contains the report type of the selected report.
	 * @param stationId
	 *            - contains the station ID of the selected report.
	 * @param date
	 *            - contains the date of the selected report.
	 * @return JsonObject that contains the required report.
	 */
	public JsonObject getStationReports(String reportType, String stationId,
			String date) {
		JsonObject json = new JsonObject();
		String query = "";
		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM reports " + "WHERE reportType='"
						+ reportType + "' AND createDate='" + date + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					json = new Gson().fromJson(rs.getString("reportData"),
							JsonObject.class);
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

	/**
	 * This function receives a request to fetch the required marketing manager
	 * report from the database by report type and date.
	 * 
	 * @param reportType
	 *            - contains the report type of the selected report.
	 * @param date
	 *            - contains the date of the selected report.
	 * @return JsonObject that contains the required report.
	 */
	public JsonObject getMarketingManagerReports(String reportType, String date) {
		JsonObject json = new JsonObject();
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
