package client.gui.allusers;

import java.io.IOException;
import java.util.Calendar;

import client.controller.ObjectContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class HomeController {

	@FXML
	private Pane homePane;
	
	@FXML
	private Label lblMessageTitle;
	
	public void load(Pane changePane) { // load pane to change pane.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeForm.fxml"));

		try {
			homePane = loader.load();
			changePane.getChildren().add(homePane);
			ObjectContainer.homeController = loader.getController();
			ObjectContainer.homeController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initUI() {
		String userName = ObjectContainer.currentUserLogin.getName();
		String message = "";
		
		Calendar c = Calendar.getInstance();
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

		if(timeOfDay >= 0 && timeOfDay < 12){
		    message = "Good Morning";        
		}else if(timeOfDay >= 12 && timeOfDay < 17){
		    message = "Good Afternoon";
		}else if(timeOfDay >= 17 && timeOfDay < 21){
		    message = "Good Evening";
		}else if(timeOfDay >= 21 && timeOfDay < 24){
		    message = "Good Night";
		}
		
		message += ", " + userName;
		
		lblMessageTitle.setText(message);
		
	}
}
