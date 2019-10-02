package com.goaffilate.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.adapter.StoreshowAdapter;
import com.goaffilate.app.model.StoreshowModel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreshowActvity extends AppCompatActivity {

    TextView storename,call,Share,address;

    RecyclerView viewPager;
    SharedPreferences locpref;
String getlat,getlong;
String latitude,longitude;
    private List<StoreshowModel> sildermodelslist = new ArrayList<>();
ProgressDialog progressDialog;
    String name;
    private static int NUM_PAGES = 0;
TextView distance,getdirection;
ImageView map;
    SharedPreferences sharedPreferences;

    String storenameshow,userid,storeimage,storephone,storesociety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_storeshow_actvity);
        locpref=getSharedPreferences("location",MODE_PRIVATE);

        latitude=locpref.getString("lat","");
        longitude=locpref.getString("lng","");
        distance=findViewById(R.id.dist);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
map=findViewById(R.id.map);
getdirection=findViewById(R.id.get);
        name=getIntent().getStringExtra("name");

        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");

        storename=findViewById(R.id.storename);

        call=findViewById(R.id.call);

        Share=findViewById(R.id.share);

        viewPager=findViewById(R.id.vp);

        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(StoreshowActvity.this, LinearLayoutManager.HORIZONTAL, false);

        viewPager.setLayoutManager(gridLayoutManagercat);

        viewPager.setHasFixedSize(true);

        viewPager.setItemAnimator(new DefaultItemAnimator());

        viewPager.setHasFixedSize(true);

        viewPager.setNestedScrollingEnabled(false);

        address=findViewById(R.id.address);

        storeshow();


        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query="+getlat+","+getlong));
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query="+getlat+","+getlong));
                startActivity(intent);
            }
        });
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

    }

    private void storeshow(){


        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", userid);
        params.put("lat",latitude);
        params.put("lng",longitude);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Store_url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            progressDialog.dismiss();
                            JSONArray jsonArray=response.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                storenameshow=jsonObject.getString("store_name");

                                if (name.contains(storenameshow)){
                                    storeimage=jsonObject.getString("store_images");

                                    storephone=jsonObject.getString("store_phone");
                                    getlat=jsonObject.getString("getlat");
                                    getlong=jsonObject.getString("getlng");
                                    storesociety=jsonObject.getString("store_address");
                                    distance.setText(jsonObject.getString("location"));
                                    storename.setText(name);

                                    call.setText(storephone);
                                    address.setText(storesociety);

                                    JSONArray storeimarr=new JSONArray(storeimage);


                                    for (int j=0;j<storeimarr.length();j++){

                                         StoreshowModel storeshowModel=new StoreshowModel();

                                         String datashow= String.valueOf(storeimarr.get(j));

                                         storeshowModel.setImage(datashow);

                                         sildermodelslist.add(storeshowModel);


                                    }

                                    StoreshowAdapter silderAdapter1 = new StoreshowAdapter(sildermodelslist);

                                    viewPager.setAdapter(silderAdapter1);


                                }

                            }
                                                  }
                                                  else {
                            progressDialog.dismiss();

                        }
                    }
                    else {
                        Toast.makeText(StoreshowActvity.this, "No Data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(StoreshowActvity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

}
