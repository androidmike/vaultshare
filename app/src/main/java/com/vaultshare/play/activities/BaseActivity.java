package com.vaultshare.play.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.vaultshare.play.App;
import com.vaultshare.play.FirebaseController;
import com.vaultshare.play.MainActivity;
import com.vaultshare.play.SessionController;
import com.vaultshare.play.StationActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by mchang on 6/11/15.
 */
public abstract class BaseActivity extends ActionBarActivity {
    /**
     * Define the layout for this entire activity to be used in setContentView
     */
    public abstract int getLayout();

    public abstract void initUI();

    AccessTokenTracker accessTokenTracker;
    CallbackManager    callbackManager;

    public void evaluateSession() {
        // Router depends on FB logged in state
        if (SessionController.getInstance().isLoggedInFacebook()) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.inject(this);
        initUI();

        callbackManager = CallbackManager.Factory.create();
        FacebookCallback<LoginResult> fbCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, fbCallback);


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                onFacebookAccessTokenChange(currentAccessToken);
                evaluateSession();
            }
        };

    }


    /**
     * Facebook token observed all the time for late log-out
     *
     * @param token
     */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token == null) {
            FirebaseController.getInstance().getRef().unauth();
            return;
        }

        Firebase.AuthResultHandler handler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("provider", authData.getProvider());
                if (authData.getProviderData().containsKey("id")) {
                    map.put("provider_id", authData.getProviderData().get("id").toString());
                }
                if (authData.getProviderData().containsKey("displayName")) {
                    String displayName = authData.getProviderData().get("displayName").toString();
                    map.put("displayName", displayName);

                    Toast.makeText(App.getContext(), String.format("You are logged in as %s", displayName), Toast.LENGTH_SHORT).show();
                }

                Firebase usersRef = FirebaseController.getInstance().getRef().child("users");
                usersRef.child(authData.getUid()).setValue(map);
                SessionController.getInstance().getSession().setUid(authData.getUid());

                // Testing
                FirebaseController.getInstance().testCreateRoomTracksSets();

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Toast.makeText(App.getContext(), "logged failed", Toast.LENGTH_SHORT).show();

            }
        };

        FirebaseController.getInstance().getRef()
                .authWithOAuthToken("facebook", token.getToken(), handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        accessTokenTracker.stopTracking();
        super.onStop();
    }
}
