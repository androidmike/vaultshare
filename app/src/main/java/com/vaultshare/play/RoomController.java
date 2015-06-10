package com.vaultshare.play;

/**
 * Created by mchang on 6/8/15.
 */
public class RoomController {
    private static RoomController instance;

    public RoomController() {
    }

    public static RoomController getInstance() {
        if (instance == null) {
            instance = new RoomController();
        }
        return instance;
    }

}
