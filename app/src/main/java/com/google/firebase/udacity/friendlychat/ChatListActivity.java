package com.google.firebase.udacity.friendlychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private  FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private DatabaseReference mMessagesDatabaseReference;
    private ConversationAdapter mConversationAdapter;
    private ListView mConversationListView;
    private ChildEventListener mChildEventListener;
    private Button mNewChatButton;
    private EditText mChatEditTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_lists);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //initialize Auth reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        //initialize storage
        mFirebaseStorage = FirebaseStorage.getInstance();

        mFirebaseRemoteConfig= FirebaseRemoteConfig.getInstance();


        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("chats");

        mConversationListView = (ListView) findViewById(R.id.conversationListView);

        List<FriendlyConversation> conversations = new ArrayList<>();
        mConversationAdapter = new ConversationAdapter(this, R.layout.item_conversation, conversations);
        mConversationListView.setAdapter(mConversationAdapter);

        Log.e("TAG", "going to attach database listener");
        attachDatabaseReadListener();

        mNewChatButton = (Button) findViewById(R.id.newChatButton);
        mChatEditTitle = (EditText) findViewById(R.id.chatEditTitle);
        mNewChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = mChatEditTitle.getText().toString();
                Log.e("TAG",test);
                Toast.makeText(ChatListActivity.this, test, Toast.LENGTH_LONG).show();
                //use defualt users name
                String defaultUsers = "Jon, Tom, Dick";
                FriendlyConversation fc = new FriendlyConversation(test, defaultUsers);
                mMessagesDatabaseReference.push().setValue(fc);
                mChatEditTitle.setText("");
            }
        });
    }


    private void attachDatabaseReadListener(){
        Log.e("TAG", "in the atttachDBRL method");
        if(mChildEventListener==null) {
            Log.e("TAG", "is null");
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //snapshot is current snapshot, then we seseriaze the snapshot to FM
                    FriendlyConversation fc = dataSnapshot.getValue(FriendlyConversation.class);
                    Log.e("TAG", fc.getTitle());
                    mConversationAdapter.add(fc);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
}
