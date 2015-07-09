package com.vaultshare.play;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mchang on 7/8/15.
 */
public class BiographyLicense {
    String type;
    String attribution;
    @SerializedName("attribution-url")
    String attribution_url;
    String url;
    String version;
}
