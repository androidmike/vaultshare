package com.vaultshare.play;

import com.vaultshare.play.model.Track;

/**
 * Created by mchang on 7/7/15.
 */
public class TrackStarted {
    public Track currentTrack;
    public TrackStarted(Track currentTrack) {
        this.currentTrack = currentTrack;
    }
}
