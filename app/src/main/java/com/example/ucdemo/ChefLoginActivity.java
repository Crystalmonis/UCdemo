package com.example.ucdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ChefLoginActivity extends AppCompatActivity {

    TabLayout chefTabLayout;
    ViewPager chefViewPager;
    FloatingActionButton fb,google,insta;
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_login);

        chefTabLayout = findViewById(R.id.cheftab_layout);
        chefViewPager = findViewById(R.id.chefview_pager);
        fb = findViewById(R.id.fab_fb_chef);
        google = findViewById(R.id.fab_google_chef);
        insta = findViewById(R.id.fab_insta_chef);

        chefTabLayout.addTab(chefTabLayout.newTab().setText("Login"));
        chefTabLayout.addTab(chefTabLayout.newTab().setText("Signup"));
        chefTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ChefLoginAdapter adapter = new ChefLoginAdapter(getSupportFragmentManager(),this,chefTabLayout.getTabCount());
        chefViewPager.setAdapter(adapter);
        chefViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(chefTabLayout));
        //chefTabLayout.setupWithViewPager(chefViewPager);

        fb.setTranslationY(300);
        google.setTranslationY(300);
        insta.setTranslationY(300);
        chefTabLayout.setTranslationY(300);

        fb.setAlpha(v);
        google.setAlpha(v);
        insta.setAlpha(v);
        chefTabLayout.setAlpha(v);

        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        insta.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        chefTabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
    }
}