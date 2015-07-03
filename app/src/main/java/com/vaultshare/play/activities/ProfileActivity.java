package com.vaultshare.play.activities;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.vaultshare.play.DJProfileFragment;
import com.vaultshare.play.NavigationDrawerFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.fragments.Login1;
import com.vaultshare.play.fragments.Login2;
import com.vaultshare.play.fragments.Login3;
import com.vaultshare.play.fragments.Login4;
import com.vaultshare.play.fragments.Login5;
import com.vaultshare.play.fragments.Login6;

import butterknife.InjectView;

/**
 * Created by mchang on 6/11/15.
 */
public class ProfileActivity extends BaseActivity {
    @InjectView(R.id.toolbar_actionbar)
    Toolbar toolbar;

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public void initUI() {
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, DJProfileFragment.newInstance())
                .commit();
    }

    public void setToolbarVisibility(int toolbarVisibility) {
        this.toolbar.setVisibility(toolbarVisibility);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
