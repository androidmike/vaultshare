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
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {

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
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "The Hype";
                    case 1:
                        return "Friends";
                    case 2:
                        return "Local";
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
                                android.R.color.black,
                                "http://www.istockphoto.com/image-zoom/2809325/3/380/269/zoom-2809325-3.jpg");
//                                "http://i.istockimg.com/file_thumbview_approve/21254133/6/stock-photo-21254133-volume-control-knob-on-eletric-guitar-vintage-mood.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                android.R.color.black,
//                                "http://www.istockphoto.com/image-zoom/1512806/3/380/252/zoom-1512806-3.jpg");
                                "http://www.istockphoto.com/image-zoom/39793/3/380/341/zoom-39793-3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                android.R.color.black,
//                                "http://www.istockphoto.com/image-zoom/1191587/3/380/285/zoom-1191587-3.jpg");
                                "http://www.istockphoto.com/image-zoom/46121864/3/380/259/zoom-46121864-3.jpg");
//                                "http://www.istockphoto.com/image-zoom/51842712/3/380/219/zoom-51842712-3.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
//                                "http://www.istockphoto.com/image-zoom/20722237/3/380/253/zoom-20722237-3.jpg");
                                "http://www.istockphoto.com/image-zoom/32255930/3/380/253/zoom-32255930-3.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.getViewPager().setCurrentItem(1);
    }

    @Override
    public void onPause() {
        ((MainActivity) getActivity()).setToolbarVisibility(View.VISIBLE);
        super.onPause();
    }
}
