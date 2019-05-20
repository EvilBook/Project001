package com.example.project001.database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.project001.RidersActivity;
import com.example.project001.SideBarActivity;
import com.example.project001.fragment.HomeFragment;
import com.example.project001.fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.google.android.gms.wearable.DataMap.TAG;

public class DBConnection {

    //Variables
    Trip trip;


    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ArrayList<Trip> trips = new ArrayList<>();

    public RidersActivity r;
    public ProfileFragment p;


    //method for registering the user
    private void addUserToDB(String email, String name,int AverageRating,int NumberOfRating) {

        // Create a new user with a first and last name
        Map<String, Object> person = new HashMap<>();

        person.put("name", name);
        person.put("email", email);
        person.put("AverageRating",AverageRating);
        person.put("NumberOfRating",NumberOfRating);

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
    public void checkIfExists(final String email, final String name,final int AverageRating,final int NumberOfRating) {

        db.collection("person")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //if exists break
                                if(document.get("email").equals(email)) {
                                    Log.e("the email", "exists");
                                    return;
                                }
                            }
                            //if doesn't exist
                            addUserToDB(email, name,AverageRating,NumberOfRating);

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

    //method for getting trips for the map
    public void getTripsforMap() {


        trips.clear();

        db.collection("trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        trip = new Trip(
                                document.get("date").toString(),
                                document.get("time").toString(),
                                document.get("departure").toString(),
                                document.get("destination").toString(),
                                document.get("price").toString(),
                                document.get("seats").toString(),
                                document.getString("author"));



                        trips.add(trip);
//                        System.out.println("SUZ ARRAY: " + trips.size());

                    }
                    r.getArrayList(trips);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    //method for adding a trip request
    //0 = pending, 1 = accepted, 2 = declined
    public void addTripRequest(final String driver, final String passenger, final String status,
                               final String departure, final String destination, final String date){

        db.collection("trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

//                    System.out.println("THE VALUES: "
//                            + "\ndriver: "+ driver
//                            + "\npassenger: " + passenger
//                            + "\ndeparture: " + departure
//                            + "\ndestination: " + destination
//                            + "\ndate: " + date
//                            + "\nstatus: " + status);



                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        final String tripId;

//                        System.out.println("THE VALUES DATABASE: "
//                                + "\ndriver: "+ document.getString("author")
//                                + "\ndeparture: "+ document.getString("departure")
//                                + "\ndestination: "+ document.getString("destination")
//                                + "\ndate: "+ document.getString("date"));


                        if(document.getString("author").equals(driver) &&
                                document.getString("destination").equals(destination) &&
                                document.getString("departure").equals(departure) &&
                                document.getString("date").equals(date)){


                            if(document.getString("author").equals(passenger)) {
                                Log.e("", "You can't request a trip where you are the driver.");

                                //show dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(r.getContext()).create();
                                alertDialog.setTitle("Problem Requesting Trip");
                                alertDialog.setMessage("You can't request a trip where you are the driver.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                break;


                            }else {
                                tripId = document.getId();

                                final String seat = document.getString("seats");
                                Log.e("Seats xxx ", "" + seat);




                                //CHECK IS REQUEST IS ALREADY IN TABLE
                                db.collection("request").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            boolean checkIfinTable = false;

                                            for (QueryDocumentSnapshot document2 : task.getResult()) {


//                                            System.out.println("VALUES: " +
//                                                    "\ntripid " + tripId +
//                                                    "\npass " + passenger);
//
//                                            System.out.println("VALUES db: " +
//                                                    "\ntripid " + document2.getString("tripId") +
//                                                    "\npass " + document2.getString("passenger"));



                                                if (document2.getString("tripId").equals(tripId)) {
                                                    System.out.println("request already in table");

                                                    if (document2.getString("passenger").equals(passenger)) {

                                                        //SET BOOLEAN TO TRUE WHEN FOUND
                                                        System.out.println("REQUEST FOUND IN TABLE");
                                                        checkIfinTable = true;
                                                        break;
                                                    }
                                                }

                                            }

//                                        System.out.println("BOOLEAN: " + checkIfinTable);


                                            //CHECK BOOLEAN WHETHER QUEST IS FOUND OR NOT
                                            if (checkIfinTable) {

                                                //show dialog
                                                AlertDialog alertDialog = new AlertDialog.Builder(r.getContext()).create();
                                                alertDialog.setTitle("Problem Requesting Trip");
                                                alertDialog.setMessage("You already made a request for this trip.");
                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                alertDialog.show();


                                                System.out.println("nothing happens");

                                            } else {

                                                //ADD REQUEST TO DB
                                                Request request = new Request(tripId, passenger, driver, status);
                                                db.collection("request").document().set(request);
                                                Log.e("", "Added to request table.");


                                                //show dialog
                                                AlertDialog alertDialog = new AlertDialog.Builder(r.getContext()).create();
                                                alertDialog.setTitle("Your Request Has Been Made");
                                                alertDialog.setMessage("Request has been added to your trips.");
                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                alertDialog.show();


                                                //Convert seat string and decrease
                                                String str = seat;
                                                int result = Integer.parseInt(str);
                                                result = result - 1;
                                                String numberAsString = Integer.toString(result);


                                                //UPDATE SEATS IN DB
                                                DocumentReference washingtonRef = db.collection("trip").document(tripId);

                                                washingtonRef.update("seats", numberAsString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                            }


                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            }
                            break;
                        }else{
                            Log.d(TAG, "Can't find the trip in the database: ", task.getException());
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            } }); }

    //method for getting profile information
    public void getProfileInformation() {

        final String userEmail = SideBarActivity.email;

        db.collection("person").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(document.getString("email").equals(userEmail)){

//

                            p.setValues(userEmail, document.getString("name"), "0677773842");
                            break;
                        }
                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    public Context context;



    public void addTripRequestFromSearch(final String driver, final String passenger, final String status,
                               final String departure, final String destination, final String date){

        db.collection("trip").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    System.out.println("THE VALUES: "
                            + "\ndriver: "+ driver
                            + "\npassenger: " + passenger
                            + "\ndeparture: " + departure
                            + "\ndestination: " + destination
                            + "\ndate: " + date
                            + "\nstatus: " + status);



                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final String tripId;


                        System.out.println("THE VALUES DATABASE: "
                                + "\ndriver: "+ document.getString("author")
                                + "\ndeparture: "+ document.getString("departure")
                                + "\ndestination: "+ document.getString("destination")
                                + "\ndate: "+ document.getString("date"));


                        if(document.getString("author").equals(driver) &&
                                document.getString("destination").equals(destination) &&
                                document.getString("departure").equals(departure) &&
                                document.getString("date").equals(date)){


                            if(document.getString("author").equals(passenger)) {
                                Log.e("", "You can't request a trip where you are the driver.");

                                //show dialog
                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                alertDialog.setTitle("Problem Requesting Trip");
                                alertDialog.setMessage("You can't request a trip where you are the driver.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                break;


                            }else {
                                tripId = document.getId();


                                //CHECK IS REQUEST IS ALREADY IN TABLE
                                db.collection("request").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            boolean checkIfinTable = false;

                                            for (QueryDocumentSnapshot document2 : task.getResult()) {

//                                            System.out.println("VALUES: " +
//                                                    "\ntripid " + tripId +
//                                                    "\npass " + passenger);
//
//                                            System.out.println("VALUES db: " +
//                                                    "\ntripid " + document2.getString("tripId") +
//                                                    "\npass " + document2.getString("passenger"));


//                                             &&
//                                                    document2.getString("passenger").equals(passenger))

                                                if (document2.getString("tripId").equals(tripId)) {
                                                    System.out.println("request already in table");

                                                    if (document2.getString("passenger").equals(passenger)) {

                                                        //SET BOOLEAN TO TRUE WHEN FOUND
                                                        System.out.println("REQUEST FOUND IN TABLE");
                                                        checkIfinTable = true;
                                                        break;
                                                    }
                                                }

                                            }

//                                        System.out.println("BOOLEAN: " + checkIfinTable);


                                            //CHECK BOOLEAN WHETHER QUEST IS FOUND OR NOT
                                            if (checkIfinTable) {

                                                //show dialog
                                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                                alertDialog.setTitle("Problem Requesting Trip");
                                                alertDialog.setMessage("You already made a request for this trip.");
                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                alertDialog.show();


                                                System.out.println("nothing happens");

                                            } else {

                                                //ADD REQUEST TO DB
                                                Request request = new Request(tripId, passenger, driver, status);
                                                db.collection("request").document().set(request);
                                                Log.e("", "Added to request table.");


                                                //show dialog
                                                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                                                alertDialog.setTitle("Your Request Has Been Made");
                                                alertDialog.setMessage("Request has been added to your trips.");
                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                alertDialog.show();
                                            }


                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            }
                            break;
                        }else{
                            Log.d(TAG, "Can't find the trip in the database: ", task.getException());
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            } }); }


            public void addTheNumbers() {



                /*BufferedReader reader;

                try {
                    final InputStream file = context.getAssets().open("personnumer.csv");
                    reader = new BufferedReader(new InputStreamReader(file));
                    String line = reader.readLine();
                    while (line != null) {
                        personnumer n=new personnumer(line, "Martin", "Zannato", "Östra Boulevarden 34", "291 31", "Kristianstad", "false","real.martin.zannato@gmail.com");
                        db.collection("personalinfo").document(line).set(n).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        line = reader.readLine();
                    }
                } catch (Exception e) {
                    System.out.println("please die"+e);
                    Log.e("fart", e.getMessage());
                }*/


            }




}

