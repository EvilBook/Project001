package com.example.project001.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.project001.R;
import com.example.project001.database.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import static com.google.android.gms.wearable.DataMap.TAG;

public class TripFragment extends Fragment {

    //variables
    private TripAdapter tripAdapter;
    private ListView tripView;
    String email;
    Trip trip;

    //objects
    ArrayList<Trip> voyages = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            Log.e("tripFragment", email);
        } else {
            Log.e("doesnt", "work");
        }

        tripAdapter = new TripAdapter(getContext());
        tripView = getView().findViewById(R.id.tripView);
        tripView.setAdapter(tripAdapter);

        createTrips();

    }

    public void createTrips() {

        voyages.clear();
        db.collection("trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("the email:", email);
                                if(document.getString("author").equals(email)) {
                                    trip = new Trip(document.get("destination").toString(),
                                            document.get("departure").toString(),
                                            document.get("date").toString(),
                                            document.get("price").toString(),
                                            document.get("availableSeats").toString(),
                                            document.get("freeSeats").toString(),
                                            document.getString("author"));
                                    voyages.add(trip);
                                    Log.e("trip: ", trip.getAuthor());
                                }
                            }

                            for (Trip T : voyages) {
                                tripAdapter.add(T);
                                // scroll the ListView to the last added element
                                tripView.setSelection(tripView.getCount() - 1);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}
