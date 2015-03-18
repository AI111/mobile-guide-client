package com.example.sasha.osmdroid.cash.loader;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.sasha.osmdroid.database.HelperFactory;
import com.example.sasha.osmdroid.map.MainMapActivity;

import java.sql.SQLException;

/**
 * Created by sasha on 3/6/15.
 */
public class InstalledItemFragment extends DownloadListFragment {
    @Override
    protected void getData() {
        try {

            guides.clear();
            guides.addAll(HelperFactory.getHelper().getCityGuideDAO().getAllCities());
            mAdapter.notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.d(MainActivity.LOG_TAG, guides.toString());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClickItem(View rootView, View view, final int position) {
        Intent intent = new Intent(getActivity(), MainMapActivity.class);
        intent.putExtra(MainMapActivity.ID_TAG, guides.get(position).getId());
        startActivity(intent);
    }
}
