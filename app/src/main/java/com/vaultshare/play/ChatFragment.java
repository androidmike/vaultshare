package com.vaultshare.play;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.vaultshare.play.chat.ChatListAdapter;
import com.vaultshare.play.model.Chat;

import butterknife.InjectView;

/**
 * Created by mchang on 6/11/15.
 */
public class ChatFragment extends BaseFragment {

    @InjectView(R.id.messageInput)
    EditText    inputText;
    @InjectView(R.id.sendButton)
    ImageButton sendButton;
    @InjectView(R.id.chat_list_view)
    ListView    listView;

    private ValueEventListener mConnectedListener;
    private ChatListAdapter    mChatListAdapter;
    private Firebase           mFirebaseChatRef;

    @Override
    public int getLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    public void initUI() {
        // Setup our Firebase mFirebaseChatRef
        mFirebaseChatRef = FirebaseController.getInstance().getRef().child("chat");

        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseChatRef.limit(50), getActivity(),
                R.layout.chat_message, "facebook:10105340362088383");
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseChatRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(App.getContext(), "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getContext(), "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseChatRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void sendMessage() {
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, "facebook:10105340362088383");
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseChatRef.push().setValue(chat);
            inputText.setText("");
        }
    }

    public static ChatFragment newInstance(Bundle bundle) {
        ChatFragment chatFragment =  new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }
}