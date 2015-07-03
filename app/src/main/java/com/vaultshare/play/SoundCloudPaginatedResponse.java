package com.vaultshare.play;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by mchang on 7/1/15.
 */
public class SoundCloudPaginatedResponse {
    public List<SoundCloudTrackResp> collection;
    public String next_href;
    public boolean hasNextPage() {
        return !(TextUtils.isEmpty(next_href));
    }
}
