package com.vaultshare.play;

import com.vaultshare.play.model.Station;

import java.util.List;

import retrofit.Callback;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mchang on 6/8/15.
 */
public interface SoundCloudAPIDef {
    @GET("/tracks/{track_id}.json")
    void getTrackInfo(@Path("track_id") String trackId, @Query("client_id") String clientId, Callback<SoundCloudTrackResp> cb);
}