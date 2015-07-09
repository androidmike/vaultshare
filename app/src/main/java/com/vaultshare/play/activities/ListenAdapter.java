package com.vaultshare.play.activities;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vaultshare.play.App;
import com.vaultshare.play.Artist;
import com.vaultshare.play.EchoNestCache;
import com.vaultshare.play.EchoNestImage;
import com.vaultshare.play.R;
import com.vaultshare.play.model.Track;

import java.util.List;

/**
 * Created by mchang on 6/25/15.
 */
public class ListenAdapter extends BaseAdapter {
    Context     context;
    List<Track> tracks;

    public ListenAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = tracks.get(position);
        Artist artist = EchoNestCache.getInstance().getArtistByName(track.track_artist);
        List<EchoNestImage> images = EchoNestCache.getInstance().getImagesOfArtistName(track.track_artist);

        convertView = View.inflate(context, R.layout.layout_mixtape, null);
        ImageView avatarRow = (ImageView) convertView.findViewById(R.id.avatar_row);
        ImageView image1 = (ImageView) convertView.findViewById(R.id.image_1);

        TextView lyric1 = (TextView) convertView.findViewById(R.id.lyric_1);

        TextView lyric2 = (TextView) convertView.findViewById(R.id.lyric_2);
        TextView note = (TextView) convertView.findViewById(R.id.note);

        TextView lyric3 = (TextView) convertView.findViewById(R.id.lyric_3);

        TextView songTitle = (TextView) convertView.findViewById(R.id.song_title);
        TextView artistText = (TextView) convertView.findViewById(R.id.artist);
        if (artist != null) {
            artistText.setText(artist.name);
        } else {
            artistText.setText("");
        }
        if (images != null && !images.isEmpty()) {
            Picasso.with(App.getContext()).load(images.get(0).url).into(image1);
        }

        return convertView;
    }
}