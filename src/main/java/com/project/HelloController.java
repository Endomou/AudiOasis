package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class HelloController {
    @FXML
    private Text nowPlaying;
    Deque<Media> musicQueue = new LinkedList<>();
    Stack<Media> backStack = new Stack<>();
    MediaPlayer mediaPlayer;
    Boolean isPlaying=false;

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
        if(isPlaying){
            mediaPlayer.pause();
            isPlaying=false;
        }

        else{
        mediaPlayer.play();
        String title = mediaPlayer.getMedia().getMetadata().get("title").toString();
        nowPlaying.setText(title);
        isPlaying=true;
        }
    }
    @FXML
    protected void nextTrack(){
        mediaPlayer.stop();
        backStack.push(musicQueue.peek());
        musicQueue.poll();

        if(!musicQueue.isEmpty()){
            mediaPlayer=new MediaPlayer(musicQueue.peek());
            mediaPlayer.play();
            isPlaying=true;
            String title= mediaPlayer.getMedia().getMetadata().get("title").toString();
            nowPlaying.setText(title);


        }
    }
    @FXML
    protected void backTrack(){
        if(!backStack.isEmpty()) {
            musicQueue.offerFirst(mediaPlayer.getMedia());
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(backStack.pop());

            mediaPlayer.play();
            isPlaying=true;
            Media currentPlaying = mediaPlayer.getMedia();
            String title= mediaPlayer.getMedia().getMetadata().get("title").toString();
            nowPlaying.setText(title);
        }

    }
}