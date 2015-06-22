package com.vaultshare.play.fragments;

import android.os.Bundle;

import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.activities.LoginFragImpl;

/**
 * Created by mchang on 6/22/15.
 */
public class Login4 extends BaseFragment implements LoginFragImpl {

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Login4 newInstance() {
        Login4 f = new Login4();
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
        return R.layout.guide_4;
    }

    @Override
    public void initUI() {

    }


    @Override
    public void onPageSelected() {

    }
}
