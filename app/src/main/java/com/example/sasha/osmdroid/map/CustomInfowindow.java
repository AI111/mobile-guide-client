package com.example.sasha.osmdroid.map;

import android.view.View;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
import org.osmdroid.views.MapView;

/**
 * Created by sasha on 12/19/14.
 */

public class CustomInfowindow extends InfoWindow {
    public CustomInfowindow(int layoutResId, MapView mapView) {
         super(layoutResId, mapView);

    }

    @Override
    public void onOpen(Object item)
    {
        TextView textView = (TextView)super.mView.findViewById(R.id.textView);
        textView.setText("qqqqqqqqqqqqq");
        //super.onOpen(item);
    }

    @Override
    public void onClose() {
       // super.onClose();
    }

    @Override
    public View getView() {
        return super.getView();
    }
}
