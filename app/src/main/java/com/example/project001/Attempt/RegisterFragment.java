package com.example.project001.Attempt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project001.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    EditText username, email, password;
    Button register;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    String txt_username;
    String txt_email;
    String txt_password;
    String userID;

    HashMap<String, String> hashMap = new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        username = getView().findViewById(R.id.reg_username);
        email = getView().findViewById(R.id.reg_email);
        password = getView().findViewById(R.id.reg_password);
        register = getView().findViewById(R.id.reg_button);


        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_username = username.getText().toString();
                txt_email = email.getText().toString();
                txt_password = password.getText().toString();

                if(txt_password.length() < 6) {
                    Toast.makeText(getActivity(), "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_email, txt_password);

                }

            }
        });

    }


    public void register(final String username, String email, String password) {

        //Log.e("this fucking faggot", auth.toString() + email + " " + password);

        auth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            firebaseUser = auth.getCurrentUser();
                            userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                            hashMap.put("id", userID);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {

                                        // Create new fragment and transaction


                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), "You can't register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
