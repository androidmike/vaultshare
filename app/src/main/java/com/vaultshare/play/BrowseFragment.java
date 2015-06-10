package com.vaultshare.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.InjectView;

/**
 * Created by mchang on 6/9/15.
 */
public class BrowseFragment extends BaseFragment {
    @InjectView(R.id.stations_listing_recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public int getLayout() {
        return R.layout.browse_layout;
    }

    @Override
    public void initUI() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BrowseStationAdapter(FirebaseController.getInstance().getStationsRef(), getActivity()); 
        mRecyclerView.setAdapter(mAdapter);

    }
}
