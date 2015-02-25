package com.example.sasha.osmdroid.cash.loader;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.types.CityGuide;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public static ArrayList<CityGuide> guides;
    // = new ArrayList<>(Arrays.asList(
//            new CityGuide[]{new CityGuide("Odessa Ukraine", "The RecyclerView widget is a more advanced and flexible version of ListView. This widget is a container for displaying large data sets that can be scrolled very efficiently by maintaining a limited number of views. Use the RecyclerView widget when you have data collections whose elements change at runtime based on user action or network events."
//                    , "https://lh6.googleusercontent.com/-hWrHTKA8dsg/UhdiOTmhNwI/AAAAAAAAA40/jCx8Hvd-3FU/w1203-h902-no/IMG_20130822_221423.JPG", (byte) 5, "urlCash","https://mega.co.nz/#!FEk2TIIb!rhlwEhAj-UC6KIsesfeqzqkyl560SbzSEhlvUu2_bEg"),
//            new CityGuide("Odessa", "description", "https://lh5.googleusercontent.com/-hh-mJKr5C3E/U9Uuk4fsKhI/AAAAAAAADck/QaI_s6KbGrE/w1145-h859-no/IMG_20140727_175448.jpg", (byte) 5, "urlCash","mapCash"),
//            new CityGuide("Odessa", "description", "https://lh6.googleusercontent.com/-aHWnoKUODx8/U8erOLuga8I/AAAAAAAADE8/MsjHp6ASQsI/w1203-h902-no/IMG_20140714_192145.jpg", (byte) 5, "urlCash","mapCash"),
//            new CityGuide("Odessa", "description", "https://lh6.googleusercontent.com/--gpgKdawen4/U8errG36YFI/AAAAAAAADIk/x8MMcVXBJ7Q/w1203-h902-no/IMG_20140715_174957.jpg", (byte) 5, "urlCash","mapCash"),
//            new CityGuide("Odessa", "description", "https://lh6.googleusercontent.com/-Qwqhx0tlkoA/U8EgdZaBRcI/AAAAAAAAC3Y/UDbwxISQvNA/w1203-h902-no/IMG_20140712_083445.jpg", (byte) 5, "urlCash","mapCash"),
//            new CityGuide("Odessa", "description", "https://lh4.googleusercontent.com/-azMH99iPn54/U8EgSHUKp_I/AAAAAAAAC2o/dJSlhP1QDu4/w1203-h902-no/IMG_20140712_065329.jpg", (byte) 5, "urlCash","mapCash"),
//            new CityGuide("Odessa", "description", "https://lh3.googleusercontent.com/-pxkqr2fj8cA/UhkdBjNtHuI/AAAAAAAAA7Y/5eh40FcgcGk/w1203-h902-no/IMG_20130823_184120.jpg", (byte) 5, "urlCash","mapCash")}));
    public static final String LOG_TAG = "OSM_DROID_TAG";
    public static DownloadListFragment listFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        final ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.primary));
        mActionBar.setBackgroundDrawable(cd);

        //testFragment = new TestFragment();
        listFragment = new DownloadListFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment)
                    .commit();
        }
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

                Log.d(LOG_TAG, "item_clear_memory_cache");
                return true;
            case R.id.item_clear_disc_cache:

                return true;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
