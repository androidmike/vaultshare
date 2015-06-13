package com.vaultshare.play;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mchang on 6/12/15.
 */
public class UserAvatarAdapter extends BaseAdapter {

    List<StationUserInfo> users = new ArrayList<>();
    Context mContext;

    static class ViewHolder {
        UserAvatarView avatarView;
        View           chatIndicator;
        TextView       textView;

        public ViewHolder(View view) {
            avatarView = (UserAvatarView) view.findViewById(R.id.user_avatar);
            chatIndicator = view.findViewById(R.id.concert_person_chat);
            textView = (TextView) view.findViewById(R.id.concert_person_text);
        }
    }


    public UserAvatarAdapter(Context mContext, List<StationUserInfo> users) {
        this.users = users;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.concert_person, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.textView.setText("");
            holder.chatIndicator.setVisibility(View.GONE);
            holder.avatarView.setVisibility(View.VISIBLE);
        }
        return null;
    }
}
