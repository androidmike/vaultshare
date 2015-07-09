package com.vaultshare.play;

import android.util.Log;

import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

/**
 * Created by mchang on 6/8/15.
 */


public class EchoNest {

    private final EchoNestAPIDef api;
    static        EchoNest       echoNestWebService;

    public static final String TAG = EchoNest.class.getCanonicalName();

    public EchoNest() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(App.getContext().getString(R.string.echonest_endpoint))
                .setExecutors(Executors.newFixedThreadPool(5), new MainThreadExecutor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(EchoNestAPIDef.class);
    }

    public static EchoNest getInstance() {
        if (echoNestWebService == null) {
            echoNestWebService = new EchoNest();
        }
        return echoNestWebService;
    }

    /*
    http://developer.echonest.com/api/v4/artist/search?api_key=Y12DFIO1RTME9BHAR&format=json&name=nipsey
    &results=1&fuzzy_match=true&bucket=biographies&bucket=blogs&bucket=discovery&bucket=discovery_rank
    &bucket=familiarity&bucket=familiarity_rank&bucket=genre&bucket=hotttnesss&bucket=hotttnesss_rank
     */
    public void search(final String name) {
        api.search("Y12DFIO1RTME9BHAR", "json", name, 1, true, "biographies", "blogs", "discovery", "discovery_rank",
                "familiarity", "familiarity_rank", "genre", "hotttnesss", "hotttnesss_rank",
                new Callback<EchoNestArtistSearchResponse>() {

                    @Override
                    public void success(EchoNestArtistSearchResponse resp, Response response) {
                        Bus.getInstance().post(new EchoNestSearchResult(name, resp));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        int j = 0;
                        //Log.e(TAG, String.format("getTrackInfo failed for %s: %s", trackId, LogUtil.getString(error)));
                    }
                });
    }

    public void searchImages(final String name) {
        api.searchImages("Y12DFIO1RTME9BHAR", "json", name, 20, 0, null,
                new Callback<EchoNestImageSearchResponse>() {

                    @Override
                    public void success(EchoNestImageSearchResponse resp, Response response) {
                        Bus.getInstance().post(new EchoNestImageSearchResult(name, resp));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        int j = 0;
                        //Log.e(TAG, String.format("getTrackInfo failed for %s: %s", trackId, LogUtil.getString(error)));
                    }
                });
    }
}