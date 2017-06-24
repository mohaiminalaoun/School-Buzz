package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;
/**
 * Created by Jesse on 6/23/2017.
 */

public class ConversationAdapter  extends ArrayAdapter<FriendlyConversation> {
    public ConversationAdapter(Context context, int resource, List<FriendlyConversation> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_conversation, parent, false);
        }

        ImageView conversationIcon = (ImageView) convertView.findViewById(R.id.conversationIcon);
        TextView conversationTitle = (TextView) convertView.findViewById(R.id.conversationTitle);

        FriendlyConversation friendlyConversation = getItem(position);
        conversationTitle.setText(friendlyConversation.getTitle());

        return convertView;
    }
}