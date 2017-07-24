package com.google.firebase.udacity.friendlychat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class StartNewConversationActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("lightgrey"));
        toolbar.setTitle("Post Something");
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        setSupportActionBar(toolbar);
        EditText et = (EditText) findViewById(R.id.firstMessagePost);
        et.setHorizontallyScrolling(false);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        //Set up Firebase Auth for getting username

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartNewConversationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });




        // Check if signed in
        // create auth listener // type new authstatelistener and wait for autocomplete :p
        FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // is user is signed in
                    onSignedInInitialize(user.getDisplayName());
                    //TODO: remove this to its own method
                    Log.e("TAG", "in the atttachDBRL method");
                    Log.e("ChatList:",user.getDisplayName());
                    Toast.makeText(StartNewConversationActivity.this, "I am logged in", Toast.LENGTH_LONG).show();

                    //TODO: implement these methods
                    //attachDatabaseReadListener();
                    //attachClickListener();
                } else {
                    // if user is not signed in
                    Toast.makeText(StartNewConversationActivity.this, "I am not logged in", Toast.LENGTH_LONG).show();
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

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);





    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
       //     }
        });*/


}

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.backfromnewchat);
        item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newchatmenu, menu);
        MenuItem postButton = menu.findItem(R.id.postNewChatButton);
        final EditText mChatEditTitle = (EditText)findViewById(R.id.newposttitle);
        final EditText newConversation = (EditText) findViewById(R.id.firstMessagePost);
        postButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //TODO: what happens when you click on new post
                String test = mChatEditTitle.getText().toString();
                if(test.length()>10 && test.length()<=91){
                    //TODO:FIXXXXXXXXXXXXX THIS
                    Toast.makeText(StartNewConversationActivity.this, test, Toast.LENGTH_LONG).show();
                    //use defualt users name
                    String defaultUsers = mUsername;
                    FriendlyConversation fc = new FriendlyConversation(test, defaultUsers, "");
                    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference mMessagesDatabaseReference =
                            mFirebaseDatabase.getReference().child("chats");
                    DatabaseReference df = mMessagesDatabaseReference.push();
                    String id =df.getKey();
                    Log.e("id i just pushed is ",""+id);
                    fc.setId(id);
                    mMessagesDatabaseReference.child(id).setValue(fc);
                    mChatEditTitle.setText("");

                    DatabaseReference mMessagesDatabaseReference2 =
                            mFirebaseDatabase.getReference().child("messages");
                    FriendlyMessage friendlyMessage = new FriendlyMessage
                            (newConversation.getText().toString(), mUsername, null,id);
                    Log.e("onCLick",id);
                    if(newConversation.getText().length()>0){
                        mMessagesDatabaseReference2.push().setValue(friendlyMessage); // this saves the value
                    }

                    Log.d("MainActivty", "Checking if this is printing");

                    // Clear input box
                    goToChatActivity(id, defaultUsers);

                }else if(test.length()<10){
                    Toast.makeText(StartNewConversationActivity.this,
                            "Message must be mundane!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(StartNewConversationActivity.this,
                            "Your message must be shorter", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void goToChatActivity(String id, String users) {
        Intent intent = new Intent(StartNewConversationActivity.this, ChatActivity.class);
        intent.putExtra("clickedId",id);
        Log.e("TAGid",id);
        startActivity(intent);
    }

}
