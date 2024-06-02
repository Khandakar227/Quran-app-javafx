package com.example.quranapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Settings extends AnchorPane {
    static Stage settingsStage;
    public static String translationText, fontSizeText;
    @FXML
    ChoiceBox<String> translation;
    @FXML
    ChoiceBox<String> font_size;
    @FXML
    Button save;

    Settings() {
        String createSettingsTable = "CREATE TABLE IF NOT EXISTS settings id INTEGER PRIMARY KEY AUTOINCREMENT, translation TEXT NOT NULL, fontSize TEXT NOT NULL CHECK (fontSize  IN ('Small', 'Medium', 'Large')))";
        if (settingsStage != null) {
            settingsStage.close();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Settings.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            settingsStage = new Stage();
            settingsStage.setScene(scene);
            settingsStage.getIcons().add(new Image(String.valueOf(getClass().getResource("App-icon.png"))));
            settingsStage.show();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize() {
        translation.getItems().addAll("English", "Bangla");
        font_size.getItems().addAll("Small", "Medium", "Large");
        initSettingsData();
        translation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            translationText = newValue;
        });
        font_size.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fontSizeText = newValue;
        });
        save.setOnAction(ev -> {
            try {
                Connection connection = DbController.getInstance();
                String sql = "UPDATE settings SET translation = ? , fontSize = ? WHERE id = 1";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, translationText);
                statement.setString(2, fontSizeText);
                statement.executeUpdate();
                save.setText("Saved");
                if(AppData.currentPage.equals("Surah"))
                    NavigationController.goToSurah(AppData.currentSurahNumber, AppData.fromAyahNumber,AppData.toAyahNumber, AppData.numberOfAyahs);
//                else if(AppData.currentPage.equals("Home"))
//                    NavigationController.goToHome();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void initSettingsData() {
        try {
            Connection connection = DbController.getInstance();
            String sql = "SELECT * FROM settings WHERE id = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                fontSizeText = resultSet.getString("fontSize");
                translationText = resultSet.getString("translation");
            }
            resultSet.close();
            translation.setValue(translationText);
            font_size.setValue(fontSizeText);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    static void getSettingsData() {
        try {
            Connection connection = DbController.getInstance();
            String sql = "SELECT * FROM settings WHERE id = 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                fontSizeText = resultSet.getString("fontSize");
                translationText = resultSet.getString("translation");
            }
            resultSet.close();
           } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getTranslation() {
        if (translationText == null) {
            getSettingsData();
        }
        return translationText;
    }
    public static String getFontSize() {
        if (fontSizeText == null) {
            getSettingsData();
        }
        return fontSizeText;
    }
}
