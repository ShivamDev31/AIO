package com.goaffilate.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationShow extends AppCompatActivity {

    String gettitle,getdes,getimage;
    TextView title,description;
    CircleImageView iv;
    public static String TAG="Notificationshow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification_show);
      title=findViewById(R.id.title);
     description=findViewById(R.id.des);
      iv=findViewById(R.id.iv);


      gettitle=getIntent().getStringExtra("title");

      getdes=getIntent().getStringExtra("des");

      getimage=getIntent().getStringExtra("image");

title.setText(gettitle);

description.setText(getdes);
        Glide.with(this)
                .load(getimage)
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iv);



    }

}
