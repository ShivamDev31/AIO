package com.goaffilate.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goaffilate.app.networkconnectivity.OnSmsCatchListener;
import com.goaffilate.app.networkconnectivity.SmsVerifyCatcher;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.Session_management;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Otp_Activity extends AppCompatActivity {

    Button submit;

EditText edtotp;
TextView number;
String getusername,getuserphone,token;
String status,message;
TextView resend;
    SharedPreferences sharedPreferences;
    String getotp;
    String setotp;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
public static String TAG="Otp";
String username,mobile,userid;
private SmsVerifyCatcher smsVerifyCatcher;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_);
      resend=findViewById(R.id.resend);
        progressDialog=new ProgressDialog(this);
         edtotp=findViewById(R.id.et_otp);


         number=findViewById(R.id.txnm);
       token = FirebaseInstanceId.getInstance().getToken();

        number.setText("+91"+getIntent().getStringExtra("user_phone"));

        submit=findViewById(R.id.submit_bt);


        getusername=getIntent().getStringExtra("user_name");

        getuserphone=getIntent().getStringExtra("user_phone");

     setotp=getIntent().getStringExtra("otp");


        if (setotp.length()>0){
            edtotp.setText(setotp);
            resend.setVisibility(View.GONE);
        }

        else {


            resend.setVisibility(View.VISIBLE);

        }

    sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

    editor=sharedPreferences.edit();
//
resend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        progressDialog.show();
        progressDialog.setMessage("Loading");
        resendotp();
    }
});

    smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {

        @Override
        public void onSmsCatch(String message) {

            String code = parseCode(message);//Parse verification code

            edtotp.setText(code);

        }
    });

    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog.show();
            progressDialog.setMessage("Loading");


            if (setotp.length()>0){
                setotpverify();
            }
            else {
                otpverify();
            }

        }
    });

}
    public void resendotp() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_name", getusername);

        params.put("user_phone",getuserphone);
        ;
        params.put("device_id",token);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    String status = response.getString("status");
                    if (status.contains("1")) {

                        progressDialog.dismiss();

                        resend.setEnabled(false);

                        String msg = response.getString("message");

                        Toast.makeText(Otp_Activity.this, "" + msg, Toast.LENGTH_SHORT).show();



                    } else {


                        progressDialog.dismiss();
                        String error = response.getString("message");
                        resend.setEnabled(true);
                        Toast.makeText(Otp_Activity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Otp_Activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void otpverify() {


        getotp=edtotp.getText().toString();

        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getuserphone);

        params.put("otp_value", getotp);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                BaseURL.Otp_verify, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                     status = response.getString("status");

                      if (status.contains("1")) {

                          progressDialog.dismiss();


                         message = response.getString("message");

//                         Toast.makeText(Otp_Activity.this, message, Toast.LENGTH_SHORT).show();
                                   send();

//                        Intent i = new Intent(Otp_Activity.this, Otp_Activity.class);
//
//                        i.putExtra("user_phone",getuserphone);
//                          Animatoo.animateSlideLeft(Otp_Activity.this);
//                        startActivity(i);

                        finish();

                         JSONObject obj = response.getJSONObject("data");

                        String user_fullname = obj.getString("user_name");

                        String user_phone = obj.getString("user_phone");

                        String userid=obj.getString("id");

//                        editor.putString("user",userid);
//
//                        editor.putString("username",user_fullname);
//
//                        editor.commit();
//
//                        Session_management sessionManagement = new Session_management(Otp_Activity.this);
//
//                        sessionManagement.createLoginSession(user_fullname, user_phone,userid);


                        submit.setEnabled(false);

                    } else {

                          progressDialog.dismiss();

                          String error = response.getString("message");

                          submit.setEnabled(true);

                        Toast.makeText(Otp_Activity.this, error, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Otp_Activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public void setotpverify() {


        getotp=edtotp.getText().toString();

        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getuserphone);

        params.put("otp_value", setotp);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                BaseURL.Otp_verify, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    status = response.getString("status");

                    if (status.contains("1")) {

                        progressDialog.dismiss();


                        message = response.getString("message");

//                         Toast.makeText(Otp_Activity.this, message, Toast.LENGTH_SHORT).show();

//                        Intent i = new Intent(Otp_Activity.this, Otp_Activity.class);
//
//                        i.putExtra("user_phone",getuserphone);
//                          Animatoo.animateSlideLeft(Otp_Activity.this);
//                        startActivity(i);
                        send();

                        JSONObject obj = response.getJSONObject("data");

                        String user_fullname = obj.getString("user_name");

                        String user_phone = obj.getString("user_phone");

                        String userid=obj.getString("id");

//                        editor.putString("user",userid);
//
//                        editor.putString("username",user_fullname);
//
//                        editor.commit();
//
//                        Session_management sessionManagement = new Session_management(Otp_Activity.this);
//
//                        sessionManagement.createLoginSession(user_fullname, user_phone,userid);


                        submit.setEnabled(false);

                    } else {

                        progressDialog.dismiss();

                        String error = response.getString("message");

                        submit.setEnabled(true);

                        Toast.makeText(Otp_Activity.this, error, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Otp_Activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    private String parseCode(String message) {

        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Otp_Activity.this,RegisterActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(Otp_Activity.this);
    }
    public void send() {

        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getuserphone);

        params.put("society_id", "1");


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

//                        Toast.makeText(Otp_Activity.this, messagedata, Toast.LENGTH_SHORT).show();

                        JSONObject obj = response.getJSONObject("data");

                        String user_fullname = obj.getString("user_name");

                        String user_phone = obj.getString("user_phone");

                        String userid=obj.getString("id");

                        Animatoo.animateSlideLeft(Otp_Activity.this);

                        Intent i = new Intent(Otp_Activity.this, MainActivity.class);

                        startActivity(i);

                        finish();


                        editor.putString("user",userid);

                        editor.putString("username",user_fullname);

                        editor.commit();

                        Session_management sessionManagement = new Session_management(Otp_Activity.this);

                        sessionManagement.createLoginSession(user_fullname, user_phone,userid);


                        submit.setEnabled(false);

                    } else {
                        progressDialog.dismiss();
                        String error = response.getString("message");

                        submit.setEnabled(true);

                        Toast.makeText(Otp_Activity.this, error, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Otp_Activity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
