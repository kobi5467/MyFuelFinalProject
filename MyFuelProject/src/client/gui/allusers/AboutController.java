package client.gui.allusers;

import java.io.IOException;

import client.controller.ObjectContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
/**
 * This class responsible to load the 'About' Controller
 * @author oyomtov
 * @version - Final
 */
public class AboutController {

    @FXML
    private Pane aboutPane;

    
    public void load(Pane changePane) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("AboutPane.fxml"));

		try {
			aboutPane = loader.load();
			changePane.getChildren().add(aboutPane);
			ObjectContainer.aboutController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
