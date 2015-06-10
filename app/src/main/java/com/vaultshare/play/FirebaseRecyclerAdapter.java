package com.vaultshare.play;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.vaultshare.play.model.FirebaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FirebaseRecyclerAdapter extends RecyclerView.Adapter {

    private Query                          mRef;
    private Class<? extends FirebaseModel> mModelClass;
    private List<FirebaseModel>            mModels;
    private Map<String, FirebaseModel>     mModelKeys;
    private ChildEventListener             mListener;

    public FirebaseRecyclerAdapter(Context context, Query mRef, Class<? extends FirebaseModel> mModelClass) {
        this.mRef = mRef;
        this.mModelClass = mModelClass;
        mModels = new ArrayList<FirebaseModel>();
        mModelKeys = new HashMap<String, FirebaseModel>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                FirebaseModel model = dataSnapshot.getValue(FirebaseRecyclerAdapter.this.mModelClass);
                model.setKey(dataSnapshot.getKey());
                mModelKeys.put(dataSnapshot.getKey(), model);

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    mModels.add(0, model);
                } else {
                    FirebaseModel previousModel = mModelKeys.get(previousChildName);
                    int previousIndex = mModels.indexOf(previousModel);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(model);
                    } else {
                        mModels.add(nextIndex, model);
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                // One of the mModels changed. Replace it in our list and name mapping
                String modelName = dataSnapshot.getKey();
                FirebaseModel oldModel = mModelKeys.get(modelName);
                FirebaseModel newModel = dataSnapshot.getValue(FirebaseRecyclerAdapter.this.mModelClass);
                int index = mModels.indexOf(oldModel);
                newModel.setKey(dataSnapshot.getKey());
                mModels.set(index, newModel);
                mModelKeys.put(modelName, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String modelName = dataSnapshot.getKey();
                FirebaseModel oldModel = mModelKeys.get(modelName);
                mModels.remove(oldModel);
                mModelKeys.remove(modelName);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String modelName = dataSnapshot.getKey();
                FirebaseModel oldModel = mModelKeys.get(modelName);
                FirebaseModel newModel = dataSnapshot.getValue(FirebaseRecyclerAdapter.this.mModelClass);
                newModel.setKey(dataSnapshot.getKey());

                mModelKeys.put(modelName, newModel);
                int index = mModels.indexOf(oldModel);
                mModels.remove(index);
                if (previousChildName == null) {
                    mModels.add(0, newModel);
                } else {
                    FirebaseModel previousModel = mModelKeys.get(previousChildName);
                    int previousIndex = mModels.indexOf(previousModel);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(newModel);
                    } else {
                        mModels.add(nextIndex, newModel);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FirebaseModel model = mModels.get(position);

        // Call out to subclass to marshall this model into the provided view
        populateView(holder, model);
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the mLayout and mModelClass given to the constructor of this class.
     * <p/>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v     The view to populate
     * @param model The object containing the data used to populate the view
     */
    protected abstract void populateView(RecyclerView.ViewHolder v, FirebaseModel model);

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        mModels.clear();
        mModelKeys.clear();
    }
}
