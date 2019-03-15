package com.example.project001.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.project001.R;
import com.example.project001.RidersActivity;
import com.example.project001.SideBarActivity;
import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class
HomeFragment extends Fragment {


    Button nav_messages, nav_trips, nav_settings, nav_logout;
    LinearLayout profile;
    String displayName;
    String email;
    GoogleSignInClient googleApiClient;
    DrawerLayout drawerLayout;
    LinearLayout linearLayout;
    TabHost frameLayout;

    EditText destination;
    EditText departure;
    EditText date;
    EditText price;
    EditText availableSeats;
    EditText freeSeats;
    TextView textView;
    Button riderButton;
    Button button;


    //Database
    Trip trip;
    DBConnection dbc = new DBConnection();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(getArguments() != null) {
            email = getArguments().getString("email");
            Log.e("homeFragment", email);
        } else {
            Log.e("doesnt", "work");
        }


        frameLayout = getView().findViewById(R.id.tabHost);


        //Tabs
        TabHost host = getView().findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);


        linearLayout = getView().findViewById(R.id.tab1);
        mainScreen();

        button = getView().findViewById(R.id.addTrip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrip();
            }
        });

    }

    public void mainScreen() {

        Fragment fragment=new RidersActivity();


        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(linearLayout.getId(), fragment, "maps");
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();


        for(int i=0; i<linearLayout.getChildCount(); i++){

            Log.e("oneone", linearLayout.getChildAt(i).toString());

        }

    }




    public void addTrip() {
        destination = getView().findViewById(R.id.destination);
        departure = getView().findViewById(R.id.departure);
        date = getView().findViewById(R.id.date);
        price = getView().findViewById(R.id.price);
        availableSeats = getView().findViewById(R.id.availableSeats);
        freeSeats = getView().findViewById(R.id.freeSeats);
        textView = getView().findViewById(R.id.textView);

        trip = new Trip(destination.getText().toString(),
                departure.getText().toString(),
                date.getText().toString(),
                price.getText().toString(),
                availableSeats.getText().toString(),
                freeSeats.getText().toString(),
                email);

        Log.e("it contains: ", destination.getText().toString());
        Log.e("it contains: ", departure.getText().toString());

        if(destination.getText().toString().isEmpty() ||
                departure.getText().toString().isEmpty() ||
                date.getText().toString().isEmpty() ||
                price.getText().toString().isEmpty() ||
                availableSeats.getText().toString().isEmpty() ||
                freeSeats.getText().toString().isEmpty()) {

            textView.setText("*Please fill in all the fields.");
        } else {
            dbc.addTripToDB(trip);
            //trips.add(trip);
        }

        destination.setText("");
        departure.setText("");
        price.setText("");
        availableSeats.setText("");
        date.setText("");
        freeSeats.setText("");
    }


}
