package com.vaultshare.play;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vaultshare.play.activities.ProfileActivity;
import com.vaultshare.play.model.FirebaseModel;
import com.vaultshare.play.model.Station;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class BrowseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Station> stations;
    private Context       context;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL   = 1;

    public BrowseRecyclerViewAdapter(Context context, List<Station> stations) {
        this.stations = stations;
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
        return stations.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER: {
                return new RecyclerView.ViewHolder(new View(context)) {
                };
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_tiesto, parent, false);
                return new StationViewHolder(view);
            }
        }
        return null;
    }


    private class StationViewHolder extends RecyclerView.ViewHolder {
        public TextView stationName;
        public TextView stationDescription, djDescription;
        public View      liveIndicator;
        public ImageView djPictureLarge;

        public StationViewHolder(View itemView) {
            super(itemView);
            djDescription = (TextView) itemView.findViewById(R.id.dj_description);
            djPictureLarge = (ImageView) itemView.findViewById(R.id.dj_picture_large);
            stationDescription = (TextView) itemView.findViewById(R.id.station_description);
            stationName = (TextView) itemView.findViewById(R.id.station_name);
            liveIndicator = itemView.findViewById(R.id.live_indicator);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Station station = stations.get(position);
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        StationViewHolder stationHolder = (StationViewHolder) holder;
        stationHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }
        });
        if (station.description != null) {
            stationHolder.djDescription.setText(station.description);
        }
        if (station.dj_picture_large != null) {
            Picasso.with(App.getContext()).load(station.dj_picture_large).into(stationHolder.djPictureLarge);
        }

        stationHolder.stationDescription.setText(station.getDescription());
        stationHolder.stationName.setText(station.getDisplayName());
    }
}