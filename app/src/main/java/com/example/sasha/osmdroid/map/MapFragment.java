package com.example.sasha.osmdroid.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.cash.loader.MainActivity;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.util.constants.MapViewConstants;

/**
 * Created by sasha on 3/6/15.
 */
public class MapFragment extends Fragment implements MapViewConstants {
    ITileSource tileSource;
    private MapView mapView;
    private ResourceProxyImpl mResourceProxy;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "onCreateView  MapFragment");
        View v = inflater.inflate(R.layout.fragment_map, null);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);
        mapView.setTileSource(new XYTileSource("MapQuest",
                ResourceProxy.string.mapquest_osm, 0, 18, 256, ".jpg", new String[]{
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onActivityCreated  MapFragment");
        mapController = mapView.getController();
        //mapController.setZoom(80);
        myLocationOverlay = new MyLocationNewOverlay(getActivity(), mapView);
        mapView.getOverlays().add(myLocationOverlay);
        mapView.postInvalidate();


    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        myLocationOverlay.enableMyLocation();
        ;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        myLocationOverlay.disableMyLocation();
    }

}
