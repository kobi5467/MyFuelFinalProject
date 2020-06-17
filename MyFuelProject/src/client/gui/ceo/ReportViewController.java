package client.gui.ceo;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

/**
 * This class is the GUI class of report view form. This class will allow to the
 * CEO to watch about all reports were created by station manager and marketing
 * manager
 * 
 * @author Or Haim
 * @version final
 */
public class ReportViewController {

	@FXML
	private Pane viewReportPane;

	@FXML
	private Label lblReportType;

	@FXML
	private ChoiceBox<String> cbReportType;

	@FXML
	private Label lblStationId;

	@FXML
	private ChoiceBox<String> cbStationId;

	@FXML
	private Label lblDate;

	@FXML
	private ChoiceBox<String> cbDate;

	@FXML
	private Button btnOpen;
	
    @FXML
    private Label lblDetails;

    @FXML
    private Label lblBottomDetails;

	@FXML
	private TableView<ObservableList<String>> tblViewReport;
	
	private JsonArray currentColumns;

	/**
	 * This function is called while the user clicking the "Open" button for
	 * display reports. He calls custom functions according to the type of
	 * report the user has selected.
	 * 
	 * @param event
	 *            - this parameter contains value of click event.
	 */
	@FXML
	void openReport(ActionEvent event) {
		if(checkInput()){
			if(cbReportType.getValue().equals("Quarterly Revenue")
					|| cbReportType.getValue().equals("Purchases By Type")
					|| cbReportType.getValue().equals("Inventory items")){
				getStationsReports();
			}
			else {
				getMarketingManagerReports();
			}
		}
	}

	/**
	 * This function sends a request to the server to get from database report
	 * of marketing manager by report type and report creation date.
	 */
	public void getMarketingManagerReports() {
		JsonObject response = new JsonObject();
		JsonObject request = new JsonObject();
		request.addProperty("reportType", cbReportType.getValue());
		request.addProperty("date", cbDate.getValue());
		Message msg = new Message(MessageType.GET_MARKETING_MANAGER_REPORTS,
				request.toString());
		ClientUI.accept(msg);
		response = ObjectContainer.currentMessageFromServer
				.getMessageAsJsonObject();
		setReport(response);
	}

	/**
	 * This function sends a request to the server to get from database report
	 * of station manager by report type, station id and report creation date.
	 */
	public void getStationsReports() {
		JsonObject response = new JsonObject();
		JsonObject request = new JsonObject();
		request.addProperty("reportType", cbReportType.getValue());
		request.addProperty("stationId", cbStationId.getValue());
		request.addProperty("date", cbDate.getValue());
		Message msg = new Message(MessageType.GET_STATIONS_REPORTS,
				request.toString());
		ClientUI.accept(msg);
		response = ObjectContainer.currentMessageFromServer
				.getMessageAsJsonObject();
		setReport(response);
	}

	/**
	 * This function prepares all the required data for the report that came
	 * from the database to view the report.
	 * 
	 * @param response
	 *            - contains all data of the report that came from database
	 */
	public void setReport(JsonObject response) {
		String[] details = response.get("labelText").getAsString().split("_");
		lblDetails.setText(details[0]);
		if(details.length > 1)
			lblBottomDetails.setText(details[1]);
		ArrayList<String> columns = new ArrayList<>();
		ArrayList<ArrayList<String>> rows = new ArrayList<>();
		columns = setColumns(response.get("columns").getAsJsonArray());
		rows = setRows(cbReportType.getValue(), response.get("rows")
				.getAsJsonArray());
		fillTable(columns, rows);
	}

	/**
	 * This function that creates an arrayList that will contain all the
	 * relevant columns of the report.
	 * 
	 * @param jsonColumns
	 *            - JsonArray containing the columns of the report.
	 * @return - arrayList that contains the required columns.
	 */
	public ArrayList<String> setColumns(JsonArray jsonColumns) {
		ArrayList<String> columns = new ArrayList<>();
		for (int i = 0; i < jsonColumns.size(); i++) {
			columns.add(jsonColumns.get(i).getAsString());
		}
		return columns;
	}

	/**
	 * Function that creates an arrayList containing all the relevant data by
	 * report type. Puts the data(parameter "jsonRows") into rows to display in
	 * the table.
	 * 
	 * @param reportType
	 * @param jsonRows
	 *            - the type of report that is required to display the data
	 *            about him.
	 * @return - arrayList of rows that contains the required data.
	 */
	public ArrayList<ArrayList<String>> setRows(String reportType,
			JsonArray jsonRows) {
		ArrayList<ArrayList<String>> rows = new ArrayList<>();
		if (reportType.equals("Purchases By Type")) {
			for (int j = 0; j < jsonRows.size(); j++) {
				JsonObject order = new JsonObject();
				ArrayList<String> orderAsString = new ArrayList<>();
				order = jsonRows.get(j).getAsJsonObject();
				if (order.get("orderID").isJsonNull()
						|| order.get("customerId").isJsonNull()
						|| order.get("amountOfLitters").isJsonNull()
						|| order.get("totalPrice").isJsonNull()) {
					break;
				}
				orderAsString.add(order.get("orderID").getAsString());
				orderAsString.add(order.get("customerId").getAsString());
				orderAsString.add(order.get("amountOfLitters").getAsString());
				orderAsString.add(order.get("totalPrice").getAsString());

				rows.add(orderAsString);
			}

		} else if (reportType.equals("Inventory items")) {
			for (int j = 0; j < jsonRows.size(); j++) {
				JsonObject order = new JsonObject();
				ArrayList<String> orderAsString = new ArrayList<>();
				order = jsonRows.get(j).getAsJsonObject();
				if (order.get("currentFuelAmount").isJsonNull()) {
					break;
				}
				orderAsString.add(order.get("fuelType").getAsString());
				orderAsString.add(order.get("currentFuelAmount").getAsString());

				rows.add(orderAsString);
			}

		} else if (reportType.equals("Quarterly Revenue")) {
			for (int j = 0; j < jsonRows.size(); j++) {
				JsonObject order = new JsonObject();
				ArrayList<String> orderAsString = new ArrayList<>();
				order = jsonRows.get(j).getAsJsonObject();
				orderAsString.add(order.get("fuelType").getAsString());
				if (order.get("totalAmountOfFuel").isJsonNull()
						|| order.get("totalPriceOfFuel").isJsonNull()) {
					break;
				}
				orderAsString.add(order.get("totalAmountOfFuel").getAsString());
				orderAsString.add(order.get("totalPriceOfFuel").getAsString());

				rows.add(orderAsString);
			}
		} else if (reportType.equals("Periodic characterization of clients")) {
			for (int j = 0; j < jsonRows.size(); j++) {
				JsonObject order = new JsonObject();
				ArrayList<String> orderAsString = new ArrayList<>();
				order = jsonRows.get(j).getAsJsonObject();
				System.out.println(order.toString());
				orderAsString.add(order.get("customerID").getAsString());
				Message msg = new Message(MessageType.GET_FUEL_COMPANIES_NAMES,
						"");
				ClientUI.accept(msg);
				JsonArray fuelCompanies = ObjectContainer.currentMessageFromServer
						.getMessageAsJsonObject().get("fuelCompanies")
						.getAsJsonArray();
				for (int i = 0; i < fuelCompanies.size(); i++) {
					String price = order
							.get(fuelCompanies.get(i).getAsString())
							.getAsFloat() > 0 ? order.get(
							fuelCompanies.get(i).getAsString()).getAsString()
							: "-";
					orderAsString.add(price);
				}
				orderAsString.add(order.get("total").getAsString());
				rows.add(orderAsString);
			}
		} else { // Comments report
			for (int j = 0; j < jsonRows.size(); j++) {
				JsonObject order = new JsonObject();
				ArrayList<String> orderAsString = new ArrayList<>();
				order = jsonRows.get(j).getAsJsonObject();
				orderAsString.add(order.get("customerID").getAsString());
				orderAsString.add(order.get("sumOfPurchase").getAsString());
				orderAsString.add(order.get("amountOfPayment").getAsString());

				rows.add(orderAsString);
			}
		}
		return rows;
	}

	/**
	 * This function fills all the data of report in the display table.
	 * 
	 * @param columns
	 *            - contains list of the columns names.
	 * @param rows
	 *            - contains list of rows(report data).
	 */
	public void fillTable(ArrayList<String> columns,
			ArrayList<ArrayList<String>> rows) {
		tblViewReport.getColumns().clear();
		tblViewReport.getItems().clear();
		tblViewReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		currentColumns = new JsonArray();
		for (int i = 0; i < columns.size(); i++) {
			final int index = i;
			TableColumn<ObservableList<String>, String> column = new TableColumn<>(
					columns.get(index));
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
					param.getValue().get(index)));
			tblViewReport.getColumns().add(column);
			currentColumns.add(columns.get(i));
		}
		for (int i = 0; i < rows.size(); i++) {
			tblViewReport.getItems().add(
					FXCollections.observableArrayList(rows.get(i)));
		}
	}

	/**
	 * This function checks whether all the information needed to display the
	 * report has been entered by the user.
	 * 
	 * @return - boolean response, true - if all fields are filled correctly,
	 *         false - if something wrong.
	 */
	public boolean checkInput() {
		boolean flag=false;
		if (cbReportType.getValue().equals("Choose report type")) {
			ObjectContainer.showMessage("Error", "Report error",
					"Please choose report type first!");
		} else if (cbStationId.isVisible() && cbStationId.getValue().equals("Choose:")) {
			ObjectContainer.showMessage("Error", "Report error",
					"Please choose station id first!");
		} else if (cbDate.isVisible() && cbDate.getValue().equals("Choose date:")) {
			ObjectContainer.showMessage("Error", "Report errorl",
					"Please choose date first!");
		} else {
			flag=true;
		}
		return flag;
	}

	/**
	 * This function initializes all choice options to the choice box by type of
	 * report for all reports. In addition, is responsible for loading the stations ID and creation dates of existing reports in a
	 * database into a choice boxes by the report type.
	 * This is done by a listener that when the user changes the report type it loads the choice options.
	 */
	public void setOptionOfReportsType() {
		cbReportType.getItems().add("Choose report type");
		cbReportType.getItems().add("Periodic characterization of clients");
		cbReportType.getItems().add("Comments report");
		cbReportType.getItems().add("Quarterly Revenue");
		cbReportType.getItems().add("Purchases By Type");
		cbReportType.getItems().add("Inventory items");
		cbReportType.setValue(cbReportType.getItems().get(0));

		cbReportType.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observableValue,
							Number number, Number number2) {
						if((Integer)number != (Integer)number2){
							tblViewReport.getColumns().clear();
							tblViewReport.getItems().clear();
							lblDetails.setText("");
							lblBottomDetails.setText("");
							ObjectContainer.setChoiceOptionOfChoiceBox(cbDate,
									new JsonArray(), "Choose date:");
						}
						String value = cbReportType.getItems().get(
								(Integer) number2);
						Message msg;
						JsonObject response = new JsonObject();
						JsonObject request = new JsonObject();
						request.addProperty("reportType", value);

						if (value.equals("Quarterly Revenue")
								|| value.equals("Purchases By Type")
								|| value.equals("Inventory items")) {
							setVisibleChoiceBox(value);
							JsonArray stations = getStationIDByReportType(request);
							setStationIDChoiceBox(stations);
						} else if (value
								.equals("Periodic characterization of clients")
								|| value.equals("Comments report")) {
							setVisibleChoiceBox(value);
							msg = new Message(
									MessageType.GET_CREAT_DATES_BY_REPORT_TYPE,
									request.toString());
							ClientUI.accept(msg);

							response = ObjectContainer.currentMessageFromServer
									.getMessageAsJsonObject();
							JsonArray createDates = response.get("createDates")
									.getAsJsonArray();
							ObjectContainer.setChoiceOptionOfChoiceBox(cbDate,
									createDates, "Choose date:");
						} else {
							setVisibleChoiceBox("Choose report type");
						}
					}
				});

	}

	/**
	 * This function makes the components of the reports visible\invisible by
	 * the type of report.
	 * 
	 * @param reportType
	 *            - contains the type of report by which the components need to
	 *            be changed to visible\invisible.
	 */
	protected void setVisibleChoiceBox(String reportType) {
		if (reportType.equals("Quarterly Revenue")
				|| reportType.equals("Purchases By Type")
				|| reportType.equals("Inventory items")) {
			lblStationId.setVisible(true);
			cbStationId.setVisible(true);
			lblDate.setVisible(true);
			cbDate.setVisible(true);
			lblDate.setLayoutX(550.0);
			cbDate.setLayoutX(550.0);
		} else if ((reportType.equals("Periodic characterization of clients") || reportType
				.equals("Comments report"))) {
			lblStationId.setVisible(false);
			cbStationId.setVisible(false);
			lblDate.setVisible(true);
			cbDate.setVisible(true);
			lblDate.setLayoutX(380.0);
			cbDate.setLayoutX(380.0);
		} else {
			lblStationId.setVisible(false);
			cbStationId.setVisible(false);
			lblDate.setVisible(false);
			cbDate.setVisible(false);
		}
	}

	/**
	 * This function is responsible for loading the creation dates of the existing reports in a
	 * database by station ID and report type.
	 * This is done by sending a request to the server to receive all required stations ID and dates.
	 * This is done by a listener that when the user changes the station id it loads the choice options.
	 * @param stations - contains  all stations ID that match the same report type that exists in a database. 
	 */
	protected void setStationIDChoiceBox(JsonArray stations) {
		ObjectContainer.setChoiceOptionOfChoiceBox(cbStationId, stations,
				"Choose:");
		cbStationId.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observableValue,
							Number number, Number number2) {
						if((Integer)number2<0)
							return;
						if((Integer)number != (Integer)number2){
							tblViewReport.getColumns().clear();
							tblViewReport.getItems().clear();
							lblDetails.setText("");
							lblBottomDetails.setText("");
						}
						String stationID = cbStationId.getItems().get(
								(Integer) number2);
						JsonObject request = new JsonObject();
						request.addProperty("stationID", stationID);
						request.addProperty("reportType",
								cbReportType.getValue());
						Message msg = new Message(
								MessageType.GET_CREAT_DATES_BY_STATION_ID_AND_REPORT_TYPE,
								request.toString());
						ClientUI.accept(msg);
						JsonObject response = ObjectContainer.currentMessageFromServer
								.getMessageAsJsonObject();
						JsonArray createDates = response.get("createDates")
								.getAsJsonArray();
						ObjectContainer.setChoiceOptionOfChoiceBox(cbDate,
								createDates, "Choose date:");
					}
				});
		cbDate.getSelectionModel().selectedIndexProperty()
		.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue,
					Number number, Number number2) {
				if((Integer)number != (Integer)number2){
					tblViewReport.getColumns().clear();
					tblViewReport.getItems().clear();
					lblDetails.setText("");
					lblBottomDetails.setText("");
				}
			}
		});
	}

	/**
	 * This function sends a request to a server that requests all stations ID by report type.
	 * @param request - contains the type of report for which the request is being made.
	 * @return
	 */
	public JsonArray getStationIDByReportType(JsonObject request) {
		Message msg = new Message(MessageType.GET_STATION_ID_BY_REPORT_TYPE,
				request.toString());
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer
				.getMessageAsJsonObject();
		return response.get("stations").getAsJsonArray();
	}

	/**
	 * Function responsible to get the 'fxml' file and call to the function that
	 * init the UI.
	 * @param paneChange  - this is the value that responsible to change the panes by
	 *            the correct button.
	 */
	public void load(Pane paneChange) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ViewReports.fxml"));

		try {
			viewReportPane = loader.load();
			paneChange.getChildren().add(viewReportPane);
			ObjectContainer.reportViewController = loader.getController();
			ObjectContainer.reportViewController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function responsible to init all buttons,text, labels and initial
	 * functions.
	 */
	private void initUI() {
		btnOpen.setId("dark-blue");
		tblViewReport.setId("my-table");
		setVisibleChoiceBox("Choose report type");
		setOptionOfReportsType();

	}
}
