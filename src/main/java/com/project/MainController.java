package com.project;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

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
    Deque<Song> musicQueue = new LinkedList<>();
    Stack<Song> backStack = new Stack<>();
    MediaPlayer mediaPlayer;
    @FXML
    ListView<Song> musicListDisplay = new ListView<>();
    @FXML
    ListView<Playlist> playlistDisplay = new ListView<>();
    Boolean isRepeat=false;
    ObservableList<Song> observableMusicList = FXCollections.observableList(new ArrayList<>());
    ObservableList<Playlist> observablePlaylist = FXCollections.observableList(new ArrayList<>());
    @FXML
    Slider seekSlider = new Slider();


    public void initialize() {
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
            observableMusicList.add(0, new Song(mediaPlayer.getMedia()));
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

                observableMusicList.add(new Song(media));

                musicQueue.add(new Song(media));
            }

            mediaPlayer = new MediaPlayer(musicQueue.peek().getMedia());
            musicListDisplay.setItems(observableMusicList);
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
    @FXML
    protected void addToPlaylist() {
        Song selectedSong = musicListDisplay.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No song selected.");
            alert.show();
            return;
        }

        Playlist selectedPlaylist = playlistDisplay.getSelectionModel().getSelectedItem();

        if (selectedPlaylist == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No playlist selected.");
            alert.show();
            return;
        }

        selectedPlaylist.addSong(selectedSong);
    }
    @FXML
    protected void createPlaylist() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Playlist");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the title of the playlist:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(title -> {
            Playlist newPlaylist = new Playlist(title);
            observablePlaylist.add(newPlaylist);
            playlistDisplay.setItems(observablePlaylist);
        });

    }
    @FXML
    protected void playlistToQueue() {
        Playlist selectedPlaylist = playlistDisplay.getSelectionModel().getSelectedItem();

        if (selectedPlaylist == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No playlist selected.");
            alert.show();
            return;
        }

        Deque<Song> playlistSongs = new LinkedList<>();
        playlistSongs.addAll(Arrays.asList(selectedPlaylist.getMusicList()));


        clearQueue();
        // Add songs from the playlist to the queue and observable list
        musicQueue.addAll(playlistSongs);
        observableMusicList.addAll(playlistSongs);

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
    @FXML
    protected void clearQueue(){
        musicQueue.clear();
        observableMusicList.clear();
    }

}