package com.vaultshare.play;

/**
 * Created by mchang on 7/1/15.
 */
public class QueryResult {
    SoundCloudPaginatedResponse response;
    String query;
    public QueryResult(String finalQuery, SoundCloudPaginatedResponse soundCloudTrackResp) {
        this.query = finalQuery;
        this.response = soundCloudTrackResp;
    }
}
