package com.example.ucdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView logo,splashImg;
    LottieAnimationView lottieAnimationView;
    private static final int NUM_PAGES=3;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    Animation anim;
    FirebaseAuth firebaseAuth;
    ConstraintLayout introconstraintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        logo = findViewById(R.id.logo);
        splashImg = findViewById(R.id.splashImg);
        lottieAnimationView = findViewById(R.id.lottie);
        firebaseAuth = FirebaseAuth.getInstance();
        introconstraintlayout = findViewById(R.id.introconstraintlayout);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        anim = AnimationUtils.loadAnimation(this,R.anim.o_b_anim);
        viewPager.startAnimation(anim);

        splashImg.animate().translationY(-1900).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(1700).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1700).setDuration(1000).setStartDelay(4000);


    }



    //@Override
    //protected void onStart() {
        //super.onStart();

        //FirebaseUser currentuser = firebaseAuth.getCurrentUser();

        //if(currentuser != null){
            //Intent mainIntent = new Intent(IntroductoryActivity.this,UserMainPage.class);
            //startActivity(mainIntent);
            //finish();
        //}
    //}

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{


        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
                    return tab1;
                case 1:
                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
                    return tab2;
                case 2:
                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}