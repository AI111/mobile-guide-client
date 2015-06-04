package com.example.sasha.osmdroid.views.loader;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.database.HelperFactory;
import com.example.sasha.osmdroid.mega.Mega;
import com.example.sasha.osmdroid.types.GeoPoint;
import com.example.sasha.osmdroid.types.Guide;
import com.j256.ormlite.table.TableUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.Picasso;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;

/**
 * Created by sasha on 12/22/14.
 */
public class DetailCityInfoActivity2 extends AppCompatActivity implements View.OnClickListener {
    public final static String SER_KEY = "com.example.sasha.osmdroid.types.ser";
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
    public final static int STATUS_FINISH = 200;
    public final static String BROADCAST_ACTION = "com.example.sasha.osmdroid";
    public static final String FINISH = "finish";
    ProgressBar progressBar;
    ImageView imageView;
    TextView name;
    TextView descriptiionView;
    ActionBar mActionBar;
    Guide guide;
    private int maxDist, minDist = 0, DX;
    private FloatingActionButton mFab;
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            int status = intent.getIntExtra(FINISH, 0);
            Log.d(MainActivity.LOG_TAG, "onReceive " + STATUS_FINISH);

            if (status == STATUS_FINISH) {
                Log.d(MainActivity.LOG_TAG, "onReceive " + STATUS_FINISH);
                progressBar.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
                mFab.setImageResource(R.drawable.ic_delete_black_24dp);
                mFab.setVisibility(View.VISIBLE);
            }
        }
    };
    private boolean mFabIsShown = true;
    private int VER_SDK;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);

        registerReceiver(br, intFilt);
        guide = (Guide) getIntent().getSerializableExtra(SER_KEY);
        imageView = (ImageView) findViewById(R.id.backdrop);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //name = (TextView) findViewById(R.id.textView9);
        descriptiionView = (TextView) findViewById(R.id.description);
        // if (guide.installed) mFab.setImageResource(R.drawable.ic_delete_black_24dp);
        descriptiionView.setText(guide.getDescription());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(guide.getName());


        ViewCompat.setTransitionName(imageView, VIEW_NAME_HEADER_IMAGE);
        //ViewCompat.setTransitionName(name, VIEW_NAME_HEADER_TITLE);
        loadItem();

//

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.LOG_TAG, "onDestroy DetailActivityt");
        unregisterReceiver(br);
//        Picasso.with(this).cancelRequest(imageView);
//        ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
//        imageView.setImageDrawable(null);

//        imageView=null;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void showFab(boolean animated) {
        if (mFab == null) {
            return;
        }
        if (!mFabIsShown) {
            if (animated) {
                ViewPropertyAnimator.animate(mFab).cancel();
                ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            } else {
                mFab.setScaleX(1);
                mFab.setScaleY(1);
            }
            mFabIsShown = true;
        } else {
            // Ensure that FAB is shown
            mFab.setScaleX(1);
            mFab.setScaleY(1);
        }
    }

    private void hideFab(boolean animated) {
        if (mFab == null) {
            return;
        }
        if (mFabIsShown) {
            if (animated) {
                ViewPropertyAnimator.animate(mFab).cancel();
                ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            } else {
                mFab.setScaleX(0);
                mFab.setScaleY(0);
            }
            mFabIsShown = false;
        } else {
            // Ensure that FAB is hidden
            mFab.setScaleX(0);
            mFab.setScaleY(0);
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

        int id = item.getItemId();

        switch (id) {

            case R.id.clear_db:
                try {
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Guide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.show_db:
                try {
                    Log.d(MainActivity.LOG_TAG, "\n DB = " + HelperFactory.getHelper().getGuideDAO().getAllCities() + "\n----------\n" + HelperFactory.getHelper().getGeoPointDAO().getAllPoints());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.update_db:
                try {
                    TableUtils.dropTable(HelperFactory.getHelper().getConnectionSource(), Guide.class, true);
                    TableUtils.dropTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class, true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), Guide.class);
                    TableUtils.createTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            hideFab(false);
        super.onBackPressed();
    }

    private void loadItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {

            loadThumbnail();
        } else {

            loadFullSizeImage();
            showFab(true);
        }
    }

    private void loadThumbnail() {
        Picasso.with(imageView.getContext())
                .load(guide.getImgUrl())
                .noFade()
                .into(imageView);
    }


    private void loadFullSizeImage() {
        Picasso.with(imageView.getContext())
                .load(guide.getFullImgUrl())
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
                    showFab(true);

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

    @Override
    public void onClick(View view) {
        Log.d(MainActivity.LOG_TAG, "FAB OnClick");
        switch (view.getId()) {
            case R.id.fab:
                String path;
                if (guide.installed) {
                    new CashRemover().execute(guide);
                } else {

                    Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DetailCityInfoActivity2.SER_KEY, guide);
                    intent.putExtras(bundle);
                    startService(intent);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    mFab.setVisibility(View.GONE);
                    //new CashDownloader().execute(guide);
                }
                break;
        }

    }

    private class CashDownloader extends AsyncTask<Guide, String, Boolean> {
        NotificationManager mNotifyManager;
        Notification.Builder mBuilder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNotifyManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            mBuilder = new Notification.Builder(getApplicationContext());
            mBuilder.setContentTitle("Picture Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.png)
                    .setOngoing(true);
        }

        public void updateList() {

        }

        @Override
        protected Boolean doInBackground(Guide... index) {
            Log.v(MainActivity.LOG_TAG, "doInBackground" + index[0]);
            Log.v(MainActivity.LOG_TAG, "doInBackground");
            for (Guide city : index) {
                Mega mega = new Mega();
                mBuilder.setProgress(0, 0, true);
                mNotifyManager.notify(id, mBuilder.build());
                try {
                    Log.d(MainActivity.LOG_TAG, "MEGA LINK " + city.getMapCash());
                    // mega.download(city.getCasMaphUri(), getString(R.string.map_cash_path));
                    //download maps cash from MEGA server
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    GeoPoint[] geoPoints = restTemplate.getForObject(DownloadListFragment.url + "getPoints?id=2" + city.getId(), GeoPoint[].class);
                    //download data structure
                    for (GeoPoint point : geoPoints) {
                        city.addPoint(point);
                    }

                    HelperFactory.getHelper().getGuideDAO().create(city);
                    city.installed = true;

                    //save data structure in database

                    //download audio foto and text for use with structure
//
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                } catch (InvalidAlgorithmParameterException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean complete) {
            super.onPostExecute(complete);
            if (complete) {
                mBuilder.setContentText("Download complete")
                        // Removes the progress bar
                        .setProgress(0, 0, false)
                        .setOngoing(false);
                mFab.setImageResource(R.drawable.ic_delete_black_24dp);
            } else {
                mBuilder.setContentText("Download error")
                        // Removes the progress bar
                        .setProgress(0, 0, false)
                        .setOngoing(false);
            }

            mNotifyManager.notify(id, mBuilder.build());

        }
    }

    private class CashRemover extends AsyncTask<Guide, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Guide... index) {
            for (Guide city : index) {

                try {

                    if (city.points.size() == 0) {
                        city = HelperFactory.getHelper().getGuideDAO().queryForId(city.getId());
                        //city.points=HelperFactory.getHelper().getGeoPointDAO().queryForEq("cityGuide_id",city.getId());
                    }
                    city.points.clear();
                    HelperFactory.getHelper().getGuideDAO().delete(city);
                    city.installed = false;


                } catch (SQLException e) {
                    e.printStackTrace();
                    // Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean complete) {
            super.onPostExecute(complete);
            mFab.setImageResource(R.drawable.ic_play_download_black_24dp);


        }
    }
}