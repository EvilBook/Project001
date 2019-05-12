package com.example.project001;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private Button searchForShit;
    private ConstraintLayout constraint1;


    String email;

    //User Location
    Location loc;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.activity_riders, container, false));
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getArguments() != null) {
            email = getArguments().getString("email");
            Log.e("homeFragment", email);
        } else {
            Log.e("doesn't work", "");
        }

        //Database and Context
        con = getContext();
        db.r = this;
        db.getTripsforMap();

        constraint1 = getView().findViewById(R.id.constraint1);

        //create map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            //ON MAP READY
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


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

                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                } else {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



                        //Find current location of user
                        locationManager = (LocationManager)con.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        List<String> providers = locationManager.getProviders(true);
                        Location bestLocation = new Location("");
                        for (String provider : providers) {
                            Location l = locationManager.getLastKnownLocation(provider);
                            if (l == null) {
                                continue;
                            }
                            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                                // Found best last known location: %s", l);
                                bestLocation = l;
                            }
                        }


                        Log.e("YOUR LOCATION:", " " + bestLocation);

                        loc = bestLocation;

                        //SET MARKER TO CURRENT LOCATION
                        updateMapCurrentLocation(bestLocation);

                    }
                }


                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

                        String em = SideBarActivity.email;

                        Bundle bun = new Bundle();
                        bun.putString("email", em);

//                        System.out.println("THE VALUES RIDERS ACTIVITY: ");
//                        System.out.println("Author: " + infoWindowData.getAuthor() + ", passenger: " + em + ", departure: " + infoWindowData.getDeparture()
//                        + ", destination: " + infoWindowData.getDestination() + ", date: " + infoWindowData.getDate());

                        db.addTripRequest(infoWindowData.getAuthor(), em, "0",
                                infoWindowData.getDeparture(), infoWindowData.getDestination(), infoWindowData.getDate());
                    }
                });

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        Log.d("Map","Map clicked");

                        if(constraint1.getVisibility() == View.VISIBLE) {
                            constraint1.setVisibility(View.INVISIBLE);
                        }else{
                            constraint1.setVisibility(View.VISIBLE);

                        }
                    }
                });

            }
        });


        //TextView
        toLocation = getView().findViewById(R.id.ToLocation);
        fromLocation = getView().findViewById(R.id.YourLocation);

        searchForShit = getView().findViewById(R.id.button);


        searchForShit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                intent.putExtra("departure", fromLocation.getText().toString());
                intent.putExtra("destination", toLocation.getText().toString());
                intent.putExtra("email", email);


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

        if(location.getLatitude() < 1 && location.getLongitude() > -1) {
            location.setLatitude(56.031200);
            location.setLongitude(14.154950);
        }

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

//        Log.e("loc: ", userLocation.toString());


        mMap.clear();

        //move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 11));

        //add marker for current location
        MarkerOptions markerOptions = new MarkerOptions().position(userLocation)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location));
        mMap.addMarker(markerOptions);

    }



    //Get Array List from database and add markers
    public void getArrayList(ArrayList<Trip> list) {

        Log.e("map", " " + mMap);


        MarkerExtraInfo customInfoWindow = new MarkerExtraInfo(getContext());
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
                    String searchString = s.substring(0, 1).toUpperCase() + s.substring(1);
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


                        Location target = new Location("target");
                        target.setLongitude(address.getLongitude());
                        target.setLatitude(address.getLatitude());


                        //Compare user location to markers
                        if(loc.distanceTo(target) <  300000) {

                            int[] pics = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
                                    R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
                                    R.drawable.pic7, R.drawable.pic8, R.drawable.pic9,
                                    R.drawable.pic10, R.drawable.pic11,};

                            Random r = new Random();
                            int randomNumber = r.nextInt(pics.length);


                            //Markers
                            Marker marker;
                            MarkerOptions markerOptions;
                            LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());

                            //Set Marker + Window Info
                            InfoWindowData info = new InfoWindowData();
                            info.setDeparture(t.get(i).getDeparture());
                            info.setDestination(t.get(i).getDestination());
                            info.setAuthor(t.get(i).getAuthor());
                            info.setDate(t.get(i).getDate());
                            info.setPrice(t.get(i).getPrice());
                            info.setAvailableSeats(t.get(i).getSeats());

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
}
