package client.gui.marketingmanager;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ReportController {

	@FXML
	private Pane reportPane;

	@FXML
	private Text txtReportType;

	@FXML
	private ChoiceBox<String> cbReportType;

	@FXML
	private Text txtStartDate;

	@FXML
	private DatePicker dpStartDate;

	@FXML
	private Text txtEndDate;

	@FXML
	private DatePicker dpEndDate;

	@FXML
	private Text txtSaleName;

	@FXML
	private ChoiceBox<String> cbSaleName;

	@FXML
	private Text txtQuarterly;

	@FXML
	private ChoiceBox<String> cbQuarterly;

	@FXML
	private Text txtYear;

	@FXML
	private ChoiceBox<String> cbYear;

	@FXML
	private Text txtFuelType;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private Button btnGenerate;

	@FXML
	private Label lblErorrFields;

	@FXML
	private Label lblEmptyData;

	@FXML
	private TableView<ObservableList<String>> tblReport;

	@FXML
	private Label lblCountCustomer;

	@FXML
	private Label lblAmountOfPurchases;

	@FXML
	private Label lblAmountOfPayment;

	private String stationID;
	private JsonArray currentColumns;

	@FXML
	void generateReport(ActionEvent event) {
		tblReport.getColumns().clear();
		tblReport.getItems().clear();
		JsonObject request = new JsonObject();
		switch (ObjectContainer.currentUserLogin.getUserPermission()) {
		case MARKETING_MANAGER:
			if (checkInputForMarketingManager() == true) {
				createReportForMarketingManager(request);
			}
			break;
		case STATION_MANAGER:
			if (checkInputForStationManager() == true) {
				createReportForStationManager(request);
			}
			break;
		default:
		}
	}

	// fill all report data in table
	public void fillTable(ArrayList<String> columns, ArrayList<ArrayList<String>> rows) {
		tblReport.getColumns().clear();
		tblReport.getItems().clear();
		tblReport.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		currentColumns = new JsonArray();
		for (int i = 0; i < columns.size(); i++) {
			final int index = i;
			TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(index));
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));
			tblReport.getColumns().add(column);
			currentColumns.add(columns.get(i));
		}
		for (int i = 0; i < rows.size(); i++) {
			tblReport.getItems().add(FXCollections.observableArrayList(rows.get(i)));
		}
	}

	// Add new report to DB
	public void addNewReport(JsonArray rows, String text) {
		JsonObject request = new JsonObject();
		String reportType;
		reportType = cbReportType.getValue();
		request.addProperty("reportType", reportType);

		JsonObject reportData = new JsonObject();
		reportData.add("rows", rows);
		reportData.addProperty("stationID", stationID);
		reportData.add("columns", currentColumns);
		reportData.addProperty("labelText", text);
		request.add("reportData", reportData);
		Message msg = new Message(MessageType.ADD_NEW_REPORT, request.toString());
		ClientUI.accept(msg);
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
	}

	// create reports of marketing manager by report type
	public void createReportForMarketingManager(JsonObject request) {
		ArrayList<String> columns = new ArrayList<>();
		ArrayList<ArrayList<String>> rows = new ArrayList<>();
		lblEmptyData.setText("");
		int i;
		int countCustomer = 0;
		int countPurchases = 0;
		float countPayment = 0;

		lblAmountOfPayment.setText("");
		lblAmountOfPurchases.setText("");
		lblCountCustomer.setText("");
		lblEmptyData.setText("");
		request.addProperty("stationID", stationID);
		if (cbReportType.getValue().equals("Periodic characterization of clients")) {
			request.addProperty("reportType", "Periodic characterization of clients");
			request.addProperty("startDate", dpStartDate.getValue().toString());
			request.addProperty("endDate", dpEndDate.getValue().toString());

			Message msg = new Message(MessageType.GET_ORDERS_BY_DATES, request.toString());
			ClientUI.accept(msg);

			JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();

			if (rows.isEmpty()) {
				lblEmptyData.setText("There is no data for this report...");
			} else {

			}
		} else {
			if (cbReportType.getValue().equals("Comments report")) {
				request.addProperty("reportType", "Comments report");
				request.addProperty("saleName", cbSaleName.getValue());
				Message msg = new Message(MessageType.GET_ORDERS_BY_STATIONID_AND_SALE_NAME, request.toString());
				ClientUI.accept(msg);

				JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
				JsonArray homeHeatingFuelorders = response.get("homeHeatingFuelOrders").getAsJsonArray();
				JsonArray fastFuelOrders = response.get("fastFuelOrders").getAsJsonArray();

				HashMap<String, JsonObject> orders = new HashMap<>();

				for (i = 0; i < homeHeatingFuelorders.size(); i++) {
					orders.put(homeHeatingFuelorders.get(i).getAsJsonObject().get("customerID").getAsString(),
							homeHeatingFuelorders.get(i).getAsJsonObject());
				}
				for (i = 0; i < fastFuelOrders.size(); i++) {
					String customerID = fastFuelOrders.get(i).getAsJsonObject().get("customerID").getAsString();
					if (orders.containsKey(customerID)) {
						JsonObject order = new JsonObject();
						float purchase1 = Float.parseFloat(orders.get(customerID).get("sumOfPurchase").getAsString());
						float purchase2 = Float
								.parseFloat(fastFuelOrders.get(i).getAsJsonObject().get("sumOfPurchase").getAsString());
						float sumPurchases = purchase1 + purchase2;

						float pyment1 = Float.parseFloat(orders.get(customerID).get("amountOfPayment").getAsString());
						float pyment2 = Float.parseFloat(
								fastFuelOrders.get(i).getAsJsonObject().get("amountOfPayment").getAsString());
						float sumPayment = pyment1 + pyment2;

						order.addProperty("customerID", customerID);
						order.addProperty("sumOfPurchase", sumPurchases);
						order.addProperty("amountOfPayment", sumPayment);
						orders.put(customerID, order);
					} else {
						orders.put(customerID, fastFuelOrders.get(i).getAsJsonObject());
					}
				}
				Set<String> keys = orders.keySet();
				JsonArray jsonOrders = new JsonArray();
				for (String key : keys) {
					// convert HasMap to JsonArray for setRowsPerReport function
					JsonObject order = new JsonObject();
					order.addProperty("customerID", orders.get(key).get("customerID").getAsString());
					order.addProperty("sumOfPurchase", "" + orders.get(key).get("sumOfPurchase").getAsInt());
					order.addProperty("amountOfPayment", "" + orders.get(key).get("amountOfPayment").getAsFloat());
					jsonOrders.add(order);

					// calculate total data of report
					countCustomer++;
					countPurchases += orders.get(key).get("sumOfPurchase").getAsFloat();
					countPayment += orders.get(key).get("amountOfPayment").getAsFloat();

				}

				columns = setColumnsPerReport("Comments report");
				rows = setRowsPerReport("Comments report", jsonOrders);
				if (rows.isEmpty()) {
					lblEmptyData.setText("There is no data for this report...");
				} else {
					fillTable(columns, rows);
					addNewReport(jsonOrders,
							"Sale name: " + cbSaleName.getValue() + "_ Total customer: " + countCustomer
									+ "   Total purchases: " + countPurchases + "   total payment: " + countPayment);
					// show total data of report
					lblCountCustomer.setText("Total customer: " + countCustomer);
					lblAmountOfPurchases.setText("Total purchases: " + countPurchases);
					lblAmountOfPayment.setText("Total payments: " + countPayment);
				}
			}
		}
	}

	// create reports of station manager by report type
	public void createReportForStationManager(JsonObject request) {
		ArrayList<String> columns = new ArrayList<>();
		ArrayList<ArrayList<String>> rows = new ArrayList<>();
		lblEmptyData.setText("");
		request.addProperty("stationID", stationID);
		if (cbReportType.getValue().equals("Purchases By Type")) {
			request.addProperty("reportType", "Purchases By Type");
			request.addProperty("fuelType", cbFuelType.getValue());
			Message msg = new Message(MessageType.GET_ORDERS_BY_STATIONID_AND_FUEL_TYPE, request.toString());
			ClientUI.accept(msg);
			JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
			JsonArray orders = response.get("orders").getAsJsonArray();

			columns = setColumnsPerReport("Purchases By Type");
			rows = setRowsPerReport("Purchases By Type", orders);
			if (rows.isEmpty()) {
				lblEmptyData.setText("There is no data for this report...");
			} else {
				fillTable(columns, rows);
				addNewReport(orders, "Fuel type: " + cbFuelType.getValue());
				for (int i = 0; i < orders.size(); i++) {
					JsonObject order = orders.get(i).getAsJsonObject();
				}
			}

		} else if (cbReportType.getValue().equals("Inventory items")) {
			Message msg = new Message(MessageType.GET_FUEL_INVENTORY_PER_STATION, request.toString());
			ClientUI.accept(msg);

			JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
			JsonArray fuelInventory = response.get("fuelInventory").getAsJsonArray();

			columns = setColumnsPerReport("Inventory items");
			rows = setRowsPerReport("Inventory items", fuelInventory);
			if (rows.isEmpty()) {
				lblEmptyData.setText("There is no data for this report...");
			} else {
				fillTable(columns, rows);
				addNewReport(fuelInventory, "");
			}
		} else {
			if (cbReportType.getValue().equals("Quarterly Revenue")) {
				request.addProperty("fuelType", "");
				request.addProperty("year", cbYear.getValue().toString());
				request.addProperty("quarter", cbQuarterly.getValue());
				Message msg = new Message(MessageType.GET_ORDERS_BY_STATIONID_AND_QUARTER, request.toString());
				ClientUI.accept(msg);

				JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
				JsonArray fastFuelorders = response.get("fastFuelOrders").getAsJsonArray();

				columns = setColumnsPerReport("Quarterly Revenue");
				rows = setRowsPerReport("Quarterly Revenue", fastFuelorders);
				if (rows.isEmpty()) {
					lblEmptyData.setText("There is no data for this report...");
				} else {
					fillTable(columns, rows);
					addNewReport(fastFuelorders, "Quarterly: " + cbQuarterly.getValue());
					for (int i = 0; i < fastFuelorders.size(); i++) {
						JsonObject order = fastFuelorders.get(i).getAsJsonObject();
					}
				}
			}
		}
	}

	public void setReportTypeByUserPermissions() {
		switch (ObjectContainer.currentUserLogin.getUserPermission()) {
		case MARKETING_MANAGER:
			setOptionOfReportTypeOfMarketingManager();
			break;
		case STATION_MANAGER:
			setOptionOfReportTypeOfStationManager();
			break;
		default:
		}

	}

	// check all the input for marketing manager report
	public boolean checkInputForMarketingManager() {

		lblErorrFields.setText("");
		lblEmptyData.setText("");
		if (cbReportType.getValue().equals("Periodic characterization of clients")) {
			if ((dpStartDate.getValue() == null) || (dpEndDate.getValue() == null)) {
				lblErorrFields.setText("Please fill all fields!");
				return false;
			}
			// check that start date is before end date
			else if (dpStartDate.getValue().isAfter(dpEndDate.getValue())) {
				lblErorrFields.setText("Please fill correct dates!");
				return false;
			}
			// check that start date and end date are before current date
			else if (dpStartDate.getValue().isAfter(new Date(System.currentTimeMillis()).toLocalDate())
					|| dpEndDate.getValue().isAfter(new Date(System.currentTimeMillis()).toLocalDate())) {
				lblErorrFields.setText("Please fill correct dates!");
				return false;
			} else {
				lblErorrFields.setText("");
				return true;
			}
		} else if (cbReportType.getValue().equals("Comments report")) {
			if (cbSaleName.getValue().equals("Choose sale name")) {
				lblErorrFields.setText("Please choose sale!");
				return false;
			} else {
				lblErorrFields.setText("");
				return true;
			}
		} else {
			lblErorrFields.setText("Please choose report!");
			return false;
		}
	}

	// check all the input for station manager report
	public boolean checkInputForStationManager() {

		lblErorrFields.setText("");

		if ((cbReportType.getValue().equals("Quarterly Revenue"))) {
			if (cbQuarterly.getValue().equals("Choose quarter")) {
				lblErorrFields.setText("Please choose quarter!");
				return false;
			} else if (cbYear.getValue().equals("")) {
				lblErorrFields.setText("Please choose year!");
				return false;
			} else {
				lblErorrFields.setText("");
				return true;
			}
		}

		else if (cbReportType.getValue().equals("Purchases By Type")) {
			if (cbFuelType.getValue().equals("Choose fuel type")) {
				lblErorrFields.setText("Please choose fuel type!");
				return false;
			} else {
				lblErorrFields.setText("");
				return true;
			}
		} else if (cbReportType.getValue().equals("Inventory items")) {
			lblErorrFields.setText("");
			return true;
		} else {
			lblErorrFields.setText("Please choose report type!");
			return false;
		}
	}

	// dynamic function for set all choice option

	// set all choice option of marketing manager by report type
	public void setOptionOfReportTypeOfMarketingManager() {
		// set option of report type
		cbReportType.getItems().add("Choose type");
		cbReportType.getItems().add("Periodic characterization of clients");
		cbReportType.getItems().add("Comments report");
		cbReportType.setValue(cbReportType.getItems().get(0));

		Message msg = new Message(MessageType.GET_SALE_NAMES, "");
		ClientUI.accept(msg);

		// set option of sale
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray saleNames = response.get("saleNames").getAsJsonArray();
		ObjectContainer.setChoiceOptionOfChoiceBox(cbSaleName, saleNames, "Choose sale name");
	}

	// set all choice option of station manager by report type
	public void setOptionOfReportTypeOfStationManager() {
		// set option of report type
		cbReportType.getItems().add("Choose type");
		cbReportType.getItems().add("Quarterly Revenue");
		cbReportType.getItems().add("Purchases By Type");
		cbReportType.getItems().add("Inventory items");

		cbReportType.setValue(cbReportType.getItems().get(0));

		// set option of Quarterly
		cbQuarterly.getItems().add("Choose quarter");
		cbQuarterly.getItems().add("January - March");
		cbQuarterly.getItems().add("April - June");
		cbQuarterly.getItems().add("July - September");
		cbQuarterly.getItems().add("October - December");
		cbQuarterly.setValue(cbQuarterly.getItems().get(0));

		// set options of years
		for (int i = 2020; i <= 2030; i++)
			cbYear.getItems().add(i + "");
		cbYear.setValue(cbYear.getItems().get(0));

		// set option of fuel type
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray fuelTypes = response.get("fuelTypes").getAsJsonArray();
		ObjectContainer.setChoiceOptionOfChoiceBox(cbFuelType, fuelTypes, "Choose fuel type");
	}

	// visible/invisible all the component of comments report
	public void setVisibleCommentsReport(boolean flag) {
		txtSaleName.setVisible(flag);
		cbSaleName.setVisible(flag);
		if (flag)
			cbSaleName.setValue(cbSaleName.getItems().get(0));
		tblReport.getColumns().clear();
		tblReport.getItems().clear();
	}

	// visible/invisible all the component of fuel type report
	public void setVisibleFuelTypeReport(boolean flag) {
		txtFuelType.setVisible(flag);
		cbFuelType.setVisible(flag);
		if (flag)
			cbFuelType.setValue(cbFuelType.getItems().get(0));
		tblReport.getColumns().clear();
		tblReport.getItems().clear();
	}

	// visible/invisible all the component of quarterly report
	public void setVisibleQuartlyReport(boolean flag) {
		txtQuarterly.setVisible(flag);
		cbQuarterly.setVisible(flag);
		txtYear.setVisible(flag);
		cbYear.setVisible(flag);
		if (flag)
			cbQuarterly.setValue(cbQuarterly.getItems().get(0));
		tblReport.getColumns().clear();
		tblReport.getItems().clear();
	}

	// visible/invisible all the component of periodic report
	public void setVisiblePeriodicReport(boolean flag) {
		txtStartDate.setVisible(flag);
		dpStartDate.setVisible(flag);
		txtEndDate.setVisible(flag);
		dpEndDate.setVisible(flag);
		tblReport.getColumns().clear();
		tblReport.getItems().clear();
	}

	// set fields by report type
	public void showFieldsByReoprtType() {
		cbReportType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = cbReportType.getItems().get((Integer) number2);

				lblErorrFields.setText("");
				lblEmptyData.setText("");
				lblAmountOfPayment.setText("");
				lblAmountOfPurchases.setText("");
				lblCountCustomer.setText("");
				if (value.equals("Periodic characterization of clients")) {
					setVisiblePeriodicReport(true);
					setVisibleFuelTypeReport(false);
					setVisibleCommentsReport(false);
					setVisibleQuartlyReport(false);
				} else if (value.equals("Comments report")) {
					setVisibleCommentsReport(true);
					setVisiblePeriodicReport(false);
					setVisibleFuelTypeReport(false);
					setVisibleQuartlyReport(false);

				} else if (value.equals("Quarterly Revenue")) {
					setVisibleQuartlyReport(true);
					setVisiblePeriodicReport(false);
					setVisibleFuelTypeReport(false);
					setVisibleCommentsReport(false);
				} else if (value.equals("Purchases By Type")) {
					setVisibleFuelTypeReport(true);
					setVisibleQuartlyReport(false);
					setVisiblePeriodicReport(false);
					setVisibleCommentsReport(false);
				} else {
					setVisibleQuartlyReport(false);
					setVisiblePeriodicReport(false);
					setVisibleFuelTypeReport(false);
					setVisibleCommentsReport(false);
				}
			}
		});
	}

	public ArrayList<String> setColumnsPerReport(String reportType) {
		ArrayList<String> columns = new ArrayList<>();
		switch (ObjectContainer.currentUserLogin.getUserPermission()) {
		case STATION_MANAGER: {
			if (reportType.equals("Purchases By Type")) {
				columns.add("Order id");
				columns.add("Customer id");
				columns.add("Amount");
				columns.add("Total price");
			} else if (reportType.equals("Inventory items")) {
				columns.add("Fuel type");
				columns.add("Amount");
			} else { // Quarterly Revenue
				columns.add("Fuel type");
				columns.add("Sell amount");
				columns.add("Incomes");
			}
		}
			break;
		case MARKETING_MANAGER: {
			if (reportType.equals("Periodic characterization of clients")) {
				columns.add("Order id");
				columns.add("Customer id");
				columns.add("Amount");
				columns.add("Total price");
			} else { // Comments report
				columns.add("Customer id");
				columns.add("Amount of purchases");
				columns.add("Amount of payment");
			}
		}
			break;
		default:
			break;
		}
		return columns;
	}

	public ArrayList<ArrayList<String>> setRowsPerReport(String reportType, JsonArray orders) {
		ArrayList<ArrayList<String>> rows = new ArrayList<>();
		switch (ObjectContainer.currentUserLogin.getUserPermission()) {
		case STATION_MANAGER: {
			if (reportType.equals("Purchases By Type")) {

				for (int j = 0; j < orders.size(); j++) {
					JsonObject order = new JsonObject();
					ArrayList<String> orderAsString = new ArrayList<>();
					order = orders.get(j).getAsJsonObject();
					if (order.get("orderID").isJsonNull() || order.get("customerId").isJsonNull()
							|| order.get("amountOfLitters").isJsonNull() || order.get("totalPrice").isJsonNull()) {
						break;
					}
					orderAsString.add(order.get("orderID").getAsString());
					orderAsString.add(order.get("customerId").getAsString());
					orderAsString.add(order.get("amountOfLitters").getAsString());
					orderAsString.add(order.get("totalPrice").getAsString());

					rows.add(orderAsString);
				}

			} else if (reportType.equals("Inventory items")) {
				for (int j = 0; j < orders.size(); j++) {
					JsonObject order = new JsonObject();
					ArrayList<String> orderAsString = new ArrayList<>();
					order = orders.get(j).getAsJsonObject();
					if (order.get("currentFuelAmount").isJsonNull()) {
						break;
					}
					orderAsString.add(order.get("fuelType").getAsString());
					orderAsString.add(order.get("currentFuelAmount").getAsString());

					rows.add(orderAsString);
				}

			} else { // Quarterly Revenue
				for (int j = 0; j < orders.size(); j++) {
					JsonObject order = new JsonObject();
					ArrayList<String> orderAsString = new ArrayList<>();
					order = orders.get(j).getAsJsonObject();
					orderAsString.add(order.get("fuelType").getAsString());
					if (order.get("totalAmountOfFuel").isJsonNull() || order.get("totalPriceOfFuel").isJsonNull()) {
						break;
					}
					orderAsString.add(order.get("totalAmountOfFuel").getAsString());
					orderAsString.add(order.get("totalPriceOfFuel").getAsString());

					rows.add(orderAsString);
				}
			}
		}
			break;
		case MARKETING_MANAGER: {
			if (reportType.equals("Periodic characterization of clients")) {

			} else { // Comments report
				for (int j = 0; j < orders.size(); j++) {
					JsonObject order = new JsonObject();
					ArrayList<String> orderAsString = new ArrayList<>();
					order = orders.get(j).getAsJsonObject();
					orderAsString.add(order.get("customerID").getAsString());
					orderAsString.add(order.get("sumOfPurchase").getAsString());
					orderAsString.add(order.get("amountOfPayment").getAsString());

					rows.add(orderAsString);
				}
			}
		}
			break;
		default:
			break;
		}
		return rows;
	}

	private void getStationID() {
		JsonObject json = new JsonObject();
		json.addProperty("userName", ObjectContainer.currentUserLogin.getUsername());
		Message msg = new Message(MessageType.GET_STATION_ID_BY_USER_NAME, json.toString());
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		stationID = response.get("stationID").getAsString();
	}

	public void load(Pane paneChange) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ReportGeneration.fxml"));

		try {
			reportPane = loader.load();
			paneChange.getChildren().add(reportPane);
			ObjectContainer.reportController = loader.getController();
			ObjectContainer.reportController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initUI() {
		tblReport.setId("my-table");
		btnGenerate.setId("dark-blue");
		lblErorrFields.setText("");
		lblEmptyData.setText("");
		lblAmountOfPayment.setText("");
		lblAmountOfPurchases.setText("");
		lblCountCustomer.setText("");
		lblAmountOfPayment.setVisible(true);
		lblAmountOfPurchases.setVisible(true);
		lblCountCustomer.setVisible(true);
		lblErorrFields.setVisible(true);
		lblEmptyData.setVisible(true);
		setReportTypeByUserPermissions();
		showFieldsByReoprtType();
		getStationID();
	}

}
