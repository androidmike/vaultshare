package com.vaultshare.play;

/**
 * Created by mchang on 6/9/15.
 */
public class SoundCloudTrackResp {
    int    duration;
    String id;
    String title;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public SoundCloudUser user;
    String description;
    String artwork_url;
    String genre;
    String license;
}
