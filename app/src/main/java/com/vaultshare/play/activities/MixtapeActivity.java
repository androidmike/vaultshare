package com.vaultshare.play.activities;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import com.vaultshare.play.R;

import butterknife.InjectView;
import se.emilsjolander.flipview.FlipView;

/**
 * Created by mchang on 6/25/15.
 */
public class MixtapeActivity extends BaseActivity {
    @InjectView(R.id.flip_view)
    FlipView flipView;
    @InjectView(R.id.top_title)
    TextView title;

    @Override
    public int getLayout() {
        return R.layout.activity_mixtape;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        getWindow().setStatusBarColor(Color.parseColor("#CCCCCC"));

    }

    @Override
    public void initUI() {
        MixtapeAdapter adapter = new MixtapeAdapter(this, null);
        flipView.setAdapter(adapter);
        flipView.setOnFlipListener(new FlipView.OnFlipListener() {
            @Override
            public void onFlippedToPage(FlipView flipView, int pos, long l) {
                if (pos == 0) {
                    title.setText("Jay-Z - Song Cry");
                } else {
                    title.setText("Radiohead - Creep");
                }
            }
        });

    }
}
