package com.example.quranapp;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.*;

public class AyahCard extends VBox {
	private int surahNumber, ayahNumber;
	private String arabicText, translatedText;
	private boolean isBookmarked;
	@FXML
	TextFlow arabic_text;
	@FXML
	Label translated_text, number;
	@FXML
	Button play_btn, bookmark_btn;
	
	public AyahCard(int surahNumber, int ayahNumber, String arabicText, String translatedText, boolean isBookmarked) {
		this.surahNumber = surahNumber;
		this.ayahNumber = ayahNumber;
		this.arabicText = arabicText;
		this.translatedText = translatedText;
		this.isBookmarked = isBookmarked;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AyahCard.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(AyahCard.this);
		
		try {			
			fxmlLoader.load();
		} catch (Exception e) {
			System.out.println("[Ayah Card]: "+ e);
		}
	}

	
	public void initialize() {
		try {
			Text arabic = (Text) arabic_text.getChildren().get(0);
			arabic.setText(arabicText);
			translated_text.setText(translatedText);
			number.setText(Integer.toString(surahNumber) + ":" + Integer.toString(ayahNumber));

			bookmark_btn.setOnAction((e) -> {
				setBookmark(!isBookmarked);
				setButtonIcon(e, isBookmarked ? "bookmark.png" : "bookmarked.png");
				isBookmarked = !isBookmarked;
			});
			setButtonIcon(bookmark_btn, isBookmarked ? "bookmarked.png" : "bookmark.png");
		} catch (Exception e) {
			System.out.println("[Ayah Card initialize]: "+ e);
		}
	}

	public void setBookmark(boolean v) {
		try {
			Connection connection = DbController.getInstance();
			String sql;
			if(v)
				sql = "INSERT INTO bookmark (ayahNumber, surahNumber, createdAt) VALUES ( ?, ?, datetime('now') )";
			else
				sql = "DELETE FROM bookmark where ayahNumber = ? and surahNumber = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, ayahNumber);
			statement.setInt(2, surahNumber);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void setButtonIcon(ActionEvent e, String url) {
		Image icon = new Image(String.valueOf(getClass().getResource(url).toExternalForm()));
		ImageView imageView = new ImageView(icon);
		imageView.setFitHeight(20);
		imageView.setFitWidth(20);
		Button btn = (Button) e.getTarget();
		btn.setGraphic(imageView);
	}

	void setButtonIcon(Button btn, String url) {
		Image icon = new Image(String.valueOf(getClass().getResource(url).toExternalForm()));
		ImageView imageView = new ImageView(icon);
		imageView.setFitHeight(20);
		imageView.setFitWidth(20);
		btn.setGraphic(imageView);
	}
}
