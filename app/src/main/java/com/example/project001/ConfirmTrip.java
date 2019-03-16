package com.example.project001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.project001.message.TripObject;

import java.util.ArrayList;

public class ConfirmRide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ride);


        TextView ResultAfterComfirmation = findViewById(R.id.FinalDisplayInfo);

        ArrayList<TripObject> list = getIntent().getParcelableArrayListExtra("An Object");
        System.out.println(list);

        ResultAfterComfirmation.setText(list.toString());





    }
}
