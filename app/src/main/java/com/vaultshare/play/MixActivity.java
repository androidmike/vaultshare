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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.software.shell.fab.ActionButton;
import com.squareup.otto.Subscribe;
import com.vaultshare.play.activities.BaseActivity;
import com.vaultshare.play.fragments.MixDataProviderFragment;
import com.vaultshare.play.model.ItemPinnedMessageDialogFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

public class MixActivity extends BaseActivity implements ItemPinnedMessageDialogFragment.EventListener {
    private static final String FRAGMENT_TAG_DATA_PROVIDER      = "data provider";
    public static final  String FRAGMENT_LIST_VIEW              = "list view";
    private static final String FRAGMENT_TAG_ITEM_PINNED_DIALOG = "item pinned dialog";

    @InjectView(R.id.marquee_text)
    TextView marqueeText;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.action_button)
    ActionButton actionButton;

    @Override
    public int getLayout() {
        return R.layout.activity_mix;
    }

    @Override
    public void initUI() {
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

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        marqueeText.setSelected(true);

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
//        SnackbarManager.show(
//                Snackbar.with(getApplicationContext())
//                        .text(R.string.snack_bar_text_item_removed)
//                        .actionLabel(R.string.snack_bar_action_undo)
//                        .actionListener(new ActionClickListener() {
//                            @Override
//                            public void onActionClicked(Snackbar snackbar) {
//                                onItemUndoActionClicked();
//                            }
//                        })
//                        .actionColorResource(R.color.snackbar_action_color_done)
//                        .duration(5000)
//                        .type(SnackbarType.SINGLE_LINE)
//                        .swipeToDismiss(false)
//                , this);
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
}
