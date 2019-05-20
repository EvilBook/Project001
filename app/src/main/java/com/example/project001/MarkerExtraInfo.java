package com.example.project001;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MarkerExtraInfo implements GoogleMap.InfoWindowAdapter {

    private Context context;


    private InfoWindowData infoWindowData;


    private LinearLayout linearLayout;



    public MarkerExtraInfo(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        if(!marker.getTitle().matches("Your Location")) {

            //When marker is a trip:

            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.marker_info1, null);

            final TextView departureBody = view.findViewById(R.id.name);
            TextView destinationBody = view.findViewById(R.id.destination_body);
            TextView authorBody = view.findViewById(R.id.author_body);
            TextView dateBody = view.findViewById(R.id.date_body);
            TextView priceBody = view.findViewById(R.id.price_body);
            TextView availableSeatsBody = view.findViewById(R.id.availableSeats_body);
            linearLayout=view.findViewById(R.id.verifiedContainer);


            infoWindowData = (InfoWindowData) marker.getTag();
            RidersActivity.fromLocation.setText(infoWindowData.getDeparture());
            RidersActivity.toLocation.setText(infoWindowData.getDestination());


            departureBody.setText("Departure: " + infoWindowData.getDeparture());
            destinationBody.setText("Destination: " + infoWindowData.getDestination());
            authorBody.setText(infoWindowData.getAuthor());
            dateBody.setText("Date: " + infoWindowData.getDate());
            priceBody.setText(infoWindowData.getPrice() + " Kr");
            availableSeatsBody.setText("Total Seats: " + infoWindowData.getAvailableSeats());
            Log.e("FUCK YOU PIECE OF SHIT", infoWindowData.getVerified()+" what"+ infoWindowData.getAuthor());
            if(!infoWindowData.verified.equals("true")) {
                linearLayout.setVisibility(View.INVISIBLE);
            }else{
                linearLayout.setVisibility(View.VISIBLE);
            }


            return view;


        }else{

            //When marker is your location:

            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.marker_info_yourlocation, null);

            return view;

        }
    }

}