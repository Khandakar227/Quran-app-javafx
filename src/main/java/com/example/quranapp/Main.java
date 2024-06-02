package com.example.quranapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            AppData.translationMap.put("English", "en_ayahs");
            AppData.translationMap.put("Bangla", "bn_ayahs");

            //Font.loadFont(getClass().getResourceAsStream("resources/UthmanicScriptHAFS.otf"), 24);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root,500,700);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            RootPaneController rootPaneController = loader.getController();
            rootPaneController.setScene(scene);

            // Set application icon
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("App-icon.png"))));

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

