package com.vaultshare.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.vaultshare.play.fragments.ScrollFragment;

/**
 * Created by mchang on 6/13/15.
 */
public class MixFragment extends BaseFragment {
    public static Fragment newInstance() {
        return new MixFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_mix;
    }

    @Override
    public void initUI() {

    }

    private ObservableScrollView mScrollView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }
}
