package com.vaultshare.play;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.vaultshare.play.model.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by mchang on 6/13/15.
 */
public class ProfileVaultFragment extends BaseFragment {
    @InjectView(R.id.scrollView)
    ObservableScrollView mScrollView;

    public static Fragment newInstance() {
        return new ProfileVaultFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_profile_vault;
    }


    @Override
    public void initUI() {
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }
}
