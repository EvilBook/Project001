package com.example.project001;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerExtraInfo implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public MarkerExtraInfo(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_info, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.message_body);
        TextView hotel_tv = view.findViewById(R.id.message_body2);

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        name_tv.setText(infoWindowData.getFood());
        hotel_tv.setText(infoWindowData.getHotel());
        details_tv.setText(infoWindowData.getTransport());

        return view;
    }
}