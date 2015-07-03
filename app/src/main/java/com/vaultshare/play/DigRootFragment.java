package com.vaultshare.play;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.florent37.materialviewpager.HeaderDesign;
import com.github.florent37.materialviewpager.MaterialViewPager;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class DigRootFragment extends BaseFragment {

    @InjectView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    Toolbar mFragmentToolbar;
    Toolbar mBaseToolbar;

    @Override
    public void onResume() {
        super.onResume();
        mBaseToolbar = ((MainActivity) getActivity()).getToolbar();
        ((MainActivity) getActivity()).setToolbarVisibility(View.GONE);
        mFragmentToolbar = mViewPager.getToolbar(); // Handle local toolbar
        mFragmentToolbar.setNavigationIcon(R.drawable.menu_red_64);
        mFragmentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.getInstance().post(new DrawerToggle());
            }
        });

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#222222"));
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_browse_stations;
    }

    @Override
    public void initUI() {
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Fragment fragment = FollowingFragment.newInstance();
                        Bundle args = new Bundle();
                        args.putSerializable(FollowingFragment.LISTING_TYPE, FollowingFragment.ListingType.FOLLOWING);
                        fragment.setArguments(args);
                        return fragment;
                    case 1:
                        Fragment fragment1 = FollowingFragment.newInstance();
                        Bundle args1 = new Bundle();
                        args1.putSerializable(FollowingFragment.LISTING_TYPE, FollowingFragment.ListingType.HYPE);
                        fragment1.setArguments(args1);
                        return fragment1;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Following";
                    case 1:
                        return "The Hype";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.MaterialViewPagerListener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#000000"), null);
//                        return HeaderDesign.fromColorAndDrawable(
//                                Color.parseColor("#000000"), getResources().getDrawable(R.drawable.istock_speakers));
                    case 1:

                        return HeaderDesign.fromColorAndDrawable(Color.parseColor("#000000"), null);
//                        return HeaderDesign.fromColorAndDrawable(
//                                Color.parseColor("#000000"), getResources().getDrawable(R.drawable.istock_arms_dj));
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getPagerTitleStrip().setTextColor(Color.parseColor("#99FFFFFF"));
        mViewPager.getViewPager().setCurrentItem(1);
    }

    @Override
    public void onPause() {
        ((MainActivity) getActivity()).setToolbarVisibility(View.VISIBLE);
        super.onPause();
    }
}
