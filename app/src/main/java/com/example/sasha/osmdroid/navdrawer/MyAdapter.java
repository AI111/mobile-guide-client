package com.example.sasha.osmdroid.navdrawer;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sasha.osmdroid.R;
import com.example.sasha.osmdroid.cash.loader.OnItemClicklistener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sasha on 3/3/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    List<ViewHolder> viewHolders;
    boolean multipleSelection;
    private MenuItem[] items;
    private String name;
    private String url;
    private String email;
    private Context context;
    private OnItemClicklistener onItemClicklistener;

    public MyAdapter(Context context, MenuItem[] items, String Name, String Email, String url, boolean multipleSelection) {
        this.items = items;
        name = Name;
        email = Email;
        this.url = url;
        this.context = context;
        this.multipleSelection = multipleSelection;
        if (!multipleSelection) viewHolders = new ArrayList<>();
    }

    protected int getItemRowRes() {
        return R.layout.item_row;
    }

    public void changeTitle(String Name, String Email, String url) {
        this.name = Name;
        this.email = Email;
        this.url = url;
        this.notifyDataSetChanged();

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(getItemRowRes(), parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType, onItemClicklistener);
            if (!multipleSelection) viewHolders.add(vhItem);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType, onItemClicklistener);

            return vhHeader;

        }
        return null;

    }


    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {

            holder.textView.setText(items[position - 1].stringRes);
            holder.imageView.setImageResource(items[position - 1].imgRes);
        } else {

            if (url != null)
                Picasso.with(context).load(url).transform(new RoundedTransformation(100, 8)).into(holder.profile);
            holder.Name.setText(name);
            holder.email.setText(email);
        }
    }


    @Override
    public int getItemCount() {
        return items.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public void setOnItemClickListener(OnItemClicklistener onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class MenuItem {
        public int stringRes;
        public int imgRes;

        public MenuItem(int stringRes, int imgRes) {
            this.imgRes = imgRes;
            this.stringRes = stringRes;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;
        LinearLayout layout;
        View itemView;
        boolean checked;
        private OnItemClicklistener onItemClicklistener;

        public ViewHolder(View itemView, int ViewType, OnItemClicklistener onItemClicklistener) {
            super(itemView);

            this.itemView = itemView;
            this.onItemClicklistener = onItemClicklistener;

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
                itemView.setOnClickListener(this);
            } else {

                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }
        }
        @Override
        public void onClick(View v) {
            if (!multipleSelection) {
                for (ViewHolder holder : viewHolders) {
                    holder.imageView.setColorFilter(0);
                    holder.itemView.setBackgroundResource(R.color.cardview_light_background);
                }
            }
            if (checked) {
                imageView.setColorFilter(0);
                itemView.setBackgroundResource(R.color.cardview_light_background);
                checked = false;
            } else {
                imageView.setColorFilter(R.color.primary_dark_material_dark);
                itemView.setBackgroundResource(R.color.background_floating_material_light);
                checked = true;
            }
            onItemClicklistener.onClickItem(itemView, v, getPosition());
        }
    }

}
