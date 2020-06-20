package server.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.controller.ServerUI;
/**
 * This class is the main server controller.
 * @author Kobi Malka
 * @version - Final
 */
public class MainServerController {

	@FXML
    private ImageView imgBGServer;
	
    @FXML
    private Label lblServerTitle;

    @FXML
    private Button btnStart;

    @FXML
    private TextField txtPort;
    
    @FXML
    private ScrollPane spUserContainer;

    @FXML
    private VBox vbUserContainer;
    
    @FXML
    private Button btnClose;

    private ArrayList<UserPaneController> userPanes;
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
    
    
    @FXML
    void onClose(ActionEvent event) {
    	ObjectContainer.showMessage("yes_no", "Exit Server", "Are you sure that you want to exit server?");
    	if(ObjectContainer.yesNoMessageResult && ServerUI.serverController != null && 
    			ServerUI.serverController.isListening())
    		ServerUI.stopServer();
    	ServerUI.serverStage.close();
    	System.exit(0);
    }
    
    /**
     * This method is responsible to start the current stage.
     * After that load the 'xml' class and call to 'initUI' method.
     * @param primaryStage - current stage to show.
     * @throws IOException - Throw exception if their is no stage to show.
     */
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/server/gui/ServerForm.fxml"));
		Pane root = loader.load();
		ServerUI.mainServerController = loader.getController();
		ServerUI.mainServerController.initUI();
		
		ObjectContainer.allowDrag(root, primaryStage);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Server Manage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void addNewUser(JsonObject userJson) {
		new Thread() {
			@Override
			public void run() {
				UserPaneController userPane = new UserPaneController();
				String color = userPanes.size() % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
				userPanes.add(userPane.load(userJson, color));
				Platform.runLater(() -> showAllUsers());
			}
		}.start();
	}
	
	public void removeUser(String userName) {
		new Thread() {
			@Override
			public void run() {
				for(int i = 0; i < userPanes.size(); i++) {
					if(userPanes.get(i).getUserName().equals(userName)) {
						userPanes.remove(i);
					}
				}
				Platform.runLater(() -> showAllUsers());
			}
		}.start();
	}
	
	public void showAllUsers() {
		vbUserContainer.getChildren().clear();
		for (int i = 0; i < userPanes.size(); i++) {
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			userPanes.get(i).updateColor(color);
			vbUserContainer.getChildren().add(userPanes.get(i).getMainPane());
		}
	}
	
	/**
	 * This method is responsible to init the port to "5555".
	 */
	private void initUI() {
		ObjectContainer.setImageBackground("/images/fast_fuel_BG.png", imgBGServer);
		btnStart.setDefaultButton(true);
		vbUserContainer.setSpacing(5);
		userPanes = new ArrayList<UserPaneController>();
		btnClose.setBackground(new Background(new BackgroundImage(new Image(getClass().getResource("/images/delete_icon.png").toExternalForm()),
				BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT)));
	}

}
