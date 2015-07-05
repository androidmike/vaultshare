package com.vaultshare.play;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;
import com.vaultshare.play.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by mchang on 7/1/15.
 */
public class SearchActivity extends BaseActivity {
    public static final String EXTRA_PENDING_SET_ID = SearchActivity.class.getCanonicalName() + ".extra.pending_set_id";
    public static final String PENDING_TRACK_NUMBER = SearchActivity.class.getCanonicalName() + ".extra.pending_track_number";
    List<SoundCloudTrackResp> mResults = new ArrayList();
    TracksResultAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    @InjectView(R.id.track_recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.track_edit_text)
    EditText     trackEditText;
    @InjectView(R.id.search_track_button)
    Button       searchTrackButton;
    @InjectView(R.id.progress_view)
    View         busy;


    @Override
    public int getLayout() {
        return R.layout.search_track_layout;
    }

    @Override
    public void initUI() {
        searchTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundCloud.getInstance().search(trackEditText.getText().toString());
                showSearchBusy(View.VISIBLE);
            }
        });

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void showSearchBusy(int visibility) {
        busy.setVisibility(visibility);
    }

    @Subscribe
    public void onQueryResult(QueryResult e) {
        showSearchBusy(View.GONE);
        this.mResults = e.response.collection;
        String pendingSetId = getIntent().getStringExtra(EXTRA_PENDING_SET_ID);
        int pendingTrackNumber = getIntent().getIntExtra(PENDING_TRACK_NUMBER, 0);
        mAdapter = new TracksResultAdapter(this, this.mResults, pendingSetId, pendingTrackNumber);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onPendingTrackAdded(PendingTrackAdded e) {
        finish();
    }
}
