package com.example.sasha.osmdroid.views.navdrawer;

/**
 * Created by sasha on 3/3/15.
 */

import android.accounts.Account;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.views.loader.DownloadListFragment;
import com.example.sasha.osmdroid.views.loader.InstalledItemFragment;
import com.example.sasha.osmdroid.views.loader.OnItemClicklistener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, OnItemClicklistener,
        ConnectionCallbacks, OnConnectionFailedListener {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    private static final int RC_SIGN_IN = 0;
    RegisrtrationFragment regisrtrationFragment;
    MyAdapter.MenuItem[] items = new MyAdapter.MenuItem[]{
            new MyAdapter.MenuItem(R.string.my_cities, R.drawable.ic_play_download_grey600_24dp),
            new MyAdapter.MenuItem(R.string.install_cash, R.drawable.ic_shop_grey600_24dp),
            new MyAdapter.MenuItem(R.string.action_settings, R.drawable.ic_settings_grey600_24dp)
    };
    String NAME = "name";
    String EMAIL = "email";
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    MyAdapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private ConnectionResult mConnectionResult;
    private Toolbar toolbar;                              // Declaring the Toolbar Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        Log.d(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "onCreate");

    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        btnSignIn = (SignInButton) findViewById(R.id.button6);
        btnSignOut = (Button) findViewById(R.id.button7);
        btnRevokeAccess = (Button) findViewById(R.id.button8);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(this, items, NAME, EMAIL, null, false);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made


        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        mAdapter.setOnItemClickListener(this);
        if (savedInstanceState == null) {
            regisrtrationFragment = new RegisrtrationFragment();
            regisrtrationFragment.setOnClickListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, regisrtrationFragment)
                    .commit();
        }
        //Picasso p = new Picasso.Builder(getApplicationContext()).memoryCache(new LruCache(24000)).build();

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClickItem(View rootView, View view, int position) {
        Log.d(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, position + "");
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (view.getId() == R.id.menu_item) {
            switch (position) {
                case 1:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new InstalledItemFragment())
                            .commit();
                    Drawer.closeDrawers();
                    break;
                case 2:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new DownloadListFragment())
                            .commit();
                    Drawer.closeDrawers();
                    break;
            }
        } else {

        }
//
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button6:
                // Signin button clicked
                signInWithGplus();
                break;
            case R.id.button7:
                // Signout button clicked
                signOutFromGplus();
                break;
            case R.id.button8:
                // Revoke access button clicked
                revokeGplusAccess();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "onConnectionFailed");

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
        Log.d(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "onActivityResult");
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
        Log.d(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "onConnected");
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
                            Log.e(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "User access revoked!");
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
                mAdapter.changeTitle(personName, email, personPhotoUrl);
                new GetIdTokenTask().execute();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetIdTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            String scopes = "audience:server:client_id:" + "191215476409-ng9efc9a11ss140jeeoh5jc1v4kq8dlh.apps.googleusercontent.com"; // Not the app's client ID.
            String idToken = "";
            try {
                idToken = GoogleAuthUtil.getToken(getApplicationContext(), Plus.AccountApi.getAccountName(mGoogleApiClient), scopes);
            } catch (IOException e) {
                Log.e(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "Error retrieving ID token.", e);
            } catch (GoogleAuthException e) {
                Log.e(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "Error retrieving ID token.", e);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return idToken;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(com.example.sasha.osmdroid.views.loader.MainActivity.LOG_TAG, "ID token: " + result);
            //mStatus.setText(result);
        }
//
    }
}
