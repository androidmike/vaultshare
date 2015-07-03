package com.vaultshare.play;

import android.util.Log;

import com.firebase.client.Firebase;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

/**
 * Created by mchang on 6/8/15.
 */


public class SoundCloud {

    private final SoundCloudAPIDef api;
    static        SoundCloud       soundCloudWebService;

    public SoundCloud() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(App.getContext().getString(R.string.soundcloud_endpoint))
                .setExecutors(Executors.newFixedThreadPool(5), new MainThreadExecutor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(SoundCloudAPIDef.class);
    }

    public static SoundCloud getInstance() {
        if (soundCloudWebService == null) {
            soundCloudWebService = new SoundCloud();
        }
        return soundCloudWebService;
    }

    public static final String TAG = SoundCloud.class.getCanonicalName();

    public void search(String query) {
        final String finalQuery = query;
        api.search(query, 100, 1, App.getContext().getString(R.string.soundcloud_id), new Callback<SoundCloudPaginatedResponse>() {

            @Override
            public void success(SoundCloudPaginatedResponse soundCloudTrackResp, Response response) {
                //  cb.onInfoRetrieved(soundCloudTrackResp);
                Bus.getInstance().post(new QueryResult(finalQuery, soundCloudTrackResp));
            }

            @Override
            public void failure(RetrofitError error) {
                int j = 0;
                //Log.e(TAG, String.format("getTrackInfo failed for %s: %s", trackId, LogUtil.getString(error)));
            }
        });
    }

    public interface ScTrackInfoCallback {
        void onInfoRetrieved(SoundCloudTrackResp soundCloudTrackResp);
    }

    public void getTrackInfo(final String trackId, final ScTrackInfoCallback cb) {
        api.getTrackInfo(trackId, App.getContext().getString(R.string.soundcloud_id), new Callback<SoundCloudTrackResp>() {

            @Override
            public void success(SoundCloudTrackResp soundCloudTrackResp, Response response) {
                cb.onInfoRetrieved(soundCloudTrackResp);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, String.format("getTrackInfo failed for %s: %s", trackId, LogUtil.getString(error)));
            }
        });
    }

}