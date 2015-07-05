package com.vaultshare.play;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.makeramen.dragsortadapter.DragSortAdapter;
import com.vaultshare.play.model.AwsController;
import com.vaultshare.play.model.Track;

import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class MixRecyclerViewAdapter extends DragSortAdapter<MixRecyclerViewAdapter.BaseViewHolder> {

    private List<String> trackIds;
    private Context      context;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL   = 1;
    String pendingSetId;

    public MixRecyclerViewAdapter(Context context, RecyclerView recyclerView, List<String> trackIds, String pendingSetId) {
        super(recyclerView);
        this.trackIds = trackIds;
        this.context = context;
        this.pendingSetId = pendingSetId;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return trackIds.size() + 1;
    }

    public class BaseViewHolder extends DragSortAdapter.ViewHolder {

        public BaseViewHolder(DragSortAdapter<?> dragSortAdapter, View itemView) {
            super(dragSortAdapter, itemView);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mix_header_layout, parent, false);
                return new BaseViewHolder(MixRecyclerViewAdapter.this, view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_browse_track_small, parent, false);
                return new PendingTrackViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public int getPositionForId(long l) {
        for (int i = 0; i < trackIds.size(); i++) {
            if (hashString(trackIds.get(i)) == l) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return hashString(trackIds.get(position));
    }

    public int hashString(String string) {
        int hash = 7;
        for (int i = 0; i < string.length(); i++) {
            hash = hash * 31 + string.charAt(i);
        }
        return hash;
    }

    @Override
    public boolean move(int from, int to) {
//
//        Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId).child("tracks");
//        Map map = new HashMap();
//        map.put(trackIds.get(from), to);
//        setRef.updateChildren(map);
//        trackIds.get(from);
        trackIds.add(to, trackIds.remove(from));
        return true;
    }

    public void onDrop() {
    }

    public class PendingTrackViewHolder extends BaseViewHolder {
        public TextView name;
        public TextView description;

        public PendingTrackViewHolder(View itemView) {
            super(MixRecyclerViewAdapter.this, itemView);
            description = (TextView) itemView.findViewById(R.id.track_description);
            name = (TextView) itemView.findViewById(R.id.track_name);
        }
    }

    int     totalDuration = 0;
    boolean isRecording   = false;
    MediaRecorder mRecorder;
    File          mFileName;

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_HEADER:

                // Invoke Search
                holder.itemView.findViewById(R.id.add_track_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(App.getContext(), SearchActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra(SearchActivity.EXTRA_PENDING_SET_ID, pendingSetId);
                        i.putExtra(SearchActivity.PENDING_TRACK_NUMBER, trackIds.size());
                        context.startActivity(i);
                    }
                });

                holder.itemView.findViewById(R.id.rec_drop).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRecording) {
                            stopRecording();

                            new MaterialDialog.Builder(context)
                                    .title("Name Recording")
                                            // .content(R.string.input_content)
                                    .inputType(InputType.TYPE_CLASS_TEXT |
                                            InputType.TYPE_TEXT_VARIATION_PASSWORD)
                                    .input("Custom intro, drop, ad-lib", "", new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(MaterialDialog dialog, CharSequence input) {
                                            String title = input.toString();
                                            final Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").push();
                                            HashMap map = new HashMap();
                                            map.put("added_time", TimeUtils.getCurrentTimestamp());
                                            map.put("added_by", SessionController.getInstance().getSession().getUid());

                                            map.put("track_title", title);
                                            map.put("src", Track.Source.VAULTSHARE.name());
                                            long duration = getDuration();


                                            map.put("duration", duration);
                                            trackRef.updateChildren(map);
                                            AwsController.getInstance().upload(trackRef.getKey(), trackRef.getKey() + ".3gpp", mFileName);
                                        }
                                    }).show();


                        } else {
                            mFileName = new File(context.getFilesDir(), "temp_rec.3gp");
                            startRecording();
                        }
                    }

                    private void startRecording() {
                        stopRecording();
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecorder.setOutputFile(mFileName.getAbsolutePath());
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                        try {
                            mRecorder.prepare();
                        } catch (IOException e) {

                        }

                        mRecorder.start();
                        isRecording = true;
                    }

                    private void stopRecording() {
                        try {
                            if (mRecorder != null) {
                                mRecorder.stop();
                                mRecorder.release();
                                mRecorder = null;
                            }
                        } catch (Exception e) {

                        }
                        isRecording = false;
                    }
                });
                final TextView durationText = ((TextView) holder.itemView.findViewById(R.id.duration_text));
                final TextView numberTracks = ((TextView) holder.itemView.findViewById(R.id.tracks_num_text));
                totalDuration = 0;


                for (String it : trackIds) {
                    Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(it);

                    trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Track track = dataSnapshot.getValue(Track.class);
                            if (track != null) {
                                totalDuration += track.duration;
                                numberTracks.setText("" + trackIds.size());
                                durationText.setText(TimeUtils.getDurationBreakdown(totalDuration));
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                break;
            case TYPE_CELL:
                final PendingTrackViewHolder h = (PendingTrackViewHolder) holder;
                final String trackId = trackIds.get(position - 1);
                Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);
                h.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        h.startDrag();
                        return true;
                    }
                });
                trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Track track = dataSnapshot.getValue(Track.class);
                        if (track != null) {
                            h.description.setText("Duration: " + TimeUtils.getDurationBreakdown(track.getDuration()));
                            h.name.setText(track.track_title);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                break;
        }
    }

    private long getDuration() {
        MediaPlayer mp = new MediaPlayer();
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(mFileName.getAbsoluteFile());
            mp.setDataSource(stream.getFD());
            stream.close();
            mp.prepare();
        } catch (Exception e) {

        }
        long duration = mp.getDuration();
        mp.release();
        return duration;
    }

    private String formatDuration(int duration) {
        Duration dur = new Duration(0, duration);
        Period p = dur.toPeriod();
        PeriodFormatter hm = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(2) // gives the '01'
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .toFormatter();
        return hm.print(p);
    }
}