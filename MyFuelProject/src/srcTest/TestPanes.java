package srcTest;

import client.gui.HomeHeatingFuelController;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestPanes extends Application {

	public static HomeHeatingFuelController controller;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		controller = new HomeHeatingFuelController();
		controller.start(stage);
	}
}
