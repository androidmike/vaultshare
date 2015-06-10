package com.vaultshare.play.model;

/**
 * Created by mchang on 6/10/15.
 */

public class PlayState {
    int  trackNumber;
    long positionMs;

    public PlayState(int trackNumber, long positionMs) {
        this.trackNumber = trackNumber;
        this.positionMs = positionMs;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public long getPositionMs() {
        return positionMs;
    }

    public void setPositionMs(long positionMs) {
        this.positionMs = positionMs;
    }
}