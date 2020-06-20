package client.gui.allusers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import client.gui.ceo.RatesToApproveController;
import client.gui.ceo.ReportViewController;
import client.gui.ceo.SubscribeRateRequestController;
import client.gui.customer.HomeHeatingFuelController;
import client.gui.customer.OrderTrackingController;
import client.gui.marketingmanager.DeterminingFuelRatesController;
import client.gui.marketingmanager.InventoryOrdersController;
import client.gui.marketingmanager.ReportController;
import client.gui.marketingrepresentative.AnalyticSystemController;
import client.gui.marketingrepresentative.CustomerRegistrationController;
import client.gui.marketingrepresentative.SaleTemplateController;
import client.gui.marketingrepresentative.UpdateCustomerController;
import client.gui.stationmanager.InventoryController;
import client.gui.supplier.SupplierController;
import entitys.Customer;
import entitys.Message;
import entitys.PurchaseModel;
import entitys.SubscribeType;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
/**
 * This class responsible to init the main Pane to all users.
 * @author oyomtov
 * @version - Final
 */
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
	
	private ArrayList<String> buttonNames;
	@FXML
	void onExitWindow(ActionEvent event) {
		ObjectContainer.showMessage("yes_no","Exit","Are you sure \nyou want to exit program?");
	}

	@FXML
	void onMinimize(ActionEvent event) {
		ObjectContainer.mainStage.setIconified(true);
	}

	public ArrayList<String> getButtonNames(UserPermission userPermission) {
		buttonNames = new ArrayList<>();
		buttonNames.add("Home");
		
		switch (userPermission) {
			case CUSTOMER: 
				buttonNames.add("HomeHeatingFuel");
//				buttonNames.add("FastFuelOrder");
				buttonNames.add("OrderTracking");
				break;
			case MARKETING_MANAGER: 
				buttonNames.add("DeterminingRates");
				buttonNames.add("RunningSales");
				buttonNames.add("ReportGeneration");
				break;
			case MARKETING_REPRESENTATIVE: 
				buttonNames.add("CustomerRegistration");
				buttonNames.add("UpdateCustomer");
				buttonNames.add("SaleTemplates");
				buttonNames.add("ActivityTracking");
				break;
			case STATION_MANAGER:
				buttonNames.add("InventoryOrders");
				buttonNames.add("Inventory");
				buttonNames.add("ReportGeneration");
				buttonNames.add("ActivityTracking");
				break;
			case CEO:
				buttonNames.add("FuelRatesRequest");
				buttonNames.add("DiscountRequests");
				buttonNames.add("ViewReports");
				break;
			case SUPPLIER:
				buttonNames.add("OrdersReceived");
				break;
		}

		buttonNames.add("About");
		buttonNames.add("Logout");

		return buttonNames;
	}
	
	/**
	 * This methid responsible to init the clock.
	 */
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
		scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
		ObjectContainer.mainStage.setScene(scene);
		ObjectContainer.loginStage.hide();
		ObjectContainer.mainStage.show();
	}
	/**
	 * This method responsible to init the data.
	 */
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
		setPane("Home");
		setBackgroundImage("Home");
	}
	/**
	 * This method responsible to set background.
	 * @param title - string value of title.
	 */
	public void setBackgroundImage(String title) {
		String url = title.equals("Home") ? "/images/HomeBG.jpg" : "/images/mainBG.jpg"; 
		ObjectContainer.setImageBackground(url, imgBackgroundMain);
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
		}else {
			ObjectContainer.currentUserLogin.setUserPermission(userPermission);
			ObjectContainer.currentUserLogin.setName(response.get("name").getAsString());
		}
	}
	/**
	 * This method responsible to request from the server to take the customer deatils by user name.
	 * @param userName - string value of user name.
	 * @return - return Json object with customer details.
	 */
	private Customer getCustomerDetails(String userName) {
		JsonObject json = new JsonObject();
		json.addProperty("userName", userName);
		Message msg = new Message(MessageType.GET_CUSTOMER_DETAILS_BY_USERNAME,json.toString());
		ClientUI.accept(msg);
		
		JsonObject customerJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();

		Customer customer = new Customer();
		customer.setCity(customerJson.get("city").getAsString());
		customer.setCustomerId(customerJson.get("customerID").getAsString());
		customer.setName(customerJson.get("name").getAsString());
		customer.setUsername(customerJson.get("userName").getAsString());
		customer.setEmail(customerJson.get("email").getAsString());
		customer.setPhoneNumber(customerJson.get("phoneNumber").getAsString());
		customer.setCustomerType(customerJson.get("customerType").getAsString());
		customer.setSubscribeType(new SubscribeType(customerJson.get("subscribeType").getAsString(),0));
		customer.setPurchaseModel(new PurchaseModel(customerJson.get("purchaseModelType").getAsString(), 0, null));
		customer.setStreet(customerJson.get("street").getAsString());
		customer.setUserPermission(UserPermission.stringToEnumVal(customerJson.get("userPermission").getAsString()));
		return customer;
	}
	/**
	 * This method responsible to set inner pane by current click.
	 * @param btnCurrent - button value.
	 * @param buttonNames - array list of buttons name.
	 */
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
	
	public void setHomeButtonSelect() {
		for (int i = 0; i < menuButtons.length; i++) {
			if(i == 0) {
				setButtonImage(buttonNames.get(i), i, true);
			}else {
				setButtonImage(buttonNames.get(i), i, false);
			}
		}
	}
	
	/**
	 * This method responsible set pane by the current title.
	 * @param title - string value of title.
	 */
	public void setPane(String title) {
		/***************************** ALL USERS **********************************/  
		if(title.equals("Logout")) {
			ObjectContainer.showMessage("yes_no","Logout","Are you sure \nyou want to logout?");
			return;
		}
		
		lblMainTitle.setText(fixTitle(title));
		changePane.getChildren().clear();
		setBackgroundImage(title);
		
		if(title.equals("About")) {
			if(ObjectContainer.aboutController == null) {
				ObjectContainer.aboutController = new AboutController();
			}
			ObjectContainer.aboutController.load(changePane);
		}
		
		if(title.equals("Home")) {
			lblMainTitle.setText("");
			setHomeButtonSelect();
			if(ObjectContainer.homeController == null) {
				ObjectContainer.homeController = new HomeController();
			}
			ObjectContainer.homeController.load(changePane);
		}
		
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
		
		if(title.equals("SaleTemplates")) {
			if(ObjectContainer.saleTemplateController == null) {
				ObjectContainer.saleTemplateController = new SaleTemplateController();
			}
			ObjectContainer.saleTemplateController.load(changePane);
		}
		
		if(title.equals("ActivityTracking")) {
			if(ObjectContainer.analyticSystemController == null) {
				ObjectContainer.analyticSystemController = new AnalyticSystemController();
			}
			ObjectContainer.analyticSystemController.load(changePane);
		}
		
		/***************************** Marketing Manager **********************************/
		
		if(title.equals("DeterminingRates")) {
			if(ObjectContainer.determiningFuelRatesController == null) {
				ObjectContainer.determiningFuelRatesController = new DeterminingFuelRatesController();
			}
			ObjectContainer.determiningFuelRatesController.load(changePane);
		}
		
		if(title.equals("RunningSales")) {
			if(ObjectContainer.saleTemplateController == null) {
				ObjectContainer.saleTemplateController = new SaleTemplateController();
			}
			ObjectContainer.saleTemplateController.load(changePane);
		}
		
		if(title.equals("ReportGeneration")) {
			if(ObjectContainer.reportController == null) {
				ObjectContainer.reportController = new ReportController();
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
		
//		if(title.equals("FastFuelOrder")) {
//			if(ObjectContainer.fastFuelController == null) {
//				ObjectContainer.fastFuelController = new FastFuelController();
//			}
//			ObjectContainer.fastFuelController.load(changePane);
//		}
		
		if(title.equals("OrderTracking")) {
			if(ObjectContainer.orderTrackingController == null) {
				ObjectContainer.orderTrackingController = new OrderTrackingController();
			}
			ObjectContainer.orderTrackingController.load(changePane);
		}
		
		
		/************************************** CEO ***************************************/
		
		if (title.equals("FuelRatesRequest")) {
			if(ObjectContainer.ratesToApproveController == null) {
				ObjectContainer.ratesToApproveController = new RatesToApproveController();
			}
			ObjectContainer.ratesToApproveController.load(changePane);
		}
		
		if (title.equals("DiscountRequests")) {
			if(ObjectContainer.subscribeRateRequestController == null) {
				ObjectContainer.subscribeRateRequestController = new SubscribeRateRequestController();
			}
			ObjectContainer.subscribeRateRequestController.load(changePane);
		}
		
		if (title.equals("ViewReports")){
			if(ObjectContainer.reportViewController == null) {
				ObjectContainer.reportViewController = new ReportViewController();
			}
			ObjectContainer.reportViewController.load(changePane);
		}
		
		/************************************ Station Manager ***************************/
		if(title.equals("InventoryOrders")) {
			if(ObjectContainer.inventoryOrdersController == null) {
				ObjectContainer.inventoryOrdersController = new InventoryOrdersController();
			}
			ObjectContainer.inventoryOrdersController.load(changePane);
		}
		
		if(title.equals("Inventory")) {
			if(ObjectContainer.inventoryController == null) {
				ObjectContainer.inventoryController = new InventoryController();
			}
			ObjectContainer.inventoryController.load(changePane);
		}
		
		/********************************* Supplier ************************************/
		
		if(title.equals("OrdersReceived")) {
			if(ObjectContainer.supplierController == null) {
				ObjectContainer.supplierController = new SupplierController();
			}
			ObjectContainer.supplierController.load(changePane);
		}
	}
	/**
	 * This method responsible to log out from the server.
	 */
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
	/**
	 * This method responsible to set images on the buttons.
	 * @param buttonName - string value of button name.
	 * @param index - current index.
	 * @param isChecked - boolean value.
	 */
	public void setButtonImage(String buttonName, int index, boolean isChecked) {
		String url = "/images/menuButtons/"+buttonName;
		url += isChecked ? "Check.png" : "UnCheck.png";
		ObjectContainer.setButtonImage(url, menuButtons[index]);
	}
	
}
