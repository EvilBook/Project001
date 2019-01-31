package com.example.project001;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class CreateActivity extends Activity {

    //variables
    int popWidth;
    int popHeight;


    //objects
    DisplayMetrics dp = new DisplayMetrics();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create);

        getWindowManager().getDefaultDisplay().getMetrics(dp);

        popWidth = dp.widthPixels;
        popHeight = dp.heightPixels;

        getWindow().setLayout((int)(popWidth * 0.9), (int) (popHeight * 0.9));



    }

}
