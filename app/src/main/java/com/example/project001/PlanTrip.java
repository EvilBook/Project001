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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import com.example.project001.database.Trip;
import com.example.project001.fragment.ProfileFragment;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class PlanTrip extends Fragment {

    //variables
    public static String date;
    public static String time;
    public static String email;
    public static EditText departure;
    public static EditText destination;
    public static EditText price;
    public static EditText seats;
    ImageView calendar, clock;
    Button createButton,buttonCalendar,buttonClock;
    Trip trip;

    int hour,min,day,month,year;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_trip, container, false);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                price.getText().toString(),
                seats.getText().toString(),
                email
        );

        final ArrayList<Trip> list = new ArrayList<>();

        list.add(trip);
        System.out.println(list);

        if(destination.getText().toString().isEmpty() ||
                departure.getText().toString().isEmpty() ||
                date.isEmpty() ||
                price.getText().toString().isEmpty() ||
                seats.getText().toString().isEmpty() ||
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

}
