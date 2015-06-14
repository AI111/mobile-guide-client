package com.example.sasha.myapplication;

import android.accounts.Account;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasha.myapplication.views.MainActivity;
import com.example.sasha.myapplication.views.RoundedTransformation;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    TextView textView;
    ImageView imageView;
    Button buttonIn;
    Button buttonOut;
    CheckBox checkBox;
    TextView textViewToken;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        textViewToken = (TextView) findViewById(R.id.textView4);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).addScope(new Scope(Scopes.PROFILE)).build();






        super.onResume();
    }


    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onConnectionFailed(ConnectionResult result) {
        Log.d(MainActivity.LOG_TAG, "onConnectionFailed");

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        Log.d(MainActivity.LOG_TAG, "onActivityResult");
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        Log.d(MainActivity.LOG_TAG, "onConnected");
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(MainActivity.LOG_TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }

    private void updateUI(boolean b) {

    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getProfileInformation() {

        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + 200;
                Picasso.with(this).load(personPhotoUrl).transform(new RoundedTransformation(100, 8)).into(imageView);
                textView.setText(personName + " " + email);


            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                signInWithGplus();
                break;
            case R.id.button3:
                signOutFromGplus();
                break;
            case R.id.button4:
                new GetIdTokenTask().execute();
                break;
        }
    }

    private class GetIdTokenTask extends AsyncTask<String, Void, String> {
        private final static String G_PLUS_SCOPE =
                "oauth2:https://www.googleapis.com/auth/plus.me";
        private final static String USERINFO_SCOPE =
                "https://www.googleapis.com/auth/userinfo.profile";
        private final static String EMAIL_SCOPE =
                "https://www.googleapis.com/auth/userinfo.email";
        private final static String accesToken = G_PLUS_SCOPE + " " + USERINFO_SCOPE;
        private final String google_id = "audience:server:client_id:221601576513-9gdgmghqm3mkss52ehl4fifv08tpo3v3.apps.googleusercontent.com"; // Not the app's client ID.
        private String SCOPES;

        @Override
        protected void onPreExecute() {
            SCOPES = checkBox.isChecked() ? accesToken : google_id;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);

            String idToken = "";
            try {
                idToken = GoogleAuthUtil.getTokenWithNotification(getApplicationContext(), account, SCOPES, new Bundle());
            } catch (IOException e) {
                Log.e(MainActivity.LOG_TAG, "Error retrieving ID token.", e);
            } catch (GoogleAuthException e) {
                Log.e(MainActivity.LOG_TAG, "Error retrieving ID token.", e);
            }
            return idToken;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(MainActivity.LOG_TAG, "ID token:  " + result);
            textViewToken.setText(result);

        }

    }
}
