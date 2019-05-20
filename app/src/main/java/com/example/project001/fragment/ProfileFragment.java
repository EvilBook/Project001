package com.example.project001.fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.project001.LoginActivity;
import com.example.project001.R;
import com.example.project001.SideBarActivity;
import com.example.project001.database.DBConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.project001.database.Trip;
import com.example.project001.database.personnumer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.wearable.DataMap.TAG;

public class ProfileFragment extends Fragment {

    //local variables
    public static String email = "sample";
    public static String name = "sample";
    public static String phone = "sample";
    public static String numberString = "sample";

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    //xml variables
    ImageView ProfilePic2;
    TextView nameTextView, emailTextView, phoneTextView, number;
    Button buttonEditPhone, personnumerButton;
    TextView streetAddress, city, postCode, verifiedText;
    EditText extraInfo;


    ImageView verifiedImage;



    String verified="false";


    String numberText="";




    //popup xml variables
    DisplayMetrics dp = new DisplayMetrics();

    TextView editPhone;
    Button editPhoneButton;

    TextView rating;


    TextView editNumber;
    Button editNumberButton;



    //url profile pic variables
    Uri profilePic;
    URL url;

    //Database
    DBConnection dbc = new DBConnection();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //db
        dbc.p = ProfileFragment.this;


        //set text views
        emailTextView = getView().findViewById(R.id.emailTextView);
        nameTextView = getView().findViewById(R.id.nameTextView);
        phoneTextView = getView().findViewById(R.id.phoneTextView);
        rating = getView().findViewById(R.id.RateText);

        number = getView().findViewById(R.id.number);
        extraInfo=getView().findViewById(R.id.editText2);


        streetAddress = getView().findViewById(R.id.streetAddressText);
        city = getView().findViewById(R.id.cityText);
        postCode = getView().findViewById(R.id.postCodeText);


        verifiedImage=getView().findViewById(R.id.verifiedImage);


        verifiedText=getView().findViewById(R.id.verified);





        System.out.println("-----------------------------------------------------------------------");

        db.collection("person")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("email").equals(email)) {
                                    final Long path = Long.valueOf(String.valueOf(document.get("FinalRating")));
                                    System.out.println("Rating---->" + " " + path);

                                    String rating1 = path.toString(path);
                                    rating.setText(rating1+"/"+"5");





                                }}}}});



                                    //Handle Profile Picture
        profilePic = LoginActivity.personPhoto;

        try {
            url = new URL(profilePic.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new SideBarActivity.DownloadImageTask((ImageView) getView().findViewById(R.id.ProfilePic2)).execute(String.valueOf(url));


        //get info from db
        dbc.getProfileInformation();


        //pop up window when button clicked
        buttonEditPhone = getView().findViewById(R.id.buttonEditPhone);
        personnumerButton = getView().findViewById(R.id.personnumerButton);



        buttonEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //popup window info
                LayoutInflater inflater = (LayoutInflater) ProfileFragment.this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.activity_create,null);


                editPhone = layout.findViewById(R.id.editPhone);
                editPhone.setText(phone);
                editPhoneButton = layout.findViewById(R.id.editPhoneButton);


                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display. getSize(size);
                int width = (int)(size.x*0.7);
                int height = (int)(size.y*0.5);

                final PopupWindow pw = new PopupWindow(layout, (int)width, (int)height, true);

                editPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editPhone.getText().equals("")){
                            Log.e("PHONE NUM: ", editPhone.getText().toString());
                            phoneTextView.setText(editPhone.getText());
                        }else{
                            Log.e("PHONE NUM: ", "please fill in the phone number.");
                        }

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


        checkIfVerified();


        extraInfo.setImeOptions(EditorInfo.IME_ACTION_DONE);
        extraInfo.setRawInputType(InputType.TYPE_CLASS_TEXT);
        extraInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    saveInfo(extraInfo.getText().toString());
                    Log.e("fuck off", "twice");
                }
                return false;
            }
        });





    }

    private void saveInfo(String toString) {


        Map<String, Object> extraInfo1 = new HashMap<>();

        extraInfo1.put("info", toString);


        db.collection("userBio").document(email).set(extraInfo1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

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


    //set values collected from the db
    public void setValues(String e, String n, String p){

        email = e;
        name = n;
        phone  = p;

        emailTextView.setText(email);
        nameTextView.setText(name);
        phoneTextView.setText(phone);
    }


    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }


    String path="";
    Bitmap imageBitmap;



    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
                email();
            }
        }
    }


    public void email(){


        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "louliet@gmail.com" });
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Verify THIS");
        emailIntent.putExtra(Intent.EXTRA_STREAM, imageBitmap);
        emailIntent.putExtra(Intent.ACTION_ATTACH_DATA, imageBitmap);


        getContext().startActivity(Intent.createChooser(emailIntent,"Sending email..."));


        loadInfo();



    }


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    personnumer n;




    public void loadInfo(){

        db.collection("personalinfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("number").equals(number.getText().toString())) {

                                    n = new personnumer(
                                            document.get("number").toString(),
                                            document.get("firstName").toString(),
                                            document.get("lastName").toString(),
                                            document.get("address").toString(),
                                            document.get("city").toString(),
                                            document.get("postcode").toString(),
                                            document.getString("verified"),
                                    document.getString("email"));


                                    streetAddress.setText(document.getString("address"));
                                    city.setText(document.getString("city"));
                                    postCode.setText(document.getString("postcode"));


                                    savePersonnumer();


                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });






    }

    private void savePersonnumer() {


        String filename = "personnumer.txt";
        String fileContents = numberText;
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(filename, false);
            outputStream.write(fileContents.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public void checkIfVerified(){


        File directory = getContext().getFilesDir();
        File file = new File(directory, "personnumer.txt");
        String line = null;


        if(file.exists()) {


            try {
                FileInputStream fileInputStream = new FileInputStream(file);


                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

                line = reader.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }


            db.collection("personalinfo")
                    .document(line).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot=task.getResult();
                            verified=documentSnapshot.getString("verified");
                            numberText=documentSnapshot.getString("number");


                            loadScreen();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });


        }






    }


    public void loadScreen(){



        if (verified.equals("true")){
            Log.e("bitchass", "ass");


            number.setText(numberText);


            verifiedImage.setImageResource(R.drawable.verified);


            verifiedText.setText("You are verified!");



            verifiedText.setTextColor(Color.parseColor("#009900"));


            personnumerButton.setClickable(false);
            personnumerButton.setText("");
            personnumerButton.setWillNotDraw(true);
            personnumerButton.setBackgroundColor(Color.parseColor("#111111"));


            db.collection("personalinfo")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getId().equals(numberText)) {

                                        n = new personnumer(
                                                document.get("number").toString(),
                                                document.get("firstName").toString(),
                                                document.get("lastName").toString(),
                                                document.get("address").toString(),
                                                document.get("city").toString(),
                                                document.get("postcode").toString(),
                                                document.getString("verified"),
                                                document.getString("email"));


                                        streetAddress.setText(document.getString("address"));
                                        city.setText(document.getString("city"));
                                        postCode.setText(document.getString("postcode"));


                                    }
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });






        }else {


            personnumerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //popup window info
                    LayoutInflater inflater = (LayoutInflater) ProfileFragment.this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.pop_up_personnumer, null);


                    editNumber = layout.findViewById(R.id.editPhone);
                    editNumber.setText(numberString);
                    editNumberButton = layout.findViewById(R.id.editPhoneButton);


                    float density = ProfileFragment.this.getResources().getDisplayMetrics().density;


                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = (int) (size.x * 0.7);
                    int height = (int) (size.y * 0.5);

                    final PopupWindow pw = new PopupWindow(layout, (int) width, (int) height, true);

                    editNumberButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editNumber.getText().equals("")) {
                                Log.e("Personnumer: ", editNumber.getText().toString());
                                number.setText(editNumber.getText());
                                numberText = editNumber.getText().toString();
                                openCameraIntent();
                            } else {
                                Log.e("Personnumer: ", "please fill in the phone number.");
                            }

                            pw.dismiss();
                        }
                    });

                    //handle touch outside popup window
                    pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    pw.setTouchInterceptor(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
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


        }





    }







}
