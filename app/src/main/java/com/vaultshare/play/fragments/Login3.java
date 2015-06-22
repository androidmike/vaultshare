package com.vaultshare.play.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.activities.LoginFragImpl;

import java.util.Random;

import butterknife.InjectView;

/**
 * Created by mchang on 6/22/15.
 */
public class Login3 extends BaseFragment implements LoginFragImpl {

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Login3 newInstance() {
        Login3 f = new Login3();
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
        return R.layout.guide_3;
    }

    @Override
    public void initUI() {

    }


    @Override
    public void onPageSelected() {

    }
}
