package com.google.firebase.udacity.friendlychat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Mohaimin on 7/2/2017.
 */
class SimpleFragmentPagerAdapter  extends FragmentPagerAdapter {

    SimpleFragmentPagerAdapter(FragmentManager fm) {

        super(fm);
        Log.e("SFPA: ","ctor called");
    }

    private String tabTitles[] = new String[] { "Status", "Chats", "Profile" };

    @Override
    public Fragment getItem(int position) {
        //Log.e("SFPA: ","Get Item called");
        if (position == 0) {
            return new StatusFragment();
        } else if (position == 1){
            return new ChatListFragment();
        } else {
            return new ProfileFragment();
        }
    }

    @Override
    public int getCount() {
        //Log.e("SFPA: ","Get count");
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //Log.e("SFPA: ", "getPageTitle called");
        return tabTitles[position];
    }
}
