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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This class is responsible to approve/deny subscribe rate request from the station manager.
 * @author Or Yom Tov
 * @version - Final
 */
public class SubscribeRateRequestController {
	
	public static ArrayList<AnchorPane> rateRequestsAnchorPanes;
	
	public static ArrayList<SubscribePane> subscribeRequestPanes;

    @FXML
    private Pane requestPane;

    @FXML
    private VBox vbSubscribeRate;
    
	public static JsonArray requests;
	
	@FXML
    private Label lblNoRequests;
	
    /**
     * This function send a message to the server with the request to get the rates request from the
     * DB. the response from the server inserted into the ratesRequest JsonArray
     * @return JsonArray of the Rates Requests
     */
	public JsonArray getRatesRequests() {
		Message msg = new Message(MessageType.GET_DISCOUNT_REQUESTS, "");
		ClientUI.accept(msg);
		
		JsonObject responseJson = ObjectContainer.currentMessageFromServer.getMessageAsJsonObject();
		JsonArray ratesRequests = responseJson.get("RateRequests").getAsJsonArray();
		return ratesRequests;
	}

	/**
	 * this function loads the form with all of the panes in it, and the details.
	 * @param paneChange
	 */
	public void load(Pane paneChange) {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SubscribesRateRequest.fxml"));
		try {
			requestPane = loader.load();
			paneChange.getChildren().add(requestPane);
			ObjectContainer.subscribeRateRequestController = loader.getController();
			ObjectContainer.subscribeRateRequestController.initUI();
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
		subscribeRequestPanes= new ArrayList<>();
		vbSubscribeRate.setSpacing(5);
		for (int i = 0; i < requests.size(); i++) {
			SubscribePane subPane = new SubscribePane();
			subscribeRequestPanes.add(subPane);
			String color = i % 2 == 0 ? ObjectContainer.rowColorBG1 : ObjectContainer.rowColorBG2;
			AnchorPane pane = subPane.load(requests.get(i).getAsJsonObject(), color);
			rateRequestsAnchorPanes.add(pane);
		}
		showAllRequests();

	}
	
	/**
	 * this function insert to the VBOX the current rates and remove the other rates
	 * that maybe been initialized and got declined so they have to be removed.
	 */
	
	public void showAllRequests() {
		vbSubscribeRate.getChildren().clear();
		for (int i = 0; i < requests.size(); i++) {
			if(!subscribeRequestPanes.get(i).tookDecision) {
				vbSubscribeRate.getChildren().add(rateRequestsAnchorPanes.get(i));
			}	
			else {
				subscribeRequestPanes.remove(i);
				rateRequestsAnchorPanes.remove(i);
				requests.remove(i);
			}
		}
		lblNoRequests.setVisible(requests.size() == 0);
	}

}
