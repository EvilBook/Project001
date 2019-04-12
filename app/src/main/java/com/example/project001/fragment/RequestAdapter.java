package com.example.project001.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project001.R;
import com.example.project001.database.Request;
import com.example.project001.database.Trip;
import com.google.android.gms.games.request.Requests;

import java.util.ArrayList;
import java.util.List;

public class RequestAdapter extends BaseAdapter {


    List<Request> requests = new ArrayList<>();
    Context context;

    public RequestAdapter(Context context) {
        this.context = context;
    }


    public void add(Request request) {
        this.requests.add(request);
        notifyDataSetChanged(); // to render the list we need to notify
    }



    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RequestViewHolder holder = new RequestViewHolder();
        LayoutInflater tripInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Request treps = requests.get(position);

        convertView = tripInflater.inflate(R.layout.activity_request, null);
        holder.departure = convertView.findViewById(R.id.departure);
        holder.destination = convertView.findViewById(R.id.destination);
        holder.author = convertView.findViewById(R.id.author);
        convertView.setTag(holder);

        holder.departure.setText(treps.requestId);
        holder.destination.setText(treps.getDriver());
        holder.author.setText(treps.getPassenger());
        convertView.setBackgroundColor(Color.parseColor(treps.getColour()));

        return convertView;
    }



}

class RequestViewHolder {

    TextView departure;
    TextView destination;
    TextView author;

}
