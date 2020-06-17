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

/**
 * This class is the gui controller class of Rates to Approve form.
 * this class will display the request of the Marketing manager to determine new rates.
 * it will allow him to approve or decline the new rates request, if approve it will send a 
 * request to the server to update the new rate, if the CEO decline the request,
 * he will have to insert a reason why he declined, 
 * all of the details will be saved in the DB. 
 * @author Barak
 * @version final
 */

public class RatesToApproveController {
	/**
	 * Array List of the rate Request anchor panes to display
	 */
	public static ArrayList<AnchorPane> rateRequestsAnchorPanes;
	/**
	 * Array list of the Rates request panes separated
	 */
	public static ArrayList<RequestPane> rateRequestsPanes;
	
	/*
	 * static Json array of the requests.
	 */
	public static JsonArray requests;

	@FXML
    private Pane RatesToApprovePane;

    @FXML
    private ScrollPane spRequestsContainer;

    @FXML
    private VBox vbRequestsContainer;

    
    /**
     * This function send a message to the server with the request to get the rates request from the
     * DB. the response from the server inserted into the ratesRequest JsonArray
     * @return JsonArray of the Rates Requests
     */
	public JsonArray getRatesRequests() {
		Message msg = new Message(MessageType.GET_RATES_REQUESTS, "");
		ClientUI.accept(msg);

		JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ratesRequests = responseJson.get("RateRequests").getAsJsonArray();
		return ratesRequests;
	}

	/**
	 * this function loads the form with all of the panes in it, and the detailes.
	 * @param paneChange
	 */
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

	/**
	 * this function Initialize the screen in its first, set the colors and the panes in it
	 * when clicked and get into the page.
	 * it shows all the requests.
	 */
	public void initUI() {
		requests = getRatesRequests();
		rateRequestsAnchorPanes = new ArrayList<>();
		rateRequestsPanes= new ArrayList<>();
		vbRequestsContainer.setSpacing(5);
		for (int i = 0; i < requests.size(); i++) {
			RequestPane reqPane = new RequestPane();
			rateRequestsPanes.add(reqPane);
			String color = i % 2 == 0 ? "#0277ad" : "#014b88";
			AnchorPane pane = reqPane.load(requests.get(i).getAsJsonObject(), color);
			rateRequestsAnchorPanes.add(pane);
		}
		showAllRequests();

	}
	
	/**
	 * this function insert to the VBOX the current rates and remove the other rates
	 * that maybe been initialized and got declined so they have to be removed.
	 */
	
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
	/**
	 * this function start the primary stage.
	 * @param primaryStage
	 * @throws IOException
	 */
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
