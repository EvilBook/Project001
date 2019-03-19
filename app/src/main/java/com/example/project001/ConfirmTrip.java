package com.example.project001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;

import java.util.ArrayList;

public class ConfirmTrip extends AppCompatActivity {

    //variables
    Button confirm;
    Button cancel;

    //objects
    DBConnection dbc = new DBConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_trip);
        TextView ResultAfterConfirmation = findViewById(R.id.displayTrip);
        ArrayList<Trip> list = getIntent().getParcelableArrayListExtra("An Object");
        ResultAfterConfirmation.setText(list.toString());

        confirm = findViewById(R.id.confirmButton);
        cancel = findViewById(R.id.cancelButton);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Trip> list = getIntent().getParcelableArrayListExtra("An Object");
                dbc.addTripToDB(list.get(0));

                PlanTrip.departure.setText("");
                PlanTrip.destination.setText("");
                PlanTrip.price.setValue(0);
                PlanTrip.seats.setValue(0);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
