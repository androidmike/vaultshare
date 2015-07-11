package com.vaultshare.play;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mchang on 6/8/15.
 */
public interface GettyAPIDef {
//  GET https://api.gettyimages.com/v3/search/images?fields=id,title,thumb,referral_destinations&phrase=jay-z&sort_order=best

    @GET("/search/images")
    void search(@Query("fields") String fields,
                @Query("phrase") String phrase,
                @Query("sort_order") String sortOrder,
                Callback<GettySearchResponse> cb);

}