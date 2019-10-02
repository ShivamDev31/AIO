package com.goaffilate.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.R;
import com.goaffilate.app.model.StoreModelrc;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

    private List<StoreModelrc> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,address,phone,distance;
        public ImageView storeimage,phimagee;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            storeimage = (ImageView) view.findViewById(R.id.storeimage);
            address=view.findViewById(R.id.address);
            phone=view.findViewById(R.id.ph);
            distance=view.findViewById(R.id.distance);
            phimagee=view.findViewById(R.id.phim);
        }
    }

    public StoreAdapter(List<StoreModelrc> modelList) {
        this.modelList = modelList;
    }

    @Override
    public StoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storerecylay, parent, false);

        context = parent.getContext();

        return new StoreAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StoreAdapter.MyViewHolder holder, int position) {
        final StoreModelrc mList = modelList.get(position);


        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getStore_images())
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.storeimage);

        holder.phone.setText(mList.getStore_phone());
        holder.address.setText(mList.getStore_society());
        holder.name.setText(mList.getStore_name());
        holder.distance.setText(mList.getLocation());
//
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

