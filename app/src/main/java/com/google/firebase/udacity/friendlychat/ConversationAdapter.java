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
        ColorWheel cw = new ColorWheel();
        conversationIcon.setBackgroundColor(cw.getColor());

        TextView conversationTitle = (TextView) convertView.findViewById(R.id.conversationTitle);
        TextView userNameConversation = (TextView) convertView.findViewById(R.id.userNameConversation);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        FriendlyConversation friendlyConversation = getItem(position);
        conversationTitle.setText(friendlyConversation.getTitle());
        userNameConversation.setText(friendlyConversation.getUsers());


        long mtime = friendlyConversation.getEpochTime();
        Date messageTime = new Date( mtime * 1000 );
        String dateString = messageTime.toString();
        String[] arr = dateString.split(" ");
        String sb = arr[0];
        for(int i=1; i <4; i++){
            sb+=" "+arr[i];
        }
        time.setText(sb);

        return convertView;
    }
}