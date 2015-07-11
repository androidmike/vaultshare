package com.vaultshare.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vaultshare.play.model.Track;

import java.util.List;

public class TrackAdapter extends FragmentStatePagerAdapter {
    public static final String TRACK_ID = TrackAdapter.class.getCanonicalName() + ".extra.track_id";
    List<String> trackIds;

    public TrackAdapter(FragmentManager fm, List<String> trackIds) {
        super(fm);
        this.trackIds = trackIds;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment radioFrag = new RadioFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TRACK_ID, trackIds.get(position));
        radioFrag.setArguments(bundle);
        return radioFrag;
    }

    @Override
    public int getCount() {
        return trackIds.size();
    }
}