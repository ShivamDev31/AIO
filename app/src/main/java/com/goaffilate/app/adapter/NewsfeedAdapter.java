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
import com.goaffilate.app.model.newsfeedmodel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.MyViewHolder> {

    private List<newsfeedmodel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description=(TextView) view.findViewById(R.id.description);

        }
    }

    public NewsfeedAdapter(List<newsfeedmodel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public NewsfeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newsfeed, parent, false);

        context = parent.getContext();

        return new NewsfeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsfeedAdapter.MyViewHolder holder, final int position) {
        newsfeedmodel mList = modelList.get(position);
         holder.title.setText(mList.getFeed_heading());
         holder.description.setText(mList.getFeed_description());
       


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
