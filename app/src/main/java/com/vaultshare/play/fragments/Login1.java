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
public class Login1 extends BaseFragment implements LoginFragImpl {

    @InjectView(R.id.video_view)
    VideoView videoView;

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static Login1 newInstance() {
        Login1 f = new Login1();
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
        return R.layout.guide_1;
    }

    @Override
    public void initUI() {
        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        try {
            String uri = "android.resource://" + App.getContext().getPackageName() + "/" + R.raw.driving;
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

    private void setDimension() {
        // Adjust the size of the video
        // so it fits on the screen
        float videoProportion = getVideoProportion();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenHeight / (float) screenWidth;
        android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();

        if (videoProportion < screenProportion) {
            lp.height = screenHeight;
            lp.width = (int) ((float) screenHeight / videoProportion);
        } else {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth * videoProportion);
        }
        videoView.setLayoutParams(lp);
    }

    /**
     * Height / width
     *
     * @return
     */
    private float getVideoProportion() {
        return 1.5f;
    }

    @Override
    public void onPageSelected() {

    }
}
