package com.example.project001;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.project001.database.DBConnection;
import com.example.project001.database.SearchResult;
import com.example.project001.fragment.SearchAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;
import static java.security.AccessController.getContext;

public class SearchResultsActivity extends AppCompatActivity {


    //variables
    private SearchAdapter searchAdapter;
    private ListView listView;
    SearchResult searchResult;

    ArrayList<SearchResult> searchResults = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DBConnection db1= new DBConnection();


    String departure="";
    String destination="";


    String email;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);



        searchAdapter = new SearchAdapter(this);
        listView = findViewById(R.id.searchView);
        listView.setAdapter(searchAdapter);


        db1.context=this;




        Intent intent=getIntent();
        departure=intent.getStringExtra("departure");
        destination=intent.getStringExtra("destination");
        email=intent.getStringExtra("email");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final String tripId="";
                final String driver= ((TextView)((RelativeLayout)view).getChildAt(2)).getText().toString();
                final String destination= ((TextView)((RelativeLayout)view).getChildAt(1)).getText().toString();
                final String departure= ((TextView)((RelativeLayout)view).getChildAt(0)).getText().toString();
                final String passenger= "";
                final String status="0";
                final String date=((TextView)((RelativeLayout)view).getChildAt(5)).getText().toString();


                //popup window info
                LayoutInflater inflater = (LayoutInflater) SearchResultsActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.popup_search,null);

                View editPhoneButton=layout.findViewById(R.id.acceptSearch);
                View editPhoneButton1=layout.findViewById(R.id.cancelSearch);


                float density= SearchResultsActivity.this.getResources().getDisplayMetrics().density;
                final PopupWindow pw = new PopupWindow(layout, (int)density*350, (int)density*234, true);



                editPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db1.addTripRequestFromSearch(driver.replace("Driver: ",""), email, "0",departure.replace("Departure: ",""),destination.replace("Destination: ",""), date.replace("Date: ",""));


                        db1.addTheNumbers();














                                    pw.dismiss();
                    }
                });

                editPhoneButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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





        searches();

    }

    private void searches() {

        Log.e("departure", departure);
        Log.e("destination", destination);



        searchResults.clear();
        db.collection("trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(!departure.equals("") && destination.equals("")){
                                    Log.e("departure1", departure);
                                    Log.e("destination1", destination);
                                    if(document.getString("departure").contains(departure)) {

                                        searchResult = new SearchResult(document.get("departure").toString(),
                                                document.get("destination").toString(),
                                                document.get("author").toString(),
                                                document.get("seats").toString(),
                                                document.get("price").toString(),
                                                document.get("date").toString(),
                                                document.get("time").toString()
                                        );

                                        searchResults.add(searchResult);
                                    }

                                }else if(departure.equals("") && !destination.equals("")){
                                    if(document.getString("destination").contains(destination)) {
                                        Log.e("departure2", departure);
                                        Log.e("destination2", destination);

                                        searchResult = new SearchResult(document.get("departure").toString(),
                                                document.get("destination").toString(),
                                                document.get("author").toString(),
                                                document.get("seats").toString(),
                                                document.get("price").toString(),
                                                document.get("date").toString(),
                                                document.get("time").toString()
                                        );

                                        searchResults.add(searchResult);
                                    }

                                }else if(!departure.equals("") && !destination.equals("")){
                                    Log.e("departure3", departure);
                                    Log.e("destination3", destination);
                                    if(document.getString("departure").contains(departure) && document.getString("destination").contains(destination)) {

                                        Log.e("is ", "actually running");
                                        searchResult = new SearchResult(document.get("departure").toString(),
                                                document.get("destination").toString(),
                                                document.get("author").toString(),
                                                document.get("seats").toString(),
                                                document.get("price").toString(),
                                                document.get("date").toString(),
                                                document.get("time").toString()
                                        );

                                        searchResults.add(searchResult);
                                    }

                                }
                            }

                            for (SearchResult T : searchResults) {
                                searchAdapter.add(T);
                                // scroll the ListView to the last added element
                                listView.setSelection(listView.getCount() - 1);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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

}
