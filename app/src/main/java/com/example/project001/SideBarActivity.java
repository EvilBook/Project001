package com.example.project001;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.example.project001.fragment.HomeFragment;
import com.example.project001.fragment.ProfileFragment;
import com.example.project001.fragment.SettingsFragment;
import com.example.project001.fragment.TripFragment;
import com.example.project001.fragment.mapsFragment;
import com.example.project001.fragment.tripsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SideBarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, mapsFragment.OnFragmentInteractionListener, tripsFragment.OnFragmentInteractionListener {

    //variables
    Button nav_messages, nav_trips, nav_settings, nav_logout;
    LinearLayout profile;
    String displayName;
    String email;
    GoogleSignInClient googleApiClient;
    DrawerLayout drawerLayout;

    //database
    DBConnection dbc = new DBConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sidebar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = GoogleSignIn.getClient(this, gso);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent1 = getIntent();

        displayName = intent1.getStringExtra("Display");
        email = intent1.getStringExtra("Email");


        //checks if the profile exists in the database
        dbc.checkIfExists(email, displayName);
        dbc.getTrip();


        Bundle bun = new Bundle();
        bun.putString("email", email);

        HomeFragment hom = new HomeFragment();
        hom.setArguments(bun);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragment, hom)
                .commit();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        TextView DisplayNameArea = findViewById(R.id.DisplayName);
        DisplayNameArea.setText(displayName);

        Log.i("DisplayName", displayName);
        Log.i("Email", email);




        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, new ProfileFragment()).commit();

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_info) {
            System.out.println("info");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            startActivity(new Intent(SideBarActivity.this, com.example.project001.message.MainActivity.class));

        } else if (id == R.id.nav_trips) {
            fragment = new TripFragment();

        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();

        } else if (id == R.id.nav_logout) {
               signOut();

        } else if(id == R.id.nav_home){
            fragment = new HomeFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment, fragment).commit();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signOut() {
        googleApiClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
