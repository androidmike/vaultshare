package com.vaultshare.play;

import android.support.v4.app.Fragment;

/**
 * Created by mchang on 6/12/15.
 */
public class VaultFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_vault;
    }

    @Override
    public void initUI() {

    }

    public static Fragment newInstance() {
        return new VaultFragment();
    }
}
