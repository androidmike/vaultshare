package com.vaultshare.play;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.github.siyamed.shapeimageview.DiamondImageView;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by mchang on 6/12/15.
 */
public class UserAvatarView extends FrameLayout {
    DiamondImageView mImage;
    String           mUid;
    Context          mContext;
    String           avatarUrl;

    public UserAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImage = (DiamondImageView) inflater.inflate(R.layout.user_avatar, this, true);
    }

    public UserAvatarView(Context context) {
        this(context, null);
    }

    public void setUid(String uid) {
        this.mUid = uid;
        Firebase userRef = FirebaseController.getInstance().getUserRefs().child(uid);
        userRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                loadAvatarUrl(snapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (!s.equals("avatar_url")) {
                    return;
                }
                loadAvatarUrl(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                loadAvatarUrl(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                loadAvatarUrl(dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void loadAvatarUrl(DataSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }
        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
        if (map == null) {
            return;
        }
        avatarUrl = (String) map.get("avatar_url");
        Picasso.with(App.getContext()).load(avatarUrl).into(mImage);
    }
}