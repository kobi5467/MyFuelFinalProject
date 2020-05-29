package client.gui;

import java.io.IOException;

import com.google.gson.JsonObject;

import client.controller.ObjectContainer;
import entitys.SaleTemplate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

public class SalePane {

	@FXML
	private AnchorPane mainSalePane;
	
    @FXML
    private Pane salePane;

    @FXML
    private Pane viewPane;

    @FXML
    private TextField txtStartTime;

    @FXML
    private TextField txtEndTime;

    @FXML
    private TextField txtFuelType;

    @FXML
    private TextField txtDiscountRate;

    @FXML
    private Button btnActiveYes;

    @FXML
    private Button btnActiveNo;

    @FXML
    private TextField txtSaleName;

    @FXML
    private Button btnView;

    @FXML
    private Button btnRunStop;
    
    public boolean isView = false;
    public boolean isRunning;
    public int index = -1;
    
    public void setBackgroundColor(String color) {
    	salePane.setStyle("-fx-background-color:"+color+";");
    }
    
    @FXML
    void onNo(ActionEvent event) {

    }

    @FXML
    void onYes(ActionEvent event) {
    	
    }
    
    @FXML
    void onRunStop(ActionEvent event) {
    	boolean methodSuccess = ObjectContainer.runningSalesController.onChangeSaleStatus(isRunning,index);
    	if(methodSuccess) {
    		if(isRunning) {
    			setButtonImage("../../images/run.png", btnRunStop);
        	}else {
        		setButtonImage("../../images/stop.png", btnRunStop);
        	}
    		isRunning = !isRunning;
    		setYesOrNo(isRunning);
    	}else {
    		ObjectContainer.showMessage("Error","Running Sale Error","You can't run two sales at the same time..");
    	}
    }
    
    @FXML
    void onView(ActionEvent event) {
    	if(viewPane.isVisible()) {
    		closeView();
    	}else {
    		showView();
    	}
    }
    public void closeView() {
    	isView = false;
    	btnRunStop.setVisible(true);
		viewPane.setVisible(false);
		mainSalePane.setPrefSize(salePane.getPrefWidth(), salePane.getPrefHeight());
		setButtonImage("../../images/add_icon.png", btnView);
    }
    
    public void showView() {
    	isView = true;
    	btnRunStop.setVisible(false);
		viewPane.setVisible(true);
		mainSalePane.setPrefSize(viewPane.getPrefWidth(), viewPane.getPrefHeight());
		setButtonImage("../../images/minus_icon.png", btnView);
    }
    
    public AnchorPane load(String color, int index) {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("SalePane.fxml"));
    	
    	SalePane pane = null;
		try {
			mainSalePane = loader.load();
			pane = loader.getController(); 
			pane.initUI(color, index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mainSalePane;
    }
    
    public void initUI(String color, int index) {
    	this.index = index;
    	mainSalePane.setStyle("-fx-background-color:"+color+";"
    					+ "-fx-border-color:#000000;" 
    					+ "-fx-border-radius:5px;");
    	salePane.setVisible(true);
    	viewPane.setVisible(false);
    	setButtonImage("../../images/add_icon.png", btnView);
    	btnRunStop.setMinSize(40, 40);
    	btnRunStop.setText("");
    	btnView.setMinSize(40, 40);
    	btnView.setText("");
    	fillData(ObjectContainer.runningSalesController.saleTemplates.get(index).getAsJsonObject());
    	isView = false;
    }

	private void fillData(JsonObject saleTemplate) {
		System.out.println(saleTemplate);
		txtSaleName.setText(saleTemplate.get("saleName").getAsString());
		txtStartTime.setText(saleTemplate.get("startSaleTime").getAsString());
		txtEndTime.setText(saleTemplate.get("endSaleTime").getAsString());
		txtFuelType.setText(saleTemplate.get("saleType").getAsString());
		txtDiscountRate.setText(saleTemplate.get("discountRate").getAsString());
		isRunning = saleTemplate.get("isRunning").getAsInt() == 1;
		
		if(isRunning) {
			setButtonImage("../../images/stop.png", btnRunStop);
			setYesOrNo(true);
		}else {
			setButtonImage("../../images/run.png", btnRunStop);
			setYesOrNo(false);
		}
	}
	
	public void setYesOrNo(boolean isYes) {
		if(isYes) {
			btnActiveYes.setStyle("-fx-background-color:#00ff00");
			btnActiveNo.setStyle("-fx-background-color:#ffffff");
		}else {
			btnActiveYes.setStyle("-fx-background-color:#ffffff");
			btnActiveNo.setStyle("-fx-background-color:#ff0000");
		}
	}
	
	public void setButtonImage(String url, Button btn) {
		BackgroundImage backgroundImage = new BackgroundImage(
				new Image(getClass().getResource(url).toExternalForm()),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
		Background background = new Background(backgroundImage);
		btn.setBackground(background);
	}
    
}
