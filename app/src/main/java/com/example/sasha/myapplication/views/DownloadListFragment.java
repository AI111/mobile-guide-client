package com.example.sasha.myapplication.views;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.Guide;
import com.example.sasha.myapplication.utils.rest.api.APIUtils;
import com.example.sasha.myapplication.utils.rest.api.AsyncTaskEvent;
import com.example.sasha.myapplication.utils.rest.api.MyAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sasha on 12/21/14.
 */

public class DownloadListFragment extends Fragment implements OnItemClicklistener, SearchView.OnQueryTextListener, AsyncTaskEvent<ArrayList<Guide>> {
    public static String url = "https://nodejs-umap.rhcloud.com";//"http://192.168.0.103:8080";
    protected RecyclerView mRecyclerView;
    protected MyAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Guide> guides = new ArrayList<>();
    MenuItem searchMenuItem;
    SearchView searchView;
    int id = 1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MyAsyncTask<String, Void, ArrayList<Guide>> myAsyncTask;
    private TextView errorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MainActivity.LOG_TAG, "onCreate");
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        searchMenuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void getData() {
        myAsyncTask = APIUtils.getGuides("", getActivity());
        myAsyncTask.setAsyncTaskEvent(this);
        myAsyncTask.execute();
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
//                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                final EditText editText = new EditText(getActivity());
//                editText.setText(url);
//                alertDialog.setView(editText);
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                url = editText.getText().toString();
//                                new HttpRequestCytiesList().execute();
//                            }
//                        });
//                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//                break;

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
            bundle.putString(DetailGuideInfoActivity.TOKEN, MainActivity.token);
            bundle.putSerializable(DetailGuideInfoActivity.SER_KEY, guides.get(position));
            intent.putExtras(bundle);
            ActivityOptionsCompat activityOptions =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                            getActivity()
                            , new Pair<View, String>(rootView.findViewById(R.id.imageView2),
                                    DetailGuideInfoActivity.VIEW_NAME_HEADER_IMAGE)
//                            , new Pair<View, String>(rootView.findViewById(R.id.textView2),
//                                    DetailCityInfoActivity.VIEW_NAME_HEADER_TITLE)
                    );
            ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(MainActivity.LOG_TAG, "onQueryTextSubmit " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(MainActivity.LOG_TAG, "onQueryTextSubmit " + newText);
        return false;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public ArrayList<Guide> onPostResult(ArrayList<Guide> result) {
        if (result != null) {
            guides.clear();
            guides.addAll(result);
            mAdapter.notifyDataSetChanged();
        }
        return result;
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

}
