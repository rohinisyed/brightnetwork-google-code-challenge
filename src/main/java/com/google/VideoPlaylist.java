package com.google;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to represent a Playlist
 */
class VideoPlaylist {

    private final String name;
    private final List<String> videos;

    VideoPlaylist(String name) {
        this.name = name;
        videos = new ArrayList<>();
    }

    /***
     * Get Playlist Name
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /***
     * Get all Videos in Playlist
     * @return videos
     */
    public List<String> getVideos() {
        return this.videos;
    }

    /***
     * Checks if playlist contains a given video
     * @param videoId
     * @return true if video there, false if not
     */
    public boolean containsVideo(String videoId){
        return videos.contains(videoId);
    }

    /***
     * Adds video to playlist
     * @param videoId
     */
    public void addVideo(String videoId){
        this.videos.add(videoId);
    }

    /***
     * Removes video from playlist
     * @param videoId
     */
    public void removeVideo(String videoId){
        this.videos.remove(videoId);
    }

    public void clearPlaylist(){
        this.videos.clear();
    }

}
