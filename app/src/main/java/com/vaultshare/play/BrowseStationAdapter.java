package com.vaultshare.play;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Query;
import com.vaultshare.play.model.FirebaseModel;
import com.vaultshare.play.model.Station;

/**
 * Created by mchang on 6/9/15.
 */
public class BrowseStationAdapter extends FirebaseRecyclerAdapter {

    public static class StationCardVh extends RecyclerView.ViewHolder {
        public TextView mDisplayNameTextView;
        public View     rootView;

        public View joinSetButton, startSetButton;
        public StationCardVh(View v) {
            super(v);
            rootView = v;
            joinSetButton = v.findViewById(R.id.join_button);
            startSetButton = v.findViewById(R.id.start_set_button);
            mDisplayNameTextView = (TextView) v.findViewById(R.id.display_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BrowseStationAdapter(Query query, Context context) {
        super(context, query, Station.class);
    }

    @Override
    protected void populateView(RecyclerView.ViewHolder v, FirebaseModel model) {
        final Station station = (Station) model;
        StationCardVh vh = (StationCardVh) v;
        vh.mDisplayNameTextView.setText(station.getDisplayName());
        vh.joinSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseController.getInstance().joinSet(station.getKey());
            }
        });
        vh.startSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseController.getInstance().startSet(station.getKey(), (String) station.sets.keySet().toArray()[0]);
            }
        });

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        StationCardVh vh = new StationCardVh(v);

        return vh;
    }
}
