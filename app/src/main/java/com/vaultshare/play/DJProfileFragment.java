package com.vaultshare.play;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.florent37.materialviewpager.HeaderDesign;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.vaultshare.play.activities.ProfileActivity;
import com.vaultshare.play.fragments.FanClubFragment;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class DJProfileFragment extends BaseFragment {

    @InjectView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    Toolbar mFragmentToolbar;
    Toolbar mBaseToolbar;

    @Override
    public void onResume() {
        super.onResume();
        mBaseToolbar = ((ProfileActivity) getActivity()).getToolbar();
        ((ProfileActivity) getActivity()).setToolbarVisibility(View.GONE);
        mFragmentToolbar = mViewPager.getToolbar(); // Handle local toolbar
        mFragmentToolbar.setNavigationIcon(R.drawable.back_icon_white_64);
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
        return R.layout.fragment_profile;
    }

    @Override
    public void initUI() {
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ProfileVaultFragment.newInstance();
                    case 1:
                        return InterviewFragment.newInstance();
                    case 2:
                        return TweetFragment.newInstance();
                    case 3: return FanClubFragment.newInstance();

                    case 4: return FanClubFragment.newInstance();
                    case 5:
                        return MerchFragment.newInstance();
                    default:
                        return TweetFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Vault";
                    case 1:
                        return "Interview";
                    case 2:
                        return "Twitter";
                    case 3:
                        return "Fan Club";
                    case 4:
                        return "Events";
                    case 5:
                        return "Merchandise";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.MaterialViewPagerListener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:

                        return HeaderDesign.fromColorAndDrawable(
                                Color.parseColor("#691999"), null);
                    case 1:
                        return HeaderDesign.fromColorAndDrawable(
                                Color.parseColor("#691999"), null);
                    case 2:
                        return HeaderDesign.fromColorAndDrawable(
                                Color.parseColor("#691999"), null);
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
        ((ProfileActivity) getActivity()).setToolbarVisibility(View.VISIBLE);
        super.onPause();
    }

    public static Fragment newInstance() {

        return new DJProfileFragment();
    }
}
