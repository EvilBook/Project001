package com.example.project001;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.project001.message.TripObject;

import java.util.ArrayList;

public class PlanTrip extends AppCompatActivity {

    String FromLocation;
    String ToLocation;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);

        Intent intent1 = getIntent();

        FromLocation = intent1.getStringExtra("From Destination");
        ToLocation = intent1.getStringExtra("To Destination");

        TextView fromLocation = findViewById(R.id.TravellingFrom);
        TextView toLocation = findViewById(R.id.travellingtoText);

        fromLocation.setText(FromLocation);
        toLocation.setText(ToLocation);


        TimePicker timePicker = findViewById(R.id.timePicker1);
        DatePicker datePicker = findViewById(R.id.datePicker);

        int freeseats = 0;
        String licenseplate = null;
        String Price = null;






        TripObject tripObject = new TripObject(FromLocation,ToLocation,timePicker.getHour(),timePicker.getMinute(),datePicker.getMonth(),
                datePicker.getDayOfMonth(),datePicker.getYear(),Price,licenseplate,freeseats);

        final ArrayList<TripObject> list = new ArrayList<>();

        list.add(tripObject);


        System.out.println(list);


        Button comfirmButton = findViewById(R.id.ComfirmButton);

        comfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PlanTrip.this, ConfirmRide.class);

                myIntent.putParcelableArrayListExtra("An Object",list);
                PlanTrip.this.startActivity(myIntent);


            }
        });
















    }
}
