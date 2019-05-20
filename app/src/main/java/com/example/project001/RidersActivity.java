package com.example.project001;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.example.project001.fragment.ProfileFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RidersActivity extends Fragment implements OnMapReadyCallback {


    TextView x;

    //Variables
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    //Database and Context
    DBConnection db = new DBConnection();
    ArrayList<Trip> t = new ArrayList<>();
    Context con;



    public static TextView fromLocation;
    public static TextView toLocation;

    Button searchForShit;


    String email;


    InfoWindowData info;



    FirebaseFirestore db1 = FirebaseFirestore.getInstance();






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.activity_riders, container, false));
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(getArguments() != null){
            email = getArguments().getString("email");
            Log.e("homeFragment", email);
        }else{
            Log.e("doesn't work", "");
        }


        //Database and Context
        con = getContext();
        db.r = this;
        db.getTripsforMap();



        //create map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            //ON MAP READY
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                Log.e("map is ready", "");

                //Location
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                //Location
                if (Build.VERSION.SDK_INT < 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                } else {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lastKnownLocation != null) {

                            //SET MARKER TO CURRENT LOCATION
                            updateMapCurrentLocation(lastKnownLocation);

                        }

                    }
                }

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(final Marker marker) {
                        final InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();



                        db.db.collection("userBio").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                              @Override
                                                                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                  if (task.isSuccessful()) {

                                                                                      for (final QueryDocumentSnapshot document : task.getResult()) {


                                                                                          if(document.getId().equals(infoWindowData.getAuthor())) {


                                                                                              db.db.collection("person").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                  @Override
                                                                                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                      if (task.isSuccessful()) {

                                                                                                          for (QueryDocumentSnapshot document1 : task.getResult()) {


                                                                                                              if (document1.getString("email").equals(infoWindowData.getAuthor())) {


                                                                                                                  //popup window info
                                                                                                                  LayoutInflater inflater = (LayoutInflater) RidersActivity.this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                                                                                                                  View layout = inflater.inflate(R.layout.pup_up_detailed_marker, null);


                                                                                                                  Button request = layout.findViewById(R.id.requestButton);
                                                                                                                  Button close = layout.findViewById(R.id.closeButton);
                                                                                                                  TextView name = layout.findViewById(R.id.name);
                                                                                                                  TextView address = layout.findViewById(R.id.address);
                                                                                                                  TextView email = layout.findViewById(R.id.email);
                                                                                                                  TextView phone = layout.findViewById(R.id.phone);
                                                                                                                  TextView bio = layout.findViewById(R.id.bio);
                                                                                                                  ImageView profilePic = layout.findViewById(R.id.imageView);
                                                                                                                  ImageView carPic1 = layout.findViewById(R.id.imageView3);
                                                                                                                  ImageView carPic2 = layout.findViewById(R.id.imageView4);
                                                                                                                  ImageView carPic3 = layout.findViewById(R.id.imageView5);


                                                                                                                  LinearLayout linearLayout = layout.findViewById(R.id.verifiedContainer);

                                                                                                                  final String em = SideBarActivity.email;


                                                                                                                  float density = RidersActivity.this.getResources().getDisplayMetrics().density;


                                                                                                                  Display display = getActivity().getWindowManager().getDefaultDisplay();
                                                                                                                  Point size = new Point();
                                                                                                                  display.getSize(size);
                                                                                                                  int width = (int) (size.x * 0.8);
                                                                                                                  int height = (int) (size.y * 0.6);

                                                                                                                  final PopupWindow pw = new PopupWindow(layout, (int) width, (int) height, true);

                                                                                                                  //handle touch outside popup window
                                                                                                                  pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                                                                  pw.setTouchInterceptor(new View.OnTouchListener() {
                                                                                                                      public boolean onTouch(View v, MotionEvent event) {
                                                                                                                          if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                                                                                                              pw.dismiss();
                                                                                                                              return true;
                                                                                                                          }
                                                                                                                          return false;
                                                                                                                      }
                                                                                                                  });
                                                                                                                  pw.setOutsideTouchable(true);

                                                                                                                  //start pop up window
                                                                                                                  pw.showAtLocation(layout, Gravity.CENTER, 0, -100);

                                                                                                                  dimBehind(pw);


                                                                                                                  request.setOnClickListener(new View.OnClickListener() {
                                                                                                                      @Override
                                                                                                                      public void onClick(View v) {

                                                                                                                          Bundle bun = new Bundle();
                                                                                                                          bun.putString("email", em);

//                        System.out.println("THE VALUES RIDERS ACTIVITY: ");
//                        System.out.println("Author: " + infoWindowData.getAuthor() + ", passenger: " + em + ", departure: " + infoWindowData.getDeparture()
//                        + ", destination: " + infoWindowData.getDestination() + ", date: " + infoWindowData.getDate());

                                                                                                                          db.addTripRequest(infoWindowData.getAuthor(), em, "0",
                                                                                                                                  infoWindowData.getDeparture(), infoWindowData.getDestination(), infoWindowData.getDate());

                                                                                                                      }
                                                                                                                  });


                                                                                                                  close.setOnClickListener(new View.OnClickListener() {
                                                                                                                      @Override
                                                                                                                      public void onClick(View v) {
                                                                                                                          pw.dismiss();
                                                                                                                      }
                                                                                                                  });


                                                                                                                  name.setText(document1.getString("name"));
                                                                                                                  address.setText("From: "+infoWindowData.getDeparture());
                                                                                                                  phone.setText("Destination: "+infoWindowData.getDestination());
                                                                                                                  bio.setText("Personal Bio: \n"+document.getString("info"));
                                                                                                                  email.setText("Date: "+infoWindowData.getDate());


                                                                                                                  if (!infoWindowData.getVerified().equals("true")) {

                                                                                                                      linearLayout.setVisibility(View.INVISIBLE);

                                                                                                                  }
                                                                                                              }


                                                                                                          }


                                                                                                      }


                                                                                                  }


                                                                                              });
                                                                                          }
                                                                                      }
                                                                                          }


                                                                                      }


                                                                                  });



                    }
                });
            }
        });




        //TextView
        toLocation = getView().findViewById(R.id.ToLocation);
        fromLocation = getView().findViewById(R.id.YourLocation);

        searchForShit=getView().findViewById(R.id.button);


        searchForShit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                intent.putExtra("departure",fromLocation.getText().toString());
                intent.putExtra("destination",toLocation.getText().toString());
                intent.putExtra("email",email);


                startActivity(intent);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //Add marker Current Location & Move camera
    public void updateMapCurrentLocation(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.clear();

        //move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 11));

        //add marker for current location
        MarkerOptions markerOptions = new MarkerOptions().position(userLocation)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        mMap.addMarker(markerOptions);

    }


    MarkerExtraInfo customInfoWindow;
    Marker marker;
    MarkerOptions markerOptions;




    //Get Array List from database and add markers
    public void getArrayList(ArrayList<Trip> list) {

        customInfoWindow = new MarkerExtraInfo(getContext());
        mMap.setInfoWindowAdapter(customInfoWindow);

        //copy array list
        t = list;
        Log.e("log", String.valueOf(list.size()));

        Geocoder geocoder = new Geocoder(con);
        Log.e("CONTEXT: ", "" + con);


        //For each trip
        if(t.size() > 0) {
            for (int i = 0; i < t.size(); i++) {

                if(t.get(i).getSeats().equals("0")) {

                }else{
                    //Search for location
                    String s = t.get(i).departure;
                    final String searchString = s.substring(0, 1).toUpperCase() + s.substring(1);
                    System.out.println("SEARCH STRING: " + searchString);

                    List<Address> l = new ArrayList<>();

                    try {
                        l = geocoder.getFromLocationName(searchString, 1);

                    } catch (IOException e) {
                        Log.e(".", "geoLocate: IOException: " + e.getMessage());
                    }

                    if (list.size() > 0 && l.size()>0) {
                        Address address = l.get(0);

                        Log.d("", "geoLocate: found a location: " + address.toString());

                        final int[] pics = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
                                R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
                                R.drawable.pic7, R.drawable.pic8, R.drawable.pic9,
                                R.drawable.pic10, R.drawable.pic11,};

                        Random r=new Random();
                        final int randomNumber = r.nextInt(pics.length);


                        //Markers
                        final LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());

                        //Set Marker + Window Info
                        final int finalI = i;
                        db1.collection("personalinfo")
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    if (document.getString("email") != null) {
                                        if (document.getString("email").equals(t.get(finalI).getAuthor())) {
                                            info = new InfoWindowData();
                                            info.setDeparture(t.get(finalI).getDeparture());
                                            info.setDestination(t.get(finalI).getDestination());
                                            info.setAuthor(t.get(finalI).getAuthor());
                                            info.setDate(t.get(finalI).getDate());
                                            info.setPrice(t.get(finalI).getPrice());
                                            info.setAvailableSeats(t.get(finalI).getSeats());
                                            info.setVerified(document.getString("verified"));
                                            Log.e("GOD FUCKING DAMN IT", info.getAuthor() + " " + document.getString("verified") + " " + document.getString("email"));


                                            markerOptions = new MarkerOptions()
                                                    .position(pos)
                                                    .title(searchString)
                                                    .icon(BitmapDescriptorFactory.fromResource(pics[randomNumber]));

                                            //Add Marker
                                            marker = mMap.addMarker(markerOptions);

                                            //Add Window
                                            marker.setTag(info);
                                            marker.showInfoWindow();

                                        }
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }


            }
        }else{
            Log.e("Error: ", "Can't add markers to map because array is empty.");
            //show dialog
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Problem Requesting Trips");
            alertDialog.setMessage("Please Refresh The Home Page.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //DON'T ADD ANYTHING HERE, IT DOESN'T WORK.
    }


    private void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }

}
