package com.example.project001;

import android.content.Intent;
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
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("title",  PlanTrip.destination.getText().toString());
                intent.putExtra("allDay", true);
                intent.putExtra("rule", "FREQ=YEARLY");
                intent.putExtra("dtstart", PlanTrip.date);
                intent.putExtra("allDay", 0);
                intent.putExtra("eventStatus", 1);// tentative 0, confirmed 1 canceled 2
                intent.putExtra("visibility", 3);
                intent.putExtra("description","YOUR TRIP TO "+ PlanTrip.destination.getText().toString());
                startActivity(intent);


                PlanTrip.departure.setText("");
                PlanTrip.destination.setText("");
                PlanTrip.price.setValue(0);
                PlanTrip.seats.setValue(0);

                System.out.println(PlanTrip.destination);
                System.out.println(PlanTrip.date);




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
