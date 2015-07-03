package com.vaultshare.play.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.vaultshare.play.App;
import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.activities.LoginFragImpl;

import java.util.Random;

import butterknife.InjectView;

/**
 * Created by mchang on 6/22/15.
 */
public class Login5 extends BaseFragment implements LoginFragImpl {

    @InjectView(R.id.map)
    ImageView map;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Login5 newInstance() {
        Login5 f = new Login5();
        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayout() {
        return R.layout.guide_5;
    }

    @Override
    public void initUI() {

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 3f, 1f, 3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(8000);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        map.startAnimation(scaleAnimation);


    }


    @Override
    public void onPageSelected() {

    }
}
