package com.project;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
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
    Deque<Song> musicQueue = new LinkedList<>();
    Stack<Song> backStack = new Stack<>();
    MediaPlayer mediaPlayer;
    @FXML
    ListView<String> musicListDisplay = new ListView<>();
    @FXML
    ListView<String> playlistDisplay = new ListView<>();
    Boolean isRepeat=false;
    ObservableList<String> observableMusicList = FXCollections.observableList(new ArrayList<>());
    ObservableList<String> observablePlaylist = FXCollections.observableList(new ArrayList<>());
    @FXML
    Slider seekSlider = new Slider();

    public void initialize() {
        musicListDisplay.setItems(observableMusicList);
        playlistDisplay.setItems(observablePlaylist);


    }

    @FXML
    protected void playMusic(){
        if(musicQueue.isEmpty()||mediaPlayer==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No Songs in the Queue");
            alert.show();
            return;
        }
        MediaPlayer.Status status = mediaPlayer.getStatus();
        if(status == MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
        }

        else{
            mediaPlayer.play();
            String title = mediaPlayer.getMedia().getMetadata().get("title").toString();
            seekBinder();
            System.out.println("Debug Title: "+title);
            nowPlaying.setText(title);
            mediaPlayer.setOnEndOfMedia(()->{
                seekSlider.setValue(0);
                nextTrack();

            });




        }

    }

    protected void playMusic(Media media){
        mediaPlayer = new MediaPlayer(media);
        playMusic();
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
        mediaPlayer.dispose();
        System.out.println("DEBUG: NEXT TRACK");
        backStack.push(musicQueue.poll());
        if(!musicQueue.isEmpty()) {
            playMusic(musicQueue.peek().getMedia());
        }
        observableMusicList.remove(0);

    }
    @FXML
    protected void backTrack(){
        if(!backStack.isEmpty()) {
            musicQueue.offerFirst(new Song(mediaPlayer.getMedia()));

            mediaPlayer.stop();
            playMusic(backStack.pop().getMedia());
            String title= mediaPlayer.getMedia().getMetadata().get("title").toString();
            observableMusicList.add(0, title);
        }
}
    @FXML
    protected void addFolder() {
        DirectoryChooser directory = new DirectoryChooser();
        File selectedFolder = directory.showDialog(null);

        if(selectedFolder!=null) {
            File[] listOfFiles = selectedFolder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if(!isAudio(listOfFiles[i].getName())){
                    continue;
                }
                Media media = new Media(listOfFiles[i].toURI().toString());

                MediaPlayer mediaPlayer1 = new MediaPlayer(media);
                mediaPlayer1.setOnReady(() -> {
                    String title = mediaPlayer1.getMedia().getMetadata().get("title").toString();
                    observableMusicList.add(title);

                });

                musicQueue.add(new Song(media));
            }


            mediaPlayer = new MediaPlayer(musicQueue.peek().getMedia());

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

    protected void seekBinder() {
        ReadOnlyObjectProperty<Duration> duration = mediaPlayer.totalDurationProperty();

        // Set max value of slider to 100 (percentage)
        seekSlider.setMax(99.5);

        // Add listener to update media player time based on percentage
        seekSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (seekSlider.isValueChanging()) {
                double percentage = newValue.doubleValue();
                double totalDuration = duration.get().toSeconds();
                double newTime = (percentage / 100) * totalDuration;
                mediaPlayer.seek(Duration.seconds(newTime));
            }
        });

        // Add listener to update slider based on media player time
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!seekSlider.isValueChanging()) {
                double totalDuration = duration.get().toSeconds();
                double currentTime = newValue.toSeconds();
                double percentage = (currentTime / totalDuration) * 100;
                seekSlider.setValue(percentage);
            }
        });
    }




}