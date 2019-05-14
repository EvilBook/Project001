package com.example.project001.Attempt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.project001.PlanTrip;
import com.example.project001.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartFragment extends Fragment {


    Button register, login;
    ConstraintLayout startLayout;

    FirebaseUser firebaseUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        register = getView().findViewById(R.id.start_reg);
        login = getView().findViewById(R.id.start_log);
        startLayout = getView().findViewById(R.id.fragment_start);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null) {
            //checks if the user is not in the database
        }


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerScreen();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginScreen();
            }
        });

    }


    public void registerScreen() {

        Bundle bun = new Bundle();

        Fragment registerFragment = new Fragment();
        registerFragment.setArguments(bun);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_start, registerFragment)
                .commit();
    }




    public void loginScreen() {

        Bundle bun = new Bundle();

        Fragment loginFragment = new Fragment();
        loginFragment.setArguments(bun);

        getChildFragmentManager()
                .beginTransaction()
                .replace(startLayout.getId(), loginFragment)
                .commit();

    }







}
