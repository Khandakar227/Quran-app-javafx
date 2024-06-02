package com.example.quranapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Bookmark extends AnchorPane {
    static Stage bookmarkStage;
    @FXML
    ScrollPane container;
    @FXML
    AnchorPane anchor;

    public void initialize(){
        getBookedAyahs();
    }
    public Bookmark(){
        if (bookmarkStage != null) {
            bookmarkStage.close();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Bookmark.fxml"));
        fxmlLoader.setController(this);
        try{
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            bookmarkStage = new Stage();
            bookmarkStage.setScene(scene);
            bookmarkStage.show();

        }catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    public void getBookedAyahs(){
        Connection connection = DbController.getInstance();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        container.setContent(vbox);
        container.setFitToWidth(true);

        try{
            String sql = "SELECT a.number, b.ayahNumber,b.surahNumber,a.text AS arabic,e.text AS translation,b.id AS isBookmarked FROM BOOKMARK b \n" +
                    " JOIN ayahs a ON a.numberInSurah = b.ayahNumber and a.surahNumber = b.surahNumber\n" +
                    " JOIN en_ayahs e ON e.number = a.number;";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                int number = resultSet.getInt("number");
                int ayahNumber = resultSet.getInt("ayahNumber");
                String arabicText = resultSet.getString("arabic");
                String translatedText = resultSet.getString("translation");
                boolean isBookmarked = resultSet.getBoolean("isBookmarked");
                int surahNumber = resultSet.getInt("surahNumber");
                Platform.runLater(() -> {
                    AyahCard ayah = new AyahCard(number, surahNumber, ayahNumber, arabicText, translatedText, isBookmarked);
                    vbox.getChildren().add(ayah);
                });
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
