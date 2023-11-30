package com.project;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Song {
    private final Media source;
    public String title;
    public String artist;

    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }

    public Media getMedia(){
        return source;
    }

    public Song(Media media) {
        this.source = media;
        MediaPlayer player = new MediaPlayer(source);
        player.setOnReady(()-> {
            title = player.getMedia().getMetadata().get("title").toString();
            artist = player.getMedia().getMetadata().get("artist").toString();
        });
    }
    @Override
    public String toString() {
        if (title != null && artist != null) {
            return title + " - " + artist;
        } else {
            return "Loading...";
        }
    }
}
