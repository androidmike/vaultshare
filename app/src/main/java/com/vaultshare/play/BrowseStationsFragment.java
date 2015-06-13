package com.vaultshare.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.florent37.materialviewpager.HeaderDesign;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.vaultshare.play.fragments.ScrollFragment;
import com.vaultshare.play.fragments.WebViewFragment;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class BrowseStationsFragment extends BaseFragment {

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
        mFragmentToolbar.setNavigationIcon(R.drawable.com_facebook_button_icon);
        mFragmentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bus.getInstance().post(new DrawerToggle());
            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_browse_stations;
    }

    @Override
    public void initUI() {
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ExploreFragment.newInstance();
                    case 1:
                        return ExploreFragment.newInstance();
                    case 2:
                        return WebViewFragment.newInstance();
                    default:
                        return ScrollFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Cosigns";
                    case 1:
                        return "Charts";
                    case 2:
                        return "Friends";
                    case 3:
                        return "Around";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.MaterialViewPagerListener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.getViewPager().setCurrentItem(1);
    }

}
