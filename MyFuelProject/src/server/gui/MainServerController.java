package server.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.controller.ServerUI;
/**
 * This class is the main server controller.
 * @author Kobi Malka
 * @version - Final
 */
public class MainServerController {

    @FXML
    private Label lblServerTitle;

    @FXML
    private Button btnStart;

    @FXML
    private TextField txtPort;
    /**
     * This method is responsible to start the server by click on 'Start'.
     * @param event - when we press on start button.
     */
    @FXML
    void onStartServer(ActionEvent event) {
    	if(btnStart.getText().equals("Start")) {
    		String portNumber = txtPort.getText().trim();
    		if(portNumber.isEmpty()) {
//    			lblTitle.setText("Please fill port number");
    		}else {
    			lblServerTitle.setText("Server is on !");
    			lblServerTitle.setStyle("-fx-text-fill:#00ff00;");
    			txtPort.setText("");
    			btnStart.setText("Stop");
    			ServerUI.runServer(portNumber);
    		}    		
    	}else { // here button is stop..
    		ServerUI.stopServer();
    		lblServerTitle.setText("Server is off");
    		lblServerTitle.setStyle("-fx-text-fill:#ff0000;");
    		btnStart.setText("Start");
    	}
    }
    /**
     * This method is responsible to start the current stage.
     * After that load the 'xml' class and call to 'initUI' method.
     * @param primaryStage - current stage to show.
     * @throws IOException - Throw exception if their is no stage to show.
     */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../gui/ServerForm.fxml"));
		Pane root = loader.load();
		ServerUI.mainServerController = loader.getController();
		ServerUI.mainServerController.initUI();
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../../client/gui/style.css").toExternalForm());
		primaryStage.setTitle("Server Manage");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	
	public void onClientConnected() {
		
	}
	/**
	 * This method is responsible to init the port to "5555".
	 */
	private void initUI() {
		txtPort.setText("5555");
	}

}
