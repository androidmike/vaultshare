package com.vaultshare.play;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.layer.atlas.Atlas;
import com.layer.atlas.AtlasConversationsList;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Conversation;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {


    static public LayerClient               layerClient;
    static public Atlas.ParticipantProvider participantProvider;

    private AtlasConversationsList myConversationList;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar                  mToolbar;

    AccessTokenTracker accessTokenTracker;

    @InjectView(R.id.start_set_button)
    Button startSetButton;

    @Override
    public void onStop() {
        accessTokenTracker.stopTracking();
        super.onStop();
    }

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData("John Doe", "johndoe@doe.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));


        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                    }
                });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                onFacebookAccessTokenChange(currentAccessToken);
            }
        };

//
//        layerClient = LayerClient.newInstance(this, getString(R.string.layer_app_test_id));
//        layerClient.registerAuthenticationListener(new MyAuthenticationListener(this));
//        layerClient.connect();
////        if (!layerClient.isAuthenticated()) {
////        layerClient.authenticate();
////        } else {
////            onUserAuthenticated();
////        }
//        layerClient.deauthenticate();
//        layerClient.authenticate();
    }


    public void onUserAuthenticated() {
        participantProvider = new Atlas.ParticipantProvider() {
            Map<String, Atlas.Participant> users = new HashMap<String, Atlas.Participant>();

            {
                users.put("Tee", new User());
                users.put(SessionController.getInstance().getSession().getUid(),
                        SessionController.getInstance().getSession().getCurrentUser());
            }

            public Map<String, Atlas.Participant> getParticipants(String filter,
                                                                  Map<String, Atlas.Participant> result) {

                for (Map.Entry<String, Atlas.Participant> entry : users.entrySet()) {
                    if (entry.getValue().getFirstName().indexOf(filter) > -1)
                        result.put(entry.getKey(), entry.getValue());
                }

                return result;
            }

            public Atlas.Participant getParticipant(String userId) {
                return users.get(userId);
            }
        };

        myConversationList = (AtlasConversationsList) findViewById(R.id.conversationlist);
        myConversationList.init(layerClient, participantProvider);
        myConversationList.setClickListener(new AtlasConversationsList.ConversationClickListener() {
            public void onItemClick(Conversation conversation) {
                startMessagesActivity(conversation);
            }
        });

        layerClient.registerEventListener(myConversationList);

        View newconversation = findViewById(R.id.newconversation);
        newconversation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMessagesActivity(null);
            }
        });
    }


    private void startMessagesActivity(Conversation c) {
        Intent intent = new Intent(this, com.vaultshare.play.MessagesActivity.class);
        if (c != null) {
            intent.putExtra("conversation-id", c.getId());
        }
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            FirebaseController.getInstance().getRef()
                    .authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
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

                                Toast.makeText(MainActivity.this, String.format("You are logged in as %s", displayName), Toast.LENGTH_SHORT).show();
                            }

                            Map<String, User> users = new HashMap<String, User>();
                            Firebase usersRef = FirebaseController.getInstance().getRef().child("users");
                            usersRef.child(authData.getUid()).setValue(map);

                            SessionController.getInstance().getSession().setUid(authData.getUid());


                            FirebaseController.getInstance().testCreateRoomTracksSets();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // there was an error
                            Toast.makeText(MainActivity.this, "logged failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            FirebaseController.getInstance().getRef().unauth();
        }
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        Fragment f = null;
        switch (position) {
            case 0: // Following/Newsfeed
                f = new BrowseFragment();
                break;
            case 1: // Vault
                f = new BrowseFragment();
                break;
            case 2: // Mixing
                f = new BrowseFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, f)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
