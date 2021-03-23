package com.example.ucdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordFragment extends Fragment {

    private TextView forgotpasstext,dontworrytext,gobacktext;
    private EditText emailidtext;
    private Button resetbutton;
    private ImageView forgotpassimageview;
    ConstraintLayout parentConstraintLayout;
    private FirebaseAuth firebaseAuth;
    float b = 0;
    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;
    //Activity context = getContext();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);

        forgotpasstext = v.findViewById(R.id.forgotpasstext);
        dontworrytext = v.findViewById(R.id.dontworrytext);
        gobacktext = v.findViewById(R.id.gobacktext);
        emailidtext = v.findViewById(R.id.editTextTextEmailAddress);
        resetbutton = v.findViewById(R.id.resetbutton);
        forgotpassimageview = v.findViewById(R.id.forgotpassimageview);
        parentConstraintLayout = getActivity().findViewById(R.id.parentConstraintLayout);
        emailIconContainer = v.findViewById(R.id.forgot_password_email_icon_container);
        emailIcon = v.findViewById(R.id.forgot_password_email_icon);
        emailIconText = v.findViewById(R.id.forgot_password_email_icon_text);
        progressBar = v.findViewById(R.id.forgot_password_progressbar);
        firebaseAuth = FirebaseAuth.getInstance();


        forgotpasstext.setTranslationX(-800);
        dontworrytext.setTranslationX(-800);
        emailidtext.setTranslationX(800);
        resetbutton.setTranslationX(800);
        forgotpassimageview.setTranslationY(-800);
        gobacktext.setTranslationY(800);

        forgotpasstext.setAlpha(b);
        dontworrytext.setAlpha(b);
        emailidtext.setAlpha(b);
        resetbutton.setAlpha(b);
        forgotpassimageview.setAlpha(b);
        gobacktext.setAlpha(b);

        forgotpasstext.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        forgotpassimageview.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        dontworrytext.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        emailidtext.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        resetbutton.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        gobacktext.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailidtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gobacktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gobackintent = new Intent(getActivity(),UserLoginActivity.class);
                startActivity(gobackintent);
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                resetbutton.setEnabled(false);
                resetbutton.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(emailidtext.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,emailIcon.getWidth()/2,emailIcon.getHeight()/2);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);

                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            emailIconText.setText("Recovery email sent successfully ! check your inbox");
                                            emailIconText.setTextColor(getResources().getColor(R.color.successGreen));

                                            TransitionManager.beginDelayedTransition(emailIconContainer);
                                            emailIconText.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            emailIcon.setVisibility(View.VISIBLE);
                                            emailIcon.setImageResource(R.drawable.green_email);
                                        }
                                    });

                                    emailIcon.startAnimation(scaleAnimation);
                                } else {
                                    String error = task.getException().getMessage();
                                    resetbutton.setEnabled(true);
                                    resetbutton.setTextColor(Color.rgb(255,255,255));

                                    emailIconText.setText(error);
                                    emailIconText.setTextColor(getResources().getColor(R.color.btn_red));
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIconText.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        });
            }
        });
    }

    private void checkInputs() {
        if(TextUtils.isEmpty(emailidtext.getText())){
            resetbutton.setEnabled(false);
            resetbutton.setTextColor(Color.argb(50,255,255,255));
        } else {
            resetbutton.setEnabled(true);
            resetbutton.setTextColor(Color.rgb(255,255,255));
        }
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentConstraintLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}