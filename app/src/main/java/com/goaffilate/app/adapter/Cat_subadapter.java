package com.goaffilate.app.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.CategoryPopup;
import com.goaffilate.app.CategoryWebview;
import com.goaffilate.app.R;
import com.goaffilate.app.model.cat_submodel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class Cat_subadapter extends RecyclerView.Adapter<Cat_subadapter.MyViewHolder> {

    private List<cat_submodel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        Button  button;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
            button= view.findViewById(R.id.add_home_button1);
        }
    }

    public Cat_subadapter(List<cat_submodel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Cat_subadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_app, parent, false);

        context = parent.getContext();

        return new Cat_subadapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Cat_subadapter.MyViewHolder holder, int position) {
        final cat_submodel mList = modelList.get(position);
//        Picasso.with(context).load(BaseURL.IMG_CATEGORY_URL + mList.getCategory_icon()).into(holder.image);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getApp_logo())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CategoryWebview.class);
                intent.putExtra("categorylink",mList.getApp_link());
                context.startActivity(intent);
            }
        });
        holder.title.setText(mList.getApp_name());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenti=new Intent(context, CategoryPopup.class);
                intenti.putExtra("cat_image",BaseURL.IMG_CATEGORY_URL+mList.getApp_logo());
                intenti.putExtra("catname",mList.getApp_name());
                intenti.putExtra("categorylink",mList.getApp_link());
                context.startActivity(intenti);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();

    }
}