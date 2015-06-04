package com.example.sasha.osmdroid.views.map;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.types.GeoPoint;
import com.example.sasha.osmdroid.views.loader.MainActivity;
import com.example.sasha.osmdroid.views.loader.OnItemClicklistener;
import com.example.sasha.osmdroid.views.navdrawer.MyAdapter;

import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sasha on 3/6/15.
 */
public class MainMapActivity extends SlidingUpBaseActivity implements OnItemClicklistener, MyOnItemGestureListener<OverlayItem, GeoPoint>, View.OnClickListener {
    public static final String ID_TAG = "CITY_ID";
    MyAdapter.MenuItem[] items = new MyAdapter.MenuItem[]{
            new MyAdapter.MenuItem(R.string.my_cities, R.drawable.ic_play_download_grey600_24dp),
            new MyAdapter.MenuItem(R.string.install_cash, R.drawable.ic_shop_grey600_24dp),
            new MyAdapter.MenuItem(R.string.action_settings, R.drawable.ic_settings_grey600_24dp)
    };
    RecyclerView mRecyclerView;
    MyAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    private MapFragment mapFragment;
    private String NAME = "";
    private String EMAIL = "";
    private String dirPath;

    @Override
    protected int getLayout() {
        return R.layout.activity_map_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate  MainMapActivity");
        mapFragment = new MapFragment();
        mFab.setOnClickListener(this);
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, mapFragment)
                    .commit();
            Log.d(MainActivity.LOG_TAG, "commit  MainMapActivity");
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MyAdapter(this, items, NAME, EMAIL, null, true);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, null, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

        };


        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        mAdapter.setOnItemClickListener(this);
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
