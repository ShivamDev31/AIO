package com.goaffilate.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.Fragment.FragmentMyRewards;
import com.goaffilate.app.Fragment.FragmentSetting;
import com.goaffilate.app.Fragment.FragmentStore;
import com.goaffilate.app.Fragment.FragmentWelcomeScreen;
import com.goaffilate.app.Fragment.Fragmnet_Home;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.Session_management;
import com.google.android.gms.ads.MobileAds;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  implements Animation.AnimationListener {


    TextView home_text,more_text,store_text,rewards_text;
     public static MainActivity activity;
    LocationManager locationManager;

     public  static Toolbar toolbar;
    String latitude,longitude;

     SharedPreferences sharedPreferences;

     LinearLayout rl_home,rl_rewards,rl_more,rl_apps;

     SharedPreferences.Editor editor;
     String userimgae;
     ImageView notify;

     Session_management session_management;

     Button store;

     private static final String TAG = "MainActivity";

     Button home,rewards,setting;
     SharedPreferences.Editor loceditor;
     CircleImageView apps,userimageprofile;

//     Animation zoom;

    public static String userid;

    String homev="false";
    SharedPreferences locationpre;

    String rewardsv="false";

    String appsv="false";

    String storev="false";
    String morev="false";

    TextView tv;

    int padding = 0;
    public static ImageView gifImageView;
// public static   com.goaffilate.app.utils.GifImageView gifImageView;
     String username;
RelativeLayout rl_stores;
    TextView  title;

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(newBase);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        Fragment fm = new Fragmnet_Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contentPanel, fm, "Home_fragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        rl_stores=findViewById(R.id.rl_stores);
//        zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
//        zoom.setAnimationListener(this);
         activity=MainActivity.this;
        session_management=new Session_management(this);

        MobileAds.initialize(MainActivity.this, getString(R.string.admob_app_id));

//      locationpre=getSharedPreferences("location",MODE_PRIVATE);

//       loceditor=locationpre.edit();
        home_text=findViewById(R.id.home_text);
        rewards_text=findViewById(R.id.reward_text);
        store_text=findViewById(R.id.stores_text);
        more_text=findViewById(R.id.more_text);





        store=findViewById(R.id.stores_botton);

        home=findViewById(R.id.home_botton);

        rewards=findViewById(R.id.rewards_botton);

        home.setBackground(getResources().getDrawable(R.drawable.homewhite));

//        home.startAnimation(zoom);

        setting=findViewById(R.id.setting_botton);

       rl_home=findViewById(R.id.home_layout);

       rl_rewards=findViewById(R.id.reward_layout);

       rl_more=findViewById(R.id.setting_layout);

        rl_apps=findViewById(R.id.home_welcome);

        apps=findViewById(R.id.home_welcomebt);

//        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//            return;
//        }
//        else {
//            // Write you code here if permission already given.
//        }
//
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//
//        }

        store.setOnClickListener(v -> {
            store.setBackground(getResources().getDrawable(R.drawable.storedark));
            rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
            home.setBackground(getResources().getDrawable(R.drawable.home));
            setting.setBackground(getResources().getDrawable(R.drawable.more));

            store_text.setTextColor(getResources().getColor(R.color.text));
            rewards_text.setTextColor(getResources().getColor(R.color.black));
            home_text.setTextColor(getResources().getColor(R.color.black));
            more_text.setTextColor(getResources().getColor(R.color.black));

            Fragment fm12 = new FragmentStore();
            FragmentManager fragmentManager12 = getSupportFragmentManager();
            fragmentManager12.beginTransaction()
                    .replace(R.id.contentPanel, fm12, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            storev = "true";

            if (storev.contains("true")) {

                store.setEnabled(false);
                rl_stores.setEnabled(false);

                home.setEnabled(true);
                rl_home.setEnabled(true);

                rewards.setEnabled(true);
                rl_rewards.setEnabled(true);

                apps.setEnabled(true);
                rl_apps.setEnabled(true);

                setting.setEnabled(true);
                rl_more.setEnabled(true);
            } else if (storev.contains("false")) {
                store.setEnabled(true);
                rl_stores.setEnabled(true);

                home.setEnabled(false);
                rl_home.setEnabled(false);

                rewards.setEnabled(false);
                rl_rewards.setEnabled(false);

                apps.setEnabled(false);
                rl_apps.setEnabled(false);

                setting.setEnabled(false);
                rl_more.setEnabled(false);
            }
            homev = "false";
            rewardsv = "false";
            appsv = "false";
            morev = "false";


        });
        rl_stores.setOnClickListener(v -> {
            store.setBackground(getResources().getDrawable(R.drawable.storedark));
            rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
            home.setBackground(getResources().getDrawable(R.drawable.home));
            setting.setBackground(getResources().getDrawable(R.drawable.more));

            store_text.setTextColor(getResources().getColor(R.color.text));
            rewards_text.setTextColor(getResources().getColor(R.color.black));
            home_text.setTextColor(getResources().getColor(R.color.black));
            more_text.setTextColor(getResources().getColor(R.color.black));

            Fragment fm1 = new FragmentStore();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .replace(R.id.contentPanel, fm1, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            storev = "true";

            if (storev.contains("true")) {

                store.setEnabled(false);
                rl_stores.setEnabled(false);

                home.setEnabled(true);
                rl_home.setEnabled(true);

                rewards.setEnabled(true);
                rl_rewards.setEnabled(true);

                apps.setEnabled(true);
                rl_apps.setEnabled(true);

                setting.setEnabled(true);
                rl_more.setEnabled(true);
            } else if (storev.contains("false")) {
                store.setEnabled(true);
                rl_stores.setEnabled(true);

                home.setEnabled(false);
                rl_home.setEnabled(false);

                rewards.setEnabled(false);
                rl_rewards.setEnabled(false);

                apps.setEnabled(false);
                rl_apps.setEnabled(false);

                setting.setEnabled(false);
                rl_more.setEnabled(false);
            }
            homev = "false";
            rewardsv = "false";
            appsv = "false";
            morev = "false";

        });
         home.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void onClick(View v) {
               Fragment fm = new Fragmnet_Home();
               FragmentManager fragmentManager = getSupportFragmentManager();
               fragmentManager.beginTransaction()
                       .replace(R.id.contentPanel, fm, "Home_fragment")
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                       .commit();
//               apps.setBackground(getResources().getDrawable(R.drawable.appss));
               store_text.setTextColor(getResources().getColor(R.color.black));
               rewards_text.setTextColor(getResources().getColor(R.color.black));
               home_text.setTextColor(getResources().getColor(R.color.text));
               more_text.setTextColor(getResources().getColor(R.color.black));


               store.setBackground(getResources().getDrawable(R.drawable.stores));
               rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
               home.setBackground(getResources().getDrawable(R.drawable.homewhite));
               setting.setBackground(getResources().getDrawable(R.drawable.more));
               homev="true";

               if(homev.contains("true")){

                   home.setEnabled(false);
                   rl_home.setEnabled(false);

                   rewards.setEnabled(true);
                   rl_rewards.setEnabled(true);

                   store.setEnabled(true);
                   rl_stores.setEnabled(true);

                   apps.setEnabled(true);
                   rl_apps.setEnabled(true);

                   setting.setEnabled(true);
                   rl_more.setEnabled(true);

               }

               else if (homev.contains("false")){

                   home.setEnabled(true);
                   rl_home.setEnabled(true);

                   rewards.setEnabled(false);
                   rl_rewards.setEnabled(false);

                   apps.setEnabled(false);
                   rl_apps.setEnabled(false);

                   setting.setEnabled(false);
                   rl_more.setEnabled(false);

                   store.setEnabled(false);
                   rl_stores.setEnabled(false);

               }

                 rewardsv="false";
                 appsv="false";
                 morev="false";
                   storev="false";

//               rl_home.setBackground(getResources().getDrawable(R.drawable.shadow));
//               rl_more.setBackgroundColor(Color.parseColor("#ffffff"));
//               rl_rewards.setBackgroundColor(Color.parseColor("#ffffff"));

           }
       });

       rl_home.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void onClick(View v) {
               Fragment fm = new Fragmnet_Home();
               FragmentManager fragmentManager = getSupportFragmentManager();
               fragmentManager.beginTransaction()
                       .replace(R.id.contentPanel, fm, "Home_fragment")
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                       .commit();
//               apps.setBackground(getResources().getDrawable(R.drawable.appss));

               store_text.setTextColor(getResources().getColor(R.color.black));
               rewards_text.setTextColor(getResources().getColor(R.color.black));
               home_text.setTextColor(getResources().getColor(R.color.text));
               more_text.setTextColor(getResources().getColor(R.color.black));

               store.setBackground(getResources().getDrawable(R.drawable.stores));

               rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
               home.setBackground(getResources().getDrawable(R.drawable.homewhite));
               setting.setBackground(getResources().getDrawable(R.drawable.more));
//               rl_home.setBackground(getResources().getDrawable(R.drawable.shadow));
//               rl_more.setBackgroundColor(Color.parseColor("#ffffff"));
//               rl_rewards.setBackgroundColor(Color.parseColor("#ffffff"));
               homev="true";
               if(homev.contains("true")){
                   home.setEnabled(false);
                   rl_home.setEnabled(false);

                   rewards.setEnabled(true);
                   rl_rewards.setEnabled(true);

                   apps.setEnabled(true);
                   rl_apps.setEnabled(true);

                   setting.setEnabled(true);
                   rl_more.setEnabled(true);

                   store.setEnabled(true);
                   rl_stores.setEnabled(true);


               }
               else if (homev.contains("false")) {
                   home.setEnabled(true);
                   rl_home.setEnabled(true);

                   rewards.setEnabled(false);
                   rl_rewards.setEnabled(false);

                   apps.setEnabled(false);
                   rl_apps.setEnabled(false);

                   store.setEnabled(false);
                   rl_stores.setEnabled(false);

                   setting.setEnabled(false);
                   rl_more.setEnabled(false);

               }
               storev="false";
               rewardsv="false";
               appsv="false";
               morev="false";

           }
       });

       rewards.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void onClick(View v) {
               Fragment fm = new FragmentMyRewards();
               FragmentManager fragmentManager = getSupportFragmentManager();
               fragmentManager.beginTransaction()
                       .replace(R.id.contentPanel, fm, "Home_fragment")
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                       .commit();

               store_text.setTextColor(getResources().getColor(R.color.black));
               rewards_text.setTextColor(getResources().getColor(R.color.text));
               home_text.setTextColor(getResources().getColor(R.color.black));
               more_text.setTextColor(getResources().getColor(R.color.black));


//               apps.setBackground(getResources().getDrawable(R.drawable.appss));
               store.setBackground(getResources().getDrawable(R.drawable.stores));

               rewards.setBackground(getResources().getDrawable(R.drawable.rewardsblack));
               home.setBackground(getResources().getDrawable(R.drawable.home));
               setting.setBackground(getResources().getDrawable(R.drawable.more));
//               rl_rewards.setBackground(getResources().getDrawable(R.drawable.shadow));
//               rl_more.setBackgroundColor(Color.parseColor("#ffffff"));
//               rl_home.setBackgroundColor(Color.parseColor("#ffffff"));
               rewardsv="true";
                if (rewardsv.contains("true")){
                   rewards.setEnabled(false);
                   rl_rewards.setEnabled(false);

                    home.setEnabled(true);
                    rl_home.setEnabled(true);

                    apps.setEnabled(true);
                    rl_apps.setEnabled(true);

                    setting.setEnabled(true);
                    rl_more.setEnabled(true);

                    store.setEnabled(true);
                    rl_stores.setEnabled(true);


                }  else if (rewardsv.contains("false")) {

                   rewards.setEnabled(true);
                   rl_rewards.setEnabled(true);

                    home.setEnabled(false);
                    rl_home.setEnabled(false);

                    apps.setEnabled(false);
                    rl_apps.setEnabled(false);

                    setting.setEnabled(false);
                    rl_more.setEnabled(false);

                    store.setEnabled(false);
                    rl_stores.setEnabled(false);


                }
               storev="false";
               homev="false";
               appsv="false";
               morev="false";

           }
       });

      rl_rewards.setOnClickListener(new View.OnClickListener() {
          @RequiresApi(api = Build.VERSION_CODES.M)
          @Override
          public void onClick(View v) {
              Fragment fm = new FragmentMyRewards();
              FragmentManager fragmentManager = getSupportFragmentManager();
              fragmentManager.beginTransaction()
                      .replace(R.id.contentPanel, fm, "Home_fragment")
                      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                      .commit();
//              apps.setBackground(getResources().getDrawable(R.drawable.appss));

              store_text.setTextColor(getResources().getColor(R.color.black));
              rewards_text.setTextColor(getResources().getColor(R.color.text));
              home_text.setTextColor(getResources().getColor(R.color.black));
              more_text.setTextColor(getResources().getColor(R.color.black));

              store.setBackground(getResources().getDrawable(R.drawable.stores));

              rewards.setBackground(getResources().getDrawable(R.drawable.rewardsblack));
              home.setBackground(getResources().getDrawable(R.drawable.home));
              setting.setBackground(getResources().getDrawable(R.drawable.more));
//              rl_rewards.setBackground(getResources().getDrawable(R.drawable.shadow));
//              rl_more.setBackgroundColor(Color.parseColor("#ffffff"));
//              rl_home.setBackgroundColor(Color.parseColor("#ffffff"));
              rewardsv="true";

              if (rewardsv.contains("true")){
                  rewards.setEnabled(false);
                  rl_rewards.setEnabled(false);


                  home.setEnabled(true);
                  rl_home.setEnabled(true);

                  apps.setEnabled(true);
                  rl_apps.setEnabled(true);

                  setting.setEnabled(true);
                  rl_more.setEnabled(true);

                  store.setEnabled(true);
                  rl_stores.setEnabled(true);


              }  else if (rewardsv.contains("false")) {

                  rewards.setEnabled(true);
                  rl_rewards.setEnabled(true);


                  home.setEnabled(false);
                  rl_home.setEnabled(false);

                  apps.setEnabled(false);
                  rl_apps.setEnabled(false);

                  setting.setEnabled(false);
                  rl_more.setEnabled(false);

                  store.setEnabled(false);
                  rl_stores.setEnabled(false);


              }
              storev="false";
              homev="false";
              appsv="false";
              morev="false";

          }
      });

       rl_more.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.M)
           @Override
           public void onClick(View v) {
               Fragment fm = new FragmentSetting();
               FragmentManager fragmentManager = getSupportFragmentManager();
               fragmentManager.beginTransaction()
                       .replace(R.id.contentPanel, fm, "Home_fragment")
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                       .commit();
               setting.setBackground(getResources().getDrawable(R.drawable.moredark));
//               apps.setBackground(getResources().getDrawable(R.drawable.appss));
               store.setBackground(getResources().getDrawable(R.drawable.stores));

               store_text.setTextColor(getResources().getColor(R.color.black));
               rewards_text.setTextColor(getResources().getColor(R.color.black));
               home_text.setTextColor(getResources().getColor(R.color.black));
               more_text.setTextColor(getResources().getColor(R.color.text));

               rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
               home.setBackground(getResources().getDrawable(R.drawable.home));
//               rl_more.setBackground(getResources().getDrawable(R.drawable.shadow));
//               rl_rewards.setBackgroundColor(Color.parseColor("#ffffff"));
//               rl_home.setBackgroundColor(Color.parseColor("#ffffff"));

               morev="true";
               if (morev.contains("true")){
                   setting.setEnabled(false);
                   rl_more.setEnabled(false);

                   rewards.setEnabled(true);
                   rl_rewards.setEnabled(true);

                   apps.setEnabled(true);
                   rl_apps.setEnabled(true);

                   store.setEnabled(true);
                   rl_stores.setEnabled(true);

                   home.setEnabled(true);
                   rl_home.setEnabled(true);

               }
               else if (morev.contains("false")) {

                   setting.setEnabled(true);
                   rl_more.setEnabled(true);

                   store.setEnabled(false);
                   rl_stores.setEnabled(false);

                   rewards.setEnabled(false);
                   rl_rewards.setEnabled(false);

                   apps.setEnabled(false);
                   rl_apps.setEnabled(false);

                   home.setEnabled(false);
                   rl_home.setEnabled(false);

               }
               storev="false";
               rewardsv="false";
               appsv="false";
               homev="false";

           }
       });
setting.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Fragment fm = new FragmentSetting();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contentPanel, fm, "Home_fragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        setting.setBackground(getResources().getDrawable(R.drawable.moredark));
        store.setBackground(getResources().getDrawable(R.drawable.stores));

        store_text.setTextColor(getResources().getColor(R.color.black));
        rewards_text.setTextColor(getResources().getColor(R.color.black));
        home_text.setTextColor(getResources().getColor(R.color.black));
        more_text.setTextColor(getResources().getColor(R.color.text));


        rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
        home.setBackground(getResources().getDrawable(R.drawable.home));
//        rl_more.setBackground(getResources().getDrawable(R.drawable.shadow));
//        rl_rewards.setBackgroundColor(Color.parseColor("#ffffff"));
//        rl_home.setBackgroundColor(Color.parseColor("#ffffff"));
        morev="true";

        if (morev.contains("true")){

            setting.setEnabled(false);
            rl_more.setEnabled(false);

            rewards.setEnabled(true);
            rl_rewards.setEnabled(true);

            apps.setEnabled(true);
            rl_apps.setEnabled(true);

            store.setEnabled(true);
            rl_stores.setEnabled(true);

            home.setEnabled(true);
            rl_home.setEnabled(true);

        }
        else if (morev.contains("false")) {
            setting.setEnabled(true);
            rl_more.setEnabled(true);

            rewards.setEnabled(false);
            rl_rewards.setEnabled(false);

            apps.setEnabled(false);
            rl_apps.setEnabled(false);

            home.setEnabled(false);
            rl_home.setEnabled(false);

            store.setEnabled(false);
            rl_stores.setEnabled(false);


        }
        storev="false";
        rewardsv="false";
        appsv="false";
        homev="false";

    }
});

        rl_apps.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Fragment fm = new FragmentWelcomeScreen();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, fm, "Home_fragment")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                setting.setBackground(getResources().getDrawable(R.drawable.more));
//                apps.setBackground(getResources().getDrawable(R.drawable.appsiconn));
                store.setBackground(getResources().getDrawable(R.drawable.stores));
                store_text.setTextColor(getResources().getColor(R.color.black));
                rewards_text.setTextColor(getResources().getColor(R.color.black));
                home_text.setTextColor(getResources().getColor(R.color.black));
                more_text.setTextColor(getResources().getColor(R.color.black));

                rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
                home.setBackground(getResources().getDrawable(R.drawable.home));
//               rl_more.setBackground(getResources().getDrawable(R.drawable.shadow));
//               rl_rewards.setBackgroundColor(Color.parseColor("#ffffff"));
//               rl_home.setBackgroundColor(Color.parseColor("#ffffff"));
                appsv="true";
                  if (appsv.contains("true")){

                     apps.setEnabled(false);
                      rl_apps.setEnabled(false);


                      rewards.setEnabled(true);
                      rl_rewards.setEnabled(true);

                      home.setEnabled(true);
                      rl_home.setEnabled(true);

                      setting.setEnabled(true);
                      rl_more.setEnabled(true);

                      store.setEnabled(true);
                      rl_stores.setEnabled(true);


                  }
                  else if (appsv.contains("false"))  {
                    apps.setEnabled(true);
                    rl_apps.setEnabled(true);


                      rewards.setEnabled(false);
                      rl_rewards.setEnabled(false);

                      home.setEnabled(false);
                      rl_home.setEnabled(false);

                      setting.setEnabled(false);
                      rl_more.setEnabled(false);

                      store.setEnabled(false);
                      rl_stores.setEnabled(false);

                  }
                storev="false";
                rewardsv="false";
                homev="false";
                morev="false";

            }
        });
        apps.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Fragment fm = new FragmentWelcomeScreen();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, fm, "Home_fragment")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                setting.setBackground(getResources().getDrawable(R.drawable.more));
                store.setBackground(getResources().getDrawable(R.drawable.stores));
                store_text.setTextColor(getResources().getColor(R.color.black));
                rewards_text.setTextColor(getResources().getColor(R.color.black));
                home_text.setTextColor(getResources().getColor(R.color.black));
                more_text.setTextColor(getResources().getColor(R.color.black));

                rewards.setBackground(getResources().getDrawable(R.drawable.rewards));
//                apps.setBackground(getResources().getDrawable(R.drawable.appsiconn));
                home.setBackground(getResources().getDrawable(R.drawable.home));
//               rl_more.setBackground(getResources().getDrawable(R.drawable.shadow));
//               rl_rewards.setBackgroundColor(Color.parseColor("#ffffff"));
//               rl_home.setBackgroundColor(Color.parseColor("#ffffff"));
                appsv="true";
                if (appsv.contains("true")){
                    apps.setEnabled(false);
                    rl_apps.setEnabled(false);

                    rewards.setEnabled(true);
                    rl_rewards.setEnabled(true);

                    home.setEnabled(true);
                    rl_home.setEnabled(true);

                    setting.setEnabled(true);
                    rl_more.setEnabled(true);

                    store.setEnabled(true);
                    rl_stores.setEnabled(true);

                }
                else if (appsv.contains("false"))  {
                    apps.setEnabled(true);
                    rl_apps.setEnabled(true);




                    rewards.setEnabled(false);
                    rl_rewards.setEnabled(false);

                    home.setEnabled(false);
                    rl_home.setEnabled(false);

                    setting.setEnabled(false);
                    rl_more.setEnabled(false);

                    store.setEnabled(false);
                    rl_stores.setEnabled(false);

                }
                storev="false";
                rewardsv="false";
                homev="false";
                morev="false";

            }
        });

        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");


        username=sharedPreferences.getString("username","");

        userimgae=sharedPreferences.getString("user_image","");

        home_text.setTextColor(getResources().getColor(R.color.text));
//        more_text.setTextColor(getResources().getColor(R.color.black));

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());
////
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gifImageView = (ImageView) toolbar.findViewById(R.id.GifImageView);

        userimageprofile=toolbar.findViewById(R.id.userimage);

        title = toolbar.findViewById(R.id.title);

        Glide.with(MainActivity.this)
                .load(BaseURL.IMG_CATEGORY_URL + userimgae)
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(userimageprofile);


//        notify=toolbar.findViewById(R.id.notify);

        gifImageView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
});

        title.setText(username+ " "+"!");

        count();
//
    }

    @Override
    public void onBackPressed() {


                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

        }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuact, menu);

//        getMenuInflater().inflate(R.menu.menuact, menu);

//        final MenuItem item = menu.findItem(R.id.notification);
//        item.setVisible(true);

//        View count = item.getActionView();
//        count.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                menu.performIdentifierAction(item.getItemId(), 0);
//            }
//        });
//
//        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);
//        totalBudgetCount.setText("" + dbcart.getCartCount());
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.notification){
//            openLanguageDialog();
        }

//        if (id == R.id.action_cart) {
//            if (dbcart.getCartCount() > 0) {
//                android.app.Fragment fm = new Cart_fragment();
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//            }
//            else {
//                android.app.Fragment fm = new Empty_cart_fragment();
//                android.app.FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
//            }
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }
    public static void count() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Notification_count, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
//                            gifImageView.setGifImageResource(R.drawable.rewardmm);

                        }
//                        else {
////                            gifImageView.setGifImageResource(R.drawable.rewardm);
//                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(activity, "Time out", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
