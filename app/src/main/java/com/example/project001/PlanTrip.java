package com.example.project001;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project001.database.Trip;
import com.example.project001.fragment.ProfileFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class PlanTrip extends Fragment implements AdapterView.OnItemClickListener{

    //variables
    public static String date;
    public static String time;
    public static String email;
    public static EditText departure;
    public static EditText destination;
    public static NumberPicker price;
    public static NumberPicker seats;
    ImageView calendar, clock;
    Button createButton,buttonCalendar,buttonClock;
    Trip trip;

    int hour,min,day,month,year;


    AutoCompleteTextView autoCompView;
    AutoCompleteTextView autoCompView2;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_trip, container, false);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        autoCompView = (AutoCompleteTextView) getView().findViewById(R.id.departureTXT);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        autoCompView2 = (AutoCompleteTextView) getView().findViewById(R.id.destinationTXT);
        autoCompView2.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        autoCompView2.setOnItemClickListener(this);



        if(getArguments() != null) {
            email = getArguments().getString("email");
            //Log.e("tripFragment", email);
        } else {
            Log.e("doesnt", "work");
        }


        Log.e("something", "happening");

        departure = getView().findViewById(R.id.departureTXT);
        destination = getView().findViewById(R.id.destinationTXT);
        price = getView().findViewById(R.id.priceTXT);
        seats = getView().findViewById(R.id.seatsTXT);
        calendar = getView().findViewById(R.id.calendar);
        clock = getView().findViewById(R.id.clock);
        createButton = getView().findViewById(R.id.createButton);

        price.setMinValue(50);
        price.setMaxValue(1500);

        seats.setMinValue(1);
        seats.setMaxValue(15);


        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //popup window info
                LayoutInflater inflater = (LayoutInflater) PlanTrip.this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup_calender,null);

                //layout fields
                buttonCalendar = layout.findViewById(R.id.buttonCalendar);
                final DatePicker datePicker = layout.findViewById(R.id.date);



                float density=PlanTrip.this.getResources().getDisplayMetrics().density;
                final PopupWindow pw = new PopupWindow(layout, (int)density*350, (int)density*460, true);


                buttonCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        day = datePicker.getDayOfMonth();
                        month = datePicker.getMonth();
                        year = datePicker.getYear();

                        date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                        pw.dismiss();
                    }
                });


                //handle touch outside popup window
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.setOutsideTouchable(true);

                //start pop up window
                pw.showAtLocation(layout, Gravity.CENTER, 0, -100);

                dimBehind(pw);
            }
        });


        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popup window info
                LayoutInflater inflater = (LayoutInflater) PlanTrip.this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.popup_clock,null);

                //layout fields
                buttonClock = layout.findViewById(R.id.buttonClock);
                final TimePicker timePicker = layout.findViewById(R.id.time);


                float density=PlanTrip.this.getResources().getDisplayMetrics().density;
                final PopupWindow pw = new PopupWindow(layout, (int)density*350, (int)density*460, true);

                buttonClock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hour = timePicker.getHour();
                        min = timePicker.getMinute();
                        time = String.valueOf(hour) + ":" + String.valueOf(min);

                        pw.dismiss();
                    }
                });

                //handle touch outside popup window
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.setOutsideTouchable(true);

                //start pop up window
                pw.showAtLocation(layout, Gravity.CENTER, 0, -100);

                dimBehind(pw);
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTrip();

            }
        });
    }


    //dim background for pop up window
    private void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.6f;
        wm.updateViewLayout(container, p);
    }


    public void addTrip() {

        trip = new Trip(
                date,
                time,
                departure.getText().toString(),
                destination.getText().toString(),
                String.valueOf(price.getValue()),
                String.valueOf(seats.getValue()),
                email
        );


        final ArrayList<Trip> list = new ArrayList<>();

        list.add(trip);
        System.out.println(list);

        if(destination.getText().toString().isEmpty() ||
                departure.getText().toString().isEmpty() ||
                date.isEmpty() ||
                String.valueOf(price.getValue()).isEmpty() ||
                String.valueOf(seats.getValue()).isEmpty() ||
                time.isEmpty()) {


            //show dialog
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Problem Creating Trip");
            alertDialog.setMessage("Please Fill In All Fields");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            createButton.setClickable(false);

        } else {

            Intent myIntent = new Intent(getContext(), ConfirmTrip.class);
            myIntent.putParcelableArrayListExtra("An Object", list);
            PlanTrip.this.startActivity(myIntent);

        }

        createButton.setClickable(true);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
}


class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList resultList;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return (String) resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            private ArrayList autocomplete(String toString) {
                String LOG_TAG = "GooglePlaceAutocomplete";
                String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
                String TYPE_AUTOCOMPLETE = "/autocomplete";
                String OUT_JSON = "/json";
                String API_KEY = "AIzaSyC6m2lEXUWFI7csc1393xi-ldWIe_SC9FM";

                ArrayList resultList = null;

                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                    sb.append("?key=" + API_KEY);
                    sb.append("&components=country:se");
                    sb.append("&input=" + URLEncoder.encode(toString, "utf8"));

                    URL url = new URL(sb.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Load the results into a StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                        System.out.println(jsonResults);
                    }
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error processing Places API URL", e);
                    return resultList;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to Places API", e);
                    return resultList;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

                try {
                    // Create a JSON object hierarchy from the results
                    JSONObject jsonObj = new JSONObject(jsonResults.toString());
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    // Extract the Place descriptions from the results
                    resultList = new ArrayList(predsJsonArray.length());
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                        System.out.println("============================================================");
                        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Cannot process JSON results", e);
                }

                return resultList;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();

                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}