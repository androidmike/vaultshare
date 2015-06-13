package com.vaultshare.play;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by mchang on 6/12/15.
 */
public class Bus extends com.squareup.otto.Bus {
    static Bus bus;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public static Bus getInstance() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Bus.super.post(event);
                }
            });
        }
    }
}
