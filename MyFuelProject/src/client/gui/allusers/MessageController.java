package client.gui.allusers;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * This class responsible to show the message to the user.
 * @author oyomtov
 * @version - Final
 */
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

    private String title;
    
    @FXML
    void onNo(ActionEvent event) {
    	ObjectContainer.yesNoMessageResult = false;
    	ObjectContainer.messageStage.close();
    }

    @FXML
    void onOk(ActionEvent event) {
    	ObjectContainer.messageStage.close();
    }

    @FXML
    void onYes(ActionEvent event) {
    	ObjectContainer.yesNoMessageResult = true;
    	ObjectContainer.messageStage.close();
    	if(title.equals("Logout") || title.equals("Exit")) {
    		ObjectContainer.mainFormController.logout();
    	}
    }

	public void start(String type, String title, String msg) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("MessageForm.fxml"));

		if (ObjectContainer.messageStage == null) {
			ObjectContainer.messageStage = new Stage();
			ObjectContainer.messageStage.initStyle(StageStyle.UNDECORATED);
			ObjectContainer.messageStage.initModality(Modality.APPLICATION_MODAL);
		}

		try {
			messagePane = loader.load();
			ObjectContainer.messageController = loader.getController();
			ObjectContainer.messageController.initUI(type,title,msg);
			ObjectContainer.allowDrag(messagePane, ObjectContainer.messageStage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(messagePane);
		ObjectContainer.messageStage.setScene(scene);
		ObjectContainer.messageStage.centerOnScreen();
		ObjectContainer.messageStage.showAndWait();
	}

	private void initUI(String type, String title, String msg) {
		this.title = title;
		if(type.equals("yes_no")) {
			btnOk.setVisible(false);
			btnYes.setVisible(true);
			btnNo.setVisible(true);
		}else if(type.equals("Error") || type.equals("connection")) {
			btnYes.setVisible(false);
			btnNo.setVisible(false);
			btnOk.setVisible(true);
		}
		lblMessageTitle.setText(title);
		txtMessage.setText(msg);
	}

}
