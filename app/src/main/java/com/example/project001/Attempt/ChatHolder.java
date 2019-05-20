package com.example.project001.Attempt;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.project001.R;

public class ChatHolder extends AppCompatActivity {


    Button registerButton, loginButton;
    FrameLayout chat_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_holder);

        chat_holder = findViewById(R.id.chat_holder);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerScreen();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginScreen();
            }
        });

    }

    private void loginScreen() {

        Bundle bun = new Bundle();

        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bun);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chat_holder, loginFragment)
                .commit();

    }

    private void registerScreen() {

        Bundle bun = new Bundle();

        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setArguments(bun);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chat_holder, registerFragment)
                .commit();

    }


}
