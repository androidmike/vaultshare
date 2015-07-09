package com.vaultshare.play;/*
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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.software.shell.fab.ActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.vaultshare.play.activities.BaseActivity;
import com.vaultshare.play.activities.ListenAdapter;
import com.vaultshare.play.fragments.ExampleDataProvider;
import com.vaultshare.play.fragments.MixDataProviderFragment;
import com.vaultshare.play.model.ItemPinnedMessageDialogFragment;
import com.vaultshare.play.model.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import se.emilsjolander.flipview.FlipView;

public class MixActivity extends BaseActivity implements ItemPinnedMessageDialogFragment.EventListener {
    private static final String FRAGMENT_TAG_DATA_PROVIDER      = "data provider";
    public static final  String FRAGMENT_LIST_VIEW              = "list view";
    private static final String FRAGMENT_TAG_ITEM_PINNED_DIALOG = "item pinned dialog";
    @InjectView(R.id.flip_view)
    FlipView             flipView;
    @InjectView(R.id.play_bar)
    View                 playBar;
    @InjectView(R.id.slide_up_panel_container)
    SlidingUpPanelLayout slideUpPanel;

    @InjectView(R.id.toolbar)
    Toolbar      toolbar;
    @InjectView(R.id.play_button)
    ImageButton  playButton;
    @InjectView(R.id.marquee_text)
    TextView     marqueeText;
    @InjectView(R.id.action_button)
    ActionButton actionButton;

    ListenAdapter flipViewAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_mix;
    }

    @Override
    public void initUI() {


        playBar.setVisibility(View.VISIBLE);
        slideUpPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {
//                playBar.setVisibility(View.VISIBLE);
//                slideUpPanel.setTouchEnabled(true);
            }

            @Override
            public void onPanelExpanded(View view) {
//                slideUpPanel.setTouchEnabled(false);
//                playBar.setVisibility(View.GONE);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
        final Firebase userRef = FirebaseController.getInstance().getRef().child("users").child(SessionController.getInstance().getSession().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                String pendingSetId = (String) newPost.get("pending_set");
                if (pendingSetId == null) {
                    pendingSetId = FirebaseController.getInstance().getRef().child("sets").push().getKey();
                    HashMap map = new HashMap();
                    map.put("pending_set", pendingSetId);
                    userRef.updateChildren(map);
                }


                final List<Track> tracks = new ArrayList<Track>();
                final List trackIds = new ArrayList();
                Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId).child("tracks");
                ChildEventListener mListener = setRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        String trackId = dataSnapshot.getKey();
                        if (!trackIds.contains(trackId)) {
                            trackIds.add(trackId);
                            Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);


                            trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final Track track = dataSnapshot.getValue(Track.class);
                                    if (track != null) {
                                        tracks.add(track);
                                        flipViewAdapter = new ListenAdapter(MixActivity.this, tracks);
                                        flipView.setAdapter(flipViewAdapter);
                                        flipView.setOnFlipListener(new FlipView.OnFlipListener() {
                                            @Override
                                            public void onFlippedToPage(FlipView flipView, int pos, long l) {
                                                if (pos == 0) {
                                                } else {
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
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


                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(new MixDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                            .commit();


                    Fragment fragment = new MixRecyclerViewFragment();

                    Bundle args = new Bundle();
                    args.putString(MixRecyclerViewFragment.PENDING_SET_ID, pendingSetId);
                    fragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, fragment, FRAGMENT_LIST_VIEW)
                            .commit();
                }


                setSupportActionBar(toolbar);

                actionButton.setImageDrawable(App.getContext().getResources().getDrawable(R.drawable.plane_white));
                actionButton.setButtonColor(Color.parseColor("#dc0000"));

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(MixActivity.this)
                                .items(R.array.share)
                                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        /**
                                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                         * returning false here won't allow the newly selected radio button to actually be selected.
                                         **/
                                        switch (which) {
                                            case 0: // Broadcast

                                                showBroadcastOptions();
                                                break;
                                            case 1: // Share Mixtape
                                                break;
                                        }
                                        return true;
                                    }
                                })
                                .alwaysCallSingleChoiceCallback()
//                            .positiveText("Add")
                                .show();
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        marqueeText.setSelected(true);


        EchoNest.getInstance().searchImages("jay-z");
        EchoNest.getInstance().search("jay-z");
    }

    private void showBroadcastOptions() {
        new MaterialDialog.Builder(MixActivity.this)
                .items(R.array.broadcast)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        switch (which) {
                            case 0: // Broadcast


                                break;
                            case 1: // Share Mixtape
                                break;
                        }
                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
//                            .positiveText("Add")
                .show();

    }

    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

    }

    /**
     * This method will be called when a list item is removed
     *
     * @param position The position of the item within data set
     */
    public void onItemRemoved(int position) {

        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                onItemUndoActionClicked();
            }
        };

        final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);


        Snackbar
                .make(coordinatorLayoutView, "Took the garbage out...", Snackbar.LENGTH_LONG)
                .setAction("Undo", clickListener)
                .show();
    }

    /**
     * This method will be called when a list item is pinned
     *
     * @param position The position of the item within data set
     */
    public void onItemPinned(int position) {
        final DialogFragment dialog = ItemPinnedMessageDialogFragment.newInstance(position);

        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    /**
     * This method will be called when a list item is clicked
     *
     * @param position The position of the item within data set
     */
    public void onItemClicked(int position) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        AbstractDataProvider.Data data = getDataProvider().getItem(position);

        if (data.isPinnedToSwipeLeft()) {
            // unpin if tapped the pinned item
            data.setPinnedToSwipeLeft(false);
            ((MixRecyclerViewFragment) fragment).notifyItemChanged(position);
        }
    }

    private void onItemUndoActionClicked() {
        int position = getDataProvider().undoLastRemoval();
        if (position >= 0) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
            ((MixRecyclerViewFragment) fragment).notifyItemInserted(position);
        }
    }

    // implements ItemPinnedMessageDialogFragment.EventListener
    @Override
    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);

        getDataProvider().getItem(itemPosition).setPinnedToSwipeLeft(ok);
        ((MixRecyclerViewFragment) fragment).notifyItemChanged(itemPosition);
    }

    public AbstractDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER);
        return ((MixDataProviderFragment) fragment).getDataProvider();
    }


    @Subscribe
    public void onPictureTaken(PictureTaken e) {
        Toast.makeText(this, "PictureTaken event received", Toast.LENGTH_SHORT).show();

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);

        ((MixRecyclerViewFragment) fragment).notifyItemChanged(0);
    }

    @Subscribe
    public void onTrackStarted(TrackStarted e) {
        playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
        marqueeText.setText(e.currentTrack.getDisplayName() + "      " + e.currentTrack.getDisplayName());
        marqueeText.setSelected(true);
    }

    @Subscribe
    public void onTrackStopped(TrackStopped e) {
        marqueeText.setText("");
        playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
    }

    @Subscribe
    public void EchoNestImageSearchResult(EchoNestImageSearchResult e) {
        if (flipViewAdapter != null) {
            flipViewAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onEchoNestSearchResult(EchoNestSearchResult e) {
        if (flipViewAdapter != null) {
            flipViewAdapter.notifyDataSetChanged();
        }
    }
}
