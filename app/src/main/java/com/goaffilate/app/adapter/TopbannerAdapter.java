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
import com.goaffilate.app.model.TopbannerModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class TopbannerAdapter extends RecyclerView.Adapter<TopbannerAdapter.MyViewHolder> {

    private List<TopbannerModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);

        }
    }

    public TopbannerAdapter(List<TopbannerModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public TopbannerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topbanner, parent, false);

        context = parent.getContext();

        return new TopbannerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopbannerAdapter.MyViewHolder holder, int position) {
        final TopbannerModel mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getNavbar_app_icon())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

               holder.title.setText(mList.getNavbar_app_name());


    }

    @Override
    public int getItemCount() {
        return modelList.size();

    }
}