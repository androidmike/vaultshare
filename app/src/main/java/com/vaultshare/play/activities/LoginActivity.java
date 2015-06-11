package com.vaultshare.play.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.layer.sdk.exceptions.LayerException;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.vaultshare.play.LayerImpl;
import com.vaultshare.play.MainActivity;
import com.vaultshare.play.ParseImpl;
import com.vaultshare.play.R;
import com.vaultshare.play.parse.ParseLoginCallbacks;

/*
 * LoginActivity.java
 * Displays the login screen, which allows the user to enter a username and password. Also has a
 *  "Forgot Password" option and allows the user to create a new account by linking to the
 *  SignupActivity class
 */

public class LoginActivity extends BaseActivity implements ParseLoginCallbacks {

    //User inputs for username and password
    private EditText mUsername, mPassword;

    //Grabs the fields from the login_screen view
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //Register the login, signup, and forgot password buttons
        Button loginButton = (Button) findViewById(R.id.login);
        if (loginButton != null)
            loginButton.setOnClickListener(this);

        Button signUpButton = (Button) findViewById(R.id.signup);
        if (signUpButton != null)
            signUpButton.setOnClickListener(this);

        TextView forgotPasswordButton = (TextView) findViewById(R.id.forgotpassword);
        if (forgotPasswordButton != null)
            forgotPasswordButton.setOnClickListener(this);

        //Grab the username and password fields
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
    }

    //Check to see what the user has tapped and react accordingly
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login:
                Log.d("Activity", "Login button pressed");
                onLoginPressed();
                break;

            case R.id.forgotpassword:
                Log.d("Activity", "Forgot Password button pressed");
                onForgotPasswordPressed();
                break;

            case R.id.signup:
                Log.d("Activity", "Sign Up button pressed");
                onSignupPressed();
                break;

            default:
        }
    }

    //Handle logging in an existing user
    public void onLoginPressed() {

        //What did the user enter as their username as password?
        String usernameString = getTextAsString(mUsername);
        String passwordString = getTextAsString(mPassword);

        //Make sure the entries are valid
        if (usernameString.length() <= 3) {

            Log.d("Activity", "Cannot login, username is too short");
            showAlert("Login Error", "A valid username is required to login.");

        } else if (passwordString.length() <= 3) {

            Log.d("Activity", "Cannot login, password is too short");
            showAlert("Login Error", "A valid password is required to login.");

        } else {

            //Log the user in using Parse
            Log.d("Activity", "Starting login process");
            ParseImpl.loginUser(usernameString, passwordString, this);
        }
    }

    //The user was able to login
    public void loginSucceeded(ParseUser user) {

        Log.d("Activity", "User logged in with Parse. Staring Layer authentication");

        //Check to see if the user is already authenticated. If so, start the ConversationsActivity
        if (LayerImpl.isAuthenticated()) {
            onUserAuthenticated(ParseImpl.getRegisteredUser().getObjectId());
        } else {
            //User is logged into Parse, so start the Layer Authentication process
            LayerImpl.authenticateUser();
        }
    }

    //Parse could not log the user in for some reason
    public void loginFailed(ParseException e) {
        Log.d("Activity", "Cannot log in with Parse. Exception: " + e.toString());

        showAlert("Login Error", "Encountered the following error while logging in: " + e.toString()
                + "\n\n" + "Make sure you have signed up, or click \"Forgot Password?\"");
    }


    //User was Authenticated with Layer. This means their lastMsgContent/conversation history is being
    // downloaded and stored locally, and they can now send/receive messages
    public void onUserAuthenticated(String userID) {

        //Go to the conversation view
        Log.d("Activity", "User authenticated");

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Layer was not able to Authenticate the user for some reason
    public void onUserAuthenticatedError(LayerException e) {
        showAlert("Login Error", "Encountered the following error while logging in: " + e.toString()
                + "\n\n" + "Make sure you have signed up, or click \"Forgot Password?\"");

        Log.d("Activity", "Cannot authenticate Layer. Exception: " + e.toString());
    }


    //Handle resetting a user's password
    public void onForgotPasswordPressed() {

        //Build an alert dialog with instructions
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Reset Password");
        helpBuilder.setMessage("Enter the email address associated with your account. Instructions " +
                "to reset your password will be sent to this address.\n");

        //Let the user enter their email address
        final EditText emailAddress = new EditText(getApplicationContext());
        emailAddress.setHint("Your Email Address");
        emailAddress.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailAddress.setTextColor(Color.BLACK);
        emailAddress.setPadding(40, 0, 40, 0);

        //Add the text field to the alert dialog
        helpBuilder.setView(emailAddress);

        //The user can cancel the reset password request
        helpBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing but close the dialog
                        Log.d("Activity", "Canceling reset password for email address: " + getTextAsString(emailAddress));
                    }
                });

        //Or, the user can reset their password
        helpBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //Have Parse reset this user's email address
                        Log.d("Activity", "Resetting password for email address: " + getTextAsString(emailAddress));
                        ParseImpl.resetPassword(getTextAsString(emailAddress));
                    }
                });

        //Create and show this dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    //Start the registration process for a new user
    public void onSignupPressed() {

        Log.d("Activity", "Starting signup Activity");
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    //Disable the back button
    public void onBackPressed() {

    }
}
