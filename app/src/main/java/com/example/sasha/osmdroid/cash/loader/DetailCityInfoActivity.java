package com.example.sasha.osmdroid.cash.loader;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;

/**
 * Created by sasha on 12/22/14.
 */
public class DetailCityInfoActivity extends Activity {
    ImageView imageView;
    TextView name;
    TextView descriptiion;
    TextView rating;
    public static final String EXTRA_PARAM_ID = "detail:_id";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
    public static final String VIEW_DESCRIPTION = "detail:header:description";
    public static final String VIEW_RATING_HEADER_IMAGE = "detail:header:rating";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_city_activity);

        imageView = (ImageView)findViewById(R.id.imageView3);
        name = (TextView)findViewById(R.id.textView9);
        descriptiion = (TextView)findViewById(R.id.textView10);
        rating = (TextView)findViewById(R.id.textView11);
        name.setText(getIntent().getStringExtra(DetailCityInfoActivity.VIEW_NAME_HEADER_TITLE));
        descriptiion.setText(getIntent().getStringExtra(DetailCityInfoActivity.VIEW_DESCRIPTION));
        // BEGIN_INCLUDE(detail_set_view_name)
        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(imageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(name, VIEW_NAME_HEADER_TITLE);
        ViewCompat.setTransitionName(descriptiion, VIEW_DESCRIPTION);
        //ViewCompat.setTransitionName(rating, VIEW_RATING_HEADER_IMAGE);
        // END_INCLUDE(detail_set_view_name)
    }

}
