package com.example.project001.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project001.R;
import com.example.project001.database.Chat;
import com.example.project001.database.SearchResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter {

        //variables
        String email;
        String chatID;
        View view;

        //objects
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<SearchResult> searchResults = new ArrayList<SearchResult>();
        Context context;

        com.example.project001.fragment.SearchViewHolder holder = new com.example.project001.fragment.SearchViewHolder();

        public SearchAdapter(Context context) {
            this.context = context;
        }

        public void add(SearchResult searchResult) {
            this.searchResults.add(searchResult);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return searchResults.size();
        }

        @Override
        public Object getItem(int position) {
            return searchResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater tripInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            SearchResult result = searchResults.get(position);

            convertView = tripInflater.inflate(R.layout.activity_search_result, null);
            holder.departure = convertView.findViewById(R.id.departureSearch);
            holder.destination = convertView.findViewById(R.id.destinationSearch);
            holder.driver = convertView.findViewById(R.id.driverSearch);
            holder.seats = convertView.findViewById(R.id.seatsSearch);
            holder.date = convertView.findViewById(R.id.dateSearch);
            holder.time = convertView.findViewById(R.id.timeSearch);
            holder.price = convertView.findViewById(R.id.priceSearch);
            convertView.setTag(holder);


            holder.departure.setText("Departure: "+result.getDeparture());
            holder.destination.setText("Destination: "+result.getDestination());
            holder.driver.setText("Driver: "+result.getDriver());
            holder.seats.setText("Seats Left: "+result.getSeats());
            holder.date.setText("Date: "+result.getDate());
            holder.time.setText("Departure Time: "+result.getTime());
            holder.price.setText("Price Per Seat: "+result.getPrice());

            return convertView;
        }
    }

    class SearchViewHolder {
        TextView departure;
        TextView destination;
        TextView driver;
        TextView seats;
        TextView price;
        TextView date;
        TextView time;
    }
