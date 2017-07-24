package com.google.firebase.udacity.friendlychat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
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

        ImageView conversationIcon = (ImageView) convertView.findViewById(R.id.chatIcon);
        ColorWheel cw = new ColorWheel();
        //conversationIcon.setBackgroundColor(cw.getColor());
        //new DownloadImageTask(conversationIcon)
                //.execute("https://www.gravatar.com/avatar/"+
                  //      getItem(position).getEpochTime()+"?s=55&d=identicon&r=PG");

        ImageView chatIcon = (ImageView) convertView.findViewById(R.id.chatIcon);
        int color = Color.parseColor("#AE6118"); //The color u want

        TextView conversationTitle = (TextView) convertView.findViewById(R.id.conversationTitle);
        TextView userNameConversation = (TextView) convertView.findViewById(R.id.userNameConversation);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        FriendlyConversation friendlyConversation = getItem(position);
        conversationTitle.setText(friendlyConversation.getTitle());
        //show 1st name +"and"+ number + others

        String users = friendlyConversation.getUsers();

        GradientDrawable background = (GradientDrawable) chatIcon.getBackground();
        background.setColor(cw.getColor(users));
        String[] arr2 = users.split(",");
        if(arr2.length==1){
            userNameConversation.setText(arr2[0]);
        }else if (arr2.length==2){
            userNameConversation.setText(arr2[0]+" and "+arr2[1]);
        }else{
            userNameConversation.setText(arr2[0]+" and "+(arr2.length-1)+" others");
        }

        TextView letterIcon = (TextView)convertView.findViewById(R.id.nameIcon);
        letterIcon.setText(users.substring(0,1).toUpperCase());



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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Bitmap resizedBitmap = result.createScaledBitmap(result,42,42,true);
            //bmImage.setImageBitmap(resizedBitmap);
        }
    }

}