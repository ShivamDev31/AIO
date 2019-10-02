package com.goaffilate.app;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.adapter.TransactionAdapter;
import com.goaffilate.app.model.TransactionModel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {
     List<TransactionModel> transactionModels = new ArrayList<>();
    TransactionAdapter transactionAdapter;
    RecyclerView transactionrc;
    String userid;
    ImageView notrans;
    SharedPreferences sharedPreferences;
    public  static String TAG="Transaction";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transaction);
        transactionrc=findViewById(R.id.transactionrc);
        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
notrans=findViewById(R.id.notrans);
        transactionrc.setLayoutManager(gridLayoutManagercat);

        transactionrc.setHasFixedSize(true);

        transactionrc.setItemAnimator(new DefaultItemAnimator());

        transactionrc.setHasFixedSize(true);

        transactionrc.setNestedScrollingEnabled(false);

        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");
Log.d("user",userid);
        transactionlist();

    }
    private void transactionlist() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Tranaction_History, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<TransactionModel>>() {
                            }.getType();
                            transactionModels = gson.fromJson(response.getString("data"), listType);
                            transactionAdapter = new TransactionAdapter(transactionModels);
                            transactionrc.setAdapter(transactionAdapter);
                            transactionAdapter.notifyDataSetChanged();
                        }
                        else {
                            notrans.setVisibility(View.VISIBLE);
                            transactionrc.setVisibility(View.GONE);
                            Toast.makeText(TransactionActivity.this, "No Transactions", Toast.LENGTH_SHORT).show();
                        }
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
                    Toast.makeText(TransactionActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}
