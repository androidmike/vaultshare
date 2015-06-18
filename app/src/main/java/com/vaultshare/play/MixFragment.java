package com.vaultshare.play;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    public static Fragment newInstance() {
        return new MixFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_recyclerview;
    }

    RecyclerViewMaterialAdapter mAdapter;
    String pendingSetId = null;

    @Override
    public void initUI() {
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mAdapter = new RecyclerViewMaterialAdapter(new MixRecyclerViewAdapter(getActivity(), mModels));
        mRecyclerView.setAdapter(mAdapter);

        // Material viewpager boilerplate
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
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

}
