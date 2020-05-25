package client.gui;

import java.io.IOException;

import com.google.gson.JsonObject;

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
    
    public void setBackgroundColor(String color) {
    	salePane.setStyle("-fx-background-color:"+color+";");
    }
    
    @FXML
    void onNo(ActionEvent event) {

    }

    @FXML
    void onRunStop(ActionEvent event) {

    }

    @FXML
    void onView(ActionEvent event) {
    	if(viewPane.isVisible()) {
    		btnRunStop.setVisible(true);
    		viewPane.setVisible(false);
    		mainSalePane.setPrefSize(salePane.getPrefWidth(), salePane.getPrefHeight());
    		setButtonImage("../../images/add_icon.png", btnView);
    	}else {
    		btnRunStop.setVisible(false);
    		viewPane.setVisible(true);
    		mainSalePane.setPrefSize(viewPane.getPrefWidth(), viewPane.getPrefHeight());
    		setButtonImage("../../images/minus_icon.png", btnView);
    	}
    }

    @FXML
    void onYes(ActionEvent event) {

    }
    
    public AnchorPane load(JsonObject saleTemplate,String color) {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("SalePane.fxml"));
    	
    	SalePane pane = null;
		try {
			mainSalePane = loader.load();
			pane = loader.getController(); 
			pane.initUI(saleTemplate,color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mainSalePane;
    }
    
    public void initUI(JsonObject saleTemplate, String color) {
    	mainSalePane.setStyle("-fx-background-color:"+color+";");
    	salePane.setVisible(true);
    	viewPane.setVisible(false);
    	setButtonImage("../../images/add_icon.png", btnView);
    	setButtonImage("../../images/run.png", btnRunStop);
    	fillData(saleTemplate);
    }

	private void fillData(JsonObject saleTemplate) {
		System.out.println(saleTemplate);
		txtSaleName.setText(saleTemplate.get("saleName").getAsString());
		txtStartTime.setText(saleTemplate.get("startSaleTime").getAsString());
		txtEndTime.setText(saleTemplate.get("endSaleTime").getAsString());
		txtFuelType.setText(saleTemplate.get("saleType").getAsString());
		txtDiscountRate.setText(saleTemplate.get("discountRate").getAsString());
		String isRunning = saleTemplate.get("isRunning").getAsString();
		if(isRunning.equals("1")) {
			btnActiveYes.setStyle("-fx-background-color:green;");
		}else {
			btnActiveNo.setStyle("-fx-background-color:red;");
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
