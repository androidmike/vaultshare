package com.vaultshare.play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by mchang on 6/12/15.
 */
public class MeetFragment extends BaseFragment implements ChildEventListener {
    private static final java.lang.String EXTRA_STATION = MeetFragment.class.getCanonicalName() + ".extra.station";

    @InjectView(R.id.avatar_listview)
    ListView listView;

  private   List<StationUserInfo> users = new ArrayList();
    String      stationId;
    BaseAdapter userAvatarAdapter;

    public static Fragment newInstance(Bundle bundle) {
        MeetFragment fragment= new MeetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_meet;
    }

    @Override
    public void initUI() {
        if (getArguments() != null) {
            stationId = getArguments().getString(EXTRA_STATION, null);
        }
        if (stationId == null) {
            // Problem occurred
            Log.e(getLogTag(), "No station ID.");
            return;
        }

        Firebase usersRef = FirebaseController.getInstance().getRef().child("stations").
                child(stationId).child("live_users");

        usersRef.addChildEventListener(this);

        userAvatarAdapter = new UserAvatarAdapter(getActivity(), users);
        listView.setAdapter(userAvatarAdapter);
    }

    private void loadUsers(DataSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
        if (map == null) {
            return;
        }
        users.clear();
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) { // #optimize with another map
            Map.Entry pair = (Map.Entry) it.next();
            String userId = (String) pair.getKey();
            String joinTime = (String) pair.getValue();
            StationUserInfo userInfo = new StationUserInfo(userId, joinTime);
            users.add(userInfo);
            it.remove(); // avoids a ConcurrentModificationException
        }

        userAvatarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        loadUsers(snapshot);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        loadUsers(dataSnapshot);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        loadUsers(dataSnapshot);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        loadUsers(dataSnapshot);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
    }
}
