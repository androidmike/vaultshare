package com.vaultshare.play;

import java.util.List;

/**
 * Created by mchang on 6/8/15.
 */
public class User {
    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        ONLINE_PLAYING, OFFLINE, ONLINE_IDLE, ONLINE_BROWSING
    }

    private int          birthYear;
    private String       fullName;
    public  State        state;
    public  String       currentTrack;
    public  List<String> followers;
    public  String       currentPosition;

    public User() {
    }

    public User(String fullName, int birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.state = State.ONLINE_PLAYING;
    }

    public long getBirthYear() {
        return birthYear;
    }

    public String getFullName() {
        return fullName;
    }
}
