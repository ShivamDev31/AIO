package com.goaffilate.app.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.CategoryActivity;
import com.goaffilate.app.R;
import com.goaffilate.app.model.CategoryModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<CategoryModel> modelList;
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

    public CategoryAdapter(List<CategoryModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_top_app, parent, false);

        context = parent.getContext();

        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, int position) {
        final CategoryModel mList = modelList.get(position);
//        Picasso.with(context).load(BaseURL.IMG_CATEGORY_URL + mList.getCategory_icon()).into(holder.image);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getCategory_icon())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CategoryActivity.class);
                intent.putExtra("category",mList.getCategory_name());
                context.startActivity(intent);
            }
        });

        holder.title.setText(mList.getCategory_name());
    }

    @Override
    public int getItemCount() {
        return modelList.size();

    }
}
