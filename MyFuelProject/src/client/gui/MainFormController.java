package client.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import client.controller.ObjectContainer;
import entitys.User;
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
	private Label lblEmploeeRole;

	@FXML
	private Label lblDateTime;

	private Button[] menuButtons;
	
	@FXML
	void onExitWindow(ActionEvent event) {
		// add here show message if we want to logout. and logout stuff.
		ObjectContainer.mainStage.hide();
		ObjectContainer.loginStage.show();
	}

	@FXML
	void onMinimize(ActionEvent event) {
		ObjectContainer.mainStage.setIconified(true);
	}

	public ArrayList<String> getButtonNames(UserPermission userPermission) {
		ArrayList<String> buttonNames = new ArrayList<>();
		buttonNames.add("Home");

		switch (userPermission) {
		case CUSTOMER: {
			buttonNames.add("HomeHeatingFuel");
			buttonNames.add("OrderTracking");
		}
			break;
		case MARKETING_MANAGER: {
			buttonNames.add("DeterminingFuelRate");
			buttonNames.add("RunningSales");
			buttonNames.add("ReportGeneration");
		}
			break;
		case MARKETING_REPRESENTATIVE: {
			buttonNames.add("CustomerRegistration");
			buttonNames.add("UpdateCustomer");
			buttonNames.add("SaleTemplates");
		}
			break;
		case STATION_MANAGER: {
			buttonNames.add("InventoryOrders");
			buttonNames.add("Inventory");
			buttonNames.add("ReportGeneration");
		}
			break;
		case SUPPLIER: {
			buttonNames.add("Order");
		}break;
		}

		buttonNames.add("Setting");
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
	
	public void start(UserPermission userPermission) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("MainForm.fxml"));

		if (ObjectContainer.mainStage == null) {
			ObjectContainer.mainStage = new Stage();
			ObjectContainer.mainStage.initStyle(StageStyle.UNDECORATED);
		}

		try {
			mainPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectContainer.mainFormController = loader.getController();

		ObjectContainer.mainFormController.initUI(userPermission);
		ObjectContainer.allowDrag(mainPane, ObjectContainer.mainStage);

		Scene scene = new Scene(mainPane);
		ObjectContainer.mainStage.setScene(scene);
		ObjectContainer.loginStage.hide();
		ObjectContainer.mainStage.show();
	}

	public void initUI(UserPermission userPermission) {
		ArrayList<String> buttonNames = getButtonNames(userPermission);

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
	}
	
	protected void setInnerPaneByButtonClicked(Button btnCurrent, ArrayList<String> buttonNames) {
		for(int i = 0; i < menuButtons.length;i++) {
			if(btnCurrent.equals(menuButtons[i])) {
				String title = buttonNames.get(i);
				setButtonImage(title,i,true);
				lblMainTitle.setText(fixTitle(title));
				setPane(title);
			}else {
				setButtonImage(buttonNames.get(i),i,false);
			}
		}
	}
	
	private void setPane(String title) {
		if(title.equals("CustomerRegistration")) {
			if(ObjectContainer.customerRegistrationController == null) {
				ObjectContainer.customerRegistrationController = new CustomerRegistrationController();
			}
			ObjectContainer.customerRegistrationController.load(changePane);
		}
	}
	
	

	public String fixTitle(String title) {
		String fixedTitle = ""+title.charAt(0);
		for (int i = 1; i < title.length(); i++) {
			fixedTitle += title.charAt(i) >= 'A' && title.charAt(i) <= 'Z' ? " " + title.charAt(i)  : title.charAt(i);
		}
		return fixedTitle;
	}

	public void setButtonImage(String buttonName, int index, boolean isChecked) {
		String url = "../../images/menuButtons/"+buttonName;
		url += isChecked ? "Check.png" : "UnCheck.png";
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		menuButtons[index].setBackground(background);
	}
		
	public void setUserDetails(User user) {
		lblUsername.setText(user.getName());
		String userPermission = user.getUserPermission().equals(UserPermission.CUSTOMER) ? "" : user.getUserPermission().toString();
		lblEmploeeRole.setText(userPermission);
	}

}
