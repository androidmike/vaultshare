package com.vaultshare.play;

import android.app.Application;
import android.content.Context;


/**
 * Created by mchang on 6/9/15.
 */

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Main application object for the entire Pingpad app
 */
public class App extends Application {
    static App context;

    public App() {
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
