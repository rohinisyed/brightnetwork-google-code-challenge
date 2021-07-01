package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

    private final VideoLibrary videoLibrary;
    private Video currentlyPlayingVideo = null;
    private boolean isVideoPaused = false;

    private HashMap<String, VideoPlaylist> playlists;

    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
        this.playlists = new HashMap<>();
    }

    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void showAllVideos() {
        System.out.println("Here's a list of all available videos:");
        //Steam All Videos, get Title and Sort it then print Formatted Text
        videoLibrary.getVideos()
                .stream()
                .sorted(Comparator.comparing(Video::getTitle))
                .forEach(video -> System.out.printf("%s (%s) [%s] %s%n", video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()), video.isVideoFlagged() ? "- FLAGGED (reason: " + video.getFlag() + ")" : ""));
    }

    public void playVideo(String videoId) {
        Video video = videoLibrary.getVideo(videoId);
        //Check if the videoId is Present in videoLibrary
        if (video != null) {
            //Check if Any video is currently playing
            if (isAVideoPlaying()) {
                stopCurrentlyPlayingVideo();
            }
            if (video.isVideoFlagged()) {
                System.out.println("Cannot play video: Video is currently flagged (reason: " + video.getFlag() + ")");
                return;
            }
            System.out.println("Playing video: " + video.getTitle());
            currentlyPlayingVideo = video;
            isVideoPaused = false;
        } else {
            System.out.println("Cannot play video: Video does not exist");
        }
    }

    public void stopVideo() {
        // If Video is playing stop the video otherwise display nothing is playing
        if (isAVideoPlaying()) {
            stopCurrentlyPlayingVideo();
        } else {
            System.out.println("Cannot stop video: No video is currently playing");
        }
    }

    public void playRandomVideo() {
        // Check if any non flagged vids are available
        List<Video> videos = videoLibrary.getVideos();

        int availableVideoCount = videos.stream().filter(video -> !video.isVideoFlagged()).collect(Collectors.toList()).size();

        if(availableVideoCount == 0){
            System.out.println("No videos available");
            return;
        }


        // If a Video is playing, stop the video
        if (isAVideoPlaying()) {
            stopCurrentlyPlayingVideo();
        }
        // Get a Random Video and play it

        Video randomVideo = (Video) videos.get(new Random().nextInt(videos.size()));
        isVideoPaused = false;
        System.out.println("Playing video: " + randomVideo.getTitle());
    }

    public void pauseVideo() {
        // If a video is playing, pause it else print nothing is playing
        if (isAVideoPlaying()) {
            pauseCurrentlyPlayingVideo();
        } else {
            System.out.println("Cannot pause video: No video is currently playing");
        }
    }

    public void continueVideo() {
        // If a video is playing continue it else print nothing is playing
        if (isAVideoPlaying()) {
            continueCurrentlyPausedVideo();
        } else {
            System.out.println("Cannot continue video: No video is currently playing");
        }
    }

    public void showPlaying() {
        if (isAVideoPlaying()) {
            System.out.printf("Currently playing: %s (%s) [%s]", currentlyPlayingVideo.getTitle(), currentlyPlayingVideo.getVideoId(), String.join(" ", currentlyPlayingVideo.getTags()));
            if (isVideoPaused) {
                System.out.print(" - PAUSED");
            }
            System.out.println();
        } else {
            System.out.println("No video is currently playing");
        }

    }

    public void createPlaylist(String playlistName) {
        if (this.playlists.containsKey(playlistName.toLowerCase())) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        } else {
            System.out.println("Successfully created new playlist: " + playlistName);
            playlists.put(playlistName.toLowerCase(), new VideoPlaylist(playlistName));
        }
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        //Check if Playlist exists
        VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());

        if (playlist == null) {
            System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
        }

        //Check if Video Exists
        Video video = videoLibrary.getVideo(videoId);
        if (playlist != null && video == null) {
            System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
        }

        //If Playlist and video exists
        if (playlist != null && video != null) {
            if (playlist.containsVideo(videoId)) {
                System.out.println("Cannot add video to " + playlistName + ": Video already added");
            } else {
                if (video.isVideoFlagged()) {
                    System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + video.getFlag() + ")");
                    return;
                }
                System.out.println("Added video to " + playlistName + ": " + video.getTitle());
                playlist.addVideo(videoId);
            }
        }
    }

    public void showAllPlaylists() {
        if (playlists.size() == 0) {
            System.out.println("No playlists exist yet");
        } else {
            System.out.println("Showing all playlists:");
            playlists.values().forEach(playlist -> System.out.println(playlist.getName()));
        }
    }

    public void showPlaylist(String playlistName) {
        VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());
        if (playlist == null) {
            System.out.println("Cannot show playlist " + playlistName.toLowerCase() + ": Playlist does not exist");
        } else {
            System.out.println("Showing playlist: " + playlistName);
            List<String> playlistVideos = playlist.getVideos();
            if (playlistVideos.size() == 0) {
                System.out.println("No videos here yet");
            } else {
                playlistVideos.forEach(videoId -> {
                    Video video = videoLibrary.getVideo(videoId);
                    System.out.printf("%s (%s) [%s] %s%n", video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()),video.isVideoFlagged()?"- FLAGGED (reason: "+video.getFlag()+")":"");
                });
            }
        }
    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());
        if (playlist == null) {
            System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
        }
        Video video = videoLibrary.getVideo(videoId);
        if (playlist != null && video == null) {
            System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
        }

        if (video != null && playlist != null) {
            if (!playlist.containsVideo(videoId)) {
                System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
            } else {
                playlist.removeVideo(videoId);
                System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
            }
        }
    }

    public void clearPlaylist(String playlistName) {
        VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());

        if (playlist == null) {
            System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
        } else {
            System.out.println("Successfully removed all videos from " + playlistName);
            playlist.clearPlaylist();
        }
    }

    public void deletePlaylist(String playlistName) {
        VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());
        if (playlist == null) {
            System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
        } else {
            System.out.println("Deleted playlist: " + playlistName);
            playlists.remove(playlistName.toLowerCase());
        }
    }

    public void searchVideos(String searchTerm) {
        List<Video> videos = videoLibrary.getVideos();
        boolean searchResultsFound = false;
        List<Video> searchResults = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            Video currentVideo = videos.get(i);
            if (currentVideo.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) && !currentVideo.isVideoFlagged()) {
                searchResults.add(currentVideo);
                searchResultsFound = true;
            }
        }
        if (!searchResultsFound) {
            System.out.println("No search results for " + searchTerm);
        } else {
            processSearch(searchTerm, searchResults);
        }
    }

    public void searchVideosWithTag(String videoTag) {
        if (!videoTag.contains("#")) {
            System.out.println("No search results for " + videoTag);
        }

        List<Video> videos = videoLibrary.getVideos();
        boolean searchResultsFound = false;
        List<Video> searchResults = new ArrayList<>();
        for (int i = 0; i < videos.size(); i++) {
            Video currentVideo = videos.get(i);
            if (currentVideo.getTags().stream().anyMatch(videoTag.toLowerCase()::equalsIgnoreCase) && !currentVideo.isVideoFlagged()) {
                searchResults.add(currentVideo);
                searchResultsFound = true;
            }
        }
        if (!searchResultsFound) {
            System.out.println("No search results for " + videoTag);
        } else {
            processSearch(videoTag, searchResults);
        }
    }

    public void flagVideo(String videoId) {
        Video video = videoLibrary.getVideo(videoId);
        if (video == null) {
            System.out.println("Cannot flag video: Video does not exist");
        } else {
            if (!video.isVideoFlagged()) {
                video.addFlag("Not supplied");
                if(currentlyPlayingVideo!=null && currentlyPlayingVideo.getVideoId().equals(videoId)){
                    System.out.println("Stopping video: "+currentlyPlayingVideo.getTitle());
                    currentlyPlayingVideo = null;
                }
                System.out.println("Successfully flagged video: " + video.getTitle() + " (reason: Not supplied)");
            } else {
                System.out.println("Cannot flag video: Video is already flagged");
            }
        }
    }

    public void flagVideo(String videoId, String reason) {
        Video video = videoLibrary.getVideo(videoId);
        if (video == null) {
            System.out.println("Cannot flag video: Video does not exist");
        } else {
            if (!video.isVideoFlagged()) {
                video.addFlag(reason);
                if(currentlyPlayingVideo!=null && currentlyPlayingVideo.getVideoId().equals(videoId)){
                    System.out.println("Stopping video: "+currentlyPlayingVideo.getTitle());
                    currentlyPlayingVideo = null;
                }
                System.out.println("Successfully flagged video: " + video.getTitle() + " (reason: " + reason + ")");
            } else {
                System.out.println("Cannot flag video: Video is already flagged");
            }
        }
    }

    public void allowVideo(String videoId) {
        Video video = videoLibrary.getVideo(videoId);
        if(video == null){
            System.out.println("Cannot remove flag from video: Video does not exist");
        }else{
            if(!video.isVideoFlagged()){
                System.out.println("Cannot remove flag from video: Video is not flagged");
            }else{
                video.removeFlag();
                System.out.println("Successfully removed flag from video: "+video.getTitle());
            }
        }
    }

    /***
     * Checks if a Video is Playing and returns a boolean
     *
     * @return true if video is playing, false if it is not
     */
    private boolean isAVideoPlaying() {
        return currentlyPlayingVideo != null;
    }

    /***
     * Stops the currently playing video
     */
    private void stopCurrentlyPlayingVideo() {
        System.out.println("Stopping video: " + currentlyPlayingVideo.getTitle());
        currentlyPlayingVideo = null;
    }

    /***
     * Pause the currently playing video
     */
    private void pauseCurrentlyPlayingVideo() {
        if (isVideoPaused) {
            System.out.println("Video already paused: " + currentlyPlayingVideo.getTitle());
        } else {
            isVideoPaused = true;
            System.out.println("Pausing video: " + currentlyPlayingVideo.getTitle());
        }
    }

    /***
     * Continues the video if it is currently paused
     */
    private void continueCurrentlyPausedVideo() {
        if (isVideoPaused) {
            System.out.println("Continuing video: " + currentlyPlayingVideo.getTitle());
            isVideoPaused = false;
        } else {
            System.out.println("Cannot continue video: Video is not paused");
        }
    }

    /***
     * Processes User Search to Play Videos using tag or search term
     * @param searchTerm
     * @param searchResults
     */
    private void processSearch(String searchTerm, List<Video> searchResults) {
        System.out.println("Here are the results for " + searchTerm + ":");
        List<Video> sortedList = searchResults.stream().sorted(Comparator.comparing(Video::getTitle)).collect(Collectors.toList());
        int counter = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            Video video = sortedList.get(i);
            System.out.printf("%d) %s (%s) [%s]%n", counter + 1, video.getTitle(), video.getVideoId(), String.join(" ", video.getTags()));
            counter++;
        }
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
        Scanner scanner = new Scanner(System.in);
        try {
            int reply = scanner.nextInt();
            Video selectedVideo = sortedList.get(reply - 1);
            playVideo(selectedVideo.getVideoId());
        } catch (Exception e) {
            //Ignore
        }
    }

}