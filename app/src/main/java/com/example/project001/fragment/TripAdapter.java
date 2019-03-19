package com.example.project001.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project001.R;
import com.example.project001.database.Request;
import com.example.project001.database.Trip;
import com.example.project001.message.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static com.google.android.gms.wearable.DataMap.TAG;

public class TripAdapter extends BaseAdapter {


    //variables
    private RequestAdapter requestAdapter;
    private ListView requestView;
    String email;
    Request request;
    View view;


    String tripId;


    //objects
    ArrayList<Request> voyages = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    List<Trip> trips = new ArrayList<Trip>();
    Context context;


    TripViewHolder holder = new TripViewHolder();

    private float x1,x2;
    static final int MIN_DISTANCE = 12;
    static final int MIN_DISTANCE1 = -12;


    int time=0;

    ArrayList<String> buffer=new ArrayList<>();
    ArrayList<View> buffer1=new ArrayList<>();




    public TripAdapter(Context context) {
        this.context = context;
    }


    public void add(Trip trip) {
        this.trips.add(trip);
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return trips.size();
    }

    @Override
    public Object getItem(int position) {
        return trips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        LayoutInflater tripInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Trip treps = trips.get(position);

        convertView = tripInflater.inflate(R.layout.activity_trip, null);
        holder.departure = convertView.findViewById(R.id.departure);
        holder.destination = convertView.findViewById(R.id.destination);
        holder.author = convertView.findViewById(R.id.author);
        holder.requestsWindow = convertView.findViewById(R.id.requestWindow);
        convertView.setTag(holder);

        holder.departure.setText(treps.getDeparture());
        holder.destination.setText(treps.getDestination());
        holder.author.setText(treps.tripId);






        return convertView;
    }





}

class TripViewHolder {

    TextView departure;
    TextView destination;
    TextView author;
    ListView requestsWindow;


}
