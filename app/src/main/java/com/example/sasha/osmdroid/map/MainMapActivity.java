package com.example.sasha.osmdroid.map;

import android.os.Bundle;
import android.util.Log;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.cash.loader.MainActivity;

/**
 * Created by sasha on 3/6/15.
 */
public class MainMapActivity extends SlidingUpBaseActivity {
    MapFragment mapFragment;

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
//        try {
//            guide = HelperFactory.getHelper().getCityGuideDAO().queryForId(getIntent().getIntExtra(ID_TAG,-1));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }


}
