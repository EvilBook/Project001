package com.example.project001;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.plus.Plus;

public class LoginActivity extends FragmentActivity implements  ConnectionCallbacks,OnConnectionFailedListener,View.OnClickListener {


    //variables
    private TextView emailField;
    private TextView passField;
    private Button loginButton;
    private TextView forgotPassword;
    private TextView createAccount;


//Implement constant variables

    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;


    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;

    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButton = findViewById(R.id.sign_in_button);


        mSignInButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {

            switch (v.getId()) {
                    case R.id.sign_in_button:
                        resolveSignInError();


                        break;

                }
            }



    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInButton.setEnabled(false);
        // Indicate that the sign in process is complete.
        mSignInProgress = SIGNED_IN;
        try {

            String emailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);
            mStatus.setText(String.format("Signed In to My App as %s", emailAddress));

        } catch (Exception ex) {
            String exception = ex.getLocalizedMessage();
            String exceptionString = ex.toString();
        }




    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = connectionResult.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }

        onSignedOut();

    }

    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope("email"))
                .build();

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void resolveSignInError() {


        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);

                startActivity(new Intent(LoginActivity.this,SideBarActivity.class));


            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();

            }
        } else {


        }


    }
    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);

    }



}
