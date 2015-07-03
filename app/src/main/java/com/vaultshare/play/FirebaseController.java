package com.vaultshare.play;

import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.vaultshare.play.model.PlayState;
import com.vaultshare.play.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mchang on 6/8/15.
 */
public class FirebaseController {
    private static final String TAG = FirebaseController.class.getCanonicalName();
    static FirebaseController firebaseController;
    Firebase ref;

    public Query getStationsRef() {
        return ref.child("stations");
    }

    public void updateTrackInfo(final SoundCloudTrackResp soundCloudTrackResp) {
        ref.child("tracks").orderByChild("src_id").equalTo(soundCloudTrackResp.getId())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map tracksMap = (Map) dataSnapshot.getValue();
                                List<String> trackIds = new ArrayList(tracksMap.keySet());
                                if (trackIds.size() >= 1) {
                                    for (String id : trackIds) {
                                        Firebase trackRef = ref.child("tracks").child(id);
                                        HashMap map = new HashMap();
                                        map.put("duration", soundCloudTrackResp.getDuration());
                                        trackRef.updateChildren(map);
                                    }
                                } else {
                                    Log.e(TAG, String.format("Attempted to update track info for %s %s but not found in DB",
                                            soundCloudTrackResp.getId(), Track.Source.SOUNDCLOUD.name()));
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        }
                );
    }

    public Firebase getUserRefs() {
        return FirebaseController.getInstance().getRef().child("users");
    }

    public Query getFollowingStationsRef() {
        return getRef().child("followed_by_" + SessionController.getInstance().getSession().getUid());

    }

    public Query getHypeStationsRef() {
        return getRef().child("hyped_stations");
    }

    public enum Source {
        SPOTIFY, SOUNDCLOUD, VS;
    }

    public static FirebaseController getInstance() {
        if (firebaseController == null) {
            firebaseController = new FirebaseController();
        }
        return firebaseController;
    }


    public FirebaseController() {
        ref = new Firebase("https://jenina.firebaseio.com/");
    }

    public void addTestSets() {
        Firebase setRef = FirebaseController.getInstance().getRef().child("sets").push();
        HashMap map = new HashMap();
        map.put("last_updated", TimeUtils.getCurrentTimestamp());
        map.put("creator", SessionController.getInstance().getSession().getUid());
        setRef.setValue(map);

        Firebase userRef = FirebaseController.getInstance().getRef().child("users")
                .child(SessionController.getInstance().getSession().getUid());
        HashMap map2 = new HashMap();
        map2.put(setRef.getKey(), TimeUtils.getCurrentTimestamp());
        userRef.child("sets").setValue(map2);


        Firebase tracksRef = setRef.child("tracks");

        HashMap tracksMap = new HashMap();
        tracksMap.put("src_id", 7399237);
        tracksMap.put("src", Source.SOUNDCLOUD.name());
        tracksMap.put("order", 0);
        tracksMap.put("added_time", TimeUtils.getCurrentTimestamp());
        tracksRef.push().setValue(tracksMap);

        tracksMap.put("src_id", 7399247);
        tracksMap.put("src", Source.SOUNDCLOUD.name());
        tracksMap.put("order", 1);
        tracksRef.push().setValue(tracksMap);

        tracksMap.put("src_id", 4299237);
        tracksMap.put("src", Source.SOUNDCLOUD.name());
        tracksMap.put("order", 2);
        tracksRef.push().setValue(tracksMap);

        tracksMap.put("src_id", 2399237);
        tracksMap.put("src", Source.SOUNDCLOUD.name());
        tracksMap.put("order", 3);
        tracksRef.push().setValue(tracksMap);
    }

    public void getStations() {
        Firebase tracksRef = FirebaseController.getInstance().getRef().child("stations");
        Query queryRef = tracksRef.orderByChild("order");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
                //  bus.post(new StationAdded(value));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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


    public void getSet(String setId) {
        Firebase tracksRef = FirebaseController.getInstance().getRef().child("sets").child(setId);
        Query queryRef = tracksRef.orderByChild("order");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    public void getAllTracks() {
        Firebase tracksRef = FirebaseController.getInstance().getRef().child("tracks");
        Query queryRef = tracksRef.orderByChild("order");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    public Firebase getRef() {
        return ref;
    }

    /**
     * Test script
     */
    public void testCreateRoomTracksSets() {
        //Create tracks
        List trackIds = new ArrayList();
//        trackIds.add(addTrack(TimeUtils.getCurrentTimestamp(), SessionController.getInstance().getSession().getUid(),
//                Track.Source.SOUNDCLOUD, String.valueOf(63500636)));
//        trackIds.add(addTrack(TimeUtils.getCurrentTimestamp(), SessionController.getInstance().getSession().getUid(),
//                Track.Source.SOUNDCLOUD, String.valueOf(183347321)));

        String stationId = createStation("A new station", "A new description");

        addSet(stationId, trackIds);
    }


    public void joinSet(String stationId) {
        if (stationId == null) {
            Toast.makeText(App.getContext(), "Cannot join live set", Toast.LENGTH_SHORT).show();
            return;
        }
        final Firebase station = FirebaseController.getInstance().getRef().child("stations").child(stationId);
        Map map = new HashMap();
        map.put(SessionController.getInstance().getSession().getUid(), TimeUtils.getCurrentTimestamp());
        station.child("live_users").updateChildren(map);

        station.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                Map<String, Object> attributes = (Map<String, Object>) snapshot.getValue();
                final String liveSetId = (String) attributes.get("live_set");
                final String liveSetStartTime = (String) attributes.get("live_set_start_time");

                getTracksInSet(liveSetId, new RetrieveTracksIdsInSetCallBack() {
                    @Override
                    public void onTracksIdsRetrieved(List<String> trackIds) {
                        for (String tid : trackIds) {
                            Firebase trackRef = ref.child("tracks").child(tid);
                        }
                    }
                });

                ref.child("tracks").orderByChild("set_id").equalTo(liveSetId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get duration of entire set
                                Map<String, Map> valMap = (Map<String, Map>) dataSnapshot.getValue();

                                // Convert to beans for local use
                                List<Track> tracks = new ArrayList<Track>();
                                for (Map.Entry<String, Map> entry : valMap.entrySet()) {
                                    Track track = dataSnapshot.child(entry.getKey()).getValue(Track.class);
                                    tracks.add(track);
                                }
                                PlayState playState = getPlayState(liveSetStartTime, tracks);
                                try {
                                    MediaPlayerController.getInstance().play(playState, tracks);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, String.format("Track #: %s; Track Position: %s",
                                        playState.getTrackNumber(), playState.getPositionMs()));
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
    }

    private PlayState getPlayState(String liveSetStartTime, List<Track> tracks) {
        List<Long> startTime = new ArrayList<>();
        long timeRunner = 0;
        for (int i = 0; i < tracks.size(); i++) {
            startTime.add(i, timeRunner);    // First track starts at time 9
            timeRunner += tracks.get(i).getDuration();
        }
        long millisSinceStart = TimeUtils.getTimeSince(liveSetStartTime);
        for (int i = 0; i < startTime.size(); i++) {
            long startTimeForTrack = startTime.get(i);
            if (millisSinceStart > startTimeForTrack) {
                return new PlayState(i, millisSinceStart - startTimeForTrack);
            }
        }
        Log.e(TAG, "No play state found");
        return new PlayState(0, 0);
    }


    /**
     * Triggered by DJ
     *
     * @param stationId
     * @param setId
     */
    public void startSet(String stationId, String setId) {
        if (stationId == null || setId == null) {
            Toast.makeText(App.getContext(), "Cannot start set", Toast.LENGTH_SHORT).show();
            return;
        }
        final Firebase station = FirebaseController.getInstance().getRef().child("stations").child(stationId);
        final HashMap map = new HashMap();
        map.put("live_set", setId);
//        map.put("live_track_position", 0);
        map.put("live_set_start_time", TimeUtils.getCurrentTimestamp());
        // Get all the tracks by querying for the set
        getTracksInSet(setId, new RetrieveTracksIdsInSetCallBack() {
            @Override
            public void onTracksIdsRetrieved(List<String> trackIds) {

                map.put("live_track_id", trackIds.get(0));
                station.updateChildren(map);
            }

        }); // Starts from the beginning
    }

    interface RetrieveTracksIdsInSetCallBack {
        void onTracksIdsRetrieved(List<String> trackIds);

    }

    private void getTracksInSet(String setId, final RetrieveTracksIdsInSetCallBack cb) {
        Firebase tracksRef = FirebaseController.getInstance().getRef().child("sets").child(setId).child("tracks");
        Query queryRef = tracksRef.orderByChild("order");

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // do some stuff once
                Map tracksMap = (Map) snapshot.getValue();
                List trackIds = new ArrayList(tracksMap.keySet());
                cb.onTracksIdsRetrieved(trackIds);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    interface TrackInfoCallback {
        void onTracksRetrieved(List<Track> value);
    }


    /**
     * Assume that track and set are unchanged.
     *
     * @param currentPosition
     */
    public void onPlayPositionUpdate(String stationId, int currentPosition) {
        Firebase stationRef = FirebaseController.getInstance().getRef().child("stations").child(stationId);
        HashMap map = new HashMap();
//        map.put("live_track_position", currentPosition);
        stationRef.updateChildren(map);
    }

    public String createStation(String displayName, String description) {
        // Create new station under "Stations"
        Firebase stationRef = FirebaseController.getInstance().getRef().child("stations").push();
        HashMap map = new HashMap();

        // Set initial station attributes
        map.put("display_name", displayName);
        map.put("description", description);
        stationRef.setValue(map);

        // Link station to user
        Firebase userRef = FirebaseController.getInstance().getRef().child("users")
                .child(SessionController.getInstance().getSession().getUid());
        HashMap map2 = new HashMap();
        map2.put(stationRef.getKey(), true);
        userRef.child("stations").setValue(map2);

        return stationRef.getKey();
    }


    public String addTrack(final String trackTitle, final String trackArtist,
                           final String addedTime, final String addedBy, final Track.Source src, final String srcId) {

        final Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").push();
        SoundCloud.getInstance().getTrackInfo(srcId, new SoundCloud.ScTrackInfoCallback() {
            @Override
            public void onInfoRetrieved(SoundCloudTrackResp soundCloudTrackResp) {
                HashMap map = new HashMap();
                map.put("added_time", addedTime);
                map.put("added_by", addedBy);

                map.put("track_artist", trackArtist);
                map.put("track_title", trackTitle);
                map.put("src", src.name());
                map.put("src_id", srcId);
                map.put("duration", soundCloudTrackResp.duration);
                trackRef.updateChildren(map);
            }
        });
        return trackRef.getKey();
    }

    public void addSet(String stationId, List<String> trackIds) {
        // Create new set
        Firebase setRef = FirebaseController.getInstance().getRef().child("sets").push();
        HashMap map = new HashMap();
        map.put("last_updated", TimeUtils.getCurrentTimestamp());
        map.put("creator", SessionController.getInstance().getSession().getUid());
        setRef.setValue(map);

        // Add all tracks in set
        HashMap tracksMap = new HashMap();
        for (String trackId : trackIds) {
            tracksMap.put(trackId, true);
        }
        setRef.child("tracks").setValue(tracksMap);

        // Map track to set
        Firebase tracksRef = FirebaseController.getInstance().getRef().child("tracks");
        for (String trackId : trackIds) {
            map = new HashMap();
            map.put("set_id", setRef.getKey());
            tracksRef.child(trackId).updateChildren(map);
        }

        // Add set to station
        Firebase stationRef = FirebaseController.getInstance().getRef().child("stations").child(stationId);
        HashMap setMap = new HashMap();
        setMap.put(setRef.getKey(), true);
        stationRef.child("sets").setValue(setMap);
    }
}
