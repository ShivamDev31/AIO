package com.goaffilate.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goaffilate.app.adapter.CategoryAdapter;
import com.goaffilate.app.adapter.Socity_adapter;
import com.goaffilate.app.model.CategoryModel;
import com.goaffilate.app.model.Society_model;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.Session_management;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select_Society extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
String scid;
ProgressDialog progressDialog;
String username,userphone,userid,getUserphone;
String status,message;
    ArrayList<Society_model> list;
    String socetyid;
String socityname;

    private Socity_adapter adapter;

    Button submit;
    public static String TAG="Select_Society";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__society);
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        list=new ArrayList<>();
progressDialog=new ProgressDialog(this);
        getUserphone=getIntent().getStringExtra("user_phone");

        submit=findViewById(R.id.submit);

        getsociety();
        Society_model society_model11 =new Society_model();

        society_model11.setSociety_name("Select Your Township");
        society_model11.setSociety_id("false");
        list.add(society_model11);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           if (scid.contains("false")){
               Toast.makeText(Select_Society.this, "Select Your Township", Toast.LENGTH_SHORT).show();
           }
           else {
               progressDialog.show();
               progressDialog.setMessage("Loading");
               send();
           }



            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        Society_model user = adapter.getItem(position);

        scid=user.getSociety_id();

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void getsociety() {

        list.clear();
        Society_model society_model1 =new Society_model();

        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Society_list, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());


                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");


                        if (status.contains("1")) {



                            JSONArray jsonArray=response.getJSONArray("data");

                              for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject= jsonArray.getJSONObject(i);

                                socityname=jsonObject.getString("society_name");

                                socetyid=jsonObject.getString("id");

                                 Society_model society_model1 =new Society_model();

                                 society_model1.setSociety_name(socityname);

                                 society_model1.setSociety_id(socetyid);


                                list.add(society_model1);

//
                                 adapter  = new Socity_adapter(Select_Society.this,android.R.layout.simple_spinner_item,list);

                                 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                 spin.setAdapter(adapter);

                                 adapter.notifyDataSetChanged();


                            }
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
                    Toast.makeText(Select_Society.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void send() {

        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getUserphone);

        params.put("society_id", scid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                BaseURL.Society_Url, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    status = response.getString("status");

                    if (status.contains("1")) {
                        progressDialog.dismiss();
                        String messagedata = response.getString("message");

                        Toast.makeText(Select_Society.this, messagedata, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Select_Society.this, MainActivity.class);

                        startActivity(i);

                        finish();

                        Animatoo.animateSlideLeft(Select_Society.this);
                        JSONObject obj = response.getJSONObject("data");

                        String user_fullname = obj.getString("user_name");

                        String user_phone = obj.getString("user_phone");

                        String userid=obj.getString("id");

                        editor.putString("user",userid);

                        editor.putString("username",user_fullname);

                        editor.commit();

                        Session_management sessionManagement = new Session_management(Select_Society.this);

                        sessionManagement.createLoginSession(user_fullname, user_phone,userid);


                        submit.setEnabled(false);

                    } else {
                        progressDialog.dismiss();
                        String error = response.getString("message");

                        submit.setEnabled(true);

                        Toast.makeText(Select_Society.this, error, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Select_Society.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Select_Society.this,RegisterActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(Select_Society.this);
    }
}


