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
import com.goaffilate.app.model.RewardsModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> {

    private List<RewardsModel> modelList;
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

    public RewardsAdapter(List<RewardsModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public RewardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rewards, parent, false);

        context = parent.getContext();

        return new RewardsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RewardsAdapter.MyViewHolder holder, int position) {
        final RewardsModel mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getScratch_card_image())
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

        holder.title.setText(mList.getEarning());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
