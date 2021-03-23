package com.example.ucdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ChefLoginTabFragment extends Fragment {
    private EditText email,pass;
    private TextView forgetpass;
    private Button login;
    float b = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chef_login_tab_fragment, container, false);

        email = v.findViewById(R.id.email);
        pass = v.findViewById(R.id.pass);
        forgetpass = v.findViewById(R.id.forget_pass);
        login = v.findViewById(R.id.loginbutton);
        //mFirebaseAuth = FirebaseAuth.getInstance();
        //login.setOnClickListener(mOnClickListener);

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgetpass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(b);
        pass.setAlpha(b);
        forgetpass.setAlpha(b);
        login.setAlpha(b);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetpass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


        return v;
    }

}
