package com.goaffilate.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class CategoryPopup extends AppCompatActivity {
    ImageView imageView;
    private static final String TAG = "PopupWindow";
    String link, appname,image;
    private static final int CREATE_SHORTCUT = 99;
    ImageView iconapp;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);
        link = getIntent().getStringExtra("categorylink");
        iconapp=findViewById(R.id.logo_sort);

        title=findViewById(R.id.title);

image=getIntent().getStringExtra("cat_image");
        appname = getIntent().getStringExtra("catname");
        imageView = findViewById(R.id.home);

        title.setText(appname);
        Log.d("catim",image);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iconapp);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(CategoryPopup.this, CreateShortcutcat.class);
                i.putExtra("catname", appname);
                i.putExtra("cat_image",image);
                i.putExtra("categorylink", link);

                startActivityForResult(i, CREATE_SHORTCUT);
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        int h = metrics.heightPixels;
        int w = metrics.widthPixels;

        getWindow().setLayout((int) (w * .9), (int) (h * .3));

        WindowManager.LayoutParams params = getWindow().getAttributes();


//        params.alpha= (float) .6;

        params.gravity = Gravity.BOTTOM;
        params.x = 10;
        params.y = -10;
        getWindow().setAttributes(params);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CREATE_SHORTCUT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Log.d(TAG, "CreateShortcut result:" + result);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}