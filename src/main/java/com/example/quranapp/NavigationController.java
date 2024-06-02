package com.example.quranapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class NavigationController {
	static ScrollPane container = null;
	static VBox vbox;
	static AnchorPane audioContainer = null;
	static ChoiceBox<Integer> ayahNumberBox = null;

	public static void setContainer(ScrollPane container, AnchorPane audioContainer, ChoiceBox<Integer> ayahNumberBox) {
		NavigationController.container = container;
		NavigationController.audioContainer = audioContainer;
		NavigationController.ayahNumberBox = ayahNumberBox;
	}
	
	public static void clearContainer() {
		if(container != null) {
			container.setContent(null);
		}
	}
	
	public static void setCurrentPage(String page) {
		AppData.currentPage = page;
	}
	
	public static void goToHome() {
		AppData.currentPage = "Home";
		container.setContent(null);
		FlowPane flowPane = new FlowPane();
		getSurahs(flowPane);

		flowPane.setAlignment(Pos.CENTER);
		container.setContent(flowPane);
		container.setFitToWidth(true);
		container.setPadding(new Insets(0, 0, 0, 0));
		audioContainer.setVisible(false);
		AudioPlayer.stopAudio();

		ayahNumberBox.getItems().clear();
	}
	
	public static void goToSurah(int surahNumber, int from, int to, int totalAyahs) {
		AppData.currentPage = "Surah";
		boolean isChanged = false;
		if(AppData.currentSurahNumber != surahNumber) isChanged = true;
		AppData.currentSurahNumber = surahNumber;
		AppData.fromAyahNumber = from;
		AppData.toAyahNumber = to;
		AppData.numberOfAyahs = totalAyahs;
		
		vbox = new VBox();
		
		AppData.currentSurah = AppData.getSurah();
		SurahName surahNameCard = new SurahName(AppData.currentSurah);
		
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		
		vbox.getChildren().add(surahNameCard);
		
		container.setContent(vbox);
		container.setPadding(new Insets(0, 0, 80, 0));

		getAyahs(vbox);

		if(isChanged) {
			for (int i = 1; i <= AppData.numberOfAyahs; i++)
				ayahNumberBox.getItems().add(i);
		}
	}

	public static void goToSearchPage() {
		if(AppData.searchedKeyword.isBlank()) return;
		AppData.currentPage = "Search";
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(10);
		
		container.setContent(vbox);

		getSearch(vbox);
		audioContainer.setVisible(false);
		AudioPlayer.stopAudio();
	}
	
	private static void getSurahs(FlowPane flowPane) {
		Connection connection = DbController.getInstance();	
		try {
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM surahs";
			ResultSet resultSet = statement.executeQuery(sql);
			
			ObservableList<Node> flowPaneChildren = flowPane.getChildren();
			
			Thread bgThread = new Thread(() -> {
				try {
					while (resultSet.next()) {
						int surahNumber = resultSet.getInt("number");
						int numberOfAyahs = resultSet.getInt("numberOfAyahs");
						String name = resultSet.getString("name");
						String englishName = resultSet.getString("englishName");
						String englishNameTranslation = resultSet.getString("englishNameTranslation");
						String surahType = resultSet.getString("revelationType");
						
						Platform.runLater(()-> {
							flowPaneChildren.add(new SurahCard(
									surahNumber,
									numberOfAyahs,
									name,
									englishName + " (" + englishNameTranslation + ")",
									surahType
									)
								);
						});
					
					}
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			});
			
			bgThread.setDaemon(true);
			bgThread.start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void getAyahs(VBox vbox) {
		Connection connection = DbController.getInstance();
		try {
			String translationTable = AppData.translationMap.get(Settings.getTranslation());
//			ObservableList<AyahCard> ayahCards = FXCollections.observableArrayList();
			String sql = "SELECT a.number, numberInSurah, a.text as arabic, e.text as translation, b.id as isBookmarked FROM ayahs a join " + translationTable +" e on e.number = a.number left join bookmark b on b.ayahNumber = a.numberInSurah and b.surahNumber = a.surahNumber where a.surahNumber = ? and numberInSurah >= ? and numberInSurah <= ?";
			PreparedStatement  statement = connection.prepareStatement(sql);
			statement.setInt(1, AppData.currentSurahNumber);
			statement.setInt(2, AppData.fromAyahNumber);
			statement.setInt(3, AppData.toAyahNumber);
			
			ResultSet rs = statement.executeQuery();
			Thread bgThread = new Thread(()-> {
				try {
					int toAyahNumber = AppData.fromAyahNumber - 1;
					while (rs.next()) {
						int number = rs.getInt("number");
						int ayahNumber =  rs.getInt("numberInSurah");
						String arabicText = rs.getString("arabic");
						String translatedText = rs.getString("translation");
						boolean isBookmarked = rs.getBoolean("isBookmarked");
						toAyahNumber++;
						Platform.runLater(() -> {
							AyahCard ayah = new AyahCard(number, AppData.currentSurahNumber, ayahNumber, arabicText, translatedText, isBookmarked);
							vbox.getChildren().add(ayah);
						});
					}
					AppData.toAyahNumber = toAyahNumber;
					Platform.runLater(() -> {
						audioContainer.setVisible(true);
						audioContainer.getChildren().clear();
						audioContainer.getChildren().add(
								new AudioPlayer(AppData.currentSurahNumber, AppData.fromAyahNumber, AppData.toAyahNumber)
						);
					});
					rs.close();
					
					Platform.runLater(() -> {
						Pagination pagination = new Pagination(AppData.currentSurahNumber,
																AppData.fromAyahNumber,
																AppData.toAyahNumber,
																AppData.numberOfAyahs);
						vbox.getChildren().add(pagination);
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			bgThread.setDaemon(true);
			bgThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void getSearch(VBox vbox) {
		Connection connection = DbController.getInstance();
		String translationTable = AppData.translationMap.get(Settings.getTranslation());
		try {
			String sql = "SELECT e.number, numberInSurah, a.surahNumber as surahNumber, a.text as arabic, e.text as translation, b.id as isBookmarked FROM ayahs a join "+ translationTable + " e on e.number = a.number left join bookmark b on b.ayahNumber = a.numberInSurah and b.surahNumber = a.surahNumber where arabic Like ? or LOWER(translation) Like ?";
//			System.out.println(sql + " " + translationTable );
			PreparedStatement  statement = connection.prepareStatement(sql);
			statement.setString(1, "%" + AppData.searchedKeyword + "%");
			statement.setString(2, "%" + AppData.searchedKeyword + "%");
			ResultSet rs = statement.executeQuery();
			
			
			Thread bgThread = new Thread(()-> {
				try {
					int totalResult = 0;
					while (rs.next()) {
						int number = rs.getInt("number");
						int surahNumber = rs.getInt("surahNumber");
						int ayahNumber =  rs.getInt("numberInSurah");
						String arabicText = rs.getString("arabic");
						String translatedText = rs.getString("translation");
						boolean isBookmarked = rs.getBoolean("isBookmarked");
						totalResult++;
						
						Platform.runLater(() -> {
							AyahCard ayah = new AyahCard(number, surahNumber, ayahNumber, arabicText, translatedText, isBookmarked);
							vbox.getChildren().add(ayah);
						});
					}
					rs.close();
					if(totalResult == 0) {
						Platform.runLater(() -> {
							Label label = new Label("No results found.");
							label.setPadding(new Insets(10));
							vbox.getChildren().add(label);
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			bgThread.setDaemon(true);
			bgThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
