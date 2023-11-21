package com.project;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/*
NOT YET IMPLEMENTED
 */
public class Song {
    private Media source;

    public String getTitle(){
        String title = source.getMetadata().get("title").toString();
        return title;
    }
    public Media getMedia(){
        return source;
    }

    public Song(Media media) {
        this.source = media;
    }
}
