package com.goaffilate.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.Session_management;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextView btn_register;
    EditText mobile,name;
    public static String TAG="Login";
    SharedPreferences sharedPreferences;
    String token;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
//        com.goaffilate.app.utils.GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
//        gifImageView.setGifImageResource(R.drawable.logingif);

       progressDialog=new ProgressDialog(this);
         token = FirebaseInstanceId.getInstance().getToken();

        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        editor=sharedPreferences.edit();

        btn_register=findViewById(R.id.register);

        mobile=findViewById(R.id.et_req_mobile);

        btn_login=findViewById(R.id.submit);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

                Animatoo.animateSlideLeft(LoginActivity.this);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().length()==0){
                    Toast.makeText(LoginActivity.this, "Enter Registred Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.show();
                    progressDialog.setMessage("Loading");
                    //makeRegisterRequest();
                }
                startMainActivity();

            }
        });

    }

    private void startMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        Animatoo.animateSlideLeft(LoginActivity.this);
        startActivity(i);
        finish();
    }

    private void makeRegisterRequest() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", mobile.getText().toString());
        params.put("device_id",token);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Login_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {
                       progressDialog.dismiss();
                        String msg = response.getString("message");
                        Toast.makeText(LoginActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        JSONObject obj = response.getJSONObject("data");
                        String userprofile=obj.getString("user_image");
                        String user_fullname = obj.getString("user_name");
                        String user_phone = obj.getString("user_phone");
                        String userid=obj.getString("id");
                        editor.putString("user",userid);
                        editor.putString("username",user_fullname);

                        editor.putString("user_image",userprofile);
                        editor.commit();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        Animatoo.animateSlideLeft(LoginActivity.this);
                        startActivity(i);
                        finish();
                        Session_management sessionManagement = new Session_management(LoginActivity.this);
                        sessionManagement.createLoginSession(user_fullname, user_phone,userid);


                        btn_register.setEnabled(false);

                    } else if (status.contains("2")){
                        progressDialog.dismiss();
                        Intent i = new Intent(LoginActivity.this, Select_Society.class);
                        i.putExtra("user_phone",mobile.getText().toString());
                        startActivity(i);
                        finish();
                        Animatoo.animateSlideLeft(LoginActivity.this);
                        String error = response.getString("message");
                        btn_register.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        String error = response.getString("message");
                        btn_register.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
