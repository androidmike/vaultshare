package com.vaultshare.play;

import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;

/**
 * Created by mchang on 6/8/15.
 */


public class Getty {

    private final GettyAPIDef api;
    static        Getty          gettyWebService;

    public static final String TAG = Getty.class.getCanonicalName();

    public Getty() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(App.getContext().getString(R.string.getty_endpoint))
                .setExecutors(Executors.newFixedThreadPool(5), new MainThreadExecutor())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        api = restAdapter.create(GettyAPIDef.class);
    }

    public static Getty getInstance() {
        if (gettyWebService == null) {
            gettyWebService = new Getty();
        }
        return gettyWebService;
    }

//  GET https://api.gettyimages.com/v3/search/images?fields=id,title,thumb,referral_destinations&phrase=jay-z&sort_order=best
//    public void search(String name) {
//        api.search("id,title,thumb,referral_destinations,largest_downloads", name, "best",
//                new Callback<GettySearchResponse>() {
//
//                    @Override
//                    public void success(GettySearchResponse resp, Response response) {
//                        Bus.getInstance().post(new GettySearchComplete(resp));
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        int j = 0;
//                        //Log.e(TAG, String.format("getTrackInfo failed for %s: %s", trackId, LogUtil.getString(error)));
//                    }
//                });
//    }
}