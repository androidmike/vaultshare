package com.vaultshare.play.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.VideoView;

import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.activities.LoginFragImpl;

import java.util.Random;

import butterknife.InjectView;

/**
 * Created by mchang on 6/22/15.
 */
public class Login2 extends BaseFragment implements LoginFragImpl {
    @InjectView(R.id.white_notes)
    View introDisc;
    @InjectView(R.id.white_notes2)
    View introDisc2;
    @InjectView(R.id.white_notes3)
    View introDisc3;
    @InjectView(R.id.white_notes4)
    View introDisc4;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Login2 newInstance() {
        Login2 f = new Login2();
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
        return R.layout.guide_2;
    }

    @Override
    public void initUI() {
        animateDisc(introDisc);
        animateDisc(introDisc2);
        animateDisc(introDisc4);
        animateDisc(introDisc3);
    }

    private void animateDisc(View disc) {
        disc.setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(false);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(200);
        alpha.setFillAfter(true);
        set.addAnimation(alpha);
        set.setStartOffset(new Random().nextInt(1000));
        TranslateAnimation translate = new TranslateAnimation(0, 600, 0, 0);
        translate.setDuration(2000);
        set.addAnimation(translate);
        set.setDuration(2000);

        alpha.setRepeatCount(Animation.INFINITE);
        translate.setRepeatCount(Animation.INFINITE);
        translate.setInterpolator(new DecelerateInterpolator());
        disc.startAnimation(set);
    }

    @Override
    public void onPageSelected() {

    }
}
