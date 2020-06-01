package client.gui.allusers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import client.gui.ceo.RatesToApproveController;
import client.gui.customer.FastFuelController;
import client.gui.customer.HomeHeatingFuelController;
import client.gui.customer.OrderTrackingController;
import client.gui.marketingmanager.DeterminingFuelRatesController;
import client.gui.marketingmanager.ReportControler;
import client.gui.marketingmanager.RunningSalesController;
import client.gui.marketingrepresentative.CustomerRegistrationController;
import client.gui.marketingrepresentative.UpdateCustomerController;
import client.gui.stationmanager.InventoryController;
import entitys.Customer;
import entitys.Message;
import entitys.enums.MessageType;
import entitys.enums.UserPermission;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainFormController {

	@FXML
	private Pane mainPane;

	@FXML
	private ImageView imgBackgroundMain;

	@FXML
	private Pane buttonContainer;

	@FXML
	private Pane changePane;

	@FXML
	private Pane titlePane;

	@FXML
	private Label lblMainTitle;

	@FXML
	private Button btnExit;

	@FXML
	private Button btnMinimize;

	@FXML
	private Pane logoPane;

	@FXML
	private Label lblUsername;

	@FXML
	private Label lblEmployeeRole;

	@FXML
	private Label lblDateTime;

	private Button[] menuButtons;
	
	private int currentPane = 0;
	
	@FXML
	void onExitWindow(ActionEvent event) {
		ObjectContainer.showMessage("yes_no","Exit","Are you sure \nyou want to exit program?");
	}

	@FXML
	void onMinimize(ActionEvent event) {
		ObjectContainer.mainStage.setIconified(true);
	}

	public ArrayList<String> getButtonNames(UserPermission userPermission) {
		ArrayList<String> buttonNames = new ArrayList<>();
		buttonNames.add("Home");
		
		switch (userPermission) {
			case CUSTOMER: 
				buttonNames.add("HomeHeatingFuel");
				buttonNames.add("FastFuelOrder");
				buttonNames.add("OrderTracking");
				break;
			case MARKETING_MANAGER: 
				buttonNames.add("DeterminingFuelRates");
				buttonNames.add("RunningSales");
				buttonNames.add("ReportGeneration");
				break;
			case MARKETING_REPRESENTATIVE: 
				buttonNames.add("CustomerRegistration");
				buttonNames.add("UpdateCustomer");
				buttonNames.add("SaleTemplates");
				break;
			case STATION_MANAGER:
				buttonNames.add("InventoryOrders");
				buttonNames.add("Inventory");
				buttonNames.add("ReportGeneration");
				break;
			case CEO:
				buttonNames.add("RatesRequests");
				break;
			case SUPPLIER:
				buttonNames.add("OrdersRecived");
				break;
		}

		buttonNames.add("About");
		buttonNames.add("Logout");

		return buttonNames;
	}

	@FXML
	public void initialize() {
	    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			lblDateTime.setText(dtf.format(now));
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	}
	
	public void start() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("MainForm.fxml"));

		if (ObjectContainer.mainStage == null) {
			ObjectContainer.mainStage = new Stage();
			ObjectContainer.mainStage.initStyle(StageStyle.UNDECORATED);
		}

		try {
			mainPane = loader.load();
			ObjectContainer.mainFormController = loader.getController();
			ObjectContainer.mainFormController.initUI();
			ObjectContainer.allowDrag(mainPane, ObjectContainer.mainStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(mainPane);
		ObjectContainer.mainStage.setScene(scene);
		ObjectContainer.loginStage.hide();
		ObjectContainer.mainStage.show();
	}

	public void initUI() {
		updateUserDetails();
		ArrayList<String> buttonNames = getButtonNames(ObjectContainer.currentUserLogin.getUserPermission());

		menuButtons = new Button[buttonNames.size()];
		
		for (int i = 0; i < menuButtons.length; i++) {
			menuButtons[i] = new Button();
			menuButtons[i].relocate(5, 5 + i * 60);
			menuButtons[i].setPrefSize(240, 60);
			menuButtons[i].setId("menuButton");
			buttonContainer.getChildren().add(menuButtons[i]);
			boolean isChecked = (i == 0);
			setButtonImage(buttonNames.get(i), i , isChecked);
			menuButtons[i].setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Button btnCurrent = (Button) event.getSource();
					ObjectContainer.mainFormController.setInnerPaneByButtonClicked(btnCurrent,buttonNames);
				}
			});
		}
		lblMainTitle.setText(fixTitle("Home"));
	}
	
	private void updateUserDetails() {
		String userName = ObjectContainer.currentUserLogin.getUsername();
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		Message msg = new Message(MessageType.GET_USER_DETAILS,json.toString());
		ClientUI.accept(msg);
		
		JsonObject response = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		lblUsername.setText(response.get("name").getAsString());
		String employeeRole = response.get("employeeRole").getAsString();
		lblEmployeeRole.setText(employeeRole);
		UserPermission userPermission = UserPermission.stringToEnumVal(response.get("userPermission").getAsString());
		if(userPermission == UserPermission.CUSTOMER) {
			ObjectContainer.currentUserLogin = getCustomerDetails(userName);
			System.out.println(ObjectContainer.currentUserLogin.toString());
		}else {
			ObjectContainer.currentUserLogin.setUserPermission(userPermission);
		}
	}
	
	private Customer getCustomerDetails(String userName) {
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		Message msg = new Message(MessageType.GET_CUSTOMER_DETAILS_BY_USERNAME,json.toString());
		ClientUI.accept(msg);
		
		JsonObject customerJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		Customer customer = new Gson().fromJson(customerJson.get("customerDetails").getAsString(), Customer.class);
		return customer;
	}

	protected void setInnerPaneByButtonClicked(Button btnCurrent, ArrayList<String> buttonNames) {
		String title = "";
		for(int i = 0; i < menuButtons.length;i++) {
			if(btnCurrent.equals(menuButtons[i])) {
				title = buttonNames.get(i);
				if(!title.equals("Logout")) {
					setButtonImage(title,i,true);
					currentPane = i;
				}else {
					setButtonImage(buttonNames.get(currentPane),currentPane,true);
				}
			}else {
				setButtonImage(buttonNames.get(i),i,false);
			}
		}
		setPane(title);
	}
	
	public void setPane(String title) {
		
		/***************************** ALL USERS **********************************/  
		lblMainTitle.setText(fixTitle(title));
		if(title.equals("Logout")) {
			ObjectContainer.showMessage("yes_no","Logout","Are you sure \nyou want to logout?");
			return;
		}
		changePane.getChildren().clear();
		/***************************** Marketing Representative **********************************/  
		if(title.equals("CustomerRegistration")) {
			if(ObjectContainer.customerRegistrationController == null) {
				ObjectContainer.customerRegistrationController = new CustomerRegistrationController();
			}
			ObjectContainer.customerRegistrationController.load(changePane);
		}
		
		if(title.equals("UpdateCustomer")) {
			if(ObjectContainer.updateCustomerController == null) {
				ObjectContainer.updateCustomerController = new UpdateCustomerController();
			}
			ObjectContainer.updateCustomerController.load(changePane);
		}
		
		/***************************** Marketing Manager **********************************/
		
		if(title.equals("DeterminingFuelRates")) {
			if(ObjectContainer.determiningFuelRatesController == null) {
				ObjectContainer.determiningFuelRatesController = new DeterminingFuelRatesController();
			}
			ObjectContainer.determiningFuelRatesController.load(changePane);
		}
		
		if(title.equals("RunningSales")) {
			if(ObjectContainer.runningSalesController == null) {
				ObjectContainer.runningSalesController = new RunningSalesController();
			}
			ObjectContainer.runningSalesController.load(changePane);
		}
		
		if(title.equals("ReportGeneration")) {
			if(ObjectContainer.reportController == null) {
				ObjectContainer.reportController = new ReportControler();
			}
			ObjectContainer.reportController.load(changePane);
		}
		
		/***************************** Customer **********************************/  
		
		if(title.equals("HomeHeatingFuel")) {
			if(ObjectContainer.homeHeatingFuelController == null) {
				ObjectContainer.homeHeatingFuelController = new HomeHeatingFuelController();
			}
			ObjectContainer.homeHeatingFuelController.load(changePane);
		}
		
		if(title.equals("FastFuelOrder")) {
			if(ObjectContainer.fastFuelController == null) {
				ObjectContainer.fastFuelController = new FastFuelController();
			}
			ObjectContainer.fastFuelController.load(changePane);
		}
		
		if(title.equals("OrderTracking")) {
			if(ObjectContainer.orderTrackingController == null) {
				ObjectContainer.orderTrackingController = new OrderTrackingController();
			}
			ObjectContainer.orderTrackingController.load(changePane);
		}
		
		
		/************************************* MARKETING MANAGER ****************************/
		
		if(title.equals("Inventory")) {
			if(ObjectContainer.inventoryController == null) {
				ObjectContainer.inventoryController = new InventoryController();
			}
			ObjectContainer.inventoryController.load(changePane);
		}
		
		/************************************** CEO ***************************************/
		
		if(title.equals("RatesRequests")) {
			if(ObjectContainer.ratesToApproveController == null) {
				ObjectContainer.ratesToApproveController = new RatesToApproveController();
			}
			ObjectContainer.ratesToApproveController.load(changePane);
		}
	}
	
	public void logout() {
		JsonObject json = new JsonObject();
		json.addProperty("userName", ObjectContainer.currentUserLogin.getUsername());
		Message msg = new Message(MessageType.LOGOUT, json.toString());	
		ClientUI.accept(msg);
		ObjectContainer.mainStage.close();
		ObjectContainer.loginStage.show();
	}
	
	public String fixTitle(String title) {
		String fixedTitle = ""+title.charAt(0);
		for (int i = 1; i < title.length(); i++) {
			fixedTitle += title.charAt(i) >= 'A' && title.charAt(i) <= 'Z' ? " " + title.charAt(i)  : title.charAt(i);
		}
		return fixedTitle;
	}

	public void setButtonImage(String buttonName, int index, boolean isChecked) {
		String url = "../../../images/menuButtons/"+buttonName;
		url += isChecked ? "Check.png" : "UnCheck.png";
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		menuButtons[index].setBackground(background);
	}
	
}
