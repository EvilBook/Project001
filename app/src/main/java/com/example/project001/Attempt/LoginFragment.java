package com.example.project001.Attempt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.google.android.gms.wearable.DataMap.TAG;

public class LoginFragment extends Fragment {


    EditText email, password;
    Button log_button;

    FirebaseAuth auth;

    String txt_email;
    String txt_password;
    String userID;

    HashMap<String, String> hashMap = new HashMap<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        email = getView().findViewById(R.id.log_email);
        password = getView().findViewById(R.id.log_password);
        log_button = getView().findViewById(R.id.log_button);

        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_email = email.getText().toString();
                txt_password = email.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(getActivity(), "fill all the fields", Toast.LENGTH_SHORT).show();

                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {




                            } else {
                                Toast.makeText(getActivity(), "Login Failed!", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Error adding document" + auth.getCurrentUser());
                            }

                        }
                    });
                }
            }
        });

    }

    public void login(String email, String password) {

        auth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "Login Failed!", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

}
