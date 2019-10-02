package com.goaffilate.app.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.R;
import com.goaffilate.app.model.StoreshowModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class StoreshowAdapter extends RecyclerView.Adapter<StoreshowAdapter.MyViewHolder>{

private List<StoreshowModel> modelList;

private Context context;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;

    public MyViewHolder(View view) {
        super(view);

        image = (ImageView) view.findViewById(R.id.iv);
    }

}

    public StoreshowAdapter(List<StoreshowModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public StoreshowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storeshowimage, parent, false);

        context = parent.getContext();

        return new StoreshowAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StoreshowAdapter.MyViewHolder holder, final int position) {
        StoreshowModel mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}