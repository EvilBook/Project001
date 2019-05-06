package com.example.project001.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.project001.R;
import com.example.project001.database.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;

public class TripTab2 extends Fragment {


    //variables
    private TripAdapter tripAdapter;
    private ListView tripListView;
    private String email;
    private Trip trip;
    private String tripId;


    ArrayList<Trip> trips = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


//    TripTab2_InfoWindow tab2_infoWindow_activity;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            Log.e("tripFragment", email);
        } else {
            Log.e("doesn't", "work");
        }

        tripAdapter = new TripAdapter(getContext());
        tripAdapter.email = email;
        tripAdapter.view = getView();
        tripListView = getView().findViewById(R.id.tripList);
        tripListView.setAdapter(tripAdapter);


        createTrips();


        tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tripId = ((TextView)((((LinearLayout)((((LinearLayout)(((RelativeLayout)(view)).getChildAt(1))).getChildAt(0)))).getChildAt(2)))).getText().toString();

//                TripTab2_InfoWindow activ = new TripTab2_InfoWindow();
//                tab2_infoWindow_activity.addData(tripId);

                Intent intent = new Intent(getView().getContext(), TripTab2_InfoWindow.class);
                intent.putExtra("id", tripId);
                startActivity(intent);

            }
        });


    }


    public void createTrips() {
        trips.clear();
        db.collection("trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("author").equals(email)) {
                                    Log.e("correct email", email);
                                    trip = new Trip(
                                            document.get("date").toString(),
                                            document.get("time").toString(),
                                            document.get("departure").toString(),
                                            document.get("destination").toString(),
                                            document.get("price").toString(),
                                            document.get("seats").toString(),
                                            document.getString("author"));

                                    trip.tripId = document.getId();
                                    //tripAdapter.buffer.add(document.getId());
                                    trips.add(trip);
                                    Log.e("trip departure: ", "" + trip.departure);
                                }
                            }

                            for (Trip T : trips) {
                                tripAdapter.add(T);
                                Log.e("many times", "------------------");
                                // scroll the ListView to the last added element
                                tripListView.setSelection(tripListView.getCount() - 1);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
