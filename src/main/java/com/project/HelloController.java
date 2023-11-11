package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class HelloController {
    @FXML
    private Label welcomeText;
    Queue<Media> musicQueue = new LinkedList<>();
    Stack<Media> backStack = new Stack<>();
    MediaPlayer mediaPlayer;

    public void initialize(){
        musicQueue.add(new Media(new File("src/main/resources/assets/test.mp3").toURI().toString()));
        musicQueue.add(new Media(new File("src/main/resources/assets/test2.mp3").toURI().toString()));
        mediaPlayer = new MediaPlayer(musicQueue.peek());
    }


    @FXML
    protected void playMusic(){
        mediaPlayer.play();
    }
    @FXML
    protected void pauseMusic() {
        mediaPlayer.pause();
    }
    @FXML
    protected void nextTrack(){
        mediaPlayer.stop();
        backStack.push(musicQueue.peek());
        musicQueue.poll();

        if(!musicQueue.isEmpty()){
            mediaPlayer=new MediaPlayer(musicQueue.peek());
            mediaPlayer.play();

        }
    }

    protected void backTrack(){
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(backStack.pop());
        mediaPlayer.play();
    }
}