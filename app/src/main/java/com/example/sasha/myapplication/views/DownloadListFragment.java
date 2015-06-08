package com.example.sasha.myapplication.views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.GeoPoint;
import com.example.sasha.myapplication.database.Guide;
import com.example.sasha.myapplication.database.HelperFactory;
import com.j256.ormlite.table.TableUtils;
import com.squareup.picasso.Picasso;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sasha on 12/21/14.
 */

public class DownloadListFragment extends Fragment implements OnItemClicklistener {
    public static String url = "http://192.168.0.103:8080";
    protected RecyclerView mRecyclerView;
    protected MyAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Guide> guides = new ArrayList<>();
    int id = 1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView errorMsg;

    //    public static final Guide[] cities = new Guide[]{
//
//            new Guide("Odessa Ukraine", "The RecyclerView widget is a more advanced and flexible version of ListView. This widget is a container for displaying large data sets that can be scrolled very efficiently by maintaining a limited number of views. Use the RecyclerView widget when you have data collections whose elements change at runtime based on user action or network events."
//                    , "https://lh5.googleusercontent.com/-_KVTFQacp3M/VM6bDTC5TZI/AAAAAAAAEGQ/eTr1wep-MT8/w280-h210-no/IMG_20130822_221423.JPG", (byte) 5, "urlCash","https://mega.co.nz/#!FEk2TIIb!rhlwEhAj-UC6KIsesfeqzqkyl560SbzSEhlvUu2_bEg",new Date()),
//            new Guide("Odessa", "description", "https://lh3.googleusercontent.com/-7-3FPBNYa1I/VM6bDaMpWSI/AAAAAAAAEGc/Sq6xqZmzyKc/w280-h210-no/IMG_20140128_165152.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new Guide("Odessa", "description", "https://lh4.googleusercontent.com/-0KjaKBQABOw/VM6bDFsNdBI/AAAAAAAAEGU/pYdjBOsWHFU/w280-h210-no/IMG_20140712_065329.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new Guide("Odessa", "description", "https://lh3.googleusercontent.com/-bP5EKroOzj8/VM6bDjYgEbI/AAAAAAAAEGM/GGnCYfr9R_c/w280-h210-no/IMG_20140712_083445.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new Guide("Odessa", "description", "https://lh3.googleusercontent.com/-NhZN8TI1HaQ/VM6bD-EBbEI/AAAAAAAAEGI/A-_a23ClueE/w280-h210-no/IMG_20140715_175055.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new Guide("Odessa", "description", "https://lh5.googleusercontent.com/-KR0PhYddFZs/VM6bEBiYLEI/AAAAAAAAEGA/PzyujSmydbs/w280-h210-no/IMG_20140715_181906.jpg", (byte) 5, "urlCash","mapCash",new Date()),
//            new Guide("Odessa", "description", "https://lh4.googleusercontent.com/--0rVSTj2r-0/VM6bER3WzBI/AAAAAAAAEGE/EjyYoJT8G4g/w280-h210-no/IMG_20140727_175532.jpg" , (byte) 5, "urlCash","mapCash",new Date())
//    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate");
        setHasOptionsMenu(true);

    }

    protected void getData() {
        new HttpRequestCytiesList().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(MainActivity.LOG_TAG, "onStart");
        getData();
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

        int id = item.getItemId();
        switch (item.getItemId()) {

            case R.id.set_ip:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                final EditText editText = new EditText(getActivity());
                editText.setText(url);
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
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), Guide.class);
                    TableUtils.clearTable(HelperFactory.getHelper().getConnectionSource(), GeoPoint.class);
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
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_guids_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
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
        mAdapter = new MyAdapter(guides);
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onClickItem(View rootView, View view, final int position) {
        if (view.getId() == R.id.imageView2) {
            Intent intent = new Intent(getActivity(), DetailGuideInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailCityInfoActivity.SER_KEY, guides.get(position));
            intent.putExtras(bundle);
            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity()
                            , new Pair<View, String>(rootView.findViewById(R.id.imageView2),
                                    DetailCityInfoActivity.VIEW_NAME_HEADER_IMAGE)
//                            , new Pair<View, String>(rootView.findViewById(R.id.textView2),
//                                    DetailCityInfoActivity.VIEW_NAME_HEADER_TITLE)
                    );
            ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
        }
    }


    protected class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<Guide> citys;
        OnItemClicklistener onItemClicklistener;

        MyAdapter(ArrayList<Guide> citys) {
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
            viewHolder.ratingBar.setRating(citys.get(i).getRating());
            viewHolder.installed.setVisibility(citys.get(i).installed ? View.VISIBLE : View.GONE);
            Log.d(MainActivity.LOG_TAG, " onBindViewHolder URL  " + citys.get(i).installed);
            Picasso.with(getActivity()).load(citys.get(i).getImgUrl()).into(viewHolder.image);
        }

        @Override
        public void onViewRecycled(MyViewHolder holder) {
            Log.d(MainActivity.LOG_TAG, "onViewRecycled ");
//            ((BitmapDrawable)holder.image.getDrawable()).getBitmap().recycle();
//            holder.image.setImageDrawable(null);

        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            Log.d(MainActivity.LOG_TAG, "onDetachedFromRecyclerView ");
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            // Log.d(MainActivity.LOG_TAG, "onAttachedToRecyclerView ");
        }

        @Override
        public void onViewAttachedToWindow(MyViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            // Log.d(MainActivity.LOG_TAG, "onViewAttachedToWindow ");
        }

        @Override
        public void onViewDetachedFromWindow(MyViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            // Log.d(MainActivity.LOG_TAG, "onViewDetachedFromWindow ");
        }

        @Override
        public int getItemCount() {
            return citys.size();
        }

    }

    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClicklistener onItemClicklistener;
        View itemView;
        private TextView name;
        private TextView description;
        private RatingBar ratingBar;
        private ImageView image;
        private ImageView installed;


        public MyViewHolder(View itemView, OnItemClicklistener onItemClicklistener) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.imageView2);
            installed = (ImageView) itemView.findViewById(R.id.imageView4);
            this.onItemClicklistener = onItemClicklistener;
            name = (TextView) itemView.findViewById(R.id.textView2);
            description = (TextView) itemView.findViewById(R.id.textView3);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            image.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemClicklistener.onClickItem(itemView, view, getPosition());

        }
    }

    private class HttpRequestCytiesList extends AsyncTask<Void, Void, ArrayList<Guide>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ArrayList<Guide> doInBackground(Void... params) {
            Log.v(MainActivity.LOG_TAG, "doInBackground");
            ArrayList<Guide> cityGuides = null;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            try {
                cityGuides = new ArrayList<Guide>(Arrays.asList(restTemplate.getForObject(url + "/guides", Guide[].class)));
                Log.d(MainActivity.LOG_TAG, cityGuides.toString());
            } catch (Exception e) {
                //Toast.makeText(getActivity(),"Connection errror",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return null;
            }
            for (Guide c : cityGuides) {
//                try {
//                  //  c.installed = HelperFactory.getHelper().getGuideDAO().idExists(c.getId());
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
            }
            return cityGuides;
        }

        @Override
        protected void onPostExecute(ArrayList<Guide> cityGuides) {

            if (cityGuides != null) {
                guides.clear();
                guides.addAll(cityGuides);
                mAdapter.notifyDataSetChanged();
//                errorMsg.setVisibility(View.GONE);
            } else {
//                errorMsg.setVisibility(View.VISIBLE);
                //               errorMsg.setText(getString(R.string.no_connection));
            }
        }

    }
}
