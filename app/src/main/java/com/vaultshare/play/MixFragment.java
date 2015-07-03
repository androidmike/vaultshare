package com.vaultshare.play;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.software.shell.fab.ActionButton;
import com.squareup.otto.Subscribe;
import com.vaultshare.play.model.FirebaseModel;
import com.vaultshare.play.model.Station;
import com.vaultshare.play.model.Track;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import butterknife.InjectView;

/**
 * Created by mchang on 6/13/15.
 */
public class MixFragment extends BaseFragment {
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.action_button)
    ActionButton actionButton;

    public static Fragment newInstance() {
        return new MixFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_recyclerview_mix;
    }

    RecyclerViewMaterialAdapter mAdapter;
    String pendingSetId = null;

    @Override
    public void initUI() {
        actionButton.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.ic_surround_sound_white_24dp));
        //    actionButton.setImageResource(R.drawable.fab_plus_icon);
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(getActivity(), "Deleted.", Toast.LENGTH_LONG).show();
                Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId);

                String trackId = mModels.get(viewHolder.getAdapterPosition() - 2);
                HashMap map = new HashMap();
                map.put(trackId, null);
                setRef.child("tracks").updateChildren(map);
            }
        };

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        List<Track> tracks = new ArrayList<>();
        final Firebase userRef = FirebaseController.getInstance().getRef().child("users").child(SessionController.getInstance().getSession().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                pendingSetId = (String) newPost.get("pending_set");
                if (pendingSetId == null) {
                    pendingSetId = FirebaseController.getInstance().getRef().child("sets").push().getKey();
                    HashMap map = new HashMap();
                    map.put("pending_set", pendingSetId);
                    userRef.updateChildren(map);
                }
                Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId);
                initializeTracklist(setRef.child("tracks"));


                mAdapter = new RecyclerViewMaterialAdapter(new MixRecyclerViewAdapter(getActivity(), mModels, pendingSetId));
                mRecyclerView.setAdapter(mAdapter);
                itemTouchHelper.attachToRecyclerView(mRecyclerView);
                // Material viewpager boilerplate
                MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    Query mRef = null;
    private List<String> mModels = new ArrayList<>();
    private ChildEventListener mListener;

    private void initializeTracklist(Query mRef) {
        this.mRef = mRef;
        mModels = new ArrayList<>();
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (!mModels.contains(dataSnapshot.getKey())) {
                    mModels.add(dataSnapshot.getKey());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (!mModels.contains(dataSnapshot.getKey())) {
                    mModels.add(dataSnapshot.getKey());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (mModels.contains(dataSnapshot.getKey())) {
                    mModels.remove(dataSnapshot.getKey());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    @Subscribe
    public void onTrackUploaded(TrackUploaded e) {
        Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId);
        HashMap map = new HashMap();
        map.put(e.trackId, true);
        setRef.child("tracks").updateChildren(map);
    }
}
