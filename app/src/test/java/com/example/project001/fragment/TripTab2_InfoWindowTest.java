package com.example.project001.fragment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.project001.database.DBConnection;
import com.example.project001.database.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TripTab2_InfoWindowTest {

    //VARIABLES
    DBConnection dbConnection;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String tripId = "abcdefgh";
    String passenger = "sammple";
    String driver = "sample";
    String status = "0";


    @Test
    public void deleteRequest() {

        //ADD REQUEST TO DB
        Request request = new Request(tripId, passenger, driver, status);
        db.collection("request").document().set(request);
        Log.e("", "Added to request table.");

        //DELETE REQUEST FROM DB
        TripTab2_InfoWindow t = new TripTab2_InfoWindow();
        t.deleteRequest(tripId);

        //CHECK IF REQUEST IS DELETED
        db.collection("request")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int actual = 2;
                            int expected = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("tripId").equals(tripId)) {
                                    actual = 1;
                                }else{
                                    actual = 0;
                                }
                            }
                            Assert.assertEquals(actual, expected);
                        }
                    }
                });
    }

    @Test
    public void deleteTrip() {
    }

    @Test
    public void deleteRequest1() {
    }

    @Test
    public void deleteTrip1() {
    }
}