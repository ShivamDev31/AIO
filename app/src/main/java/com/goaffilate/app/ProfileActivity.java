package com.goaffilate.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String userid;
    CircleImageView iv;
    Button referal;
    String referalcode;
    TextView societytx,mb,usernametx;
    private static final String TAG = "Profile";
    String userimgae;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        iv=findViewById(R.id.user_image);
       usernametx=findViewById(R.id.user_name);
       societytx=findViewById(R.id.society);
       mb=findViewById(R.id.mb);

         referal=findViewById(R.id.referal);
        userid=sharedPreferences.getString("user","");
        userimgae=sharedPreferences.getString("user_image","");
        Glide.with(ProfileActivity.this)
                .load(BaseURL.IMG_CATEGORY_URL + userimgae)
                .placeholder(R.drawable.shopicon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iv);

        userprofile();

        referal.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                 Toast.makeText(ProfileActivity.this, "Copied to Clipboard ", Toast.LENGTH_SHORT).show();
                 ClipData clip = ClipData.newPlainText("Code Copied", referalcode);
                 clipboard.setPrimaryClip(clip);
             }
         });



    }
    private void userprofile() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Userprofile, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {
                       JSONObject jsonObject=response.getJSONObject("data");
                       String username=jsonObject.getString("user_name");
                       String society=jsonObject.getString("society_name");

                       String phone=jsonObject.getString("user_phone");
 referalcode=jsonObject.getString("referral_code");
usernametx.setText(username);
societytx.setText(society);
mb.setText(phone);
referal.setText(referalcode);
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
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
