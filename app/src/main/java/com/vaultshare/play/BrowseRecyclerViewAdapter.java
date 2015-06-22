package com.vaultshare.play;

import android.content.Context;
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
import android.widget.TextView;

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

    private List<FirebaseModel> stations;
    private Context             context;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL   = 1;

    public BrowseRecyclerViewAdapter(Context context, List<FirebaseModel> stations) {
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

        View view = null;
        int i = new Random().nextInt(3);
        int ll;
        switch (i) {
            case 0:
                ll = R.layout.list_item_tiesto;
                break;
            case 1:
                ll = R.layout.list_item_sway;
                break;
            default:
                ll = R.layout.list_item_boogie;
                break;

        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(ll, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
//        switch (viewType) {
//            case TYPE_HEADER: {
//                view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_item_tiesto, parent, false);
//                return new RecyclerView.ViewHolder(view) {
//                };
//            }
//            case TYPE_CELL: {
//                view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_item_boogie, parent, false);
//                return new StationViewHolder(view);
//            }
//        }
    }

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    boolean                  flipFlop = false;

    private void flashRec(final View rec) {

        TimerTask runnable = new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (rec.getVisibility() == View.VISIBLE) {
                            rec.setVisibility(View.INVISIBLE);
                        } else {
                            rec.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };

        timer.cancel();
        timer.purge();
        timer = new Timer();
        timer.scheduleAtFixedRate(runnable, 0, 500);
    }

    Timer timer = new Timer();

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

    MediaPlayer mediaPlayer;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View rec = holder.itemView.findViewById(R.id.rec_indicator);

        View marqueeText = holder.itemView.findViewById(R.id.marquee_text);
        if (marqueeText != null) {
            marqueeText.setSelected(true);
        }
        if (rec != null) {
//            flashRec(rec);

            AlphaAnimation anim = new AlphaAnimation(1, 0);
            anim.setDuration(800);
            anim.setRepeatMode(Animation.RESTART);

anim.setRepeatCount(Animation.INFINITE);
            rec.startAnimation(anim);
        }




        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_CELL:
                final Station station = (Station) stations.get(position);
//                StationViewHolder viewHolder = (StationViewHolder) holder;
//                viewHolder.name.setText(station.getDisplayName());
//                viewHolder.description.setText(station.getDescription());
//                viewHolder.liveIndicator.setVisibility(station.isLive() ? View.VISIBLE : View.GONE);
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(context, StationActivity.class);
//                        i.putExtra(StationActivity.EXTRA_STATION_ID, station.getKey());
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        context.startActivity(i);
//                    }
//                });
                break;
        }
    }
}