package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private String flag;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.flag="";
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /***
   * Checks if current video is flagged
   * @return true if flagged, false if not flagged
   */
  public boolean isVideoFlagged(){
    return this.flag.length()!=0;
  }

  /***
   * Removes flag from the video
   */
  public void removeFlag(){
    this.flag = "";
  }

  /***
   * Add a flag to a video
   * @param flag
   */
  public void addFlag(String flag){
    this.flag = flag;
  }

  /***
   * Get the current Video Flag
   * @return
   */
  public String getFlag(){ return this.flag; }

}
