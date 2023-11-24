package com.project;
import java.util.Deque;
import java.util.LinkedList;

public class Playlist {
    private LinkedList<Song> musicList;
    String playlistName;

    public int getSize(){
        return musicList.size();
    }
    public String getPlaylistName(){
        return playlistName;
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
            String title = musicList.get(i).getTitle();
            titleList[i]=title;
        }
        return titleList;

    }
}
