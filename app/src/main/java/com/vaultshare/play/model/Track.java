package com.vaultshare.play.model;

/**
 * Created by mchang on 6/8/15.
 * DataSnapshot { key = -JrQS_8DyXPHpa2wX9Iq, value = {added_by=facebook:10105340362088383, src=SOUNDCLOUD,
 * set_id=-JrQS_8GRIbXKHdo-Ysi, duration=27099, src_id=6621631, added_time=2015-06-09T20:14:34.066-07:00} }
 */
public class Track {
    public int getDuration() {
        return duration;
    }

    public enum Source {
        SOUNDCLOUD, SPOTIFY, YOUTUBE, VAULTSHARE
    }

    public String added_by;
    public String added_time;
    public int duration;
    public String set_id;
    public String src;
    public String src_id;

    public Track() {

    }
}
