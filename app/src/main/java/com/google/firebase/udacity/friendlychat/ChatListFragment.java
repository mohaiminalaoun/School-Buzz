package com.google.firebase.udacity.friendlychat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ChildEventListener mChildEventListener;
    private static final int RC_SIGN_IN = 1;
    private ConversationAdapter mConversationAdapter;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String mUsername;
    //android.support.design.widget.FloatingActionButton mNewChatButton;


    // Butterknife components

    @BindView(R.id.fab) android.support.design.widget.FloatingActionButton mNewChatButton;
    @BindView(R.id.conversationListViewFragment) ListView mConversationListView;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("ChatListFragement", " onCreateView() is called");
        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ButterKnife.bind(this, rootView);
        //TODO: copy Oncreate function from ChatListActivity
      //  mNewChatButton =
       //         (android.support.design.widget.FloatingActionButton) rootView.findViewById(R.id.fab);

        ButterKnife.bind(this.getActivity());
        mNewChatButton.show();
        Log.e("CHAT FRAG: ",""+mNewChatButton.isClickable());
        mNewChatButton.show();


        //Firebase stuff
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        final DatabaseReference mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("chats");


        //final ListView mConversationListView = (ListView) rootView.findViewById(R.id.conversationListViewFragment);
        Log.e("CLF ", mConversationListView.toString() );



        //set onScroll listener
        mConversationListView.setOnScrollListener(new ListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("CLF: ","scroll state changed");
                mNewChatButton.show();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("CLF: ", "scrolling");
                Log.e("CLF: total", " count is "+totalItemCount);
                if(totalItemCount>visibleItemCount) {
                    mNewChatButton.hide();
                }else{
                    mNewChatButton.show();
                }
            }
        });

        List<FriendlyConversation> conversations = new ArrayList<>();
        mConversationAdapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversations);
        mConversationListView.setAdapter(mConversationAdapter); //DONE

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
                    if(mChildEventListener==null) {
                        Log.e("TAG", "is null");
                        mChildEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                //snapshot is current snapshot, then we seseriaze the snapshot to FM
                                FriendlyConversation fc = dataSnapshot.getValue(FriendlyConversation.class);
                                Log.e("TAG", "this is called when no internet");
                                mConversationAdapter.add(fc);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                FriendlyConversation fc = dataSnapshot.getValue(FriendlyConversation.class);
                                if(fc.getTitle()!=null){
                                    Log.e("REMOVE: ", fc.getTitle());
                                }else{
                                    Log.e("IT:"," IS NULLL WTFFFF");
                                }

                                mConversationAdapter.remove(fc);
                                //TODO: this doesn't refresh the listview WHYYYYYYY
                                synchronized (mConversationAdapter){
                                    mConversationAdapter.notifyDataSetChanged();
                                    mConversationAdapter.notify();
                                }

                               getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("THIS"," IS BEING CALLED");
                                        mConversationAdapter.notifyDataSetChanged();
                                        Log.e("THIS 2"," IS BEING CALLED");
                                    }
                                });
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
                    Log.e("ChatList:",user.getDisplayName());
                    Toast.makeText(getActivity(), "I am logged in", Toast.LENGTH_LONG).show();

                    //TODO: implement these methods
                    //attachDatabaseReadListener();
                    //attachClickListener();
                } else {
                    // if user is not signed in
                    Toast.makeText(getActivity(), "I am not logged in", Toast.LENGTH_LONG).show();
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


        mNewChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*Toast.makeText(getActivity(), test, Toast.LENGTH_LONG).show();
                    //use defualt users name
                    String defaultUsers = mUsername;
                    FriendlyConversation fc = new FriendlyConversation(test, defaultUsers, "");

                    DatabaseReference df = mMessagesDatabaseReference.push();
                    String id =df.getKey();
                    Log.e("id i just pushed is ",""+id);
                    fc.setId(id);
                    mMessagesDatabaseReference.child(id).setValue(fc);
                    mChatEditTitle.setText("");*/

                    //TODO: remove this after testing
                    Intent intent = new Intent(getActivity(), StartNewConversationActivity.class);
                    startActivity(intent);


            }
        });



        mConversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendlyConversation fc = (FriendlyConversation)
                        mConversationListView.getItemAtPosition(position);
                // TODO: get conversation details and pass them to the chat activity
                String mId = fc.getId();
                String title = fc.getTitle();
                goToChatActivity(mId, title, fc.getUsers());
            }
        });


        //TODO: Set long click delete / edit option
        mConversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Clicked item, ","LONG CLICK");
                //Note: return true to stop from doing single click after dbl click
                FriendlyConversation fc = (FriendlyConversation)
                        mConversationListView.getItemAtPosition(position);
                String mId = fc.getId();
                ((MainActivity) getActivity()).showAddMealDialog(view, mId);
                return true;
            }
        });





        return rootView;
    }

    private void goToChatActivity(String mId, String title, String users) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("clickedId",mId);
        intent.putExtra("title", title);
        intent.putExtra("users", users);
        Log.e("TAGid",mId);
        startActivity(intent);
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
        // can read messages when you're signed in only
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(String param1, String param2) {
        Log.e("ChatListFragement", " newInstance() is called");
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("ChatListFragement", " conCreate() is called");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("asdasd", "    notifying");
        mNewChatButton.show();
        mConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("ChatListFragement", " onAttach() is called");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
