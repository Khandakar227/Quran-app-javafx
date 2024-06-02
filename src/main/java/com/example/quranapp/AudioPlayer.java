package com.example.quranapp;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
public class AudioPlayer extends VBox implements EventHandler {
    public static int surahNumber, from, to, currentNumber, fromAyahNumber;
    public static MediaPlayer mediaPlayer;
    Media media;
    AudioSTATS status = AudioSTATS.NOTLOADED;
    Repeat repeatStatus = Repeat.NO;
    boolean isUserAction = false;
    @FXML
    Button play_btn, prev_btn, next_btn, repeat_btn;
    @FXML
    Slider audio_slider;
    @FXML
    Label current_ayah, current_time, end_time;

    public AudioPlayer(int surahNumber, int from, int to) {
        this.surahNumber = surahNumber;
        this.from = from;
        this.to = to;
        fromAyahNumber = getAyahNumber();
        currentNumber = fromAyahNumber;
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
    void setButtonIcon(Button btn, String url, int size) {
        btn.setGraphic(null);
        Image icon = new Image(String.valueOf(AudioPlayer.class.getResource(url).toExternalForm()));
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);
        btn.setGraphic(imageView);
    }
    @FXML
    public void initialize() {
        media = new Media(String.valueOf(AudioPlayer.class.getResource("audios/"+ currentNumber +".mp3").toExternalForm()));
        mediaPlayer = new MediaPlayer(media);

        setButtonIcon(play_btn, "audio-play.png");

        current_ayah.setText(surahNumber + ":" + from);
        play_btn.setOnAction((e)-> {
            playAudio();
        });

        if(repeatStatus == Repeat.NO) setButtonIcon(repeat_btn, "no-repeat-48.png", 20);
        else if (repeatStatus == Repeat.ONE) setButtonIcon(repeat_btn, "repeat-one-48.png", 20);
        else setButtonIcon(repeat_btn, "repeat-48.png", 20);

        repeat_btn.setOnAction((e) -> {
            repeatStatus = repeatStatus == Repeat.NO ? Repeat.ONE : repeatStatus == Repeat.ONE ? Repeat.ALL : Repeat.NO;
            if(repeatStatus == Repeat.NO) setButtonIcon(repeat_btn, "no-repeat-48.png", 20);
            else if (repeatStatus == Repeat.ONE) setButtonIcon(repeat_btn, "repeat-one-48.png", 20);
            else setButtonIcon(repeat_btn, "repeat-48.png", 20);
        });

        prev_btn.setOnAction((e) -> {
            currentNumber = currentNumber <= fromAyahNumber ? currentNumber : (currentNumber - 1);
            System.out.println(currentNumber);
            try {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    status = AudioSTATS.ENDED;
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            playAudio();
            current_ayah.setText(surahNumber + ":" + (currentNumber - fromAyahNumber + 1));
        });

        next_btn.setOnAction((e) -> {
            currentNumber = (currentNumber + 1) >= fromAyahNumber + (to - from) ? currentNumber : (currentNumber + 1);
            try {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                    status = AudioSTATS.ENDED;
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            playAudio();
            current_ayah.setText(surahNumber + ":" + (currentNumber - fromAyahNumber + 1));
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
        current_ayah.setText(surahNumber + ":" + (from + (currentNumber - fromAyahNumber)));
        if(status == AudioSTATS.PLAYING) {
            mediaPlayer.pause();
        } else if (status == AudioSTATS.PAUSED) {
            mediaPlayer.play();
        } else {
            System.out.println("Playing");
            media = new Media(String.valueOf(AudioPlayer.class.getResource("audios/"+ currentNumber +".mp3").toExternalForm()));
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);

            mediaPlayer.setOnReady(() -> {
                audio_slider.setMin(0);
                audio_slider.setValue(0);
                int totalDuration = (int) mediaPlayer.getTotalDuration().toSeconds();
                audio_slider.setMax(totalDuration);

                end_time.setText(String.format("%02d",totalDuration/60) + ":" + String.format("%02d",totalDuration%60));
            });
            mediaPlayer.setOnPlaying(() -> {
                status = AudioSTATS.PLAYING;
                setButtonIcon(play_btn, "pause-48.png");
//                System.out.println(mediaPlayer.getCurrentTime().toSeconds());
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                status = AudioSTATS.ENDED;
                setButtonIcon(play_btn, "audio-play.png");
                if(repeatStatus == Repeat.ONE)
                    playAudio();
                else if(repeatStatus == Repeat.ALL) {
                    currentNumber = (currentNumber + 1) >= fromAyahNumber + (to - from) ? fromAyahNumber : (currentNumber + 1);
                    playAudio();
                } else if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                }
            });
            mediaPlayer.setOnPaused(() -> {
                System.out.println("Pausing");
                status = AudioSTATS.PAUSED;
                setButtonIcon(play_btn, "audio-play.png");
            });
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                if (!audio_slider.isValueChanging()) {
                    audio_slider.setValue(newValue.toSeconds());
                    current_time.setText(String.format("%02d",(int)newValue.toSeconds()/60) + ":" + String.format("%02d",(int)(newValue.toSeconds()%60)));
                }
            });
        }
    }

    public static void stopAudio() {
        if(mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = null;
    }
    @Override
    public void handle(Event event) {
        System.out.println(event.getEventType());
    }
}
