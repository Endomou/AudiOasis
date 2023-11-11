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
        File folder = new File("src/main/resources/assets/");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            Media media = new Media( listOfFiles[i].toURI().toString());
            musicQueue.add(media);
        }
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
    @FXML
    protected void backTrack(){
        if(!backStack.isEmpty()) {
            if(mediaPlayer!=null) {
                musicQueue.add(mediaPlayer.getMedia());
            }
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(backStack.pop());
            mediaPlayer.play();
        }
    }
}