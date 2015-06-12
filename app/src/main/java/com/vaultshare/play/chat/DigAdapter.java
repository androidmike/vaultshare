package com.vaultshare.play.chat;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.vaultshare.play.R;
import com.vaultshare.play.framework.FirebaseListAdapter;
import com.vaultshare.play.model.Chat;
import com.vaultshare.play.model.Station;

public class DigAdapter extends FirebaseListAdapter<Station> {


    public DigAdapter(Query ref, Activity activity) {
        super(ref, Station.class, R.layout.station_card_view, activity);
    }

    @Override
    protected void populateView(View view, Station station) {
        TextView authorText = (TextView) view.findViewById(R.id.display_name);
        authorText.setText(station.getDisplayName());
    }
}
