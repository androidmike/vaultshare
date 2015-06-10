package com.vaultshare.play;

/**
 * Created by mchang on 6/8/15.
 */
public class SessionController {
    static SessionController sessionController;

    private User   currentUser;
    private String uid;

    public SessionController() {

    }

    public static SessionController getInstance() {
        if (sessionController == null) {
            sessionController = new SessionController();
        }
        return sessionController;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getUid() {
        return uid;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
