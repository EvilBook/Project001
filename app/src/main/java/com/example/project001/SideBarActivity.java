package com.example.project001;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class SideBarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variables
    LinearLayout profile;
    String displayName;
    String Email;
    EditText destination;
    EditText departure;
    EditText date;
    EditText price;
    EditText availableSeats;
    EditText freeSeats;
    TextView textView;
    GoogleSignInClient googleApiClient;

    //Database
    Trip trip;
    DBConnection dbc = new DBConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sidebar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = GoogleSignIn.getClient(this, gso);




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //Tabs
        TabHost host = findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

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

        Intent intent1 = getIntent();

        displayName = intent1.getStringExtra("Display");
        Email = intent1.getStringExtra("Email");

        TextView DisplayNameArea = findViewById(R.id.DisplayName);

        DisplayNameArea.setText(displayName);

        Log.i("DisplayName",displayName);
        Log.i("Email",Email);

        dbc.checkIfExists(Email, displayName);




        profile = findViewById(R.id.profile);


        profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                System.out.println("PROFILE CLICKED");
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            System.out.println("info");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        System.out.println("id: "+id);

        if (id == R.id.nav_messages) {
            System.out.println("messages");
            startActivity(new Intent(SideBarActivity.this, com.example.project001.message.MainActivity.class));

        } else if (id == R.id.nav_trips) {
            System.out.println("trips");

        } else if (id == R.id.nav_settings) {
            System.out.println("settings");

        } else if (id == R.id.nav_logout) {

               signOut();

            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
            System.out.println("logout");

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addTrip(View view) {
        destination = findViewById(R.id.destination);
        departure = findViewById(R.id.departure);
        date = findViewById(R.id.date);
        price = findViewById(R.id.price);
        availableSeats = findViewById(R.id.availableSeats);
        freeSeats = findViewById(R.id.freeSeats);
        textView = findViewById(R.id.textView);

        trip = new Trip(destination.getText().toString(),
                        departure.getText().toString(),
                        date.getText().toString(),
                        price.getText().toString(),
                        availableSeats.getText().toString(),
                        freeSeats.getText().toString(),
                        Email);
        Log.e("it contains: ", destination.getText().toString());
        Log.e("it contains: ", departure.getText().toString());

        if(destination.getText().toString().isEmpty() ||
                departure.getText().toString().isEmpty() ||
                date.getText().toString().isEmpty() ||
                price.getText().toString().isEmpty() ||
                availableSeats.getText().toString().isEmpty() ||
                freeSeats.getText().toString().isEmpty()) {

                textView.setText("*Please fill in all the fields.");
        } else {
            dbc.addTripToDB(trip);
        }



        destination.setText("");
        departure.setText("");
        price.setText("");
        availableSeats.setText("");
        date.setText("");
        freeSeats.setText("");
    }

    public void signOut() {
        googleApiClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    
}
