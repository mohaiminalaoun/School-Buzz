package com.google.firebase.udacity.friendlychat;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertDeleteFragment extends DialogFragment

    {


        /**
         * Public static constructor that creates fragment and
         * passes a bundle with data into it when adapter is created
         */

    public static AlertDeleteFragment newInstance(String conversationId) {
        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mId", conversationId);
        alertDeleteFragment.setArguments(bundle);
        return alertDeleteFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* Use the Builder class for convenient dialog construction */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                android.R.style.Theme_Material_Light_Dialog);
        /* Get the layout inflater */
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_meal, null);
        final String mId = (String )getArguments().getSerializable("mId");

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout */
        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton("Delete Thread", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteConversation(mId);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("EEEE:", " clicked negative button");
            }
        });

        return builder.create();
    }

        private void deleteConversation(String mId) {
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
            Log.e("TAG: --> ",""+mFirebaseDatabase.getReference().child("chats").child(mId));
            mFirebaseDatabase.getReference().child("chats").child(mId).setValue(null);
            Log.e("TAG", " "+mId);
        }



}
