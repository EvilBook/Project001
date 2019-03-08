package com.example.project001.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.project001.R;
import com.example.project001.database.Trip;
import com.example.project001.message.MessageAdapter;

public class TripFragment extends Fragment {

    private TripAdapter tripAdapter;
    private ListView tripView;

    //variables
    Trip[] trips = new Trip[]{
            new Trip("da", "da", "man", "tan", "son", "luck", "fuck"),
            new Trip("lad", "das", "nan", "dan", "don", "duck", "duck"),
            new Trip("faa", "dadaa", "sock", "dan", "don", "duck", "duck")
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripAdapter = new TripAdapter(getContext());
        tripView = getView().findViewById(R.id.tripView);
        tripView.setAdapter(tripAdapter);


        for (Trip T : trips) {
            tripAdapter.add(T);
            // scroll the ListView to the last added element
            tripView.setSelection(tripView.getCount() - 1);
        }



    }

}
