package com.vaultshare.play;

import android.net.Uri;

import com.vaultshare.play.model.Set;

import java.util.List;

/**
 * Created by mchang on 6/8/15.
 */
public class MixController {
    private static MixController instance;

    public MixController() {
    }


    public static MixController getInstance() {
        if (instance == null) {
            instance = new MixController();
        }
        return instance;
    }


}
