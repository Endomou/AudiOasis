package com.project;

import com.gluonhq.charm.glisten.control.CharmListView;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/* Features to implement:
    - ListView of Queue
    - Sorting algorithms for queue
    - Shuffled Playing

Currently Being implemented but not working:
    - Repeat feature
    - Automatic next track after current song is finished
 */
public class MainController {
    @FXML
    private Text nowPlaying;
    Deque<Media> musicQueue = new LinkedList<>();
    Stack<Media> backStack = new Stack<>();
    MediaPlayer mediaPlayer;
    Boolean isPlaying=false;
    @FXML
    CharmListView<String,String> musicList;

    Boolean isRepeat=false;

    public void initialize(){

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

        mediaPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == MediaPlayer.Status.STOPPED) {
                nextTrack();
            }
        });

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
    @FXML
    protected void addFolder() {
        DirectoryChooser directory = new DirectoryChooser();
        File selectedFolder = directory.showDialog(new Stage());
        if(selectedFolder!=null){
        File[] listOfFiles = selectedFolder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            Media media = new Media( listOfFiles[i].toURI().toString());
            musicQueue.add(media);
        }
        mediaPlayer = new MediaPlayer(musicQueue.peek());
        }
    }

    @FXML
    protected void setRepeat(){
        if(isRepeat){
            isRepeat=false;
        }
        else{
            isRepeat=true;
        }

    }

}