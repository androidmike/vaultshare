package com.vaultshare.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

/**
 * Created by mchang on 6/13/15.
 */
public class InterviewFragment extends BaseFragment {

    private ObservableScrollView mScrollView;

    public static Fragment newInstance() {
        return new InterviewFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_interview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }


    @Override
    public void initUI() {
    }
}
 