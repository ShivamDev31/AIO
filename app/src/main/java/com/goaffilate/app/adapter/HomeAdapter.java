package com.goaffilate.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.PopUpWindow;
import com.goaffilate.app.R;
import com.goaffilate.app.ShoppingActivity;
import com.goaffilate.app.model.PartnerModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<PartnerModel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        Button button;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
            button =(Button)view.findViewById( R.id.add_home_button1);
        }
    }

    public HomeAdapter(List<PartnerModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv, parent, false);

        context = parent.getContext();

        return new HomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {
        PartnerModel mList = modelList.get(position);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopUpWindow.class);
                intent.putExtra("cat_image",BaseURL.IMG_CATEGORY_URL+mList.getGroup_image());
                intent.putExtra("catname",mList.getGroup_name());
                intent.putExtra("id",mList.getId());
                context.startActivity(intent);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ShoppingActivity.class);
                intent.putExtra("category_name",modelList.get(position).getGroup_name());
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getGroup_image())
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



        holder.title.setText(mList.getGroup_name());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}