package com.vaultshare.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by mchang on 6/12/15.
 */
public class DoorFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_door;
    }

    @Override
    public void initUI() {

    }

    public static Fragment newInstance(Bundle bundle) {
        DoorFragment fragment =  new DoorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
