package com.project;
import java.util.Deque;
import java.util.LinkedList;

public class Playlist {
    private LinkedList<Song> musicList;
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

    @Override
    public String toString(){
        return playlistName;
    }
}
