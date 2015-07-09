package com.vaultshare.play;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vaultshare.play.model.Track;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TracksResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL   = 1;
    String                    pendingSetId;
    List<SoundCloudTrackResp> tracks;
    int                       pendingTrackNumber;

    public TracksResultAdapter(Context context, List<SoundCloudTrackResp> tracks, String pendingSetId, int pendingTrackNumber) {
        this.tracks = tracks;
        this.context = context;
        this.pendingSetId = pendingSetId;
        this.pendingTrackNumber = pendingTrackNumber;
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
        return tracks.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = new View(App.getContext());
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_track_small, parent, false);
                return new TrackViewHolder(view);
            }
        }
        return null;
    }

    private class TrackViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;

        public TrackViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.track_name);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                final SoundCloudTrackResp track = tracks.get(position - 1);
                TrackViewHolder viewHolder = (TrackViewHolder) holder;
                viewHolder.name.setText(track.getTitle());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String vsTrackId = FirebaseController.getInstance()
                                .addTrack(pendingSetId, track.getTitle(),
                                        track.user.username,
                                        TimeUtils.getCurrentTimestamp(),
                                        SessionController.getInstance().getSession().getUid(),
                                        Track.Source.SOUNDCLOUD, track.getId(), track.artwork_url);

                        Firebase setRef = FirebaseController.getInstance().getRef().child("sets").child(pendingSetId).child("tracks");
                        Map map = new HashMap();
                        map.put(vsTrackId, pendingTrackNumber);
                        setRef.updateChildren(map);
                    }
                });
                break;
        }
    }
}