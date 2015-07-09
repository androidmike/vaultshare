package com.vaultshare.play;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by mchang on 6/8/15.
 */
public interface EchoNestAPIDef {

    @GET("/artist/search")
    void search(@Query("api_key") String apiKey,
                @Query("format") String format,
                @Query("name") String name,
                @Query("results") int numResults,
                @Query("fuzzy_match") boolean isFuzzy,
                @Query("bucket") String bucket1,
                @Query("bucket") String bucket2,
                @Query("bucket") String bucket3,
                @Query("bucket") String bucket4,
                @Query("bucket") String bucket5,
                @Query("bucket") String bucket6,
                @Query("bucket") String bucket7,
                @Query("bucket") String bucket8,
                @Query("bucket") String bucket9,

                Callback<EchoNestArtistSearchResponse> cb);


    @GET("/artist/images")
    void searchImages(@Query("api_key") String apiKey,
                @Query("format") String format,
                @Query("name") String name,
                @Query("results") int numResults,
                @Query("start") int startNum,
                @Query("license") String license,

                Callback<EchoNestImageSearchResponse> cb);


}