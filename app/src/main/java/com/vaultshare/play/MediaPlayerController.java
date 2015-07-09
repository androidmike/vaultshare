package com.vaultshare.play;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.vaultshare.play.model.PlayState;
import com.vaultshare.play.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mchang on 6/9/15.
 */
public class MediaPlayerController implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    static MediaPlayerController mediaPlayerController;

    int         length             = 0;
    int         initTrackPosition  = 0;
    int         currentTrackNumber = 0;
    MediaPlayer mMediaPlayer       = null;
    List<Track> tracks             = new ArrayList<>();


    public MediaPlayerController() {
    }

    public static MediaPlayerController getInstance() {
        if (mediaPlayerController == null) {
            mediaPlayerController = new MediaPlayerController();
        }
        return mediaPlayerController;
    }

    /**
     * Playing track from tracks starting from play state.
     * - It is possible start track number is not 0.
     * - Tracks can be updated and play state adjusted, then call this function again to restart at
     * correct position.
     *
     * @param startState
     * @param trackList
     * @throws IOException
     */
    public void play(PlayState startState, final List<Track> trackList) throws IOException {
        restartMediaPlayer();

        currentTrackNumber = startState.getTrackNumber();
        initTrackPosition = (int) startState.getPositionMs();
        tracks = new ArrayList<>(trackList);
        Track initTrack = tracks.get(currentTrackNumber);
        currentTrack = initTrack;
        mMediaPlayer.setDataSource(
                String.format(App.getContext().getString(R.string.soundcloud_stream_url), initTrack.src_id,
                        App.getContext().getString(R.string.soundcloud_id)));

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
    }

    private void restartMediaPlayer() {
        stopPlaying();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void stopPlaying() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            Bus.getInstance().post(new TrackStopped(currentTrack));
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.seekTo(initTrackPosition);
        mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                length = mp.getDuration();
                mp.start();
                observer = new MediaObserver();
                Bus.getInstance().post(new TrackStarted(currentTrack));
                new Thread(observer).start();
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Bus.getInstance().post(new TrackStopped(currentTrack));
        if (observer != null) {
            observer.stop();
        }
        // Increment track number
        currentTrackNumber++;
        // If past the end, set is over
        if (currentTrackNumber == tracks.size()) {
            Toast.makeText(App.getContext(), "Set over", Toast.LENGTH_SHORT);
            return;
        }

        // Play next song
        PlayState playState = new PlayState(currentTrackNumber, 0);
        try {
            play(playState, tracks);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //((ProgressBar) findViewById(R.id.pb)).setProgress(mp.getCurrentPosition());
    }

    Track currentTrack;

    public void play(Track track) {
        restartMediaPlayer();

        try {
            mMediaPlayer.setDataSource(
                    String.format(App.getContext().getString(R.string.soundcloud_stream_url), track.src_id,
                            App.getContext().getString(R.string.soundcloud_id)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(this);
//        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
        currentTrack = track;
    }


    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
//                if (((ProgressBar) findViewById(R.id.pb)) != null) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (length != 0) {
//                                ((ProgressBar) findViewById(R.id.pb)).setProgress(
//                                        (int) (((mMediaPlayer.getCurrentPosition() * 1f) / length) * 100));
//                            }
//                        }
//                    });
//                FirebaseController.getInstance().onPlayPositionUpdate("1926", mMediaPlayer.getCurrentPosition());
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private MediaObserver observer = null;
}
