package client.gui;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReportControler {

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
	private Text txtFuelType;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private Button btnGenerate;

	@FXML
	private Label lblErorrFields;

	@FXML
	void generateReport(ActionEvent event) {

		checkInputForMarketingManager();

	}

	public void setReportTypeByUserPermissions(UserPermission userPermission) {
		switch (userPermission) {
		case MARKETING_MANAGER:
			setOptionOfReportTypeOfMarketingManager();
			break;
		case STATION_MANAGER:
			setOptionOfReportTypeOfStationManager();
			break;
		default:
		}

	}

	public void checkInputForMarketingManager() {

		if (cbReportType.getValue().equals(
				"Periodic characterization of clients")) {
			if ((dpStartDate.getValue() == null)
					|| (dpEndDate.getValue() == null)) {
				lblErorrFields.setText("Please fill all fields!");
			}
			// add more check

			else {
				lblErorrFields.setText("");
			}
		} 
		else if (cbReportType.getValue().equals("Comments report")) {
			if (cbSaleName.getValue().equals("Choose sale name")) {
				lblErorrFields.setText("Please choose sale!");
			} else {
				lblErorrFields.setText("");
			}
		}
		else lblErorrFields.setText("Please choose report!");
	}

	public void checkInputForStationManager() {
		if (cbReportType.getValue().equals("Quarterly Revenue")) {
			if (cbQuarterly.getValue().equals("Choose quarterly")) {
				lblErorrFields.setText("Please choose quarterly!");
			} else {
				lblErorrFields.setText("");
			}
		}

		else if (cbReportType.getValue().equals("Purchases By Type")) {
			if (cbFuelType.getValue().equals("Choose fuel type")) {
				lblErorrFields.setText("Please choose type!");
			} else {
				lblErorrFields.setText("");
			}
		}
		else lblErorrFields.setText("Please choose report!");
	}

	public void setChoiceOptionOfChoiceBox(ChoiceBox<String> choiceBox, JsonArray choiceOption , String defualtValue)
	{
		int i;
		choiceBox.getItems().add(defualtValue);
		for(i=0;i<choiceOption.size();i++)
		{
			choiceBox.getItems().add(choiceOption.get(i).getAsString());
		}
		choiceBox.setValue(defualtValue);
	}
	
	// change all this functions to be generic
	public void setOptionOfReportTypeOfMarketingManager() {
		cbReportType.getItems().add("Choose type");
		cbReportType.getItems().add("Periodic characterization of clients");
		cbReportType.getItems().add("Comments report");
		cbReportType.setValue(cbReportType.getItems().get(0));

		Message msg = new Message(MessageType.GET_SALE_NAMES, "");
		ClientUI.accept(msg);
		
		// set option of sale
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray saleNames = response.get("saleNames").getAsJsonArray();
		setChoiceOptionOfChoiceBox(cbSaleName, saleNames, "Choose sale name");
	}

	public void setOptionOfReportTypeOfStationManager() {
		
		cbReportType.getItems().add("Choose type");
		cbReportType.getItems().add("Quarterly Revenue");
		cbReportType.getItems().add("Purchases By Type");
		cbReportType.setValue(cbReportType.getItems().get(0));

		// set option of Quarterly
		cbQuarterly.getItems().add("Choose quarterly");
		cbQuarterly.getItems().add("quarterly 1");
		cbQuarterly.getItems().add("quarterly 2");
		cbQuarterly.getItems().add("quarterly 3");
		cbQuarterly.getItems().add("quarterly 4");
		cbQuarterly.setValue(cbQuarterly.getItems().get(0));
		
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);
		
		// set option of fuel type
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray fuelTypes = response.get("fuelTypes").getAsJsonArray();
		setChoiceOptionOfChoiceBox(cbFuelType, fuelTypes, "Choose fuel type");
	}

	public void setVisibleCommentsReport(boolean flag) {
		txtSaleName.setVisible(flag);
		cbSaleName.setVisible(flag);
		if (flag)
			cbSaleName.setValue(cbSaleName.getItems().get(0));
	}

	public void setVisibleFuelTypeReport(boolean flag) {
		txtFuelType.setVisible(flag);
		cbFuelType.setVisible(flag);
		if (flag)
			cbFuelType.setValue(cbFuelType.getItems().get(0));
	}

	public void setVisibleQuartlyReport(boolean flag) {
		txtQuarterly.setVisible(flag);
		cbQuarterly.setVisible(flag);
		if (flag)
			cbQuarterly.setValue(cbQuarterly.getItems().get(0));
	}

	public void setVisiblePeriodicReport(boolean flag) {
		txtStartDate.setVisible(flag);
		dpStartDate.setVisible(flag);
		txtEndDate.setVisible(flag);
		dpEndDate.setVisible(flag);
	}

	public void showFieldsByReoprtType() {
		cbReportType.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observableValue,
							Number number, Number number2) {
						String value = cbReportType.getItems().get(
								(Integer) number2);

						lblErorrFields.setText("");
						if (value
								.equals("Periodic characterization of clients")) {
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

	public void load(Pane paneChange) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ReportGeneration.fxml"));

		try {
			reportPane = loader.load();
			paneChange.getChildren().add(reportPane);
			ObjectContainer.reportController = loader.getController();
			ObjectContainer.reportController.initUI(ObjectContainer.currentUserLogin.getUserPermission());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initUI(UserPermission userPermission) {

		lblErorrFields.setText("");
		lblErorrFields.setVisible(true);
		setReportTypeByUserPermissions(userPermission);
		showFieldsByReoprtType();

	}
}
