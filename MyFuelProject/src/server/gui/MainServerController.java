package server.gui;

import java.io.IOException;

import client.controller.ClientUI;
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

public class MainServerController {

    @FXML
    private Label lblServerTitle;

    @FXML
    private Button btnStart;

    @FXML
    private TextField txtPort;

    @FXML
    void onStartServer(ActionEvent event) {
    	if(btnStart.getText().equals("Start")) {
    		String portNumber = txtPort.getText().trim();
    		if(portNumber.isEmpty()) {
//    			lblTitle.setText("Please fill port number");
    		}else {
//    			lblTitle.setText("Server is on !");
//    			lblTitle.setStyle("-fx-text-fill:#00ff00;");
    			txtPort.setText("");
    			btnStart.setText("Stop");
    			ServerUI.runServer(portNumber);
    		}    		
    	}else { // here button is stop..
    		ServerUI.stopServer();
//    		lblTitle.setText("Server is off");
//    		lblTitle.setStyle("-fx-text-fill:#ff0000;");
    		btnStart.setText("Start");
    	}
    }

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
		ServerUI.runServer("5555");	
	}

	private void initUI() {
		txtPort.setText("5555");
	}

}
