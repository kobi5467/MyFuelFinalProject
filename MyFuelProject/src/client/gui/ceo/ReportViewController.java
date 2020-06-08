package client.gui.ceo;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

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
	private Label lblEror;

	@FXML
	private Button btnOpen;

	@FXML
	private TableView<ObservableList<String>> tblViewReport;

	@FXML
	void openReport(ActionEvent event) {

	}

	public void checkInput() {
		lblEror.setText("");
		if (cbReportType.getValue().equals("Choose report type"))
			lblEror.setText("Please choose report type!");
	}

	public void setOptionOfReportsType() {

		cbReportType.getItems().add("Choose report type");
		cbReportType.getItems().add("Periodic characterization of clients");
		cbReportType.getItems().add("Comments report");
		cbReportType.getItems().add("Quarterly Revenue");
		cbReportType.getItems().add("Purchases By Type");
		cbReportType.getItems().add("Inventory items");

		cbReportType.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observableValue,
							Number number, Number number2) {
						String value = cbReportType.getItems().get(
								(Integer) number2);
						
						JsonObject response = new JsonObject();
						JsonObject request =  new JsonObject();
						
						if (value.equals("Quarterly Revenue")
								|| value.equals("Purchases By Type")
								|| value.equals("Inventory items")) {
							request.addProperty("reportType",cbReportType.getValue().toString());
							Message msg = new Message(MessageType.GET_STATION_ID,request.toString());
							ClientUI.accept(msg);
							
							response = ObjectContainer.currentMessageFromServer
									.getMessageAsJsonObject();
							JsonArray stations = response.get("Stations").getAsJsonArray();
							ObjectContainer.setChoiceOptionOfChoiceBox(cbStationId, stations,
									stations.get(0).getAsString());
						}

					}
				});

		

		

	}

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

	private void initUI() {

	}
}
