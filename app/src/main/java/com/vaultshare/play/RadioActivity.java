package com.vaultshare.play;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.vaultshare.play.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by mchang on 7/9/15.
 */
public class RadioActivity extends BaseActivity {
    @InjectView(R.id.pager)
    ViewPager mPager;
    TrackAdapter mPagerAdapter;
    @InjectView(R.id.skip)
    View masterSkip;


    @Override
    public int getLayout() {
        return R.layout.radio;
    }

    @Override
    public void initUI() {
        MediaPlayerController.getInstance().joinHipHop();
        masterSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.getInstance().post(new MasterSkip());
            }
        });

    }

//    @Subscribe
//    public void onTracksRetrieved(TrackRetrieved e) {
//        mPagerAdapter = new TrackAdapter(getSupportFragmentManager(), MediaPlayerController.getInstance().trackIds);
//        mPager.setAdapter(mPagerAdapter);
//    }
//
//    @Subscribe
//    public void onTrackAdded(TrackAdded e) {
//        Toast.makeText(this, "Someone just made it onto the rotation", Toast.LENGTH_SHORT).show();
//        mPagerAdapter.notifyDataSetChanged();
////        mPagerAdapter = new TrackAdapter(getSupportFragmentManager(), MediaPlayerController.getInstance().trackIds);
////        mPager.setAdapter(mPagerAdapter);
//    }
//
//    @Subscribe
//    public void onTrackRemoved(TrackRemoved e) {
//        Toast.makeText(this, "Someone just got knocked off the rotation", Toast.LENGTH_SHORT).show();
//        mPagerAdapter = new TrackAdapter(getSupportFragmentManager(), MediaPlayerController.getInstance().trackIds);
//        mPager.setAdapter(mPagerAdapter);
////        mPagerAdapter = new TrackAdapter(getSupportFragmentManager(), MediaPlayerController.getInstance().trackIds);
////        mPager.setAdapter(mPagerAdapter);
//    }


    List<String> playedTrackIds = new ArrayList<>();

    @Subscribe
    public void onTrackStarted(TrackStarted e) {
//        mPager.setCurrentItem(e.currentTrackNumber);
        playedTrackIds.add(e.currentTrackId);
        if (mPagerAdapter == null) {
            mPagerAdapter = new TrackAdapter(getSupportFragmentManager(), playedTrackIds);
            mPager.setAdapter(mPagerAdapter);
        } else {
            mPagerAdapter.notifyDataSetChanged();
        }
        mPager.setCurrentItem(playedTrackIds.size() - 1, true);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
        }
    }

    @Subscribe
    public void onSkipTrack(SkipTrack e) {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
    }

}
