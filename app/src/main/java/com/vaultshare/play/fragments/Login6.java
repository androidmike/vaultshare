package com.vaultshare.play.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.vaultshare.play.App;
import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.R;
import com.vaultshare.play.activities.LoginFragImpl;

import butterknife.InjectView;

/**
 * Created by mchang on 6/22/15.
 */
public class Login6 extends BaseFragment implements LoginFragImpl {


    @InjectView(R.id.video_view)
    VideoView videoView;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Login6 newInstance() {
        Login6 f = new Login6();
        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayout() {
        return R.layout.guide_6;
    }

    @Override
    public void initUI() {
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        try {
            String uri = "android.resource://" + App.getContext().getPackageName() + "/" + R.raw.v_bouncing_000010604598;
            videoView.setVideoURI(Uri.parse(uri));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoView.start();
        } catch (Exception e) {

        }
    }



    @Override
    public void onPageSelected() {

    }
}
