package com.vaultshare.play;

import android.support.v4.app.Fragment;

/**
 * Created by mchang on 6/13/15.
 */
public class DropFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    public void initUI() {

    }

    public static Fragment newInstance() {

        return new DropFragment();
    }
}
