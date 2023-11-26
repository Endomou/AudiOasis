package com.project;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class Playlist {
    private LinkedList<Song> musicList = new LinkedList<>();
    private String playlistName;

    public int getSize(){
        return musicList.size();
    }

    public void addSong(Song song){
        musicList.add(song);
    }
    public void removeSong(Song song){
        musicList.remove(song);
    }
    public String[] getTitleArray(){
        String[] titleList = new String[musicList.size()];
        for (int i = 0; i < musicList.size(); i++) {
            String title = musicList.get(i).title;
            titleList[i]=title;
        }
        return titleList;

    }
    public Playlist(String title){
        this.playlistName = title;
    }
    public Song[] getMusicList(){
        Song[] array = new Song[musicList.size()];
        for (int i = 0; i < musicList.size(); i++) {
            array[i]=musicList.get(i);
        }
        return array;
    }

    @Override
    public String toString(){
        return playlistName;
    }


}
