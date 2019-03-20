package com.example.project001.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project001.R;
import com.example.project001.database.Request;
import com.example.project001.database.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.wearable.DataMap.TAG;

public class RequestTab extends Fragment {

    //variables
    private TripAdapter requestAdapter;
    private ListView requestListView;
    private String email;
    private String requestId;
    private RequestAdapter reqvestAdapter;
    private Request request;
    private View view;
    private float x1,x2;
    static final int MIN_DISTANCE = 12;
    static final int MIN_DISTANCE1 = -12;

    //objects
    ArrayList<Request> requests = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            Log.e("tripFragment", email);
        } else {
            Log.e("doesn't", "work");
        }

        requestAdapter = new TripAdapter(getContext());
        requestAdapter.email = email;
        requestAdapter.view = getView();
        requestListView = getView().findViewById(R.id.requestListView);
        requestListView.setAdapter(requestAdapter);


        displayRequest();


        requestListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteRequest(view);

                LinearLayout linearLayout = view.findViewById(R.id.moveThis);
                linearLayout.setBackgroundColor(Color.parseColor("#f93943"));

                view.setEnabled(false);
                view.setOnClickListener(null);

                return true;
            }
        });

    }

    public void displayRequest() {

        requests.clear();
        db.collection("request")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("dsplaying", document.getString("passenger") + " " + email + " " + document.getString("soolean"));
                                if(document.getString("passenger").equals(email) && document.getString("soolean").equals("0")) {

                                    request = new Request(
                                            document.getString("tripId"),
                                            document.getString("passenger"),
                                            document.getString("driver"),
                                            document.getString("soolean")

                                    );
                                    Log.e("soolean", "0");
                                    request.setColour("#ffffff");
                                    request.requestId = document.getId();
                                    requests.add(request);
                                }
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("passenger").equals(email) && document.getString("soolean").equals("1")) {

                                    request = new Request(
                                            document.getString("tripId"),
                                            document.getString("passenger"),
                                            document.getString("driver"),
                                            document.getString("soolean"));

                                    Log.e("soolean", "1");
                                    request.setColour("#7CB1E2");
                                    request.requestId = document.getId();
                                    requests.add(request);
                                }
                            }

                            reqvestAdapter = new RequestAdapter(getContext());

                            requestListView.setAdapter(reqvestAdapter);

                            for (Request T : requests) {
                                reqvestAdapter.add(T);
                                // scroll the ListView to the last added element
                                requestListView.setSelection(requestListView.getCount() - 1);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    public void deleteRequest(View view) {

        //LinearLayout linearLayout = view.findViewById(R.id.tripClick);
        requestId = ((TextView)((((LinearLayout)((((LinearLayout)(((RelativeLayout)(view)).getChildAt(0))).getChildAt(0)))).getChildAt(0)))).getText().toString();

        db.collection("request").document(requestId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }


}
