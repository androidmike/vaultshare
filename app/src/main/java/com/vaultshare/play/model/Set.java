package com.vaultshare.play.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mchang on 6/8/15.
 */
public class Set {

//    public enum State {
//        LIVE_NEW, ARCHIVED, LIVE_REWIND;
//    }

//    public State state = State.ARCHIVED;

    public String        creator;
    public String        last_updated;
    public String        cover_url;
    public HashMap<String, Boolean> tracks;
//
//    public int liveTrack = -1; // index of tracks, -1 for not live
//
//    public int currentTrackPositionMs; // time in milliseconds
}
