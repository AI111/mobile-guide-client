package com.example.sasha.osmdroid.map;

import android.content.Context;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * Created by sasha on 3/7/15.
 */
public class MyCustomNewLocationOverlay extends MyLocationNewOverlay {
    private Context mContext;
    private float mOrientation;

    public MyCustomNewLocationOverlay(Context context, MapView mapView) {
        super(context, mapView);
        this.mContext = context;
    }


//        Point screenPts = mapView.getProjection().toPixels(myLocation, null);
//
//        // create a rotated copy of the marker
//        Bitmap arrowBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_navigation_black_24dp);
//        Matrix matrix = new Matrix();
//        matrix.postRotate(mOrientation);
//        Bitmap rotatedBmp = Bitmap.createBitmap(
//                arrowBitmap,
//                0, 0,
//                arrowBitmap.getWidth(),
//                arrowBitmap.getHeight(),
//                matrix,
//                true
//        );
//        // add the rotated marker to the canvas
//        canvas.drawBitmap(
//                rotatedBmp,
//                screenPts.x - (rotatedBmp.getWidth() / 2),
//                screenPts.y - (rotatedBmp.getHeight() / 2),
//                null
//        );


}
