package client.gui.marketingrepresentative;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class AnalyticSystemController {

	@FXML
	private Pane mainAnalyticPane;

	@FXML
	private TableView<ObservableList<String>> tblDataView;

	@FXML
	private Button btnShowRanks;

	@FXML
	private Button btnCustomerType;

	@FXML
	private Button btnCertainHours;

	@FXML
	private Button btnFuelType;

	@FXML
	private Button btnSort;

	@FXML
	private ChoiceBox<String> cbCertainHours;

	@FXML
	private ChoiceBox<String> cbCustomerType;

	@FXML
	private ChoiceBox<String> cbFuelType;

	@FXML
	private Label lblTitleOfPane;

	private boolean isCertainHours = false;
	private boolean isCustomerType = false;
	private boolean isFuelType = false;
	private boolean isShowRanks = false;

	private JsonArray currentColumns;

	@FXML
	void onCertainHours(ActionEvent event) {
		isCertainHours = !isCertainHours;
		if (isCertainHours) {
			isShowRanks = false;
			setButtonsImages("/images/unchecked.png", btnShowRanks);
		}
		String url = isCertainHours ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url, btnCertainHours);
		if (!isCertainHours)
			cbCertainHours.setValue(cbCertainHours.getItems().get(0));
		cbCertainHours.setVisible(isCertainHours);
	}

	@FXML
	void onCustomerType(ActionEvent event) {
		isCustomerType = !isCustomerType;
		if (isCustomerType) {
			isShowRanks = false;
			setButtonsImages("/images/unchecked.png", btnShowRanks);
		}
		String url = isCustomerType ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url, btnCustomerType);
		if (!isCustomerType)
			cbCustomerType.setValue(cbCustomerType.getItems().get(0));
		cbCustomerType.setVisible(isCustomerType);
	}

	@FXML
	void onFuelType(ActionEvent event) {
		isFuelType = !isFuelType;
		if (isFuelType) {
			isShowRanks = false;
			setButtonsImages("/images/unchecked.png", btnShowRanks);
		}
		String url = isFuelType ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url, btnFuelType);
		if (!isFuelType)
			cbFuelType.setValue(cbFuelType.getItems().get(0));
		cbFuelType.setVisible(isFuelType);
	}

	@FXML
	void onShowRanks(ActionEvent event) {
		isShowRanks = !isShowRanks;
		String url = isShowRanks ? "/images/checked.png" : "/images/unchecked.png";
		setButtonsImages(url, btnShowRanks);
		if (isShowRanks) {
			url = "/images/unchecked.png";
			isFuelType = false;
			setButtonsImages(url, btnFuelType);
			isCustomerType = false;
			setButtonsImages(url, btnCustomerType);
			isCertainHours = false;
			setButtonsImages(url, btnCertainHours);
			
			cbCertainHours.setValue(cbCertainHours.getItems().get(0));
			cbCustomerType.setValue(cbCustomerType.getItems().get(0));
			cbFuelType.setValue(cbFuelType.getItems().get(0));
			
			cbCertainHours.setVisible(false);
			cbCustomerType.setVisible(false);
			cbFuelType.setVisible(false);
		}
	}

	@FXML
	void onSort(ActionEvent event) {
		if (isShowRanks) {
			getCustomerRanks();
		} else {
			if(!isFuelType && ! isCustomerType && !isCertainHours) {
				isShowRanks = true;
				setButtonsImages("/images/checked.png", btnShowRanks);
				getCustomerRanks();
			}else {
				getDataFromDB();				
			}
		}
	}

	public ArrayList<String> convertJsonArrayToArrayListString(JsonArray jsonArray) {
		System.out.println("Columns:");
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			result.add(jsonArray.get(i).getAsString());
			System.out.println(jsonArray.get(i).getAsString());
		}

		return result;
	}

	public ArrayList<ArrayList<String>> convertJsonObjectToArrayList(JsonObject jsonObject) {
		System.out.println("Rows:");
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		JsonArray columns = jsonObject.get("columns").getAsJsonArray();
		JsonArray rows = jsonObject.get("rows").getAsJsonArray();
		for (int j = 0; j < rows.size(); j++) {
			ArrayList<String> arrayList = new ArrayList<>();
			JsonObject json = new JsonObject();
			json = rows.get(j).getAsJsonObject();
			for (int i = 0; i < columns.size(); i++) {
				arrayList.add(json.get(columns.get(i).getAsString()).getAsString());
			}
			System.out.println(arrayList.toString());
			result.add(arrayList);
		}
		return result;
	}

	public void fillDataView(ArrayList<String> columns, ArrayList<ArrayList<String>> rows) {
		tblDataView.getColumns().clear();
		tblDataView.getItems().clear();
		tblDataView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		currentColumns = new JsonArray();
		for (int i = 0; i < columns.size(); i++) {
			final int index = i;
			TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns.get(index));
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));
			tblDataView.getColumns().add(column);
			currentColumns.add(columns.get(i));
		}
		for (int i = 0; i < rows.size(); i++) {
			tblDataView.getItems().add(FXCollections.observableArrayList(rows.get(i)));
		}
	}

	public void getCustomerRanks() {
		Message msg = new Message(MessageType.GET_CUSTOMER_RANKS, "");
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		ArrayList<String> columns = convertJsonArrayToArrayListString(response.get("columns").getAsJsonArray());
		ArrayList<ArrayList<String>> rows = convertJsonObjectToArrayList(response);
		
		rows.sort(new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				int rank1 = Integer.parseInt(o1.get(o1.size() - 1));
				int rank2 = Integer.parseInt(o2.get(o2.size() - 1));
				return rank2 - rank1;
			}
		});
		
		fillDataView(columns, rows);
		lblTitleOfPane.setText("");
	}
	
	public void getDataFromDB() {
		String fuelType = cbFuelType.getValue().equals(cbFuelType.getItems().get(0)) ? "" : cbFuelType.getValue();
		String customerType = cbCustomerType.getValue().equals(cbCustomerType.getItems().get(0)) ? ""
				: cbCustomerType.getValue();
		String certainHours = cbCertainHours.getValue().equals(cbCertainHours.getItems().get(0)) ? "00:00:00 - 23:59:59"
				: cbCertainHours.getValue();

		JsonObject json = new JsonObject();
		json.addProperty("code", getCodeByCombination());

		Message msg = new Message(MessageType.GET_ACTIVITY_TRACKING_DATA, json.toString());
		ClientUI.accept(msg);

		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		ArrayList<String> columns = convertJsonArrayToArrayListString(response.get("columns").getAsJsonArray());
		ArrayList<ArrayList<String>> rows = convertJsonObjectToArrayList(response);
		
		ArrayList<ArrayList<String>> after_ignore_zero = new ArrayList<ArrayList<String>>();
		
		for(ArrayList<String> arr : rows) {
			if(!arr.get(arr.size() - 1).equals("0.0"))
				after_ignore_zero.add(arr);
		}

		after_ignore_zero.sort(new Comparator<ArrayList<String>>() {
			@Override
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				float price1 = Float.parseFloat(o1.get(o1.size() - 1));
				float price2 = Float.parseFloat(o2.get(o2.size() - 1));
				if(price2 > price1) return 1;
				if(price2 == price1) return 0;
				return -1;
			}
		});
		
		fillDataView(columns, after_ignore_zero);
		
		String text = "";
		if(after_ignore_zero.size() > 0) {
			text += "| ";
			text += fuelType.isEmpty() ? "" : "Fuel Type : " + fuelType + " | ";
			text += customerType.isEmpty() ? "" : " Customer Type : " + customerType + " | ";
			text += certainHours.equals("00:00:00 - 23:59:59") ? "" : " Certain Hours : " + certainHours + " |";
			if(text.equals("| "))text = "";			
		}else {
			text = "No data for this combination, please try something else !";
			
		}
		lblTitleOfPane.setText(text);
	}

	private void clearFields() {
		isCertainHours = false;
		cbCertainHours.setValue(cbCertainHours.getItems().get(0));
		cbCertainHours.setVisible(false);
		setButtonsImages("/images/unchecked.png", btnCertainHours);
		
		isFuelType = false;
		cbFuelType.setValue(cbFuelType.getItems().get(0));
		cbFuelType.setVisible(false);
		setButtonsImages("/images/unchecked.png", btnFuelType);
		
		isCustomerType = false;
		cbCustomerType.setValue(cbCustomerType.getItems().get(0));
		cbCustomerType.setVisible(false);
		setButtonsImages("/images/unchecked.png", btnCustomerType);
	}

	public String getCodeByCombination() {
		String code = "";
		for (int i = 0; i < cbCustomerType.getItems().size(); i++) {
			if (cbCustomerType.getValue().equals(cbCustomerType.getItems().get(i)))
				code += i;
		}
		
		for (int i = 0; i < cbCertainHours.getItems().size(); i++) {
			if (cbCertainHours.getValue().equals(cbCertainHours.getItems().get(i)))
				code += i;
		}
		
		for (int i = 0; i < cbFuelType.getItems().size(); i++) {
			if (cbFuelType.getValue().equals(cbFuelType.getItems().get(i)))
				code += i;
		}

		return code;
	}

	public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("AnalyticSystem.fxml"));

		try {
			mainAnalyticPane = loader.load();
			changePane.getChildren().add(mainAnalyticPane);
			ObjectContainer.analyticSystemController = loader.getController();
			ObjectContainer.analyticSystemController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initUI() {
		initCB();
		tblDataView.setId("my-table");
		btnSort.setId("dark-blue");
		String url = "/images/unchecked.png";
		setButtonsImages(url, btnCertainHours);
		setButtonsImages(url, btnCustomerType);
		setButtonsImages(url, btnFuelType);
		setButtonsImages("/images/checked.png", btnShowRanks);
		isShowRanks = true;
		getCustomerRanks();
	}

	private void initCB() {
		Message msg = new Message(MessageType.GET_FUEL_TYPES, "");
		ClientUI.accept(msg);
		JsonArray fuelTypes = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject().get("fuelTypes")
				.getAsJsonArray();

		ObjectContainer.setChoiceOptionOfChoiceBox(cbFuelType, fuelTypes, "Choose:");

		JsonArray hours = new JsonArray();
		hours.add("00:00:00 - 11:59:59");
		hours.add("12:00:00 - 16:59:59");
		hours.add("17:00:00 - 23:59:59");
		ObjectContainer.setChoiceOptionOfChoiceBox(cbCertainHours, hours, "Choose");

		JsonArray customerTypes = new JsonArray();
		customerTypes.add("Private");
		customerTypes.add("Company");
		ObjectContainer.setChoiceOptionOfChoiceBox(cbCustomerType, customerTypes, "Choose");

		cbFuelType.setVisible(false);
		cbCustomerType.setVisible(false);
		cbCertainHours.setVisible(false);
	}

	private void setButtonsImages(String url, Button btn) {
		BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}
}