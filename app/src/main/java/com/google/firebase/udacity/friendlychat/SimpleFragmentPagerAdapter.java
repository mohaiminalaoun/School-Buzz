package com.google.firebase.udacity.friendlychat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mohaimin on 7/2/2017.
 */
class SimpleFragmentPagerAdapter  extends FragmentPagerAdapter {

    SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
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
        return 3;
    }
}
