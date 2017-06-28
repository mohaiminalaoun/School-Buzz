package com.google.firebase.udacity.friendlychat;

import java.util.ArrayList;

/**
 * Created by Jesse on 6/23/2017.
 */

public class FriendlyConversation {


    private String title;
    private String users;
    private long epochTime;
    private String id;

    public FriendlyConversation() {
    }

    public FriendlyConversation(String title, String users, String id) {
        this.title = title;
        this.users = users;
        this.epochTime = System.currentTimeMillis()/1000;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public String getUsers() {
        return this.users;
    }

    public void addUser(String username){
        users+=","+username;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }
}
