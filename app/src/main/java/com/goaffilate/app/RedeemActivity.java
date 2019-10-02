package com.goaffilate.app;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends AppCompatActivity {
EditText amount,paytmnumber;
String userid;
ProgressDialog progressDialog;
Button redeem;
    public static String TAG="Redeem";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_redeem);
        progressDialog=new ProgressDialog(this);
        userid=getIntent().getStringExtra("user_id");
        amount=findViewById(R.id.et_amount);

        paytmnumber=findViewById(R.id.et_paytm_mobile);

        redeem=findViewById(R.id.redeem);

redeem.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (amount.getText().toString().length()==0){
            Toast.makeText(RedeemActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
        }
        else if (paytmnumber.getText().toString().length()==0){
            Toast.makeText(RedeemActivity.this, "Enter Paytm Number", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.show();
            progressDialog.setMessage("Loading");
            redeem();
        }

    }
});
    }
    private void redeem() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("redeem_points", amount.getText().toString());
        params.put("paytm_number",paytmnumber.getText().toString());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Redeem_amount, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {
                        progressDialog.dismiss();
                        String msg = response.getString("message");
                        Toast.makeText(RedeemActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        JSONObject obj = response.getJSONObject("data");


                        redeem.setEnabled(false);

                    }
                    else {
                        progressDialog.dismiss();
                        String error = response.getString("message");
                        redeem.setEnabled(true);
                        Toast.makeText(RedeemActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RedeemActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
