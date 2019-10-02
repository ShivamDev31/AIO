package com.goaffilate.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.myinnos.androidscratchcard.ScratchCard;

import static com.goaffilate.app.Fragment.Fragmnet_Home.scratchapps;

public class Scratch extends AppCompatActivity {
ImageView scratchimage;
TextView scratchtext;
String type;
String textlink,imagelink,id;
ScratchCard scratchCard;
    Button copycode;
public static String TAG="Scratch";
    SharedPreferences sharedPreferences;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        type=getIntent().getStringExtra("scratchtype");

        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");
        copycode=findViewById(R.id.copy);

        if (type.contains("coupan")){
            copycode.setVisibility(View.VISIBLE);
        }

        else {
            copycode.setVisibility(View.GONE);

        }
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);
        userid=sharedPreferences.getString("user","");
Log.d("userid",userid);
scratchimage=findViewById(R.id.image);
scratchtext=findViewById(R.id.textView);
textlink=getIntent().getStringExtra("scratchamount");
imagelink=getIntent().getStringExtra("scratchimage");
id=getIntent().getStringExtra("scratchid");
Log.d("scratch",id);
        Glide.with(this)
                .load(BaseURL.IMG_CATEGORY_URL + imagelink)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(scratchimage);
        scratchtext.setText(textlink);

        scratchCard = (ScratchCard) findViewById(R.id.scratchCard);
        copycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                Toast.makeText(Scratch.this, "Copied to Clipboard ", Toast.LENGTH_SHORT).show();
                ClipData clip = ClipData.newPlainText("Code Copied", textlink);
                clipboard.setPrimaryClip(clip);
            }
        });

        scratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.4) {
                    scratchapps();
                    scratchCard.setVisibility(View.GONE);
                    userreward();
                    Toast.makeText(Scratch.this, textlink, Toast.LENGTH_SHORT).show();

                }
            }

        });

//        final ScratchImageView scratchTextView = (ScratchImageView) findViewById(R.id.sample_image);
//
////       if (scratchTextView==null){
//
//        scratchTextView.setRevealListener(new ScratchImageView.IRevealListener() {
//            @Override
//            public void onRevealed(ScratchImageView iv) {
//                Toast.makeText(Scratch.this, "Well done you earn ₹500", Toast.LENGTH_SHORT).show();
//
////                   scratchTextView.clear();
//            }
//
//            @Override
//            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
//
//
//                if (percent>=0.3) {
//                    Log.d("check", String.valueOf(percent));
//                    Toast.makeText(Scratch.this, "you earnkjhk ₹500", Toast.LENGTH_SHORT).show();
//
//                    scratchTextView.clear();
//                    scratchTextView.setEraserMode();
//                }
//
//                Log.d("checkfin", String.valueOf(percent));
//
//            }
//        });
//

        DisplayMetrics metrics= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);



        int h= metrics.heightPixels;
        int w= metrics.widthPixels;

        getWindow().setLayout((int)(w*.7),(int)(h*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();


//        params.alpha= (float) .6;

        params.gravity= Gravity.CENTER;
        params.x=10;
        params.y=-10;
        getWindow().setAttributes(params);

    }
    private void userreward() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("scratch_id", id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.User_Reward, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {


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
                    Toast.makeText(Scratch.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

//    @Override
//    public void onBackPressed() {
//
////        Intent intent=new Intent(Scratch.this,MainActivity.class);
////
////        startActivity(intent);
//
////        Animatoo.animateSlideRight(Scratch.this);
//
//    }
}
