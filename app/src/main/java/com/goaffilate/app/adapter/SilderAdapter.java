package com.goaffilate.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.BannerActivity;
import com.goaffilate.app.R;
import com.goaffilate.app.model.Sildermodel;
import com.goaffilate.app.utils.BaseURL;

import java.util.List;

public class SilderAdapter extends PagerAdapter {

    private List<Sildermodel> sildermodellist;
      Context context;
    public SilderAdapter(Context context,List<Sildermodel> sildermodellist) {
        this.sildermodellist = sildermodellist;
        this.context=context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_list,container,false);
        ImageView banner=view.findViewById(R.id.imageView);
        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + sildermodellist.get(position).getBanner())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BannerActivity.class);

                intent.putExtra("bannerlink",sildermodellist.get(position).getBannerlink());

                context.startActivity(intent);
            }
        });
        container.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return sildermodellist.size();
    }
}

