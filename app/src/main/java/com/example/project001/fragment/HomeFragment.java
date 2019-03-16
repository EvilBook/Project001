package com.example.project001.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.project001.PlanTrip;
import com.example.project001.R;
import com.example.project001.RidersActivity;
import com.example.project001.SideBarActivity;
import com.example.project001.database.DBConnection;
import com.example.project001.database.Trip;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class
HomeFragment extends Fragment {

    //Tabs
    LinearLayout linearLayout;
    TabHost frameLayout;
    LinearLayout triliniarLayout;

    //Add trip
    String email;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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


        //Tabs
        frameLayout = getView().findViewById(R.id.tabHost);
        TabHost host = getView().findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Passenger");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Driver");
        host.addTab(spec);

        linearLayout = getView().findViewById(R.id.tab1);
        triliniarLayout = getView().findViewById(R.id.tab2);

        //load map
        planTrip();
        mainScreen();




        //add trip button
        /*button = getView().findViewById(R.id.addTrip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrip();
            }
        });*/
    }

    //Handle map fragment
    public void mainScreen() {

        //Fragment Map
        Fragment fragment=new RidersActivity();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(linearLayout.getId(), fragment, "maps");
        ft.commit();


        for(int i=0; i<linearLayout.getChildCount(); i++){

            Log.e("oneone", linearLayout.getChildAt(i).toString());

        }
    }

    public void planTrip() {


        Bundle bun = new Bundle();
        bun.putString("email", email);

        PlanTrip plan = new PlanTrip();
        plan.setArguments(bun);

        getChildFragmentManager()
                .beginTransaction()
                .replace(triliniarLayout.getId(), plan)
                .commit();

        /*Fragment fragment = new PlanTrip();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(triliniarLayout.getId(), fragment, "yo mom");
        ft.commit();
        Log.e("home", "tab2");*/
    }

}
