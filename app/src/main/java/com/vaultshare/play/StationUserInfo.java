package com.vaultshare.play;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class StationUserInfo {
    protected String dateJoined;
    protected String uid;

    public StationUserInfo(String uid, String dateJoined) {
        this.uid = uid;
        this.dateJoined = dateJoined;
    }
}