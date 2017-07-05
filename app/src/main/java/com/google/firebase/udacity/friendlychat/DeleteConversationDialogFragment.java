package com.google.firebase.udacity.friendlychat;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DeleteConversationDialogFragment extends DialogFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.delete_dialog_fragment, container,
                    false);
            getDialog().setTitle("DialogFragment Tutorial");
            rootView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            // Do something else
            return rootView;
        }

}
