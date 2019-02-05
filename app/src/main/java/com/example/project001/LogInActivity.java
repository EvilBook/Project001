package com.example.project001;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

    private TextView emailField, passwordField;
    private Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField=(TextView) findViewById(R.id.emailField);
        passwordField=(TextView) findViewById(R.id.passwordField);
        sendButton=(Button) findViewById(R.id.sendButton);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("email: "+emailField.getText()+ ", password: " + passwordField.getText());

                Intent startIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(startIntent);
            }
        });
    }

}