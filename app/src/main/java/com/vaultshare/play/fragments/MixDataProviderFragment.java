/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vaultshare.play.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.vaultshare.play.AbstractDataProvider;
import com.vaultshare.play.MixActivity;
import com.vaultshare.play.FirebaseController;
import com.vaultshare.play.MixRecyclerViewFragment;
import com.vaultshare.play.SessionController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MixDataProviderFragment extends Fragment {

    public static final int ITEM_VIEW_TYPE_SECTION_HEADER = 0;
    public static final int ITEM_VIEW_TYPE_SECTION_ITEM = 1;
    final   List<ExampleDataProvider.ConcreteData> presentData   = new ArrayList<>();
    private AbstractDataProvider                   mDataProvider = new ExampleDataProvider(presentData);
    String pendingSetId;
    private ChildEventListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);  // keep the mDataProvider instance
        final List<String> trackIds = new ArrayList<>();
        final Firebase userRef = FirebaseController.getInstance().getRef().child("users").
                child(SessionController.getInstance().getSession().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public Query mRef;

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

                presentData.clear();
                final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
                presentData.add(new ExampleDataProvider.ConcreteData(0, "", ITEM_VIEW_TYPE_SECTION_HEADER, "", swipeReaction));
                initializeTracklist(setRef.child("tracks"));
            }



            private void initializeTracklist(Query mRef) {
                this.mRef = mRef;
                trackIds.clear();
//                mDataProvider = new ExampleDataProvider(presentData);
                mListener = this.mRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        if (!trackIds.contains(dataSnapshot.getKey())) {
                            String trackId = dataSnapshot.getKey();
                            trackIds.add(trackId);

                            final int viewType = ITEM_VIEW_TYPE_SECTION_ITEM;
                            final String text = trackIds.get(0);

                            final int swipeReaction =
                                    RecyclerViewSwipeManager.REACTION_CAN_SWIPE_RIGHT;

//                            final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_LEFT |
//                                    RecyclerViewSwipeManager.REACTION_CAN_SWIPE_RIGHT;
                            presentData.add(new ExampleDataProvider.ConcreteData(trackId.hashCode(), trackId, viewType, text, swipeReaction));
                            final Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(
                                    MixActivity.FRAGMENT_LIST_VIEW);
                            ((MixRecyclerViewFragment) fragment).notifyItemInserted(((Long) dataSnapshot.getValue()).intValue());
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if (!trackIds.contains(dataSnapshot.getKey())) {
                            trackIds.add(dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if (trackIds.contains(dataSnapshot.getKey())) {
                            trackIds.remove(dataSnapshot.getKey());
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


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public AbstractDataProvider getDataProvider() {
        return mDataProvider;
    }
}
