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

public class TripFragment extends Fragment {

    //variables
    private TripAdapter tripAdapter;
    private ListView tripView;
    String email;
    Trip trip;
    String tripId;
    ArrayList<Trip> voyages = new ArrayList<>();

    //objects
    ArrayList<Request> voyages1 = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            Log.e("tripFragment", email);
        } else {
            Log.e("doesnt", "work");
        }

        tripAdapter = new TripAdapter(getContext());

        tripAdapter.email=email;
        tripAdapter.view=getView();
        tripView = getView().findViewById(R.id.tripView);
        tripView.setAdapter(tripAdapter);


        createTrips();

        tripView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTrip(view);

                LinearLayout linearLayout = view.findViewById(R.id.tripClick);
                linearLayout.setBackgroundColor(Color.parseColor("#f93943"));

                view.setEnabled(false);
                view.setOnClickListener(null);

                return true;
            }
        });


        tripView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createRequest(view);
                expand(((RelativeLayout)(view)).getChildAt(0));

            }
        });





    }

    public void createTrips() {

        voyages.clear();
        db.collection("trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("author").equals(email)) {

                                    trip = new Trip(
                                            document.get("date").toString(),
                                            document.get("time").toString(),
                                            document.get("departure").toString(),
                                            document.get("destination").toString(),
                                            document.get("price").toString(),
                                            document.get("seats").toString(),
                                            document.getString("author"));
                                    trip.tripId = document.getId();
                                    tripAdapter.buffer.add(document.getId());
                                    voyages.add(trip);
                                }
                            }

                            for (Trip T : voyages) {
                                tripAdapter.add(T);
                                // scroll the ListView to the last added element
                                tripView.setSelection(tripView.getCount() - 1);
                            }



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void deleteTrip(View view) {

        tripId = ((TextView)((((LinearLayout)((((LinearLayout)(((RelativeLayout)(view)).getChildAt(1))).getChildAt(0)))).getChildAt(2)))).getText().toString();

        db.collection("trip").document(tripId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //startChat();
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




    private float x1,x2;
    static final int MIN_DISTANCE = 12;
    static final int MIN_DISTANCE1 = -12;


    //variables
    private RequestAdapter requestAdapter;
    private ListView requestView;
    Request request;
    View view;




    public void createRequest(View view){



        tripId=((TextView)((((LinearLayout)((((LinearLayout)(((RelativeLayout)(view)).getChildAt(1))).getChildAt(0)))).getChildAt(2)))).getText().toString();
        Log.e("2", tripId);



        requestAdapter = new RequestAdapter(view.getContext());
        requestView = view.findViewById(R.id.requestWindow);
        requestView.setAdapter(requestAdapter);


        requestView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        requestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        LinearLayout linearLayout=v.findViewById(R.id.moveThis);
                        String id=((TextView)(((LinearLayout)(linearLayout.getChildAt(0))).getChildAt(0))).getText().toString();
                        String driverName=((TextView)(((LinearLayout)(linearLayout.getChildAt(0))).getChildAt(1))).getText().toString();
                        String passengerName=((TextView)(((LinearLayout)(linearLayout.getChildAt(0))).getChildAt(2))).getText().toString();
                        switch(event.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                x1 = event.getX();
                                break;
                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                float deltaX = x2 - x1;
                                Log.e("deltaX", String.valueOf(deltaX));
                                if (deltaX > MIN_DISTANCE)
                                {
                                    Toast.makeText(getContext(), "left2right swipe", Toast.LENGTH_SHORT).show ();
                                    linearLayout.setBackgroundColor(Color.parseColor("#f93943"));
                                    deleteRequest(id);
                                }
                                if (deltaX < MIN_DISTANCE1)
                                {
                                    Toast.makeText(getContext(), "right2left swipe", Toast.LENGTH_SHORT).show ();
                                    linearLayout.setBackgroundColor(Color.parseColor("#85ff9e"));
                                    acceptTrip(driverName+passengerName+"", driverName, passengerName);
                                }
                                break;
                        }
                        return true;
                    }
                });
            }
        });



        requestView.setBackgroundResource(R.drawable.customshape);




        voyages1.clear();
        db.collection("request")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //for (int i = 0; i < buffer.size(); i++) {


                                //tripId = buffer.get(i);//Log.e("the email:", email);
                                Log.e("maika ti deiba", document.get("tripId")+" "+tripId);
                                if (document.getString("tripId").equals(tripId)) {
                                    Log.e("maika ti deiba1", document.get("tripId")+" "+tripId);

                                    request = new Request(document.getString("tripId"),
                                            document.getString("passenger"),
                                            document.getString("driver"),
                                            document.getString("soolean"));
                                    request.requestId = document.getId();
                                    voyages1.add(request);
                                }
                            }

                            for (Request T : voyages1) {
                                requestAdapter.add(T);
                                // scroll the ListView to the last added element
                                requestView.setSelection(requestView.getCount() - 1);
                            }
                            // }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }





    public void deleteRequest(String id){


        db.collection("request").document(id)
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


    public void acceptTrip(String id, String driverName, String passengerName){


        Map<String, Object> chat = new HashMap<>();

        chat.put("id", id);
        chat.put("driverName", driverName);
        chat.put("passengerName", passengerName);

        // Add a new document with a generated ID
        db.collection("chat").document(id)
                .set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }




    public static void expand(final View v) {
        if(v.getLayoutParams().height>200){
            collapse(v);
            return;
        }
        v.measure(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        //final int targetHeight = v.getMeasuredHeight();
        final int targetHeight = 240;

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 0.2
                        ? AbsListView.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }






}
