package com.goaffilate.app.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.R;
import com.goaffilate.app.model.TopModels;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.MyViewHolder> {

    private List<TopModels> modelList;
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

    public TopAdapter(List<TopModels> modelList) {
        this.modelList = modelList;
    }

    @Override
    public TopAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_top_app, parent, false);

        context = parent.getContext();

        return new TopAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopAdapter.MyViewHolder holder, int position) {
        final TopModels mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getApp_logo())
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, CategoryWebview.class);
//                intent.putExtra("categorylink",mList.getApp_link());
//                context.startActivity(intent);
//            }
//        });

        holder.title.setText(mList.getApp_name());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
