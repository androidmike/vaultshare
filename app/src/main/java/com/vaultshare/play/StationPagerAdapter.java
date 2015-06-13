package com.vaultshare.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.widget.ListView;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class StationPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"Live", "Meet", "Chat"};
    String stationId;
    Bundle bundle;

    public StationPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return DoorFragment.newInstance(bundle);
            case 1:
                return MeetFragment.newInstance(bundle);
            case 2:
                return ChatFragment.newInstance(bundle);

        }
        return null;
    }
}


