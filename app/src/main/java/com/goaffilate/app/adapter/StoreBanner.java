package com.goaffilate.app.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.R;
import com.goaffilate.app.model.StoreModel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class StoreBanner extends PagerAdapter {
    List<StoreModel> storeModels;

    public StoreBanner(List<StoreModel> storeModels, Context context) {
        this.storeModels = storeModels;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_list,container,false);
        ImageView banner=view.findViewById(R.id.imageView);
        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + storeModels.get(position).getBanner_image())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(banner);
//        banner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, BannerActivity.class);
//
//                intent.putExtra("bannerlink",storeModels.get(position).getBanner_link());
//
//                context.startActivity(intent);
//            }
//        });
        container.addView(view,0);
        return view;
    }


    @Override
    public int getCount() {
        return storeModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }

}
