package com.vaultshare.play;

import com.vaultshare.play.model.Track;

/**
 * Created by mchang on 7/7/15.
 */
public class TrackStarted {
    public Track currentTrack;
    int currentTrackNumber;
    public String currentTrackId;

    public TrackStarted(Track currentTrack, int currentTrackNumber, String currentTrackId) {
        this.currentTrack = currentTrack;
        this.currentTrackNumber = currentTrackNumber;
        this.currentTrackId = currentTrackId;
    }
}
