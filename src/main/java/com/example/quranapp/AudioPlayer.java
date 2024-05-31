package com.example.quranapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.sql.*;

enum AudioSTATS {
        NOTLOADED,
        PAUSED,
        ENDED,
        PLAYING,
        };
enum Repeat {
    ALL,
    ONE,
    NO
}
public class AudioPlayer extends VBox {
    int surahNumber, from, to, currentNumber;
    MediaPlayer mediaPlayer;
    Media media;
    AudioSTATS status = AudioSTATS.NOTLOADED;
    Repeat repeatStatus = Repeat.NO;
    @FXML
    Button play_btn, prev_btn, next_btn;

    public AudioPlayer(int surahNumber, int from, int to) {
        this.surahNumber = surahNumber;
        this.from = from;
        this.to = to;
        currentNumber = getAyahNumber();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AudioPlayer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(AudioPlayer.this);
        try {
            fxmlLoader.load();
        } catch (Exception e) {
            System.out.println("[Audio player]: "+ e);
            e.printStackTrace();
        }
    }

    void setButtonIcon(Button btn, String url) {
        btn.setGraphic(null);
        Image icon = new Image(String.valueOf(AudioPlayer.class.getResource(url).toExternalForm()));
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        btn.setGraphic(imageView);
    }

    @FXML
    public void initialize() {
        setButtonIcon(play_btn, "audio-play.png");
        play_btn.setOnAction((e)-> {
            playAudio();
        });
    }
    private int getAyahNumber() {
        Connection conn = DbController.getInstance();
        int number = 1;
        try {
            String sql = "SELECT number from ayahs where surahNumber = ? and numberInSurah = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, surahNumber);
            statement.setInt(2, from);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) number = rs.getInt("number");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    private void playAudio() {
        if(status == AudioSTATS.PLAYING) {
            mediaPlayer.pause();
        } else if (status == AudioSTATS.PAUSED) {
            mediaPlayer.play();
        } else {
            System.out.println("Playing");
            media = new Media(String.valueOf(AudioPlayer.class.getResource("audios/"+ currentNumber +".mp3").toExternalForm()));
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);

            mediaPlayer.setOnPlaying(() -> {
                status = AudioSTATS.PLAYING;
                setButtonIcon(play_btn, "pause-48.png");
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                status = AudioSTATS.ENDED;
                setButtonIcon(play_btn, "audio-play.png");
                if(repeatStatus == Repeat.ONE)
                    playAudio();
                else if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
            });
            mediaPlayer.setOnPaused(() -> {
                System.out.println("Pausing");
                status = AudioSTATS.PAUSED;
                setButtonIcon(play_btn, "audio-play.png");
            });
            mediaPlayer.setOn
        }
    }
}
