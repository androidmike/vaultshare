package com.vaultshare.play;

import java.util.List;

/**
 * Created by mchang on 6/10/15.
 */
public class Session {
    public void setState(State state) {
        this.state = state;
    }

    private String uid;

    public enum State {
        ONLINE_PLAYING, OFFLINE, ONLINE_IDLE, ONLINE_BROWSING
    }

    public State state;

    public Session(String userId) {
        this.uid = userId;
    }

    public String getUid() {
        return uid;
    }

}
