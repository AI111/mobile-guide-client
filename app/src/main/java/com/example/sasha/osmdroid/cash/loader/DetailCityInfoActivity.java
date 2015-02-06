package com.example.sasha.osmdroid.cash.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;
import com.squareup.picasso.Picasso;

/**
 * Created by sasha on 12/22/14.
 */
public class DetailCityInfoActivity extends ActionBarActivity {
    ImageView imageView;
    TextView name;
    TextView descriptiionView;
    TextView rating;
    ActionBar mActionBar;

    public static final String EXTRA_PARAM_ID = "detail:_id";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
    public static final String VIEW_DESCRIPTION = "detail:header:description";
    public static final String VIEW_SMALL_IMAGE = "detail:header:smImg";
    public static final String VIEW_IMAGE = "detail:header:image";
    String img, smallImg, description, title;
    private int maxDist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_city_activity);
        img = getIntent().getStringExtra(VIEW_IMAGE);
        description = getIntent().getStringExtra(VIEW_DESCRIPTION);
        title = getIntent().getStringExtra(VIEW_NAME_HEADER_TITLE);
        smallImg = getIntent().getStringExtra(VIEW_SMALL_IMAGE);

        imageView = (ImageView) findViewById(R.id.imageView3);

        name = (TextView) findViewById(R.id.textView9);
        descriptiionView = (TextView) findViewById(R.id.textView10);

        name.setText(title);
        descriptiionView.setText(description);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);


//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black);
//        final BitmapDrawable bd = new BitmapDrawable(bitmap);
        final ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.primary));

        mActionBar.setBackgroundDrawable(cd);

        cd.setAlpha(0);

        mActionBar.setDisplayHomeAsUpEnabled(true); //to activate back pressed on home button press
        mActionBar.setDisplayShowHomeEnabled(false); //

       // Picasso.with(this).load(img).into(imageView);
        // BEGIN_INCLUDE(detail_set_view_name)
        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(imageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(name, VIEW_NAME_HEADER_TITLE);
        //ViewCompat.setTransitionName(descriptiionView, VIEW_DESCRIPTION);
        //ViewCompat.setTransitionName(rating, VIEW_RATING_HEADER_IMAGE);
        // END_INCLUDE(detail_set_view_name)
        loadItem();
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                maxDist = imageView.getMeasuredHeight();
                int finalWidth = imageView.getMeasuredWidth();
                Log.d(MainActivity.LOG_TAG,"Height: " + maxDist + " Width: " + finalWidth);
                return true;
            }
        });
        ScrollViewX scrollView = (ScrollViewX) findViewById(R.id.scrollView);
        scrollView.setOnScrollViewListener(new ScrollViewX.OnScrollViewListener() {

            @Override
            public void onScrollChanged(ScrollViewX v, int l, int t, int oldl, int oldt) {

                cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
            }

            private int getAlphaforActionBar(int scrollY) {
                Log.d(MainActivity.LOG_TAG,"T = "+scrollY+" H = "+maxDist);
                int minDist = 0;
                if(scrollY>maxDist){
                    return 255;
                }
                else if(scrollY<minDist){
                    return 0;
                }
                else {
                    int alpha = 0;
                    alpha = (int)  ((255.0/maxDist)*scrollY);
                    imageView.setTranslationY(scrollY/2);
                    return alpha;
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.list_item:
                break;
            case R.id.item_clear_memory_cache:

                Log.d(MainActivity.LOG_TAG, "item_clear_memory_cache");
                return true;
            case R.id.item_clear_disc_cache:

                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }


        return super.onOptionsItemSelected(item);
    }
    private void loadItem() {
        // Set the title TextView to the item's name and author
      //  mHeaderTitle.setText(getString(R.string.image_header, mItem.getName(), mItem.getAuthor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {

            loadThumbnail();
        } else {

            loadFullSizeImage();
        }
    }
    private void loadThumbnail() {
        Picasso.with(imageView.getContext())
                .load(smallImg)
                .noFade()
                .into(imageView);
    }


    private void loadFullSizeImage() {
        Picasso.with(imageView.getContext())
                .load(img)
                .noFade()
                .noPlaceholder()
                .into(imageView);
     }
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }
        return false;
    }
}