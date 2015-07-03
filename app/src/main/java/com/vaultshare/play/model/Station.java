package com.vaultshare.play.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.utilities.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mchang on 6/8/15.
 */
public class Station extends FirebaseModel {
    public String                   display_name;
    public String                   description;
    public HashMap<String, Boolean> sets;
    public String                   live_set;
    public String                   live_track_id;
    public String                   live_set_start_time;
    public HashMap<String, String>  live_users; // UID:Join Time
    public String                   dj_descirption;
    public String                   dj_name;
    public String dj_picture_large;

    public Station() {
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLive() {
        return live_set != null;
    }
}
