package com.example.sasha.osmdroid.map;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.sasha.osmdroid.R;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class OSMDroidMapActivity extends Activity {
    public static final String tag = "OSMSample";
    public static final GeoPoint Odessa = new GeoPoint(46.47404433, 30.74575424);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);

        mapView.setTileSource(new XYTileSource("MapQuest",
                ResourceProxy.string.mapquest_osm, 0, 18, 256, ".jpg", new String[]{
                "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                "http://otile4.mqcdn.com/tiles/1.0.0/map/"}));

        IMapController mapViewController = mapView.getController();
        mapViewController.setZoom(14);

        mapViewController.setCenter(Odessa);
        Drawable newMarker = this.getResources().getDrawable(R.drawable.ic_location_on_black_24dp);
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(new GeoPoint(46.47942309, 30.73854446));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(newMarker);
        startMarker.setTitle("fgdfgsd");
        startMarker.setInfoWindow(new CustomInfowindow(R.layout.layout, mapView));
        mapView.getOverlays().add(startMarker);
        mapView.invalidate();
//
//
//        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
//        OverlayItem item =  new OverlayItem("Here", "Current Position",new GeoPoint(46.47942309, 30.73854446));
//        OverlayItem item1 =  new OverlayItem("Here", "Current Position",new GeoPoint(46.47142309, 30.73459446));
//
//        item.setMarker(newMarker);
//
//        item1.setMarker(newMarker);
//        items.add(item);
//        items.add(item1);
//        XStream xStream;
//        ItemizedOverlay overlay = new ItemizedIconOverlay(this,items,new ItemizedIconOverlay.OnItemGestureListener() {
//            @Override
//            public boolean onItemSingleTapUp(int index, Object item) {
//                Log.d(tag, "onItemSingleTapUp   index = "+index);
//                return false;
//            }
//
//            @Override
//            public boolean onItemLongPress(int index, Object item) {
//                Log.d(tag, "onItemSingleTapUp");
//                return false;
//            }
//        });
//        mapView.getOverlays().add(overlay);

    }
}

