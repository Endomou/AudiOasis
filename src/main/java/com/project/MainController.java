package com.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.collections.ListChangeListener;

import javafx.stage.Stage;
import java.io.File;
import java.util.*;

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
    ListView<String> musicListDisplay = new ListView<>();
    Boolean isRepeat=false;
    ObservableList<String> observableMusicList = FXCollections.observableList(new ArrayList<>());

    public void initialize() {
        // Bind the ObservableList to the ListView
        musicListDisplay.setItems(observableMusicList);
        observableMusicList.add("Asdadasd");
    }

    @FXML
    protected void playMusic(){
        if(musicQueue.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No Songs in the Queue");
            alert.show();
            return;
        }

        if(isPlaying){
            mediaPlayer.pause();
            isPlaying=false;
        }

        else{
            mediaPlayer.play();
            String title = mediaPlayer.getMedia().getMetadata().get("title").toString();
            System.out.println(title);
            nowPlaying.setText(title);
            isPlaying=true;
            mediaPlayer.setOnEndOfMedia(this::nextTrack);

        }

    }

    protected void playMusic(Media media){
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        isPlaying=true;
        String title = mediaPlayer.getMedia().getMetadata().get("title").toString();
        nowPlaying.setText(title);
        mediaPlayer.setOnEndOfMedia(this::nextTrack);
    }

    @FXML
    protected void nextTrack(){
        if(mediaPlayer==null||musicQueue.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No Songs in the Queue");
            alert.show();
            return;
        }
        mediaPlayer.stop();
        backStack.push(musicQueue.poll());
        playMusic(musicQueue.peek());

    }
    @FXML
    protected void backTrack(){
        if(!backStack.isEmpty()) {
            musicQueue.offerFirst(mediaPlayer.getMedia());
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(backStack.pop());

            mediaPlayer.play();
            isPlaying=true;

            String title= mediaPlayer.getMedia().getMetadata().get("title").toString();
            nowPlaying.setText(title);
        }
}
    @FXML
    protected void addFolder() {
        DirectoryChooser directory = new DirectoryChooser();
        File selectedFolder = directory.showDialog(new Stage());

        if(selectedFolder!=null) {
            File[] listOfFiles = selectedFolder.listFiles();
            ObservableList<String> list = FXCollections.observableList(new ArrayList<>());
            for (int i = 0; i < listOfFiles.length; i++) {
                if(!isAudio(listOfFiles[i].getName())){
                    continue;
                }
                Media media = new Media(listOfFiles[i].toURI().toString());
                MediaPlayer mediaPlayer1 = new MediaPlayer(media);
                mediaPlayer1.setOnReady(() -> {
                    String title = mediaPlayer1.getMedia().getMetadata().get("title").toString();
                    list.add(title);

                });

                musicQueue.add(media);
            }

            mediaPlayer = new MediaPlayer(musicQueue.peek());



            // Set the ObservableList to the musicListDisplay
            musicListDisplay.setItems(list);
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

    protected Boolean isAudio(String file){
        return file.contains(".mp3") || file.contains(".wav") || file.contains(".flac");
    }
}