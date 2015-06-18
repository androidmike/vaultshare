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

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class MixRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> trackIds;
    private Context      context;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL   = 1;

    public MixRecyclerViewAdapter(Context context, List<String> trackIds) {
        this.trackIds = trackIds;
        this.context = context;
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
        return trackIds.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_tiesto, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_browse_station_small, parent, false);
                return new StationViewHolder(view);
            }
        }
        return null;
    }

    private class StationViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView description;
        protected View     liveIndicator;

        public StationViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.station_description);
            name = (TextView) itemView.findViewById(R.id.station_display_name);
            liveIndicator = itemView.findViewById(R.id.live_indicator);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                final String trackId = (String) trackIds.get(position);
                Firebase trackRef = FirebaseController.getInstance().getRef().child("tracks").child(trackId);
                trackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Track track = dataSnapshot.getValue(Track.class);


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



                StationViewHolder viewHolder = (StationViewHolder) holder;
//                viewHolder.name.setText(track.getDisplayName());
//                viewHolder.description.setText(track.getDescription());
//                viewHolder.liveIndicator.setVisibility(track.isLive() ? View.VISIBLE : View.GONE);
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(context, StationActivity.class);
//                        i.putExtra(StationActivity.EXTRA_STATION_ID, track.getKey());
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        context.startActivity(i);
//                    }
//                });
                break;
        }
    }
}