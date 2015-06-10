package com.example.sasha.myapplication.views;

import android.content.Intent;
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

import com.example.sasha.myapplication.Main2Activity;
import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.GeoPoint;
import com.example.sasha.myapplication.database.Guide;
import com.example.sasha.myapplication.database.HelperFactory;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


/**
 * Created by sasha on 04.06.15.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String LOG_TAG;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

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

                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public void onClick(View v) {

    }

}
