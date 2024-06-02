package com.example.quranapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {
    static Stage settingStage;


    @FXML
    private ChoiceBox<String> fontsizebox;
    private String[] font ={"Large","Medium","Small"};


    @FXML
    private ChoiceBox<String> translationbox;
    private String[] translation ={"English","Bangla"};
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        fontsizebox.getItems().addAll(font);
        translationbox.getItems().addAll(translation);
    }
}
