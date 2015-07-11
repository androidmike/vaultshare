package com.vaultshare.play;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vaultshare.play.model.Track;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.InjectView;

/**
 * Created by mchang on 7/9/15.
 */
public class RadioFragment extends BaseFragment {
//
//    @InjectView(R.id.rating)
//    ProgressBar ratingBar;

    @InjectView(R.id.dope_button)
    View dopeButton;

    @InjectView(R.id.nope_button)
    View nopeButton;

    @InjectView(R.id.image)
    ImageView image;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.sub_title)
    TextView subTitle;
    @InjectView(R.id.rating_bg_inverse)
    View     ratingBgInverse;
    @InjectView(R.id.rating_bg)
    View     ratingBg;

    @Override
    public int getLayout() {
        return R.layout.fragment_radio;
    }

    int rating    = 50;
    int influence = 5;

    String currentTrackId;

    @Override
    public void initUI() {
        currentTrackId = getArguments().getString(TrackAdapter.TRACK_ID);
//        ratingBar.setMax(100);
//        ratingBar.setProgress(rating);
        dopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating += influence;
                updateProgress(true);

            }
        });
        nopeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating -= influence;
                updateProgress(true);
            }
        });


        Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(currentTrackId);
        trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Track track = dataSnapshot.getValue(Track.class);
                if (track != null) {
                    Picasso.with(App.getContext()).load(track.artwork_url).into(image);

                    title.setText(track.track_title);
                    subTitle.setText(track.track_artist  + " #" + currentTrackId);
                    rating = track.rating;
                    if (track.rating == null) {
                        rating = 50;
                        updateProgress(true);
                    } else {
                        updateProgress(false);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        updateProgress(false);
    }

    private void updateProgress(boolean upload) {
        if (upload) {
            Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(currentTrackId);
            Map map = new HashMap();
            map.put("rating", rating);
            trackRef.updateChildren(map);

        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ratingBg.getLayoutParams();
        lp.weight = rating / 100f;
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) ratingBgInverse.getLayoutParams();
        lp2.weight = 1 - (rating / 100f);
        ratingBgInverse.setLayoutParams(lp2);
        ratingBg.setLayoutParams(lp);
        ratingBg.setBackgroundColor(UIHelper.getColor(
                rating / 100f));
        if (rating == 0) {
            Bus.getInstance().post(new SkipTrack(currentTrackId));
        }

        ratingBgInverse.setAlpha((100 - rating) / 100f);
        flicker(rating, image);

    }

    AlphaAnimation alpha;

    private void flicker(final int freq, final View view) {
        if (alpha != null) {
            alpha.cancel();
            alpha.reset();

        }
        alpha = new AlphaAnimation(1, 0);
        view.clearAnimation();

        if (freq > 40 || freq <= 0) {
            return;
        }
        int random = new Random().nextInt((int) (3000 * (freq / 100f)));

        alpha.setDuration(20);
        alpha.setStartOffset(random);
        view.startAnimation(alpha);
//        image.setAlpha(freq / 100f);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int random = new Random().nextInt((int) (3000 * (freq / 100f)));
                alpha.setStartOffset(random);
                view.startAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}