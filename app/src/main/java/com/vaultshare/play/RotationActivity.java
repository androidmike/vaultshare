package com.vaultshare.play;

import android.content.Intent;
import android.view.View;

import com.vaultshare.play.activities.BaseActivity;

import butterknife.InjectView;

/**
 * Created by mchang on 7/9/15.
 */
public class RotationActivity extends BaseActivity {
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
                Intent i = new Intent(App.getContext(), RadioActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        edm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(App.getContext(), RadioActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
