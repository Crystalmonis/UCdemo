package com.example.ucdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.ucdemo.UserLoginActivity.disableCloseBtn;

public class UserSignupTabFragment extends Fragment {

    private EditText name, email, phone, pass, confirmpass;
    private Button signup;
    //View usersignuptabfragment;
    float b = 0;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference mdatabase;
    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_signup_tab_fragment, container, false);

        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        pass = v.findViewById(R.id.pass);
        phone = v.findViewById(R.id.phone);
        confirmpass = v.findViewById(R.id.confirmpass);
        signup = v.findViewById(R.id.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkEmailAndPassword();
            }
        });


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

    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if(pass.getText().toString().equals(confirmpass.getText().toString())){
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("fullname",name.getText().toString());
                                    userdata.put("email",email.getText().toString());
                                    userdata.put("profile","");
                                    //userdata.put("phone",phone.getText().toString());
                                    //userdata.put("password",pass.getText().toString());
                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                        ////////////////MAPS
                                                        Map<String, Object> wishlistMap = new HashMap<>();
                                                        wishlistMap.put("list_size", (long) 0);

                                                        Map<String,Object> ratingsMap = new HashMap<>();
                                                        ratingsMap.put("list_size", (long) 0);

                                                        Map<String,Object> cartMap = new HashMap<>();
                                                        cartMap.put("list_size",(long)0);

                                                        Map<String,Object> myAddressesMap = new HashMap<>();
                                                        myAddressesMap.put("list_size",(long)0);

                                                        Map<String,Object> notificationsMap = new HashMap<>();
                                                        notificationsMap.put("list_size",(long)0);
                                                        ////////////////MAPS

                                                        List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESSES");
                                                        documentNames.add("MY_NOTIFICATIONS");

                                                        List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishlistMap);
                                                        documentFields.add(ratingsMap);
                                                        documentFields.add(cartMap);
                                                        documentFields.add(myAddressesMap);
                                                        documentFields.add(notificationsMap);


                                                        for(int x = 0; x < documentNames.size(); x++){
                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(finalX == documentFields.size() - 1){
                                                                        mainIntent();
                                                                    } else {
                                                                        signup.setEnabled(true);
                                                                        signup.setTextColor(Color.rgb(255,255,255));
                                                                        //String error=task.getException().getMessage();
                                                                        //Toast.makeText(getActivity(), error,Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    signup.setEnabled(true);
                                    signup.setTextColor(Color.rgb(255,255,255));
                                }
                            }
                        });
            } else {
                confirmpass.setError("Passwords don't match!");
            }
        } else {
            email.setError("Invalid email!");
        }
    }

    private void checkinputs(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(email.getText())){
                if(!TextUtils.isEmpty(phone.getText())){
                    if(!TextUtils.isEmpty(pass.getText()) && pass.length() >=6 ){
                        if(!TextUtils.isEmpty(confirmpass.getText())){
                            signup.setEnabled(true);
                            signup.setTextColor(Color.rgb(255,255,255));
                        } else {
                            signup.setEnabled(false);
                            signup.setTextColor(Color.argb(50,255,255,255));
                        }
                    } else {
                        signup.setEnabled(false);
                        signup.setTextColor(Color.argb(50,255,255,255));
                    }
                } else {
                    signup.setEnabled(false);
                    signup.setTextColor(Color.argb(50,255,255,255));
                }
            } else {
                signup.setEnabled(false);
                signup.setTextColor(Color.argb(50,255,255,255));
            }
        } else {
            signup.setEnabled(false);
            signup.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void mainIntent(){
        if(UserLoginActivity.disableCloseBtn){
            UserLoginActivity.disableCloseBtn=false;
        }else {
            startActivity(new Intent(getActivity(),UserMainPage.class));
        }
        getActivity().finish();
    }


}
