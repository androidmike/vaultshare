package com.vaultshare.play.fragments;

import com.firebase.client.Firebase;
import com.vaultshare.play.BaseFragment;
import com.vaultshare.play.FirebaseController;
import com.vaultshare.play.R;
import com.vaultshare.play.chat.DigAdapter;
import com.vaultshare.play.framework.FirebaseListAdapter;

import butterknife.InjectView;
import se.emilsjolander.flipview.FlipView;

/**
 * Created by mchang on 6/11/15.
 */
public class DigFragment extends BaseFragment {

    @InjectView(R.id.flip_view)
    FlipView flipView;

    @Override
    public int getLayout() {
        return R.layout.fragment_dig;
    }

    @Override
    public void initUI() {

        Firebase mRef = FirebaseController.getInstance().getRef().child("stations");

        FirebaseListAdapter adapter = new DigAdapter(mRef, getActivity());
        flipView.setAdapter(adapter);
    }
}
