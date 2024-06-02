package com.example.quranapp;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
	Button search_btn;
	@FXML
	Menu menu;
	@FXML
	MenuItem bookmarks, goback_btn, settings_btn;
	@FXML
	AnchorPane audio_container;
	@FXML
	ChoiceBox<Integer> go_to_verse;
	private boolean isDarkMode = false;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(root_pane == null) {
			System.out.println("No root_pane");
			return;
		}
		// Set Navigation controller for navigating to different page
		NavigationController.setContainer(container, audio_container, go_to_verse);
		NavigationController.goToHome();

		// Dark mode button function
		dark_mode_btn.setOnAction(e -> {
			if(!isDarkMode) {
				scene.getStylesheets().add(String.valueOf(RootPaneController.class.getResource("dark-theme.css")));
				setButtonIcon(e, "day-mode.png");
				isDarkMode = true;
			} else {
				scene.getStylesheets().remove(String.valueOf(RootPaneController.class.getResource("dark-theme.css")));
				setButtonIcon(e, "night-mode.png");
				isDarkMode = false;
			}
		});


		menu.setGraphic(getImage("nav-menu.png", 20));
		bookmarks.setOnAction(e -> handleBookmarks());
		goback_btn.setOnAction(e -> handleGoBack());
		settings_btn.setOnAction(e -> handleSettings());

		go_to_verse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			NavigationController.goToSurah(AppData.currentSurahNumber, newValue, newValue+20, AppData.numberOfAyahs);
		});
	 }


	public void handleBookmarks() {
		new Bookmark();
	}
	public void handleGoBack() {
		NavigationController.goToHome();
	}
	public void handleSettings() {
		new Settings();
	}

	void setButtonIcon(ActionEvent e, String url) {
		Image icon = new Image(String.valueOf(RootPaneController.class.getResource(url).toExternalForm()));
		ImageView imageView = new ImageView(icon);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		Button btn = (Button) e.getTarget();
		btn.setGraphic(imageView);
	}

	 public void setScene(Scene scene) {
        this.scene = scene;
	 }

	 public ImageView getImage(String name, int size) {
		 Image icon = new Image(String.valueOf(RootPaneController.class.getResource(name).toExternalForm()));
		 ImageView imageView = new ImageView(icon);
		 imageView.setFitHeight(size);
		 imageView.setFitWidth(size);
		 return imageView;
	 }

	 @FXML
	 void onSearch() {
		 String keyword = search_field.getText().toLowerCase();
		 if(AppData.searchedKeyword.equals(keyword)) return;
		 AppData.searchedKeyword = keyword.toLowerCase();
		 NavigationController.goToSearchPage();
	 }
}
