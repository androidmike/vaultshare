package com.vaultshare.play;

import android.support.v4.app.Fragment;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import butterknife.InjectView;

/**
 * Created by mchang on 6/13/15.
 */
public class MerchFragment extends BaseFragment {
    @InjectView(R.id.scrollView)
    ObservableScrollView mScrollView;

    public static Fragment newInstance() {
        return new MerchFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_profile_merch;
    }


    @Override
    public void initUI() {
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }
}
