package com.example.project001.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.project001.R;
import com.example.project001.database.Trip;
import com.example.project001.message.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class TripAdapter extends BaseAdapter {






    List<Trip> trips = new ArrayList<Trip>();
    Context context;

    public TripAdapter(Context context) {
        this.context = context;
    }


    public void add(Trip trip) {
        this.trips.add(trip);
        notifyDataSetChanged(); // to render the list we need to notify
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

        TripViewHolder holder = new TripViewHolder();
        LayoutInflater tripInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Trip treps = trips.get(position);

        convertView = tripInflater.inflate(R.layout.activity_trip, null);
        holder.departure = convertView.findViewById(R.id.departure);
        holder.destination = convertView.findViewById(R.id.destination);
        holder.author = convertView.findViewById(R.id.author);
        convertView.setTag(holder);

        holder.departure.setText(treps.getDeparture());
        holder.destination.setText(treps.getDestination());
        holder.author.setText(treps.getAuthor());

        return convertView;
    }



}

class TripViewHolder {

    TextView departure;
    TextView destination;
    TextView author;


}
