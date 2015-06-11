package com.vaultshare.play.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.vaultshare.play.LayerImpl;
import com.vaultshare.play.MainActivity;
import com.vaultshare.play.ParseImpl;
import com.vaultshare.play.R;

/*
 * LaunchActivity.java
 * Shows the initial loading page, and tries to initialize (and Connect) to Layer. Connecting early
 *  saves a few seconds when Authenticating since the initial handshake is already complete.
 */

public class LaunchActivity extends BaseActivity {

    //Make sure both the Layer and Parse configurations are set
    protected void onCreate(Bundle savedInstanceState) {
        if (!LayerImpl.hasValidAppID()) {

            showAlert("Invalid Layer App ID", "You will need a valid Layer App ID in order to run this example. " +
                    "If you haven't already, create a Layer account at http://layer.com/signup and then follow these instructions:\n\n" +
                    "1. Go to http://developer.layer.com and sign in\n" +
                    "2. Select \"Info\" on the left panel\n" +
                    "3. Copy the 'Staging App ID'\n" +
                    "4. Paste that value in the LayerAppID String in LayerImpl.java");

        } else if (!ParseImpl.hasValidAppID()) {

            showAlert("Invalid Parse Credentials", "You will need a valid Parse project in order to run this example. " +
                    "If you haven't already, create a Parse account at http://parse.com and follow these instructions:\n\n" +
                    "1. Sign in and mouse over the Settings icon by your App" +
                    "2. Select the \"Keys\" option" +
                    "3. Copy the Application ID and Client Key" +
                    "4. Paste those values in the ParseAppID and ParseClientKey fields in ParseImpl.java");

        } else {
            //The base class will create a Layer object (since one should not exist) and connect
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash_screen);
        }
    }

    //Once Layer is connected, show the Login screen if no user is authenticated
    public void onLayerConnected() {
        if (LayerImpl.isAuthenticated()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Authenticated by layer", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Not auth'ed, launching login activity.", Toast.LENGTH_SHORT).show();
        }
    }
}
