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
import com.goaffilate.app.model.ScratchModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class ScratchAdapter extends RecyclerView.Adapter<ScratchAdapter.MyViewHolder> {

    private List<ScratchModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.iv_home_img);
        }
    }

    public ScratchAdapter(List<ScratchModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ScratchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scratch_kay, parent, false);

        context = parent.getContext();

        return new ScratchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScratchAdapter.MyViewHolder holder, int position) {
        final ScratchModel mList = modelList.get(position);
//        Picasso.with(context).load(BaseURL.IMG_CATEGORY_URL + mList.getCategory_icon()).into(holder.image);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getScratch_card_image())
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