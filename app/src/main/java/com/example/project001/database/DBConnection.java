package com.example.project001.database;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.google.android.gms.wearable.DataMap.TAG;

public class DBConnection {

    //variables




    //Objects
    Trip trip;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Trip> trips = new ArrayList<>();




    //method for registering the user
    private void addUserToDB(String email, String name) {

        //Before everything else
        //db.setFirestoreSettings(settings);

        // Create a new user with a first and last name
        Map<String, Object> person = new HashMap<>();

        person.put("name", name);
        person.put("email", email);


        // Add a new document with a generated ID
        db.collection("person")
                .add(person)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    //method for checking if the email exists
    public void checkIfExists(final String email, final String name) {

        //db.setFirestoreSettings(settings);

        db.collection("person")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //Log.e("actually gets", document.get("email").toString());
                                if(document.get("email").equals(email)) {
                                    Log.e("the email", "exists");
                                    return;
                                }
                            }

                            //if doe
                            addUserToDB(email, name);

                        } else {
                            Log.e("data", "inserting");
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    //method to add a trip
    public void addTripToDB(Object trip) {

        db.collection("trip").document().set(trip);

    }

    public void getTrip() {

        trips.clear();

        db.collection("trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                trip = new Trip(document.get("destination").toString(),
                                        document.get("departure").toString(),
                                        document.get("date").toString(),
                                        document.get("price").toString(),
                                        document.get("availableSeats").toString(),
                                        document.get("freeSeats").toString(),
                                        document.getString("author"));

                                trips.add(trip);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        for(Trip trip: trips) {
                            Log.e("author", trip.getAuthor());
                        }
                    }
                });


    }

}
