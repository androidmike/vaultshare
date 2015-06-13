package com.vaultshare.play;

import android.support.v4.app.Fragment;

/**
 * Created by mchang on 6/12/15.
 */
public class DJProfileFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_dj_profile;
    }

    @Override
    public void initUI() {

    }

    public static Fragment newInstance() {
        return new DJProfileFragment();
    }
}
