package com.example.ucdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class ChefSignupTabFragment extends Fragment {

    private EditText name, email, phone, pass, confirmpass;
    private Button signup;
    //View usersignuptabfragment;
    float b = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chef_signup_tab_fragment, container, false);
        //usersignuptabfragment = inflater.inflate(R.layout.usersignup_tab_fragment,container,false);

        //mFirebaseAuth = FirebaseAuth.getInstance();

        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        pass = v.findViewById(R.id.pass);
        phone = v.findViewById(R.id.phone);
        confirmpass = v.findViewById(R.id.confirmpass);
        signup = v.findViewById(R.id.signup);
        //signup.setOnClickListener(mOnClickListener);


        name.setTranslationX(800);
        email.setTranslationX(800);
        phone.setTranslationX(800);
        pass.setTranslationX(800);
        confirmpass.setTranslationX(800);
        signup.setTranslationX(800);

        name.setAlpha(b);
        email.setAlpha(b);
        phone.setAlpha(b);
        pass.setAlpha(b);
        confirmpass.setAlpha(b);
        signup.setAlpha(b);


        name.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        confirmpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return v;
    }
}
