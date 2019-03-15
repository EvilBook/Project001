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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RidersActivity extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;



    DBConnection db = new DBConnection();


    ArrayList<Trip> t = new ArrayList<>();
    Context con;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (inflater.inflate(R.layout.activity_riders, container, false));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        //Database and Context
        con = getContext();
        db.r = this;
        db.getTripsforMap();





        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                Log.e("map ready", "s");

                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

//                        updateMap(location);


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
                    if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lastKnownLocation != null){
                            updateMap(lastKnownLocation);

                        }

                    }
                }
            }
        });




        final Button planTrip = getView().findViewById(R.id.PlanYourTrip);
        final TextView toLocation = getView().findViewById(R.id.ToLocation);


        toLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("u clicked ","");

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

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateMap(lastKnownLocation);
                }else{

                }

            }else{
            }

        }else{
        }
    }


    public void updateMap(Location location) {

        Marker marker;
        MarkerOptions markerOptions=new MarkerOptions();


        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions.position(userLocation).title("Blyat");

        InfoWindowData info = new InfoWindowData();
        info.setHotel("Hotel : excellent hotels available");
        info.setFood("Food : all types of restaurants available");
        info.setTransport("Reach the site by bus, car and train.");


        MarkerExtraInfo customInfoWindow = new MarkerExtraInfo(getContext());



        mMap.clear();
        mMap.setInfoWindowAdapter(customInfoWindow);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
        marker=mMap.addMarker(markerOptions);
        marker.setTag(info);
        //marker.showInfoWindow();
        TextView yourLocation =  getView().findViewById(R.id.YourLocation);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);


        yourLocation.setText(cityName);
    }



    //Get Array List from database and add markers
    public void getArrayList(ArrayList<Trip> list) {
        //copy array list
        t = list;

        Geocoder geocoder = new Geocoder(con);
        Log.e("CONTEXT: ", "" + con);


        //For each trip
        for (int i = 0; i < t.size(); i++) {

            //Search for location
            String s = t.get(i).getDeparture();
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
                //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();


                //Add marker


                Marker marker;
                MarkerOptions markerOptions;

                MarkerExtraInfo customInfoWindow = new MarkerExtraInfo(getContext());


                LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());

                InfoWindowData info = new InfoWindowData();
                info.setHotel("PLEIS : "+searchString);
                info.setFood("Food : all types of restaurants available");
                info.setTransport("Reach the site by bus, car and train.");
                markerOptions= new MarkerOptions()
                        .position(pos)
                        .title(searchString)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                marker=mMap.addMarker(markerOptions);
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
