package com.goaffilate.app;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.goaffilate.app.adapter.RewardsAdapter;
import com.goaffilate.app.model.RewardsModel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.ConnectivityReceiver;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRewardActivity extends AppCompatActivity {
SharedPreferences sharedPreferences;
String userid;
RecyclerView rewrdsrc;
TextView rewardstext;
public  static String TAG="MyReward";
    String totalamount;
    Toolbar toolbar;
    int padding;
RewardsAdapter rewardsAdapter;
List<RewardsModel> rewardsModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_reward);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rewrdsrc=findViewById(R.id.rewrdsrc);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(MyRewardActivity.this, 2);
        rewrdsrc.setLayoutManager(gridLayoutManager1);
        rewrdsrc.setItemAnimator(new DefaultItemAnimator());
        rewrdsrc.setNestedScrollingEnabled(false);
        rewrdsrc.hasFixedSize();
        rewrdsrc.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
        rewardstext=findViewById(R.id.totalamount);
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);
        userid=sharedPreferences.getString("user","");


        if (ConnectivityReceiver.isConnected()){
            userreward();
        }
        else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {

        Resources r = getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private void userreward() {
rewardsModels.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Rewards_Scratch, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    totalamount=response.getString("total_amount");
                    rewardstext.setText(totalamount);
                    if (status.contains("1")) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RewardsModel>>() {
                        }.getType();
                        rewardsModels = gson.fromJson(response.getString("data"), listType);
                        rewardsAdapter = new RewardsAdapter(rewardsModels);
                        rewrdsrc.setAdapter(rewardsAdapter);
                        rewardsAdapter.notifyDataSetChanged();

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
                    Toast.makeText(MyRewardActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return true;
    }

}
