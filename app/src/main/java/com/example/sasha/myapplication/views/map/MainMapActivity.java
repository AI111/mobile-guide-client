package com.example.sasha.myapplication.views.map;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.GeoPoint;
import com.example.sasha.myapplication.views.MainActivity;
import com.example.sasha.myapplication.views.OnItemClicklistener;

import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sasha on 3/6/15.
 */
public class MainMapActivity extends SlidingUpBaseActivity implements OnItemClicklistener, MyOnItemGestureListener<OverlayItem, GeoPoint>, View.OnClickListener {

    public static final String ID_TAG = "CITY_ID";

    RecyclerView.LayoutManager mLayoutManager;
    ImageView smallImage;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    private MapFragment mapFragment;
    private String dirPath;
    private DrawerLayout mDrawerLayout;

    @Override
    protected int getLayout() {
        return R.layout.activity_map_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate  MainMapActivity");
        smallImage = (ImageView) findViewById(R.id.small_img);
        mapFragment = new MapFragment();
        mFab.setOnClickListener(this);
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, mapFragment)
                    .commit();
            Log.d(MainActivity.LOG_TAG, "commit  MainMapActivity");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Picasso.with(this).load(R.drawable.odessa).transform(new RoundedTransformation(100, 20)).into(imageView);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mapFragment.setOnMarkerClickListener(this);
//
        try {
            dirPath = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir + "/" + getString(R.string.data_cash) + "/" + "Odessa";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClickItem(View rootView, View view, int position) {

    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        switch (menuItem.getItemId()) {
                            case R.id.showplace:
                                menuItem.setChecked(!menuItem.isChecked());
                                break;
                            case R.id.restaurant:
                                menuItem.setChecked(!menuItem.isChecked());
                                break;
                            case R.id.museum:
                                menuItem.setChecked(!menuItem.isChecked());
                                break;
                            case R.id.shop:
                                menuItem.setChecked(!menuItem.isChecked());
                                break;
                            case R.id.hotel:
                                menuItem.setChecked(!menuItem.isChecked());
                                break;
                        }
                        //menuItem.setChecked(true);
                        //mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public boolean onItemSingleTapUp(int index, OverlayItem item, GeoPoint point) {
        Toast.makeText(getApplicationContext(), point.title, Toast.LENGTH_SHORT).show();
        mTitle.setText(point.title);
        Log.d(MainActivity.LOG_TAG, Arrays.toString(GeoPoint.PointType.values()));

        Log.d(MainActivity.LOG_TAG, dirPath + "/" + point.galery[0]);
        File imgFile = new File(dirPath + "/" + point.galery[0]);

        if (imgFile.exists()) {
            Log.d(MainActivity.LOG_TAG, dirPath + "/" + point.galery[0]);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            // smallImg.setImageBitmap(myBitmap);
            mImageView.setImageBitmap(myBitmap);
            smallImage.setImageBitmap(myBitmap);

        }
        return false;
    }

    @Override
    public boolean onItemLongPress(int index, OverlayItem item, GeoPoint point) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

}
