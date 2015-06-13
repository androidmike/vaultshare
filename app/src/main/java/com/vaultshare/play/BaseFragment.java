package com.vaultshare.play;

/**
 * Created by mchang on 6/9/15.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by mchang on 6/9/15.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by mchang on 2/12/15.
 */
public abstract class BaseFragment extends Fragment {
    protected ViewGroup container;
    protected LayoutInflater inflater = (LayoutInflater) App.getContext().getSystemService
            (Context.LAYOUT_INFLATER_SERVICE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Setup View
        View rootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.inject(this, rootView);
        this.container = container;
        return rootView;
    }

    public abstract int getLayout();

    @Override
    public void onViewCreated(View view, Bundle saved) {
        super.onViewCreated(view, saved);
        initUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * UI Initialization
     * Called after onAttach when activity is available
     */
    public abstract void initUI();

    public String getLogTag() {
        return this.getClass().getName();
    }
}
