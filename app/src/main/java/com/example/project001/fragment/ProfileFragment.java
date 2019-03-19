package com.example.project001.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.project001.LoginActivity;
import com.example.project001.R;
import com.example.project001.SideBarActivity;
import com.example.project001.database.DBConnection;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileFragment extends Fragment {

    //local variables
    public static String email = "sample";
    public static String name = "sample";
    public static String phone = "sample";

    //xml variables
    ImageView ProfilePic2;
    TextView nameTextView, emailTextView, phoneTextView;
    Button buttonEditPhone;

    //popup xml variables
    DisplayMetrics dp = new DisplayMetrics();

    TextView editPhone;
    Button editPhoneButton;

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

        buttonEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //popup window info
                LayoutInflater inflater = (LayoutInflater) ProfileFragment.this.getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.activity_create,null);


                editPhone = layout.findViewById(R.id.editPhone);
                editPhone.setText(phone);
                editPhoneButton = layout.findViewById(R.id.editPhoneButton);


                float density=ProfileFragment.this.getResources().getDisplayMetrics().density;
                final PopupWindow pw = new PopupWindow(layout, (int)density*350, (int)density*234, true);

                editPhoneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editPhone.getText().equals("")){
                            Log.e("PHONE NUM: ", editPhone.getText().toString());
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
}

