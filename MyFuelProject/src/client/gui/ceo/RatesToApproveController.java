package client.gui.ceo;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import client.controller.ClientUI;
import client.controller.ObjectContainer;
import entitys.Message;
import entitys.enums.MessageType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RatesToApproveController {

	public static ArrayList<AnchorPane> rateRequestsAnchorPanes;
	public static ArrayList<RequestPane> rateRequestsPanes;

	public static JsonArray requests;

	@FXML
    private Pane RatesToApprovePane;

    @FXML
    private ScrollPane spRequestsContainer;

    @FXML
    private VBox vbRequestsContainer;

	public JsonArray getRatesRequests() {
		Message msg = new Message(MessageType.GET_RATES_REQUESTS, "");
		ClientUI.accept(msg);

		JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ratesRequests = responseJson.get("RateRequests").getAsJsonArray();
		return ratesRequests;
	}

	public void load(Pane paneChange) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("RatesToApproveForm.fxml"));

		try {
			RatesToApprovePane = loader.load();
			paneChange.getChildren().add(RatesToApprovePane);
			ObjectContainer.ratesToApproveController = loader.getController();
			ObjectContainer.ratesToApproveController.initUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initUI() {
		requests = getRatesRequests();
		rateRequestsAnchorPanes = new ArrayList<>();
		rateRequestsPanes= new ArrayList<>();
		vbRequestsContainer.setSpacing(5);
		for (int i = 0; i < requests.size(); i++) {
			RequestPane reqPane = new RequestPane();
			rateRequestsPanes.add(reqPane);
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			AnchorPane pane = reqPane.load(requests.get(i).getAsJsonObject(), color);
			rateRequestsAnchorPanes.add(pane);
		}
		showAllRequests();

	}
	
	public void showAllRequests() {
		vbRequestsContainer.getChildren().clear();
		for (int i = 0; i < requests.size(); i++) {
			if(!rateRequestsPanes.get(i).tookDecision) {
				vbRequestsContainer.getChildren().add(rateRequestsAnchorPanes.get(i));
			}	
			else {
				rateRequestsPanes.remove(i);
				rateRequestsAnchorPanes.remove(i);
				requests.remove(i);
			}
		}
	}
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new  FXMLLoader();
    	loader.setLocation(getClass().getResource("RatesToApproveForm.fxml"));
    	
    	RatesToApprovePane = loader.load();
		ObjectContainer.ratesToApproveController = loader.getController();
		ObjectContainer.ratesToApproveController.initUI();
		
		Scene scene = new Scene(RatesToApprovePane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
