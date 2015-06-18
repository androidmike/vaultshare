package com.vaultshare.play;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

/**
 * Created by mchang on 6/8/15.
 */
public class SessionController {
    private static final String SESSION_STATE_GSON = App.getContext().getPackageName() + ".session_gson";

    static  SessionController sessionController;
    private Session           currentSession;
    private SharedPreferences prefs = App.getContext().getSharedPreferences(App.getContext().getPackageName(), Context.MODE_PRIVATE);

    public SessionController() {
    }

    public static SessionController getInstance() {
        if (sessionController == null) {
            sessionController = new SessionController();

        }
        return sessionController;
    }

    public synchronized void restoreCurrentSession() {
        try {
            Session foundSession = new Gson().fromJson(prefs.getString(SESSION_STATE_GSON, null), Session.class);
            if (foundSession != null) {
                currentSession = foundSession;
            }
        } catch (Exception e) {
        }

    }

    public synchronized void saveCurrentSession() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SESSION_STATE_GSON, new Gson().toJson(currentSession));
                editor.apply();
            }
        }).run();
    }

    // Device-wide
    public boolean isLoggedInFacebook() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void logOutFacebook() {
        LoginManager.getInstance().logOut();
    }

    public Session getSession() {
        return currentSession;
    }

    public void startNewSession(String uid) {
        currentSession = new Session(uid);
        saveCurrentSession();
    }
}
