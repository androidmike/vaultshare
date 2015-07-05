package com.vaultshare.play;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vaultshare.play.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideoCaptureActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {
    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        recorder = new MediaRecorder();
        initRecorder();
        setContentView(R.layout.video_capture);

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);
    }

    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_LOW);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile(PATH);
        recorder.setMaxDuration(50000); // 50 seconds
        recorder.setVideoFrameRate(5);
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    String PATH = "/sdcard/videocapture_example.mp4";

    public void onClick(View v) {
        if (recording) {
            recorder.stop();
            recording = false;

            // URI to your video file
            Uri myVideoUri = Uri.fromFile(new File(PATH));
// MediaMetadataRetriever instance
            MediaMetadataRetriever mmRetriever = new MediaMetadataRetriever();
            mmRetriever.setDataSource(PATH);

// Array list to hold your frames
            ArrayList<Bitmap> frames = new ArrayList<Bitmap>();

//Create a new Media Player
            MediaPlayer mp = MediaPlayer.create(getBaseContext(), myVideoUri);

// Some kind of iteration to retrieve the frames and add it to Array list
            try {
                for (int i = 0; i < 200; i++) {
                    Bitmap bitmap = mmRetriever.getFrameAtTime(i * 1000 * 500);
//                    Bitmap converted = bitmap.copy(Bitmap.Config.RGB_565, false);
                    frames.add(bitmap);
                }
            } catch (Exception e) {

            }
            AnimationDrawable animatedGIF = new AnimationDrawable();

            for (int i = 0; i < frames.size(); i++) {
                animatedGIF.addFrame(new BitmapDrawable(getResources(), frames.get(i)), 50);
            }

            ImageView gif = (ImageView) findViewById(R.id.gif);
            gif.setImageDrawable(animatedGIF);
            animatedGIF.start();
            // Let's initRecorder so we can record again
            initRecorder();
            prepareRecorder();
        } else {
            recording = true;
            recorder.start();
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        prepareRecorder();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
        finish();
    }
}