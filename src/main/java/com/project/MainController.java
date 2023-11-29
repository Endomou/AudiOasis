package com.project;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;




public class MainController {
    @FXML
    private Text nowPlaying;
    @FXML
    Button repeatText;
    Deque<Song> musicQueue = new LinkedList<>();
    Stack<Song> backStack = new Stack<>();
    MediaPlayer mediaPlayer;
    @FXML
    ListView<Song> musicListDisplay = new ListView<>();
    @FXML
    ListView<Playlist> playlistDisplay = new ListView<>();
    @FXML
    ImageView albumArtView = new ImageView();
    Boolean isRepeat=false;
    ObservableList<Song> observableMusicList = FXCollections.observableList(new ArrayList<>());
    ObservableList<Playlist> observablePlaylist = FXCollections.observableList(new ArrayList<>());
    @FXML
    Slider seekSlider = new Slider();
    @FXML
    Slider volumeSlider = new Slider();
    double volume = 5;
    @FXML
    Button sortTitleButton;
    @FXML
    Button sortArtistButton;



    public void initialize() {
        playlistDisplay.setItems(observablePlaylist);
        albumArtView.setPreserveRatio(true);
        musicListDisplay.setItems(observableMusicList);
        volumeBinder();
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
            mediaPlayer.setVolume(volume);
            volumeBinder();
            mediaPlayer.setOnEndOfMedia(()->{
                seekSlider.setValue(0);
                nextTrack();
            });
            System.out.println("Debug: Metadata");
            System.out.println(mediaPlayer.getMedia().getMetadata().entrySet());
            Image image = (Image) mediaPlayer.getMedia().getMetadata().get("image");
            albumArtView.setImage(image);

        }
    }

    protected void playMusic(Media media){
        mediaPlayer.dispose();
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
        //add song to back of queue when repeat is enabled
        if(isRepeat){
            musicQueue.offerLast(new Song(mediaPlayer.getMedia()));
            observableMusicList.addLast(new Song(mediaPlayer.getMedia()));
        }
        System.out.println("DEBUG: NEXT TRACK");

        backStack.push(new Song(mediaPlayer.getMedia()));
        musicQueue.poll();
        if(!musicQueue.isEmpty()) {
            playMusic(musicQueue.peek().getMedia());
        }
        observableMusicList.remove(0);
        System.out.println(backStack.toString());
    }
    @FXML
    protected void backTrack(){

        if(!backStack.isEmpty()) {

            musicQueue.offerFirst(new Song(mediaPlayer.getMedia()));

            mediaPlayer.stop();

            playMusic(backStack.pop().getMedia());
            observableMusicList.add(0, new Song(mediaPlayer.getMedia()));
        }
        System.out.println(backStack.toString());
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
                addSongToQueue(new Song(media));
            }
            mediaPlayer = new MediaPlayer(musicQueue.peek().getMedia());
        }
    }

    @FXML
    protected void addSong(){
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.aac", "*.m4a");
        fileChooser.getExtensionFilters().add(fileExtensions);

        File song = fileChooser.showOpenDialog(null);
        if(song!=null){
            addSongToQueue(new Song(new Media(song.toURI().toString())));
        }
    }

    //this method adds to both the musicQueue and the listView
    protected void addSongToQueue(Song song){
        musicQueue.add(song);
        observableMusicList.add(song);
    }
    @FXML
    protected void removeSong() {
        Song selectedSong = musicListDisplay.getSelectionModel().getSelectedItem();

        if (selectedSong == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No song selected.");
            alert.show();
            return;
        }

        // Remove the selected song from the queue and observable list
        musicQueue.remove(selectedSong);
        observableMusicList.remove(selectedSong);

        // If the removed song is currently playing, stop the media player
        if (mediaPlayer != null && selectedSong.equals(mediaPlayer.getMedia())) {
            mediaPlayer.stop();
            backStack.clear(); // Clear the back stack as it's no longer valid
            nowPlaying.setText("");
            albumArtView.setImage(null);
        }

        // Update the musicListDisplay
        musicListDisplay.setItems(observableMusicList);
    }
    @FXML
    protected void deletePlaylist() {
        Playlist selectedPlaylist = playlistDisplay.getSelectionModel().getSelectedItem();

        if (selectedPlaylist == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No playlist selected.");
            alert.show();
            return;
        }
        // Remove the selected playlist from the observable list
        observablePlaylist.remove(selectedPlaylist);

        // Update the playlistDisplay
        playlistDisplay.setItems(observablePlaylist);
    }


    @FXML
    protected void sortByTitle() {
        if(musicQueue.isEmpty()||musicQueue.size()==1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            if(musicQueue.size()!=0){
                alert.setContentText("Cannot sort only one song");
            }
            else {
                alert.setContentText("No songs currently in the queue");
            }
            alert.show();
            return;
        }
        sortArtistButton.setText("Sort Queue By Artist");

        Song[] songsArray = observableMusicList.toArray(new Song[0]);
        MergeSort mergeSort = new MergeSort();

        if(sortTitleButton.getText().contains("^")){
            mergeSort.descendMerge(songsArray, Comparator.comparing(Song::getTitle));
            sortTitleButton.setText("Sort Queue By Title v");
        }
        else{
            mergeSort.ascendMerge(songsArray, Comparator.comparing(Song::getTitle));
            sortTitleButton.setText("Sort Queue By Title ^");
        }

        clearQueue();
        musicQueue.addAll(List.of(songsArray));
        observableMusicList.setAll(songsArray);
        musicListDisplay.setItems(observableMusicList);
        playMusic(musicQueue.peek().getMedia());
    }


    @FXML
    protected void sortByArtist() {
        if(musicQueue.isEmpty()||musicQueue.size()==1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            if(musicQueue.size()!=0){
                alert.setContentText("Cannot sort only one song");
            }
            else {
                alert.setContentText("No songs currently in the queue");
            }

            alert.show();
            return;
        }
        sortTitleButton.setText("Sort Queue By Title");
        Song[] songsArray = observableMusicList.toArray(new Song[0]);
        MergeSort mergeSort = new MergeSort();

        if(sortArtistButton.getText().contains("^")){
            mergeSort.descendMerge(songsArray, Comparator.comparing(Song::getArtist));
            sortArtistButton.setText("Sort Queue By Title v");
        }
        else{
            mergeSort.ascendMerge(songsArray, Comparator.comparing(Song::getArtist));
            sortArtistButton.setText("Sort Queue By Title ^");
        }

        clearQueue();
        musicQueue.addAll(List.of(songsArray));
        observableMusicList.setAll(songsArray);
        musicListDisplay.setItems(observableMusicList);
        playMusic(musicQueue.peek().getMedia());

    }
    @FXML
    protected void setRepeat(){
        if(isRepeat){
            isRepeat=false;
            repeatText.setText("Repeat: Disabled");
        }
        else{
            isRepeat=true;
            repeatText.setText("Repeat: Enabled");
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
        if(playlistSongs.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No songs in the playlist");
            alert.show();
            return;
        }
        clearQueue();
        // Add songs from the playlist to the queue and observable list

        musicQueue.addAll(playlistSongs);
        observableMusicList.addAll(playlistSongs);
        playMusic(musicQueue.peek().getMedia());
    }



    protected void seekBinder() {
        if (mediaPlayer == null || mediaPlayer.getTotalDuration() == null) {
            return;
        }
        ReadOnlyObjectProperty<Duration> duration = mediaPlayer.totalDurationProperty();


        //Set max below 100% to prevent the triggering the autoplay feature
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
    protected void volumeBinder(){

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!seekSlider.isValueChanging()){
                volume = newValue.doubleValue();
                if(mediaPlayer!=null) {
                    mediaPlayer.setVolume(volume);
                }
            }

        });
    }

    @FXML
    protected void shuffleQueue() {
        if (musicQueue.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Queue is empty.");
            alert.show();
            return;
        }

        List<Song> shuffledList = new ArrayList<>(musicQueue);
        Collections.shuffle(shuffledList);

        // Clear existing queue and observable list
        clearQueue();

        // Add shuffled songs to the queue and observable list
        musicQueue.addAll(shuffledList);
        observableMusicList.addAll(shuffledList);

        // Update the musicListDisplay
        musicListDisplay.setItems(observableMusicList);

        playMusic(musicQueue.peek().getMedia());
    }
    @FXML
    protected void clearQueue(){
        musicQueue.clear();
        observableMusicList.clear();
    }

    protected Boolean isAudio(String file){
        return file.contains(".mp3") || file.contains(".wav") || file.contains(".flac") || file.contains(".aac") || file.contains("m4a");
    }

}