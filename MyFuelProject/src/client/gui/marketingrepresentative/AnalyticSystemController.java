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

/**
* This class is the gui class of Analytic system controller.
* this class is responsible for all of the checks and to display 
* all of the analytic information in myFuel system
* @author Kobi Malka
* @version final
*/
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

	/**
	 * this function will make the operation that needed to be activated when click 
	 * on the certain hour button and start the event.
	 * @param event
	 */
	@FXML
	void onCertainHours(ActionEvent event) {
		isCertainHours = !isCertainHours;
		if (isCertainHours) {
			isShowRanks = false;
			ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnShowRanks);
		}
		String url = isCertainHours ? ObjectContainer.checked : ObjectContainer.unchecked;
		ObjectContainer.setButtonImage(url, btnCertainHours);
		if (!isCertainHours)
			cbCertainHours.setValue(cbCertainHours.getItems().get(0));
		cbCertainHours.setVisible(isCertainHours);
	}

	/**
	 * this function will make the operation that needed to be activated when click 
	 * on the Customer type button and start the event.
	 * @param event
	 */
	@FXML
	void onCustomerType(ActionEvent event) {
		isCustomerType = !isCustomerType;
		if (isCustomerType) {
			isShowRanks = false;
			ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnShowRanks);
		}
		String url = isCustomerType ? ObjectContainer.checked : ObjectContainer.unchecked;
		ObjectContainer.setButtonImage(url, btnCustomerType);
		if (!isCustomerType)
			cbCustomerType.setValue(cbCustomerType.getItems().get(0));
		cbCustomerType.setVisible(isCustomerType);
	}

	/**
	 * /**
	 * this function will make the operation that needed to be activated when click 
	 * on the Fuel Type button and start the event.
	 * @param event
	 */
	@FXML
	void onFuelType(ActionEvent event) {
		isFuelType = !isFuelType;
		if (isFuelType) {
			isShowRanks = false;
			ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnShowRanks);
		}
		String url = isFuelType ? ObjectContainer.checked : ObjectContainer.unchecked;
		ObjectContainer.setButtonImage(url, btnFuelType);
		if (!isFuelType)
			cbFuelType.setValue(cbFuelType.getItems().get(0));
		cbFuelType.setVisible(isFuelType);
	}

	/**
	 * this function will make the operation that needed to be activated when click 
	 * on the Show ranks button and start the event.
	 * @param event
	 */
	@FXML
	void onShowRanks(ActionEvent event) {
		isShowRanks = !isShowRanks;
		String url = isShowRanks ? ObjectContainer.checked : ObjectContainer.unchecked;
		ObjectContainer.setButtonImage(url, btnShowRanks);
		if (isShowRanks) {
			url = ObjectContainer.unchecked;
			isFuelType = false;
			ObjectContainer.setButtonImage(url, btnFuelType);
			isCustomerType = false;
			ObjectContainer.setButtonImage(url, btnCustomerType);
			isCertainHours = false;
			ObjectContainer.setButtonImage(url, btnCertainHours);
			
			cbCertainHours.setValue(cbCertainHours.getItems().get(0));
			cbCustomerType.setValue(cbCustomerType.getItems().get(0));
			cbFuelType.setValue(cbFuelType.getItems().get(0));
			
			cbCertainHours.setVisible(false);
			cbCustomerType.setVisible(false);
			cbFuelType.setVisible(false);
		}
	}

	/**
	 * this function will make the operation that needed to be activated when click 
	 * on the Sort button and start the event.
	 * @param event
	 */
	@FXML
	void onSort(ActionEvent event) {
		if (isShowRanks) {
			getCustomerRanks();
		} else {
			if(!isFuelType && ! isCustomerType && !isCertainHours) {
				isShowRanks = true;
				ObjectContainer.setButtonImage(ObjectContainer.checked, btnShowRanks);
				getCustomerRanks();
			}else {
				getDataFromDB();				
			}
		}
	}

	/**
	 * this function is responsible to convert an JsonArray to ArrayList.
	 * its gets the JsonArray with all of the information and insert it to 
	 * an array list of Strings
	 * @param jsonArray
	 * @return array list of string
	 */
	public ArrayList<String> convertJsonArrayToArrayListString(JsonArray jsonArray) {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			result.add(jsonArray.get(i).getAsString());
		}

		return result;
	}

	/**
	 * this function is reponsible to convert Json object with the data inside to
	 * an array list.
	 * @param jsonObject - Json object with the data needed.
	 * @return - array list
	 */
	public ArrayList<ArrayList<String>> convertJsonObjectToArrayList(JsonObject jsonObject) {
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
			result.add(arrayList);
		}
		return result;
	}

	/**
	 * this function is responsible to fill the data in the table and display it.
	 * it sends message to the server with the appropriate request.
	 * @param columns 
	 * @param rows
	 */
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

	/**
	 * this function get the customer ranks. it sends message to the server with the
	 * request to get the customer ranks.
	 */
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
	
	/**
	 * this function is responsible to get the analytic data from the DataBase.
	 * it send a message to the server with the request to get the activity tracking data.
	 */
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

	/**
	 * this function is responsible to clear all the fields in the screen.
	 */
	private void clearFields() {
		isCertainHours = false;
		cbCertainHours.setValue(cbCertainHours.getItems().get(0));
		cbCertainHours.setVisible(false);
		ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnCertainHours);
		
		isFuelType = false;
		cbFuelType.setValue(cbFuelType.getItems().get(0));
		cbFuelType.setVisible(false);
		ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnFuelType);
		
		isCustomerType = false;
		cbCustomerType.setValue(cbCustomerType.getItems().get(0));
		cbCustomerType.setVisible(false);
		ObjectContainer.setButtonImage(ObjectContainer.unchecked, btnCustomerType);
	}

	/**
	 * this function is responsible to get the code by specific combination.
	 * it returns the code in a string object.
	 * @return code - string type.
	 */
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
		String url = ObjectContainer.unchecked;
		ObjectContainer.setButtonImage(url, btnCertainHours);
		ObjectContainer.setButtonImage(url, btnCustomerType);
		ObjectContainer.setButtonImage(url, btnFuelType);
		ObjectContainer.setButtonImage(ObjectContainer.checked, btnShowRanks);
		isShowRanks = true;
		getCustomerRanks();
	}

	/**
	 * this function set the values in the choice boxes.
	 */
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

}