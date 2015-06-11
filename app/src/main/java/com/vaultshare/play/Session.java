package com.vaultshare.play;

/**
 * Created by mchang on 6/10/15.
 */
public class Session {
    private User    currentUser;
    private String  uid;


    public void setUid(String uid) {
        this.uid = uid;
        SessionController.getInstance().saveCurrentSession();
    }


    public String getUid() {
        return uid;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
