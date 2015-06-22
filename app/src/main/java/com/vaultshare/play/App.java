package com.vaultshare.play;

import android.app.Application;
import android.content.Context;


/**
 * Created by mchang on 6/9/15.
 */

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/source_regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        FacebookSdk.sdkInitialize(getApplicationContext());
        Firebase.setAndroidContext(this);
    }
}
