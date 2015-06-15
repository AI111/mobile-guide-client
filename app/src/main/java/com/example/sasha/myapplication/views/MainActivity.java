package com.example.sasha.myapplication.views;

import android.accounts.Account;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.myapplication.Main2Activity;
import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.GeoPoint;
import com.example.sasha.myapplication.database.Guide;
import com.example.sasha.myapplication.database.HelperFactory;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.j256.ormlite.table.TableUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Created by sasha on 04.06.15.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    public static String LOG_TAG;
    public static String token;
    private DrawerLayout mDrawerLayout;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean signIn;
    private ImageView profileImage;
    private TextView name;
    private TextView email;
    private boolean mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        profileImage = (ImageView) findViewById(R.id.circleView);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).addScope(new Scope(Scopes.PROFILE)).build();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView imageView = (ImageView) findViewById(R.id.circleView);
        //Picasso.with(this).load(R.drawable.odessa).transform(new RoundedTransformation(100, 20)).into(imageView);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.auth:
                startActivity(new Intent(this, Main2Activity.class));
                return true;
            case R.id.clear_db:
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Guide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.show_db:
                try {
                    Log.d(MainActivity.LOG_TAG, "\n DB = " + HelperFactory.getHelper().getGuideDAO().getAllCities() + "\n----------\n" + HelperFactory.getHelper().getGeoPointDAO().getAllPoints());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.update_db:
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Guide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), Guide.class);
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        if (mSignIn) {
            MenuItem item = navigationView.getMenu().findItem(R.id.login);
            item.setIcon(R.drawable.ic_person_black_24dp);
        }
        //
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        switch (menuItem.getItemId()) {
                            case R.id.installed:
                                fragmentManager.beginTransaction().replace(R.id.container, new InstalledItemFragment()).commit();
                                break;
                            case R.id.install:
                                fragmentManager.beginTransaction().replace(R.id.container, new DownloadListFragment()).commit();
                                break;
                            case R.id.login:
                                Log.d(LOG_TAG, "login");
                                menuItem.setIcon(R.drawable.ic_person_black_24dp);
                                if (mSignIn) {
                                    signOutUser();

                                } else {
                                    signInUser();
                                }
                                return true;

                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void signInUser() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignIn = true;
            mGoogleApiClient.connect();
        }
    }

    private void signOutUser() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mSignIn = true;
        updateUI(true);
        new GetIdTokenTask().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignIn = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    private void updateUI(boolean authentificated) {
        if (authentificated) {
            try {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi
                            .getCurrentPerson(mGoogleApiClient);
                    String personName = currentPerson.getDisplayName();
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    String personEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);

                    personPhotoUrl = personPhotoUrl.substring(0,
                            personPhotoUrl.length() - 2)
                            + 200;
                    Picasso.with(this).load(personPhotoUrl).transform(new RoundedTransformation(100, 8)).into(profileImage);
                    name.setText(personName);
                    email.setText(personEmail);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            profileImage.setImageResource(android.R.color.transparent);
            name.setText("");
            email.setText("");
        }
    }

    private class GetIdTokenTask extends AsyncTask<String, Void, String> {

        private final String scope = "audience:server:client_id:221601576513-9gdgmghqm3mkss52ehl4fifv08tpo3v3.apps.googleusercontent.com"; // Not the app's client ID.


        @Override
        protected String doInBackground(String... params) {

            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Account account = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);

            String idToken = "";
            try {
                idToken = GoogleAuthUtil.getTokenWithNotification(getApplicationContext(), account, scope, new Bundle());
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
            token = result;
        }

    }
}
