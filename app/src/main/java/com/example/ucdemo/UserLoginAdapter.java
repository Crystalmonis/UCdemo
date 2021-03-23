package com.example.ucdemo;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class UserLoginAdapter extends FragmentPagerAdapter {
    private final Context context;
    int totalTabs;


    public UserLoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }


    public Fragment getItem(int position) {
        switch (position){
            case 0:
                UserLoginTabFragment userLoginTabFragment = new UserLoginTabFragment();
                return userLoginTabFragment;
            case 1:
                UserSignupTabFragment userSignupTabFragment = new UserSignupTabFragment();
                return userSignupTabFragment;
            default:
                return null;
        }
    }

}
