package com.google.firebase.udacity.friendlychat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private DatabaseReference mMessagesDatabaseReference;
    private ConversationAdapter mConversationAdapter;
    private ListView mConversationListView;
    private ChildEventListener mChildEventListener;
    private Button mNewChatButton;
    private EditText mChatEditTitle;

    //Working on AuthListener
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;

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
        // Check if signed in
        // create auth listener // type new authstatelistener and wait for autocomplete :p
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // is user is signed in
                    onSignedInInitialize(user.getDisplayName());
                    Log.e("ChatList:",user.getDisplayName());
                    Toast.makeText(ChatListActivity.this, "I am logged in", Toast.LENGTH_LONG).show();
                    attachDatabaseReadListener();
                } else {
                    // if user is not signed in
                    Toast.makeText(ChatListActivity.this, "I am not logged in", Toast.LENGTH_LONG).show();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false) //smart lock is disabled
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        Log.e("TAG", "going to attach database listener");



        mNewChatButton = (Button) findViewById(R.id.newChatButton);
        mChatEditTitle = (EditText) findViewById(R.id.chatEditTitle);

        mChatEditTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()>=0){
                    mNewChatButton.setEnabled(true);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        mNewChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = mChatEditTitle.getText().toString();
                Toast.makeText(ChatListActivity.this, test, Toast.LENGTH_LONG).show();
                //use defualt users name
                String defaultUsers = mUsername;
                FriendlyConversation fc = new FriendlyConversation(test, defaultUsers);
                mMessagesDatabaseReference.push().setValue(fc);
                mChatEditTitle.setText("");
            }
        });
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
        // can read messages when you're signed in only
        attachDatabaseReadListener();
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


    @Override
    protected void onResume(){
        super.onResume();
        Log.e("ChatList", "onresume");
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }




}
