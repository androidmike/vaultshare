package com.vaultshare.play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
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
public class MixRootFragment extends BaseFragment {
    @InjectView(R.id.materialViewPager)
    MaterialViewPager mViewPager;
    Toolbar           mFragmentToolbar;
    Toolbar           mBaseToolbar;

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
        return R.layout.fragment_dj;
    }

    @Override
    public void initUI() {
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return MixFragment.newInstance();
                    case 1:
                        return DropFragment.newInstance();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Mix";
                    case 1:
                        return "Drop";
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
                                android.R.color.white,
//                                "http://www.istockphoto.com/image-zoom/55863678/3/380/285/zoom-55863678-3.jpg");
                                "http://www.istockphoto.com/image-zoom/71892/3/380/229/zoom-71892-3.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                android.R.color.white,
                                "http://www.istockphoto.com/image-zoom/8384717/3/380/228/zoom-8384717-3.jpg");
//                                "http://www.istockphoto.com/image-zoom/40825544/3/380/253/zoom-40825544-3.jpg");
//                                "http://i.istockimg.com/file_thumbview_approve/1222202/6/stock-photo-1222202-dj-spinning-black-and-white.jpg");
//                                "http://www.istockphoto.com/image-zoom/39793/3/380/341/zoom-39793-3.jpg");

                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.getViewPager().setCurrentItem(0);
    }

    @Override
    public void onPause() {
        ((MainActivity) getActivity()).setToolbarVisibility(View.VISIBLE);
        super.onPause();
    }
}
