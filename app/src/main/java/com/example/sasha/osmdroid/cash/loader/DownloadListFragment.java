package com.example.sasha.osmdroid.cash.loader;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.database.HelperFactory;
import com.example.sasha.osmdroid.mega.Mega;
import com.example.sasha.osmdroid.types.CityGuide;
import com.example.sasha.osmdroid.types.CustomGeoPoint;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by sasha on 12/21/14.
 */

public class DownloadListFragment extends Fragment implements OnItemClicklistener {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CityGuide> guides = new ArrayList<>();
    private TextView errorMsg;
    int id = 1;
    private static String url = "http://192.168.0.102:8080";

    //    public static final CityGuide[] cities = new CityGuide[]{
//
//            new CityGuide("Odessa Ukraine", "The RecyclerView widget is a more advanced and flexible version of ListView. This widget is a container for displaying large data sets that can be scrolled very efficiently by maintaining a limited number of views. Use the RecyclerView widget when you have data collections whose elements change at runtime based on user action or network events."
//                    , "https://lh5.googleusercontent.com/-_KVTFQacp3M/VM6bDTC5TZI/AAAAAAAAEGQ/eTr1wep-MT8/w280-h210-no/IMG_20130822_221423.JPG", (byte) 5, "urlCash","https://mega.co.nz/#!FEk2TIIb!rhlwEhAj-UC6KIsesfeqzqkyl560SbzSEhlvUu2_bEg",new Date()),
//            new CityGuide("Odessa", "description", "https://lh3.googleusercontent.com/-7-3FPBNYa1I/VM6bDaMpWSI/AAAAAAAAEGc/Sq6xqZmzyKc/w280-h210-no/IMG_20140128_165152.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new CityGuide("Odessa", "description", "https://lh4.googleusercontent.com/-0KjaKBQABOw/VM6bDFsNdBI/AAAAAAAAEGU/pYdjBOsWHFU/w280-h210-no/IMG_20140712_065329.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new CityGuide("Odessa", "description", "https://lh3.googleusercontent.com/-bP5EKroOzj8/VM6bDjYgEbI/AAAAAAAAEGM/GGnCYfr9R_c/w280-h210-no/IMG_20140712_083445.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new CityGuide("Odessa", "description", "https://lh3.googleusercontent.com/-NhZN8TI1HaQ/VM6bD-EBbEI/AAAAAAAAEGI/A-_a23ClueE/w280-h210-no/IMG_20140715_175055.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new CityGuide("Odessa", "description", "https://lh5.googleusercontent.com/-KR0PhYddFZs/VM6bEBiYLEI/AAAAAAAAEGA/PzyujSmydbs/w280-h210-no/IMG_20140715_181906.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new CityGuide("Odessa", "description", "https://lh4.googleusercontent.com/--0rVSTj2r-0/VM6bER3WzBI/AAAAAAAAEGE/EjyYoJT8G4g/w280-h210-no/IMG_20140727_175532.jpg" , (byte) 5, "urlCash","mapCash",new Date())
//    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate");
        setHasOptionsMenu(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(MainActivity.LOG_TAG, "onStart");
        new HttpRequestCytiesList().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(MainActivity.LOG_TAG, "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(MainActivity.LOG_TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.getRecycledViewPool().clear();
        mRecyclerView = null;
        mAdapter = null;
        guides.clear();
        guides = null;
        mLayoutManager = null;
        Log.d(MainActivity.LOG_TAG, "onDestroy");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_request:
                new HttpRequestCytiesList().execute();
                break;
            case R.id.set_ip:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                final EditText editText = new EditText(getActivity());
                editText.setText("http://192.168.0.102:8080");
                alertDialog.setView(editText);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                url = editText.getText().toString();
                                new HttpRequestCytiesList().execute();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.city_downloader, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setItemViewCacheSize(6);
        errorMsg = (TextView) rootView.findViewById(R.id.error_msg);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }

        // mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(guides);
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onClickItem(View rootView, View view, final int position) {
        if (view.getId() == R.id.imageView2) {
            Intent intent = new Intent(getActivity(), DetailCityInfoActivity.class);
            intent.putExtra(DetailCityInfoActivity.VIEW_NAME_HEADER_TITLE, guides.get(position).getName());
            intent.putExtra(DetailCityInfoActivity.VIEW_DESCRIPTION, guides.get(position).getDescription());

            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity()
                            , new Pair<View, String>(rootView.findViewById(R.id.imageView2),
                                    DetailCityInfoActivity.VIEW_NAME_HEADER_IMAGE),
                            new Pair<View, String>(rootView.findViewById(R.id.textView2),
                                    DetailCityInfoActivity.VIEW_NAME_HEADER_TITLE),
                            new Pair<View, String>(rootView.findViewById(R.id.textView3),
                                    DetailCityInfoActivity.VIEW_DESCRIPTION));
            ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
        } else if (view.getId() == R.id.button) {
            //  Toast.makeText(getActivity(),"DOWNLOAD"+position+"   "+view.getId(),Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage(getString(R.string.download_data));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new CashDownloader().execute(position);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<CityGuide> citys;
        OnItemClicklistener onItemClicklistener;

        MyAdapter(ArrayList<CityGuide> citys) {
            this.citys = citys;
        }

        public void setOnItemClickListener(OnItemClicklistener onItemClicklistener) {
            this.onItemClicklistener = onItemClicklistener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
            return new MyViewHolder(v, onItemClicklistener);
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int i) {
            viewHolder.name.setText(citys.get(i).getName());
            viewHolder.description.setText(citys.get(i).getDescription());
            viewHolder.rating.setText(citys.get(i).getRating() + " / 5");
            viewHolder.download.setText(citys.get(i).installed ? getString(R.string.delete) : getString(R.string.download));
            Log.d(MainActivity.LOG_TAG, " add URL" + citys.get(i).getImgUrl());

            Picasso.with(getActivity()).load(citys.get(i).getImgUrl()).error(R.drawable.minus).into(viewHolder.image);
        }

        @Override
        public void onViewRecycled(MyViewHolder holder) {
            //((BitmapDrawable)holder.image.getDrawable()).getBitmap().recycle();
            Log.d(MainActivity.LOG_TAG, "onViewRecycled ");
            holder.image.setImageDrawable(null);

        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            Log.d(MainActivity.LOG_TAG, "onDetachedFromRecyclerView ");
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            Log.d(MainActivity.LOG_TAG, "onAttachedToRecyclerView ");
        }

        @Override
        public void onViewAttachedToWindow(MyViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            Log.d(MainActivity.LOG_TAG, "onViewAttachedToWindow ");
        }

        @Override
        public void onViewDetachedFromWindow(MyViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            Log.d(MainActivity.LOG_TAG, "onViewDetachedFromWindow ");
        }

        @Override
        public int getItemCount() {
            return citys.size();
        }

    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button download;
        private TextView name;
        private TextView description;
        private TextView rating;
        private ImageView image;
        OnItemClicklistener onItemClicklistener;
        View itemView;


        public MyViewHolder(View itemView, OnItemClicklistener onItemClicklistener) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.imageView2);
            this.onItemClicklistener = onItemClicklistener;
            name = (TextView) itemView.findViewById(R.id.textView2);
            description = (TextView) itemView.findViewById(R.id.textView3);
            rating = (TextView) itemView.findViewById(R.id.textView4);
            download = (Button) itemView.findViewById(R.id.button);
            download.setOnClickListener(this);
            image.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // if(mItemClickListener!=null)
            Log.d(MainActivity.LOG_TAG, "onClick" + view + " " + getPosition());
            Log.d(MainActivity.LOG_TAG, "Guide " + guides.get(getPosition()));
            // if (onItemClicklistener != null)
            onItemClicklistener.onClickItem(itemView, view, getPosition());

        }
    }

    private class CashDownloader extends AsyncTask<Integer, Integer, Boolean> {
        NotificationManager mNotifyManager;
        Notification.Builder mBuilder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            mBuilder = new Notification.Builder(getActivity());
            mBuilder.setContentTitle("Picture Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.png)
                    .setOngoing(true);
        }

        public void updateList() {

        }

        @Override
        protected Boolean doInBackground(Integer... index) {
            for (int i : index) {
                Mega mega = new Mega();
                mBuilder.setProgress(0, 0, true);
                mNotifyManager.notify(id, mBuilder.build());
                try {
                    Log.d(MainActivity.LOG_TAG, "MEGA LINK " + guides.get(i).getMapCash());
                    mega.download(guides.get(i).getCasMaphUri(), getString(R.string.map_cash_path));
                    //download maps cash from MEGA server
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    CustomGeoPoint[] geoPoints = restTemplate.getForObject(url, CustomGeoPoint[].class);
                    //download data structure
                    for (CustomGeoPoint point : geoPoints) {
                        try {
                            guides.get(i).addPoint(point);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        HelperFactory.getHelper().getCityGuideDAO().create(guides.get(i));
                        guides.get(i).installed = true;
                        mAdapter.notifyItemChanged(i);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //save data structure in database

                    //download audio foto and text for use with structure

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_LONG).show();
                }
            }


            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            super.onPostExecute(aVoid);
            mBuilder.setContentText("Download complete")
                    // Removes the progress bar
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            mNotifyManager.notify(id, mBuilder.build());

        }
    }

    private class HttpRequestCytiesList extends AsyncTask<Void, Void, ArrayList<CityGuide>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ArrayList<CityGuide> doInBackground(Void... params) {
            ArrayList<CityGuide> cityGuides = null;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                Log.v("MainActivity", url);
                cityGuides = new ArrayList<CityGuide>(Arrays.asList(restTemplate.getForObject(url + "/getCities", CityGuide[].class)));

                return cityGuides;
            } catch (Exception e) {
                //Toast.makeText(getActivity(),"Connection errror",Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", e.getMessage(), e);
                Log.v("MainActivity", url);
            }

            for (CityGuide c : cityGuides) {
                try {
                    c.installed = HelperFactory.getHelper().getCityGuideDAO().idExists(c.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return cityGuides;
        }

        @Override
        protected void onPostExecute(ArrayList<CityGuide> cityGuides) {
            if (cityGuides != null) {
                guides.clear();
                guides.addAll(cityGuides);
                mAdapter.notifyDataSetChanged();
                errorMsg.setVisibility(View.GONE);
            } else {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(getString(R.string.no_connection));
            }
        }

    }
}
