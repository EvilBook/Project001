package com.example.project001;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class HomePageActivity extends AppCompatActivity {

    TabHost tabHost;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        TabHost host = findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.chats);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.users);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        Intent startIntent = new Intent(getApplicationContext(), SideBarActivity.class);
        startActivity(startIntent);

    }
}
