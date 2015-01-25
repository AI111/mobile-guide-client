package com.example.sasha.osmdroid.cash.loader;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.mega.Mega;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by sasha on 12/21/14.
 */
public class DownloadListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CityGuide> guides;
    private DisplayImageOptions options;



    int id = 1;
    public ArrayList<CityGuide> getGuides() {
        return guides;
    }

    public void setGuides(ArrayList<CityGuide> guides) {
        this.guides = guides;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate");
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.plus)
                .showImageForEmptyUri(R.drawable.png)
                .showImageOnFail(R.drawable.minus)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())

                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(MainActivity.LOG_TAG, "onStart");

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
        Log.d(MainActivity.LOG_TAG, "onDestroy");
    }
//    public void notification(){
//        final NotificationManager mNotifyManager =
//                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//      final  Notification.Builder mBuilder= new Notification.Builder(getActivity());
//         mBuilder.setContentTitle("Picture Download")
//                .setContentText("Download in progress")
//                .setSmallIcon(R.drawable.png);
//// Start a lengthy operation in a background thread
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        int incr;
//                        // Do the "lengthy" operation 20 times
//                        for (incr = 0; incr <= 100; incr+=5) {
//                            // Sets the progress indicator to a max value, the
//                            // current completion percentage, and "determinate"
//                            // state
//                            mBuilder.setProgress(100, incr, false);
//                            // Displays the progress bar for the first time.
//                            mNotifyManager.notify(id, mBuilder.build());
//                            // Sleeps the thread, simulating an operation
//                            // that takes time
//                            try {
//                                // Sleep for 5 seconds
//                                Thread.sleep(5*1000);
//                            } catch (InterruptedException e) {
//                                Log.d(MainActivity.LOG_TAG, "sleep failure");
//                            }
//                        }
//                        // When the loop is finished, updates the notification
//                        mBuilder.setContentText("Download complete")
//                                // Removes the progress bar
//                                .setProgress(0,0,false);
//                        mNotifyManager.notify(id, mBuilder.build());
//                    }
//                }
//// Starts the thread by calling the run() method in its Runnable
//        ).start();
//    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.city_downloader, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // detailCityInfoFragment = new DetailCityInfoFragment();
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        // mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(itemAnimator);
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(guides);
        mAdapter.setOnItemClickListener(new OnItemClicklistener() {
            @Override
            public void onClickItem(View rootView, View view, final int position) {
                if (view.getId() == R.id.imageView2) {
                    Intent intent = new Intent(getActivity(), DetailCityInfoActivity.class);
                    intent.putExtra(DetailCityInfoActivity.VIEW_NAME_HEADER_TITLE, MainActivity.guides.get(position).getName());
                    intent.putExtra(DetailCityInfoActivity.VIEW_DESCRIPTION, MainActivity.guides.get(position).getDescription());

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
                                  new CashDownloader().execute(guides.get(position));
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

        });

        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<CityGuide> citys;
        OnItemClicklistener onItemClicklistener;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        MyAdapter(List<CityGuide> citys) {
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
            Log.d(MainActivity.LOG_TAG, " add URL" + citys.get(i).getImgUrl());
            ImageLoader.getInstance().displayImage(citys.get(i).getImgUrl(), viewHolder.image, options, animateFirstListener);

        }

        @Override
        public int getItemCount() {
            return citys.size();
        }

    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public interface OnItemClicklistener {
        public void onClickItem(View rootView, View view, int position);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button download;
        private TextView name;
        private TextView description;
        private TextView rating;
        private ImageView image;
        OnItemClicklistener onItemClicklistener;
        View itemView;
//        private ImageView icon;

        public MyViewHolder(View itemView, OnItemClicklistener onItemClicklistener) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.imageView2);
            this.onItemClicklistener = onItemClicklistener;
            name = (TextView) itemView.findViewById(R.id.textView2);
            description = (TextView) itemView.findViewById(R.id.textView3);
            rating = (TextView) itemView.findViewById(R.id.textView4);
            download = (Button) itemView.findViewById(R.id.button);
            download.setText(getActivity().getText(R.string.download));
            download.setOnClickListener(this);
            image.setOnClickListener(this);
//            icon = (ImageView) itemView.findViewById(R.id.recyclerViewItemIcon);
        }

        @Override
        public void onClick(View view) {
            // if(mItemClickListener!=null)
            if (onItemClicklistener != null)
                onItemClicklistener.onClickItem(itemView, view, getPosition());

        }
    }
    private class CashDownloader extends AsyncTask<CityGuide ,Integer,Boolean>{
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

        @Override
        protected Boolean doInBackground(CityGuide... citys) {
            for(CityGuide city : citys){
                Mega mega = new Mega();
                mBuilder.setProgress(0,0,true);
                mNotifyManager.notify(id,mBuilder.build());
                try {
                    mega.download(city.getMapCashUrl(),getString(R.string.map_cash_path));


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
                    .setProgress(0,0,false)
                    .setOngoing(false);
            mNotifyManager.notify(id, mBuilder.build());

        }
    }
}
