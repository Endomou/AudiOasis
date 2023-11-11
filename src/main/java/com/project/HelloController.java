package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Queue;

public class HelloController {
    @FXML
    private Label welcomeText;

    Queue musicQueue = new LinkedList<File>();

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void playMusic() throws MalformedURLException {
        File file = new File("src/main/resources/assets/test.mp3");
        System.out.println(file.toURI().toString());
        System.out.println(file.exists());
//        Media media = new Media(file.toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();
    }
}