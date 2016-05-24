package com.vaultshare.play;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.otto.Subscribe;
import com.vaultshare.play.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mchang on 6/9/15.
 */
public class MediaPlayerController implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    static MediaPlayerController mediaPlayerController;

    int         length             = 0;
    int         currentTrackNumber = 0;
    MediaPlayer mMediaPlayer       = null;
    List<String> trackIds;
    String       liveTrackStartTime;
    HashMap<String, Track> trackMap = new HashMap();
    String liveSetId;

    public MediaPlayerController() {
        Bus.getInstance().register(this);
    }

    public static MediaPlayerController getInstance() {
        if (mediaPlayerController == null) {
            mediaPlayerController = new MediaPlayerController();
        }
        return mediaPlayerController;
    }


    public void joinHipHop() {


        final Firebase station = FirebaseController.getInstance().getRef().child("stations").child("-JtkMyTMpUG_yO2xWJqH");
        // Join set, add self as live user
        Map map = new HashMap();
        map.put("facebook:10105340362088383", TimeUtils.getCurrentTimestamp());
        station.child("live_users").updateChildren(map);

        /** Get position of current track and current track index **/
        station.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                Map<String, Object> attributes = (Map<String, Object>) snapshot.getValue();
                liveSetId = (String) attributes.get("live_set");
                Long liveTrackNumber = (Long) attributes.get("live_track_index");

                liveTrackStartTime = (String) attributes.get("live_track_start_time");

                if (liveTrackStartTime == null) {
                    liveTrackStartTime = TimeUtils.getCurrentTimestamp();
                    HashMap map = new HashMap();
                    map.put("live_track_start_time", liveTrackStartTime);
                    station.updateChildren(map);
                }
                if (null == liveTrackNumber) {
                    liveTrackNumber = Long.valueOf(0);
                    HashMap map = new HashMap();
                    map.put("live_track_index", liveTrackNumber);
                    station.updateChildren(map);
                }
                currentTrackNumber = liveTrackNumber.intValue();

                getSnapshotTrackIds(liveSetId);
            }

            private void getSnapshotTrackIds(String liveSetId) {
                Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(liveSetId).child("tracks");
                Query queryRef = setRef.orderByChild("order");
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map tracksMap = (Map) dataSnapshot.getValue();
                        trackIds = new ArrayList(tracksMap.keySet());

                        for (String trackId : trackIds) {
                            retrieveNewTrack(trackId, true);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        station.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("live_track_start_time")) {
                    liveTrackStartTime = (String) dataSnapshot.getValue();
                }

                if (dataSnapshot.getKey().equals("live_track_index")) {
                    Long liveTrackNumber = (Long) dataSnapshot.getValue();
                    currentTrackNumber = liveTrackNumber.intValue();
                    currentTrack = trackMap.get(trackIds.get(liveTrackNumber.intValue()));
                }

                play(trackIds.get(currentTrackNumber),
                        (int) TimeUtils.getTimeSince(liveTrackStartTime));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Snapshot get track (full)
     *
     * @param trackId
     * @param firstTime If first time, then will broadcast fully loaded
     */
    private void retrieveNewTrack(final String trackId, final boolean firstTime) {
        Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);
        trackRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Track track = dataSnapshot.getValue(Track.class);

                        trackMap.put(trackId, track);


                        if (firstTime && trackIds.size() == trackMap.size()) {
                            // Retrieved
                            Bus.getInstance().post(new TrackRetrieved());
                            int trackPositionMs = (int) TimeUtils.getTimeSince(liveTrackStartTime);


                            MediaPlayerController.getInstance().play(trackIds.get(currentTrackNumber), trackPositionMs);
                        }

                        if (!firstTime) {
                            Bus.getInstance().post(new TrackAdded());
                        }
                    }


                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }

        );
    }

    /**
     * After initializing
     *
     * @param e
     */
    @Subscribe
    public void onTrackRetrieved(TrackRetrieved e) {
        Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(liveSetId).child("tracks");
        Query queryRef = setRef.orderByChild("order");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String trackId = dataSnapshot.getKey();
                if (trackIds.contains(trackId)) {
                    return;
                } else {
                    trackIds.add(trackId);
                    retrieveNewTrack(trackId, false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int j = 0;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                trackIds.clear();
//                joinHipHop();
                String trackId = dataSnapshot.getKey();
//                if (trackIds.remove(trackId)) {
                Bus.getInstance().post(new TrackRemoved());
//                }

//                joinHipHop();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    int initTrackPosition;

    private void play(final String trackId, long time) {
        if (trackMap.get(trackId) == null) {
            // Error, track has not been loaded
            return;
        }
        restartMediaPlayer();

        currentTrack = trackMap.get(trackId);
        initTrackPosition = (int) time;
        try {
            mMediaPlayer.setDataSource(
                    String.format(App.getContext().getString(R.string.soundcloud_stream_url), currentTrack.src_id,
                            App.getContext().getString(R.string.soundcloud_id)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();


        Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);
        trackRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                String key = dataSnapshot.getKey();
                if (key.equals("rating")) {
                    int value = ((Long) dataSnapshot.getValue()).intValue();
                    // Song got knocked out
                    if (value <= 0) {
                        // Remove track
                        removeTrack(trackId);

                        // If this is the song currently playing
                        if (trackIds.get(currentTrackNumber) == trackId) {
                            final Firebase station = FirebaseController.getInstance().getRef().child("stations").child("-JtkMyTMpUG_yO2xWJqH");
                            liveTrackStartTime = TimeUtils.getCurrentTimestamp();
                            currentTrackNumber++;
                            if (trackIds.size() == currentTrackNumber) {
                                currentTrackNumber = 0;
                            }
                            HashMap map = new HashMap();
                            map.put("live_track_start_time", liveTrackStartTime);
                            map.put("live_track_index", currentTrackNumber);
                            station.updateChildren(map);
                        }

                        // Go to next track
//                        onCompletion(mMediaPlayer);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void removeTrack(String trackId) {
        Firebase tracksRef = FirebaseController.getInstance().getRef().child("sets").child(liveSetId).child("tracks");
        HashMap map = new HashMap();
        map.put(trackId, "skip");
        tracksRef.updateChildren(map);
    }

    /**
     * Playing track from tracks starting from play state.
     * - It is possible start track number is not 0.
     * - Tracks can be updated and play state adjusted, then call this function again to restart at
     * correct position.
     *
     * @throws IOException
     */
//    public void play(Track startState, final int trackList) throws IOException {
//        restartMediaPlayer();
//
//        currentTrackNumber = startState.getTrackNumber();
//        initTrackPosition = (int) startState.getPositionMs();
//        tracks = new ArrayList<>(trackList);
//        Track initTrack = tracks.get(currentTrackNumber);
//        currentTrack = initTrack;
//        mMediaPlayer.setDataSource(
//                String.format(App.getContext().getString(R.string.soundcloud_stream_url), initTrack.src_id,
//                        App.getContext().getString(R.string.soundcloud_id)));
//
//        mMediaPlayer.setOnPreparedListener(this);
//        mMediaPlayer.setOnCompletionListener(this);
//        mMediaPlayer.prepareAsync();
//    }
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
                Bus.getInstance().post(new TrackStarted(currentTrack, currentTrackNumber, trackIds.get(currentTrackNumber)));
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
        if (currentTrackNumber == trackIds.size()) {
            Toast.makeText(App.getContext(), "Set over", Toast.LENGTH_SHORT);
            currentTrackNumber = 0;
        }

        if (trackMap.get(trackIds.get(currentTrackNumber)).rating <= 0) {
            // Note that set must have at least 2
            currentTrackNumber++;
        }


        final Firebase station = FirebaseController.getInstance().getRef().child("stations").child("-JtkMyTMpUG_yO2xWJqH");
        liveTrackStartTime = TimeUtils.getCurrentTimestamp();
        HashMap map = new HashMap();
        map.put("live_track_start_time", liveTrackStartTime);
        map.put("live_track_index", currentTrackNumber);
        station.updateChildren(map);


        play(trackIds.get(currentTrackNumber), 0);


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

    @Subscribe
    public void onMasterSkip(MasterSkip e) {
        onCompletion(mMediaPlayer);
    }

    private MediaObserver observer = null;
}
