package com.example.project001;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.project001.database.DBConnection;
import com.example.project001.sql.DatabaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    //Variables
    private static final String TAG = "SignInTestActivity";
    private static final int OUR_REQUEST_CODE = 49404;
    public GoogleSignInClient mGoogleApiClient;

    public static Uri personPhoto;

    DatabaseHelper mydb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = GoogleSignIn.getClient(this, gso);


        ImageButton googlesignInButton = findViewById(R.id.sign_in_Button);

        googlesignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });




        mydb = new DatabaseHelper(this);

//        addData("suzannezomer@mgmail.com","suzanne zomer");
//        returnData();


    }


    private void signIn() {
        Intent signInIntent = mGoogleApiClient.getSignInIntent();
        startActivityForResult(signInIntent,OUR_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == OUR_REQUEST_CODE) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.i("Message",account.getDisplayName());

            Intent intent = new Intent(this, SideBarActivity.class);
            intent.putExtra("Display",account.getDisplayName());
            intent.putExtra("Email",account.getEmail());

            personPhoto = account.getPhotoUrl();

            addData(account.getEmail(),account.getDisplayName());
            returnData();

            finish();
            startActivity(intent);

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }


    public void addData(String entry, String entry2){
        boolean insertData = (boolean) mydb.addData(entry, entry2);

        if(insertData){
            toastManager("succes");
        }else{
            toastManager("something went wrong");
        }
    }

    private void toastManager(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void returnData(){
        Cursor data = mydb.getData();
        while(data.moveToNext()){
            System.out.println("DATA: " + data.getString(1) + ", " + data.getString(2));
        }
    }
}












