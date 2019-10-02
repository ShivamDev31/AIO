package com.goaffilate.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.VolleyMultipartRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
Button btn_register;

EditText mobile,name,referalcode;
TextView btn_login,click;
    private static final int SELECT_PICTURE = 200;
    CircleImageView iv;
    Bitmap bitmap;
LinearLayout show;
String status,message;
ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    String value="false";
    ImageView add;
public static String TAG="Register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
progressDialog=new ProgressDialog(this);
click=findViewById(R.id.click);
show=findViewById(R.id.show);
referalcode=findViewById(R.id.et_req_refral);
        token = FirebaseInstanceId.getInstance().getToken();
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        btn_register=findViewById(R.id.submit);
mobile=findViewById(R.id.et_req_mobile);
name=findViewById(R.id.et_reg_name);
btn_login=findViewById(R.id.login);
        add=findViewById(R.id.add);
iv=findViewById(R.id.user_image);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobile.getText().toString().length()==0){
                    Toast.makeText(RegisterActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else if (name.getText().toString().length()==0){
                    Toast.makeText(RegisterActivity.this, "Enter User Name", Toast.LENGTH_SHORT).show();
                }
                else if (value.contains("false")) {
                    Toast.makeText(RegisterActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.show();
                    progressDialog.setMessage("Loading");
                    serverpr();                }


            }



        });
iv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openImageChooser();
        value="true";
    }
});
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
                value="true";
            }
        });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             referalcode.setVisibility(View.VISIBLE);
             show.setVisibility(View.GONE);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideLeft(RegisterActivity.this);
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        Animatoo.animateSlideRight(RegisterActivity.this);


    }
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void serverpr(){

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BaseURL.REGISTER_URL

                ,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            JSONObject data = obj.getJSONObject("data");

                            if (status.contains("1")) {

                                String user_fullname = data.getString("user_name");
                                String user_phone = data.getString("user_phone");
                                String userprofile=data.getString("user_image");
                                String userid=data.getString("id");

                                send();

                                editor.putString("user",userid);

                                editor.putString("user_image",userprofile);

                                editor.putString("username",user_fullname);

                                editor.commit();






                                Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();


                                btn_register.setEnabled(false);


                            } else {

//                                Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                btn_register.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", name.getText().toString());

                params.put("user_phone", mobile.getText().toString());

                params.put("referral_code",referalcode.getText().toString());
                ;
                params.put("device_id",token);

                return params;
            }

            //
//                /*
//                 * Here we are passing image by renaming it with a unique name
//                 * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();

                params.put("user_profile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));


                return params;
            }
        };

        //adding the request to volley
       Volley.newRequestQueue(this).add(volleyMultipartRequest);
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
//        AppController.getInstance().addToRequestQueue(volleyMultipartRequest, "");

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);


                //displaying selected image to imageview
                iv.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void send() {

        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,

                BaseURL.GetOn_off, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    status = response.getString("status");

                    if (status.contains("1")) {
                        progressDialog.dismiss();

                        Animatoo.animateSlideLeft(RegisterActivity.this);
                        Intent intent = new Intent(RegisterActivity.this, Otp_Activity.class);


                        intent.putExtra("user_phone",mobile.getText().toString());

                        intent.putExtra("user_name",name.getText().toString());

                        intent.putExtra("otp","");

                        startActivity(intent);



                    } else {

                        progressDialog.dismiss();

                        Intent intent = new Intent(RegisterActivity.this, Otp_Activity.class);

                        intent.putExtra("user_phone",mobile.getText().toString());

                        intent.putExtra("user_name",name.getText().toString());

                        intent.putExtra("otp","1234");

                        startActivity(intent);



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
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
