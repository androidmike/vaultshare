package com.vaultshare.play.activities;

import android.content.Intent;

import com.vaultshare.play.MainActivity;
import com.vaultshare.play.R;
import com.vaultshare.play.SessionController;

/**
 * Created by mchang on 6/11/15.
 */
public class LaunchActivity extends BaseActivity {
    @Override
    public int getLayout() {
        return R.layout.activity_launch;
    }

    @Override
    public void initUI() {

    }

    public void onResume() {
        super.onResume();
        evaluateSession();
    }

}
