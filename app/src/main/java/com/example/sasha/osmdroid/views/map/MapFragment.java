package com.example.sasha.osmdroid.views.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.database.HelperFactory;
import com.example.sasha.osmdroid.types.GeoPoint;
import com.example.sasha.osmdroid.types.Guide;
import com.example.sasha.osmdroid.views.loader.MainActivity;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.ResourceProxyImpl;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.util.constants.MapViewConstants;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sasha on 3/6/15.
 */
public class MapFragment extends Fragment implements MapViewConstants {
    ITileSource tileSource;
    private MapView mapView;
    private ResourceProxyImpl mResourceProxy;
    private IMapController mapController;
    private MyLocationNewOverlay myLocationOverlay;
    private Guide guide;
    private CompassOverlay compassOverlay;
    private ArrayList<OverlayItem> items;
    private ItemizedIconOverlay.OnItemGestureListener<OverlayItem> listener;
    private MyOnItemGestureListener<OverlayItem, GeoPoint> gestureListener;
    private GeoPoint[] geoPoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            guide = HelperFactory.getHelper().getGuideDAO().queryForId(getActivity().getIntent().getIntExtra(MainMapActivity.ID_TAG, -1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void setOnMarkerClickListener(MyOnItemGestureListener<OverlayItem, GeoPoint> gestureListener) {
        this.gestureListener = gestureListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onActivityCreated  MapFragment");
        mapController = mapView.getController();
        org.osmdroid.util.GeoPoint center = new org.osmdroid.util.GeoPoint(guide.getLatitude(), guide.getLongitude());
        mapController.setZoom(14);
        mapController.setCenter(center);
        mapController.animateTo(center);

        compassOverlay = new CompassOverlay(getActivity(), mapView);

        mapView.getOverlays().add(compassOverlay);

        myLocationOverlay = new MyLocationNewOverlay(getActivity(), mapView);
        mapView.getOverlays().add(myLocationOverlay);
        mapView.postInvalidate();

        Drawable newMarker = this.getResources().getDrawable(R.drawable.ic_location_on_black_36dp);

//        for(GeoPoint point:guide.points){
//            Marker startMarker = new Marker(mapView);
//            startMarker.setPosition(new GeoPoint(point.latitude, point.longitude));
//            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            startMarker.setIcon(newMarker);
//            startMarker.setTitle(point.title);
//            mapView.getOverlays().add(startMarker);
//        }


        items = new ArrayList<>();
        geoPoints = guide.points.toArray(new GeoPoint[guide.points.size()]);
        for (GeoPoint point : geoPoints) {
            OverlayItem item = new OverlayItem(point.title, point.description, new org.osmdroid.util.GeoPoint(point.latitude, point.longitude));
            item.setMarker(newMarker);
            items.add(item);
        }
        listener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                gestureListener.onItemSingleTapUp(index, item, geoPoints[index]);
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        };
        ItemizedOverlay overlay = new ItemizedIconOverlay(getActivity(), items, listener);
        mapView.getOverlays().add(overlay);
        mapView.invalidate();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        myLocationOverlay.enableMyLocation();
        compassOverlay.enableCompass();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        myLocationOverlay.disableMyLocation();
        compassOverlay.disableCompass();
    }

}
