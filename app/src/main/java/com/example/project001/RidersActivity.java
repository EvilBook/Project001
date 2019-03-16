package com.example.project001;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.activity_riders, container, false));
    }

    //ON CREATE
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            }
        });


        //Button, TextView
        final Button planTrip = getView().findViewById(R.id.PlanYourTrip);
        final TextView toLocation = getView().findViewById(R.id.ToLocation);


        toLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("u clicked ", "");
            }
        });

        planTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), PlanTrip.class);
                RidersActivity.this.startActivity(myIntent);
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


    //Get Array List from database and add markers
    public void getArrayList(ArrayList<Trip> list) {

        MarkerExtraInfo customInfoWindow = new MarkerExtraInfo(getContext());
        mMap.setInfoWindowAdapter(customInfoWindow);

        //copy array list
        t = list;
        Log.e("log", String.valueOf(list.size()));

        Geocoder geocoder = new Geocoder(con);
        Log.e("CONTEXT: ", "" + con);


        //For each trip
        for (int i = 0; i < t.size(); i++) {


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

            if (list.size() > 0) {
                Address address = l.get(0);

                Log.d("", "geoLocate: found a location: " + address.toString());


                //Markers
                Marker marker;
                MarkerOptions markerOptions;
                LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());

                //Set Marker + Window Info
                InfoWindowData info = new InfoWindowData();
                info.setDeparture("Departure: " + t.get(i).departure);
                info.setDestination("Destination: " + t.get(i).destination);
                info.setAuthor(t.get(i).author);
                info.setDate("Date: " + t.get(i).date);
                info.setPrice(t.get(i).price + " Kr");
                info.setAvailableSeats("Total Seats: " + t.get(i).seats);

                markerOptions= new MarkerOptions()
                        .position(pos)
                        .title(searchString)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

                //Add Marker
                marker = mMap.addMarker(markerOptions);

                //Add Window
                marker.setTag(info);
                marker.showInfoWindow();
            }
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //DON'T ADD ANYTHING HERE, IT DOESN'T WORK.
    }
}
