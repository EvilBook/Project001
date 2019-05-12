package com.example.project001.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project001.R;
import com.example.project001.database.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;

public class TripTab2_InfoWindow extends AppCompatActivity implements OnMapReadyCallback {

    //variables
    private String tripId;
    private String destination;
    private String departure;
    private String date;
    private String seats;
    private String price;
    private String time;

    private TextView dest;
    private TextView dep;
    private TextView dat;
    private TextView sea;
    private TextView pric;
    private TextView tim;
    private TextView infotxt;
    private Button startButton;
    private Button backButton;
    private ConstraintLayout constr1;
    private ConstraintLayout constr2;

    private ListView requestView;
    private Request request;
    private String id;
    private RequestAdapter requestAdapter;
    private float x1,x2;
    static final int MIN_DISTANCE = 12;
    static final int MIN_DISTANCE1 = -12;
    private boolean allowSwipe = true;

    //objects
    ArrayList<Request> voyages1 = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //map
    private GoogleMap map;

    ImageView deleteImg;
    ImageView returnImg;

    boolean checkButton = true;
    boolean checkButton2 = true;
    boolean emptyRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trip2_infowindow);

        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        //Images
        deleteImg = findViewById(R.id.deleteImg);
        deleteImg.setImageResource(R.drawable.delete);
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrip();
            }
        });

        returnImg = findViewById(R.id.returnImg);
        returnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //get id from prev activity
        Intent intent1 = getIntent();
        id = intent1.getStringExtra("id");
        destination = intent1.getStringExtra("destination");
        destination = intent1.getStringExtra("destination");
        departure = intent1.getStringExtra("departure");
        time = intent1.getStringExtra("time");
        price = intent1.getStringExtra("price");
        seats = intent1.getStringExtra("seats");
        date = intent1.getStringExtra("date");


        dest = findViewById(R.id.totext);
        dep = findViewById(R.id.fromtext);
        dat = findViewById(R.id.datetext);
        pric = findViewById(R.id.pricetext);
        sea = findViewById(R.id.seattext);
//        tim = findViewById(R.id.timetext);
        infotxt = findViewById(R.id.infotxt);
        startButton = findViewById(R.id.startButton);
        backButton = findViewById(R.id.backButton);
        constr1 = findViewById(R.id.constr1);
        constr2 = findViewById(R.id.constr2);

        dest.setText("To: " + destination);
        dep.setText("From: " + departure);
        dat.setText(date);
        pric.setText(price + " Kr");
        sea.setText(seats + " Seats");

        Log.e("destination", " " + destination);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkButton2) {

                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(TripTab2_InfoWindow.this).create();
                    alertDialog.setTitle("Confirm");
                    alertDialog.setMessage("Are you sure you want to start the trip?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "START TRIP",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    constr2.setVisibility(View.INVISIBLE);
                                    requestView.setVisibility(View.INVISIBLE);
                                    infotxt.setVisibility(View.INVISIBLE);
                                    deleteImg.setVisibility(View.INVISIBLE);
                                    returnImg.setVisibility(View.VISIBLE);

                                    startButton.setText("Trip Completed");
                                    checkButton = false;
                                    checkButton2 = false;

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }else{

                    checkButton2 = true;
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkButton) {
                    finish();
                }else {

                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(TripTab2_InfoWindow.this).create();
                    alertDialog.setTitle("Confirm");
                    alertDialog.setMessage("Are you sure you want to end the trip?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "END TRIP",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(emptyRequests)
                                        infotxt.setVisibility(View.VISIBLE);
                                    constr2.setVisibility(View.VISIBLE);
                                    requestView.setVisibility(View.VISIBLE);
                                    deleteImg.setVisibility(View.VISIBLE);
                                    returnImg.setVisibility(View.INVISIBLE);

                                    startButton.setText("Start Trip");
                                    checkButton = true;

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            }
        });


        //requests
        requestView = (ListView) findViewById(R.id.requestList);
        requestAdapter = new RequestAdapter(this);
        requestView.setAdapter(requestAdapter);

        addData(id);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        addMarkers();
    }

    public void addMarkers(){
        com.google.android.gms.maps.model.LatLng userLocation = new LatLng(56.031200, 14.154950);


        map.clear();

        //move camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 7));

        //add marker for current location
        MarkerOptions markerOptions = new MarkerOptions().position(userLocation)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        map.addMarker(markerOptions);


        //Search for location
        Geocoder geocoder = new Geocoder(this);

        ArrayList<String> list = new ArrayList<>();
        list.add(departure);
        list.add(destination);


        for (int i=0; i < 2; i++) {
            String s = list.get(i);
            String searchString = s.substring(0, 1).toUpperCase() + s.substring(1);
            System.out.println("SEARCH STRING: " + searchString);

            List<Address> l = new ArrayList<>();

            try {
                l = geocoder.getFromLocationName(searchString, 1);

            } catch (IOException e) {
                Log.e(".", "geoLocate: IOException: " + e.getMessage());
            }

            if (list.size() > 0 && l.size() > 0) {
                Address address = l.get(0);

                Log.d("", "geoLocate: found a location: " + address.toString());


                Location target = new Location("target");
                target.setLongitude(address.getLongitude());
                target.setLatitude(address.getLatitude());


                //Markers
                LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions opt = new MarkerOptions()
                        .position(pos)
                        .title(searchString)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                Marker marker = map.addMarker(opt);
            }
        }

    }



    public void addData(final String id){

//        requestView = findViewById(R.id.requestList);

        tripId = id;
        Log.e("TRIPID: ","" + tripId);


        requestView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        requestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        LinearLayout linearLayout=v.findViewById(R.id.moveThis);
                        String id=((TextView)(((LinearLayout)(linearLayout.getChildAt(0))).getChildAt(0))).getText().toString();
                        String driverName=((TextView)(((LinearLayout)(linearLayout.getChildAt(0))).getChildAt(1))).getText().toString();
                        String passengerName=((TextView)(((LinearLayout)(linearLayout.getChildAt(0))).getChildAt(2))).getText().toString();

                        switch(event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                x1 = event.getX();
                                break;
                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                float deltaX = x2 - x1;
                                Log.e("deltaX", String.valueOf(deltaX));
                                if (deltaX > MIN_DISTANCE) {
                                    if(allowSwipe) {
                                        allowSwipe = false;
                                        Toast.makeText(TripTab2_InfoWindow.this, "left2right swipe", Toast.LENGTH_SHORT).show();
                                        linearLayout.setBackgroundColor(Color.parseColor("#f93943"));
                                        deleteRequest(id);
                                    }
                                }
                                if (deltaX < MIN_DISTANCE1) {
                                    if(allowSwipe) {
                                        allowSwipe = false;
                                        Toast.makeText(TripTab2_InfoWindow.this, "right2left swipe", Toast.LENGTH_SHORT).show();
                                        linearLayout.setBackgroundColor(Color.parseColor("#85ff9e"));
                                        acceptTrip(driverName + passengerName + "", driverName, passengerName);
                                        changeStatus(v);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });
            }
        });



        voyages1.clear();
        db.collection("request")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                Log.e("maika ti deiba", document.get("tripId")+" "+tripId);

                                if (document.getString("tripId").equals(tripId)) {
//                                    Log.e("maika ti deiba1", document.get("tripId")+" "+tripId);

                                    request = new Request(document.getString("tripId"),
                                            document.getString("passenger"),
                                            document.getString("driver"),
                                            document.getString("soolean"));
                                    request.requestId = document.getId();
                                    voyages1.add(request);
                                    Log.e("REQUEST: ","" + request.driver);
                                    Log.e("DB: ","" + document.getString("passenger"));
                                }
                            }

                            if(voyages1.isEmpty()){
                                emptyRequests = true;
                                infotxt.setVisibility(View.VISIBLE);
                            }

                            for (Request T : voyages1) {
                                requestAdapter.add(T);
                                requestView.setSelection(requestView.getCount() - 1);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void deleteRequest(String id){


        db.collection("request").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });


    }

    public void acceptTrip(String id, String driverName, String passengerName){


        Map<String, Object> chat = new HashMap<>();

        chat.put("id", id);
        chat.put("driverName", driverName);
        chat.put("passengerName", passengerName);

        // Add a new document with a generated ID
        db.collection("chat").document(id)
                .set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public void changeStatus(View view) {

        String requestId;

        requestId = ((TextView)((((LinearLayout)((((LinearLayout)(((RelativeLayout)(view)).getChildAt(0))).getChildAt(0)))).getChildAt(0)))).getText().toString();

        DocumentReference req = db.collection("request").document(requestId);

        req.update("soolean", "1").addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void deleteTrip() {

        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure you want to delete the trip?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("trip").document(tripId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}