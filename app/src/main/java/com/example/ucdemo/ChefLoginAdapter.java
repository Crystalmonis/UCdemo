package com.example.ucdemo;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ChefLoginAdapter extends FragmentPagerAdapter {

    private final Context context;
    int totalTabs;


    public ChefLoginAdapter(FragmentManager fm, Context context, int totalTabs){
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
                ChefLoginTabFragment chefLoginTabFragment = new ChefLoginTabFragment();
                return chefLoginTabFragment;
            case 1:
                ChefSignupTabFragment chefSignupTabFragment = new ChefSignupTabFragment();
                return chefSignupTabFragment;
            default:
                return null;
        }
    }
}
