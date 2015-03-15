package com.example.sasha.osmdroid.map;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.cash.loader.MainActivity;
import com.example.sasha.osmdroid.cash.loader.OnItemClicklistener;
import com.example.sasha.osmdroid.navdrawer.MyAdapter;
import com.example.sasha.osmdroid.types.CustomGeoPoint;

import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by sasha on 3/6/15.
 */
public class MainMapActivity extends SlidingUpBaseActivity implements OnItemClicklistener {
    public static final String ID_TAG = "CITY_ID";
    MyAdapter.MenuItem[] items = new MyAdapter.MenuItem[]{
            new MyAdapter.MenuItem(R.string.my_cities, R.drawable.ic_play_download_black_24dp),
            new MyAdapter.MenuItem(R.string.install_cash, R.drawable.ic_shop_black_24dp),
            new MyAdapter.MenuItem(R.string.action_settings, R.drawable.ic_settings_black_24dp)
    };
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    MyAdapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    private MapFragment mapFragment;
    private String NAME = "";
    private String EMAIL = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_map_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate  MainMapActivity");
        if (savedInstanceState == null) {
            mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, mapFragment)
                    .commit();
            Log.d(MainActivity.LOG_TAG, "commit  MainMapActivity");
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(this, items, NAME, EMAIL, null);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, null, R.string.open, R.string.close) {

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
        mapFragment.sitOnMarkerClickListener(new MyOnItemGestureListener<OverlayItem, CustomGeoPoint>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item, CustomGeoPoint point) {
                Toast.makeText(getApplicationContext(), point.title, Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item, CustomGeoPoint point) {
                return false;
            }
        });
//

    }


    @Override
    public void onClickItem(View rootView, View view, int position) {

    }
}
