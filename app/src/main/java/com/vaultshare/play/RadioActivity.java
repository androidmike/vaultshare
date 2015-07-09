package com.vaultshare.play;

import android.view.View;

import com.vaultshare.play.activities.BaseActivity;

import butterknife.InjectView;

/**
 * Created by mchang on 7/9/15.
 */
public class RadioActivity extends BaseActivity {
    @InjectView(R.id.hiphop)
    View hiphop;
    @InjectView(R.id.edm)
    View edm;

    @Override
    public int getLayout() {
        return R.layout.activity_rotation_choose;
    }

    @Override
    public void initUI() {
        hiphop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
