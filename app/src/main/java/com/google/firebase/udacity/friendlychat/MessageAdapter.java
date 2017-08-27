package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {
    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView replyTextView = (TextView) convertView.findViewById(R.id.messageTextView2);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.time);

        FriendlyMessage message = getItem(position);

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            replyTextView.setVisibility(View.GONE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            messageTextView.setVisibility(View.VISIBLE);
            replyTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            String unchangedMessage = message.getText();


            messageTextView.setText(message.getText());
            replyTextView.setText(message.getText());



            if(messageTextView.getText()==""|| messageTextView.getText()==null){
                messageTextView.setVisibility(View.GONE);
                replyTextView.setVisibility(View.GONE);
            }
        }
        authorTextView.setText(message.getName());


        long mtime = message.getEpochTime();
        Date messageTime = new Date( mtime * 1000 );
        String dateString = messageTime.toString();
        String[] arr = dateString.split(" ");
        String sb = arr[0];
        for(int i=1; i <4; i++){
            sb+=" "+arr[i];
        }
        timeTextView.setText(sb);


        //TODO: check if getItem fucks things up or not
        if(position==0){
            authorTextView.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.VISIBLE);
            replyTextView.setVisibility(View.GONE);
            //message text view is the first message
        }
        else if(position>0 && !isPhoto) {

            //check if author of current message is same as author of first message
            FriendlyMessage firstMessage = getItem(0);
            if(message.getName().equals(firstMessage.getName())){
                replyTextView.setVisibility(View.GONE);
                messageTextView.setVisibility(View.VISIBLE);
            }else{
                messageTextView.setVisibility(View.GONE);
                replyTextView.setVisibility(View.VISIBLE);
            }

            FriendlyMessage tempMessage = getItem(position - 1);
            if (message.getName().equals(tempMessage.getName())){
                authorTextView.setVisibility(View.GONE);
                timeTextView.setVisibility(View.GONE);
            }
            else {
                authorTextView.setVisibility(View.VISIBLE);
                timeTextView.setVisibility(View.VISIBLE);
            }



        }


        // method to make links clickable

        messageTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return convertView;
    }
}
