package com.vaultshare.play;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.vaultshare.play.fragments.MixDataProviderFragment;
import com.vaultshare.play.model.AwsController;
import com.vaultshare.play.model.PlayState;
import com.vaultshare.play.model.Set;
import com.vaultshare.play.model.Track;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixDraggableSwipeableAdapter
        extends RecyclerView.Adapter<MixDraggableSwipeableAdapter.MyViewHolder>
        implements DraggableItemAdapter<MixDraggableSwipeableAdapter.MyViewHolder>,
        SwipeableItemAdapter<MixDraggableSwipeableAdapter.MyViewHolder> {
    private static final String TAG = "MyDSItemAdapter";

    private AbstractDataProvider mProvider;
    private EventListener        mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;
    private Uri                  coverPhoto;
    private View.OnClickListener ratingListener;

    public interface EventListener {
        void onItemRemoved(int position);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);
    }

    public static class MyViewHolder extends AbstractDraggableSwipeableItemViewHolder {
        public FrameLayout mContainer;
        public View        mDragHandle;
        public TextView    mTextView, mTrackNumber;
        public ImageView        mImageView;
        public View             mChatButton;
        public MaterialEditText mChatRow;
        public RatingBar        mRatingBar;
        public Button           mActionButton;
        public ViewGroup        mRatingArea;
        public TextView         mTrackDuration;
        public TextView         mNoteText;
        public View             innerContainer;

        public MyViewHolder(View v) {
            super(v);
            innerContainer = v.findViewById(R.id.inner_container);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
//            mChatButton = v.findViewById(R.id.chat_icon);
            mChatRow = (MaterialEditText) v.findViewById(R.id.chat_row);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTrackNumber = (TextView) v.findViewById(R.id.track_number);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
            mImageView = (ImageView) v.findViewById(R.id.list_cover);
            mRatingArea = (ViewGroup) v.findViewById(R.id.rating_area);
            mRatingBar = (RatingBar) v.findViewById(R.id.rating_bar);
            mActionButton = (Button) v.findViewById(R.id.action_button);
            mNoteText = (TextView) v.findViewById(R.id.note_text);
            mTrackDuration = (TextView) v.findViewById(R.id.track_duration);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    String  pendingSetId;
    Context context;

    public MixDraggableSwipeableAdapter(Context context, String pendingSetId, AbstractDataProvider dataProvider) {
        this.context = context;

        Bus.getInstance().register(this);
        this.pendingSetId = pendingSetId;
        mProvider = dataProvider;
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
            }
        };
        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };


        // DraggableItemAdapter and SwipeableItemAdapter require stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);

        Firebase pendingSetRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId);

        pendingSetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set set = dataSnapshot.getValue(Set.class);
                if (set != null) {
                    if (set.cover_url != null) {
                        coverPhoto = Uri.parse(set.cover_url);
                        notifyItemChanged(0);
                    }
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true); // true --- pinned
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false --- not pinned
        }
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate((viewType == MixDataProviderFragment.ITEM_VIEW_TYPE_SECTION_HEADER) ? R.layout.intro_list_item : R.layout.list_item2, parent, false);
        return new MyViewHolder(v);
    }

    @Subscribe
    public void onPictureTaken(final PictureTaken e) {
        this.coverPhoto = e.uri;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AwsController.getInstance().uploadCover(pendingSetId, pendingSetId + ".jpg", FileHelper.getFile(e.uri));
            }
        }).run();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (holder.getItemViewType() == MixDataProviderFragment.ITEM_VIEW_TYPE_SECTION_HEADER) {
            final ImageView coverImage = (ImageView) holder.itemView.findViewById(R.id.cover_image);
            Firebase pendingSetRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId);

//            pendingSetRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Set set = dataSnapshot.getValue(Set.class);
//                    if (set != null) {
//                        if (set.cover_url != null) {
//                            coverPhoto = Uri.parse(set.cover_url);
//                            Picasso.with(App.getContext()).load(coverPhoto).into(coverImage);
//                        }
//                    }
//                }
//
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });


            Picasso.with(App.getContext()).load(coverPhoto).into(coverImage);

            holder.itemView.findViewById(R.id.add_track_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(App.getContext(), SearchActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra(SearchActivity.EXTRA_PENDING_SET_ID, pendingSetId);
                    i.putExtra(SearchActivity.PENDING_TRACK_NUMBER, mProvider.getCount());
                    context.startActivity(i);
                }
            });
            final TextView titleView = (TextView) holder.itemView.findViewById(R.id.title);
            final ImageButton editIcon = (ImageButton) holder.itemView.findViewById(R.id.edit_icon);
            titleView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        editIcon.setImageDrawable(
                                App.getContext().getResources().getDrawable(R.drawable.edit_icon));
                    } else {
                        editIcon.setImageDrawable(
                                App.getContext().getResources().getDrawable(R.drawable.done_icon));
                    }
                }
            });
            holder.itemView.findViewById(R.id.change_cover).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .items(R.array.photo_source)
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    /**
                                     * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected radio button to actually be selected.
                                     **/
                                    switch (which) {
                                        case 0: // Take photo
                                            Intent i = new Intent(App.getContext(), TakePhotoActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            context.startActivity(i);
                                            break;
                                        case 1: // Choose from gallery
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
            holder.itemView.findViewById(R.id.add_intro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .items(R.array.items)
                            .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    /**
                                     * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected radio button to actually be selected.
                                     **/

                                    return true;
                                }
                            })
                            .alwaysCallSingleChoiceCallback()
//                            .positiveText("Add")
                            .show();
                }
            });
            return;
        }

        final AbstractDataProvider.Data item = mProvider.getItem(position);

        // set listeners
        // (if the item is *not pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(mItemViewOnClickListener);

        // (if the item is *pinned*, click event comes to the mContainer)
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);
        ratingListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .customView(R.layout.rating_layout, wrapInScrollView)
                        .positiveText("OK")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                RatingBar ratingBar = (RatingBar) dialog.getCustomView().findViewById(R.id.rating_bar);
                                float rating = ratingBar.getRating();
                                TextView reviewNote = (TextView) dialog.getCustomView().findViewById(R.id.chat_row);
                                String text = reviewNote.getText().toString();
                                final String trackId = mProvider.getItem(position).getTrackId();
                                saveTrackReview(trackId, rating, text);


                                super.onPositive(dialog);
                            }
                        })
                        .show();

                final View view = dialog.getCustomView();
                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(0.5f);
                final EditText reviewNote = (EditText) view.findViewById(R.id.chat_row);


                final TextView reviewDate = (TextView) view.findViewById(R.id.review_date);
                reviewNote.requestFocus();
                final String trackId = mProvider.getItem(position).getTrackId();
                final Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);


                trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Track track = dataSnapshot.getValue(Track.class);
                        if (track != null) {
                            //     holder.description.setText("Duration: " + TimeUtils.getDurationBreakdown(track.getDuration()));
                            TextView songTitle =
                                    (TextView) view.findViewById(R.id.song_title);
                            songTitle.setText(track.track_title);
                            if (track.reviews != null) {
                                ratingBar.setRating(track.reviews.rating);
                                reviewNote.setText(track.reviews.note);
                                if (track.reviews.edit_date != null) {
                                    reviewDate.setText(TimeUtils.getTimeAgo(track.reviews.edit_date));
                                }
                            }


                        }
                    }


                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            private void saveTrackReview(String trackId, float rating, String reviewText) {
                Firebase reviewRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId).child("reviews");
                HashMap map = new HashMap();
                map.put("rating", String.valueOf(rating));
                map.put("note", reviewText);
                map.put("edit_date", TimeUtils.getCurrentTimestamp());
                reviewRef.updateChildren(map);

                notifyItemChanged(position);
            }
        };
        holder.mActionButton.setOnClickListener(ratingListener);
        holder.mRatingArea.setOnClickListener(ratingListener);
        // set text
        // holder.mTextView.setText(item.getText());

        final String trackId = mProvider.getItem(position).getTrackId();
        final Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);
        holder.mTrackNumber.setText("Track " + position);

        trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Track track = dataSnapshot.getValue(Track.class);
                if (track != null) {
                    //     holder.description.setText("Duration: " + TimeUtils.getDurationBreakdown(track.getDuration()));
                    holder.mTextView.setText(track.track_title);
                    if (track.reviews != null) {
                        holder.mNoteText.setText(track.reviews.note);
                        holder.mNoteText.setVisibility(View.VISIBLE);
                        holder.mActionButton.setText("Edit");
                        holder.mActionButton.setVisibility(View.GONE);
                        holder.mRatingArea.setVisibility(View.VISIBLE);
                        holder.mRatingBar.setRating(track.reviews.rating);
                        holder.mRatingBar.setVisibility(View.VISIBLE);
                    } else {
                        holder.mNoteText.setVisibility(View.GONE);
                        holder.mActionButton.setVisibility(View.VISIBLE);
                        holder.mActionButton.setText("Add Note");
                        holder.mRatingArea.setVisibility(View.GONE);
                    }

                    holder.mTrackDuration.setText(TimeUtils.getDurationBreakdown(track.getDuration()));
//                    UIHelper.setGrayScale(holder.mImageView);
                    if (!TextUtils.isEmpty(track.artwork_url)) {
                        Picasso.with(App.getContext()).load(track.artwork_url).into(holder.mImageView);
                    } else {
                        Picasso.with(App.getContext()).load(R.drawable.album_blank).into(holder.mImageView);
                    }
                }
                holder.innerContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayerController.getInstance().play(track);

                    }
                });
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        // set swiping properties
        holder.setSwipeItemSlideAmount(
                item.isPinnedToSwipeLeft() ? RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

        if (fromPosition == toPosition) {
            return;
        }

        mProvider.moveItem(fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        if (holder.getItemViewType() == MixDataProviderFragment.ITEM_VIEW_TYPE_SECTION_HEADER) {
            return false;
        }
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public int onGetSwipeReactionType(MyViewHolder holder, int position, int x, int y) {
        if (onCheckCanStartDrag(holder, position, x, y)) {
            return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
        } else {
            return mProvider.getItem(position).getSwipeReactionType();
        }
    }

    @Override
    public void onSetSwipeBackground(MyViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public int onSwipeItem(MyViewHolder holder, int position, int result) {
        Log.d(TAG, "onSwipeItem(result = " + result + ")");

        switch (result) {
            // swipe right
            case RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT:
                if (mProvider.getItem(position).isPinnedToSwipeLeft()) {
                    // pinned --- back to default position
                    return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
                } else {
                    // not pinned --- remove
                    return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
                }
                // swipe left -- pin
            case RecyclerViewSwipeManager.RESULT_SWIPED_LEFT:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION;
            // other --- do nothing
            case RecyclerViewSwipeManager.RESULT_CANCELED:
            default:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
        }
    }

    @Override
    public void onPerformAfterSwipeReaction(MyViewHolder holder, int position, int result, int reaction) {
        Log.d(TAG, "onPerformAfterSwipeReaction(result = " + result + ", reaction = " + reaction + ")");

        final AbstractDataProvider.Data item = mProvider.getItem(position);

        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            mProvider.removeItem(position);
            notifyItemRemoved(position);

            if (mEventListener != null) {
                mEventListener.onItemRemoved(position);
            }
        } else if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION) {
            item.setPinnedToSwipeLeft(true);
            notifyItemChanged(position);

            if (mEventListener != null) {
                mEventListener.onItemPinned(position);
            }
        } else {
            item.setPinnedToSwipeLeft(false);
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }
}
