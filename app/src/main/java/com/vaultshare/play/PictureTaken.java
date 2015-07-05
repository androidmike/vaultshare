package com.vaultshare.play;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by mchang on 7/4/15.
 */
public class PictureTaken {
    public Uri uri;

    public PictureTaken(Uri outputFileUri) {
        this.uri = outputFileUri;
    }
}
