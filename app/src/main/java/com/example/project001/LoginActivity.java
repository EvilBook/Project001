package com.example.project001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {


    //variables
    private TextView emailField;
    private TextView passField;
    private Button   loginButton;
    private TextView forgotPassword;
    private TextView createAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("hehe");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passField = findViewById(R.id.passField);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), testActivity.class);
                startActivity(startIntent);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateActivity.class));
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, com.example.project001.message.MainActivity.class));
            }
        });




    }




}
