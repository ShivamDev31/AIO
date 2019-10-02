package com.goaffilate.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.adapter.NotificationAdapter;
import com.goaffilate.app.model.NotificationModel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.RecyclerTouchListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
RecyclerView notificationrc;
ProgressDialog progressDialog;
String userid,id;
List<NotificationModel> notificationModels=new ArrayList<>();
NotificationAdapter notificationAdapter;
    SharedPreferences sharedPreferences;
    private static final String TAG = "Notify";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_notification);

        progressDialog=new ProgressDialog(this);

        progressDialog.show();
        progressDialog.setMessage("Please Wait");
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");

        notificationrc=findViewById(R.id.rcnotify);

        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false);

        notificationrc.setLayoutManager(gridLayoutManagercat);

        notificationrc.setHasFixedSize(true);

        notificationrc.setItemAnimator(new DefaultItemAnimator());

        notificationrc.setHasFixedSize(true);

        notificationrc.setNestedScrollingEnabled(false);


        notificationrc.addOnItemTouchListener(new RecyclerTouchListener(NotificationActivity.this, notificationrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                id=notificationModels.get(position).getId();
                Intent intent = new Intent(NotificationActivity.this, NotificationShow.class);
                intent.putExtra("title",notificationModels.get(position).getTitle());
                intent.putExtra("des",notificationModels.get(position).getDescription());
                intent.putExtra("image",notificationModels.get(position).getImage());
                startActivity(intent);
                notifyseen();
//                MainActivity.count();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        notifyapps();

    }
    public void notifyapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Notify, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<NotificationModel>>() {
                            }.getType();
                            notificationModels = gson.fromJson(response.getString("data"), listType);
                            notificationAdapter = new NotificationAdapter(notificationModels);
                            notificationrc.setAdapter(notificationAdapter);
                            notificationAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }
                        else {
                            Toast.makeText(NotificationActivity.this, "No Notification", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(NotificationActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NotificationActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    public void notifyseen() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Notifyseen, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {

                        }
                    }
                    else {
                        Toast.makeText(NotificationActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NotificationActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
