package client.gui;

import java.io.IOException;

import client.controller.ObjectContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageController {

    @FXML
    private Pane messagePane;

    @FXML
    private Label lblMessageTitle;

    @FXML
    private Text txtMessage;

    @FXML
    private Button btnYes;

    @FXML
    private Button btnNo;

    @FXML
    private Button btnOk;

    private String type;
    
    @FXML
    void onNo(ActionEvent event) {
    	ObjectContainer.messageStage.close();
    }

    @FXML
    void onOk(ActionEvent event) {

    }

    @FXML
    void onYes(ActionEvent event) {

    }

	public void start(String type) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("MessageForm.fxml"));

		if (ObjectContainer.messageStage == null) {
			ObjectContainer.messageStage = new Stage();
			ObjectContainer.messageStage.initStyle(StageStyle.UNDECORATED);
		}

		try {
			messagePane = loader.load();
			ObjectContainer.messageController = loader.getController();
			ObjectContainer.messageController.initUI(type);
			ObjectContainer.allowDrag(messagePane, ObjectContainer.mainStage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(messagePane);
		ObjectContainer.messageStage.setScene(scene);
		ObjectContainer.messageStage.centerOnScreen();
		ObjectContainer.messageStage.show();
	}

	private void initUI(String type) {
		this.type = type;
		if(type.equals("logout")) {
			btnOk.setVisible(false);
			lblMessageTitle.setText("Logout");
			txtMessage.setText("Are you sure you \nwant to logout ?");
		}
		
	}

}
