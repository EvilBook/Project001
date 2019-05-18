package com.example.project001;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project001.fragment.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class RateUser extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int choice;
    String displayName;

    public String user;

    public String L;

    public static Long finalAverage;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrating);

        final ListView lv = findViewById(R.id.mainListView);
        final Button btn =  findViewById(R.id.btn);

        Intent intent1 = getIntent();

        displayName = intent1.getStringExtra("Display");

        final String[] users = new String[] {

        };

        final List<String> User_List = new ArrayList<String>(Arrays.asList(users));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, User_List);

        lv.setAdapter(arrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("person")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        user = document.get("name").toString();
                                        String path = document.getId();
                                        System.out.println(path);
                                        User_List.add(user);
                                        arrayAdapter.notifyDataSetChanged();

                                    } } }}
                                    );


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,  int position, long id) {

                         final String s = lv.getItemAtPosition(position).toString();

                        System.out.println(s);

                        if (s.equals(displayName)){
                            AlertDialog alertDialog = new AlertDialog.Builder(RateUser.this).create();
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("You cant rate Yourself Sorrry");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }else {

                       L = lv.getItemAtPosition(position).toString();

                            db.collection("person")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if (document.getString("name").equals(L)){
                                                        final String path = document.getId();
                                                        System.out.println("path---->"+ " "+path);

                                                        LayoutInflater inflater = (LayoutInflater) RateUser.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                                                        View layout = inflater.inflate(R.layout.ratingbar,null);
                                                        final RatingBar mRatingBar =  layout.findViewById(R.id.ratingBar);
                                                        final TextView mRatingScale = layout.findViewById(R.id.tvRatingScale);
                                                        final Button mSendFeedback =  layout.findViewById(R.id.btnSubmit);

                                                        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                            @Override
                                                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                                                            }
                                                        });

                                                        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                            @SuppressLint("ResourceType")
                                                            @Override
                                                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                                                mRatingScale.setText(String.valueOf(v));
                                                                switch ((int) ratingBar.getRating()) {
                                                                    case 1:
                                                                        choice = 1;
                                                                        mRatingScale.setText(String.valueOf(choice));
                                                                        break;
                                                                    case 2:
                                                                        choice = 2;
                                                                        mRatingScale.setText(String.valueOf(choice));
                                                                        break;
                                                                    case 3:
                                                                        choice = 3;
                                                                        mRatingScale.setText(String.valueOf(choice));
                                                                        break;
                                                                    case 4:
                                                                        choice = 4;
                                                                        mRatingScale.setText(String.valueOf(choice));
                                                                        break;
                                                                    case 5:
                                                                        choice = 5;
                                                                        mRatingScale.setText(String.valueOf(choice));
                                                                        break;
                                                                    default:
                                                                        mRatingScale.setText("");
                                                                }

                                                                ratingBar.setEnabled(false);
                                                            }
                                                        });


                                                        mSendFeedback.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {

                                                                UpdateData(path);
                                                                Long value1 = Long.valueOf(choice);
                                                                UpdateAverege(value1,path);
                                                                getaveragefromdb(L,path);




                                                                mSendFeedback.setEnabled(false);


                                                            }
                                                        });



                                                        float density=RateUser.this.getResources().getDisplayMetrics().density;
                                                        final PopupWindow pw = new PopupWindow(layout, (int)density*350, (int)density*460, true);

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
                                                    } } } }});










                                                    }


                                                } }); }}
                                    ); }


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


    private void UpdateData(String path) {



        DocumentReference washingtonRef = db.collection("person").document(path);


        washingtonRef.update("NumberOfRating", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener< Void >() {

                    @Override

                 public void onSuccess(Void aVoid) {

                      Toast.makeText(RateUser.this, "Thanks for your Feedback",

                               Toast.LENGTH_SHORT).show();


                   }

              });

    }


    private void UpdateAverege(Long choi,String path) {

        DocumentReference Ref = db.collection("person").document(path);


        Ref.update("AverageRating", FieldValue.increment(choi))
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override

                    public void onSuccess(Void aVoid) {

                        Toast.makeText(RateUser.this, "Thanks for your Feedback",

                                Toast.LENGTH_SHORT).show();

                    }

                });


    }




    private void getaveragefromdb(final String name,final String Path){

        db.collection("person")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("name").equals(name)) {
                                    final String path = document.getId();
                                    System.out.println("path---->" + " " + path);

                                    long getNumberOfRating = document.getLong("NumberOfRating");
                                    long getAverage = document.getLong("AverageRating");

                                    System.out.println(getAverage + "  <---/---> " +getNumberOfRating);



                                    finalAverage = getAverage / getNumberOfRating;

                                    System.out.println(finalAverage);







                                    DocumentReference Ref = db.collection("person").document(Path);


                                    Ref.update("FinalRating", finalAverage)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                @Override

                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(RateUser.this, "Thanks for your Feedback",

                                                            Toast.LENGTH_SHORT).show();

                                                }

                                            });





                                } } }}});}








}



