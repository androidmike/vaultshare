package com.vaultshare.play.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.facebook.login.LoginFragment;
import com.vaultshare.play.App;
import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.fragments.Login1;
import com.vaultshare.play.fragments.Login2;
import com.vaultshare.play.fragments.Login3;
import com.vaultshare.play.fragments.Login4;
import com.vaultshare.play.fragments.Login5;
import com.vaultshare.play.fragments.Login6;

import butterknife.InjectView;

/**
 * Created by mchang on 6/11/15.
 */
public class LoginActivity extends BaseActivity {
    @InjectView(R.id.dot_1)
    ImageView dot1;
    @InjectView(R.id.dot_2)
    ImageView dot2;
    @InjectView(R.id.dot_3)
    ImageView dot3;
    @InjectView(R.id.dot_4)
    ImageView dot4;
    LoginPagerAdapter mAdapter;

    @InjectView(R.id.pager)
    ViewPager mPager;

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initUI() {
        mAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);


        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(position));
//                ((LoginFragImpl) fragment).onPageSelected();

                switch (position) {
                    case 0:
                        dot1.setImageResource(R.drawable.dot_selected);
                        dot2.setImageResource(R.drawable.dot);
                        dot3.setImageResource(R.drawable.dot);
                        dot4.setImageResource(R.drawable.dot);
                        break;

                    case 1:
                        dot1.setImageResource(R.drawable.dot);
                        dot2.setImageResource(R.drawable.dot_selected);
                        dot3.setImageResource(R.drawable.dot);
                        dot4.setImageResource(R.drawable.dot);
                        break;

                    case 2:
                        dot1.setImageResource(R.drawable.dot);
                        dot2.setImageResource(R.drawable.dot);
                        dot3.setImageResource(R.drawable.dot_selected);
                        dot4.setImageResource(R.drawable.dot);
                        break;

                    case 3:
                        dot1.setImageResource(R.drawable.dot);
                        dot2.setImageResource(R.drawable.dot);
                        dot3.setImageResource(R.drawable.dot);
                        dot4.setImageResource(R.drawable.dot_selected);
                        break;

                    default:
                        break;
                }


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    public static class LoginPagerAdapter extends FragmentStatePagerAdapter {
        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Login1.newInstance();
                case 1:
                    return Login2.newInstance();
                case 2:
                    return Login3.newInstance();
                case 3:
                    return Login4.newInstance();
                case 4:
                    return Login5.newInstance();
                case 5:
                    return Login6.newInstance();

            }
            return Login1.newInstance();
        }
    }


}
