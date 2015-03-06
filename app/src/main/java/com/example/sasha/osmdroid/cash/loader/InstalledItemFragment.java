package com.example.sasha.osmdroid.cash.loader;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.database.HelperFactory;
import com.example.sasha.osmdroid.map.MainMapActivity;
import com.example.sasha.osmdroid.map.SlidingUpBaseActivity;
import com.example.sasha.osmdroid.types.CityGuide;
import com.example.sasha.osmdroid.types.CustomGeoPoint;
import com.j256.ormlite.table.TableUtils;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {

            case R.id.clear_db:
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CustomGeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.show_db:
                try {
                    Log.d(MainActivity.LOG_TAG, "\n DB = " + HelperFactory.getHelper().getCityGuideDAO().getAllCities() + "\n----------\n" + HelperFactory.getHelper().getCustomGeoPointDAO().getAllPoints());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.clear_all:
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), CustomGeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), CityGuide.class);
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), CustomGeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickItem(View rootView, View view, final int position) {
        Intent intent = new Intent(getActivity(), MainMapActivity.class);
        intent.putExtra(SlidingUpBaseActivity.ID_TAG, guides.get(position).getId());
        startActivity(intent);
    }
}
