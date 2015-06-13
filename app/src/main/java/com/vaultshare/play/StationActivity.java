package com.vaultshare.play;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.github.florent37.materialviewpager.HeaderDesign;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.vaultshare.play.activities.BaseActivity;
import com.vaultshare.play.fragments.ScrollFragment;
import com.vaultshare.play.fragments.WebViewFragment;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class StationActivity extends BaseActivity {

    public static final String EXTRA_STATION_ID = StationActivity.class.getCanonicalName() + ".STATION_ID";

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @Override
    public int getLayout() {
        return R.layout.activity_station;
    }

    @Override
    public void initUI() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        pager.setAdapter(new StationPagerAdapter(getSupportFragmentManager(), extras));
        tabs.setViewPager(pager);
    }

}
