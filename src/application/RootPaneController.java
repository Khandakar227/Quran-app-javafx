package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import components.SurahCard;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class RootPaneController implements Initializable {
	private Scene scene;
	@FXML
	ScrollPane container;
	@FXML
	VBox root_pane;
	@FXML
	TextField search_field;
	@FXML
	Button dark_mode_btn;
	@FXML
	Label home_label;
	@FXML
	Button search_btn;
	
	private boolean isDarkMode = false;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(root_pane == null) {
			System.out.println("No root_pane");
			return;
		}
		// Set Navigation controller for navigating to different page
		NavigationController.setContainer(container);
		NavigationController.goToHome();
		
		// Dark mode button function
		dark_mode_btn.setOnAction(e -> {
			if(!isDarkMode) {
				scene.getStylesheets().add("/application/resources/dark-theme.css");
				setButtonIcon(e, "/application/resources/day-mode.png");
				isDarkMode = true;
			} else {
				scene.getStylesheets().remove("/application/resources/dark-theme.css");
				setButtonIcon(e, "/application/resources/night-mode.png");
				isDarkMode = false;
			}
		});
		
		home_label.setOnMouseClicked(e -> {
			NavigationController.goToHome();
		});
	 }
	 

	void setButtonIcon(ActionEvent e, String url) {
		Image icon = new Image(getClass().getResource(url).toExternalForm());
		ImageView imageView = new ImageView(icon);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		Button btn = (Button) e.getTarget();
		btn.setGraphic(imageView);
	}
	
	 public void setScene(Scene scene) {
        this.scene = scene;
	 }
	 
	 @FXML
	 void onSearch() {
		 String keyword = search_field.getText().toLowerCase();
		 if(AppData.searchedKeyword.equals(keyword)) return;
		 AppData.searchedKeyword = keyword.toLowerCase();
		 NavigationController.goToSearchPage();
	 }
}
