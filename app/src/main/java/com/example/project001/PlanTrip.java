package com.example.project001;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.example.project001.database.Trip;
import java.util.ArrayList;

public class PlanTrip extends Fragment {

    //variables
    public static String date;
    public static String time;
    public static String email;
    public static EditText departure;
    public static EditText destination;
    public static EditText price;
    public static EditText seats;
    Button createButton;
    Trip trip;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_trip, container, false);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            //Log.e("tripFragment", email);
        } else {
            Log.e("doesnt", "work");
        }


        Log.e("something", "happening");

        departure = getView().findViewById(R.id.departureTXT);
        destination = getView().findViewById(R.id.destinationTXT);
        price = getView().findViewById(R.id.priceTXT);
        seats = getView().findViewById(R.id.seatsTXT);

        final TimePicker timePicker = getView().findViewById(R.id.time);
        final DatePicker datePicker = getView().findViewById(R.id.date);

        createButton = getView().findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = timePicker.getHour();
                int min = timePicker.getMinute();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                time = String.valueOf(hour) + ":" + String.valueOf(min);
                date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

                addTrip();

            }
        });
    }


    public void addTrip() {

        trip = new Trip(
                date,
                time,
                departure.getText().toString(),
                destination.getText().toString(),
                price.getText().toString(),
                seats.getText().toString(),
                email
        );

        final ArrayList<Trip> list = new ArrayList<>();

        list.add(trip);
        System.out.println(list);

        if(destination.getText().toString().isEmpty() ||
                departure.getText().toString().isEmpty() ||
                date.isEmpty() ||
                price.getText().toString().isEmpty() ||
                seats.getText().toString().isEmpty() ||
                time.isEmpty()) {

            createButton.setClickable(false);

        } else {

            Intent myIntent = new Intent(getContext(), ConfirmTrip.class);
            myIntent.putParcelableArrayListExtra("An Object", list);
            PlanTrip.this.startActivity(myIntent);

        }

        createButton.setClickable(true);

    }

}
