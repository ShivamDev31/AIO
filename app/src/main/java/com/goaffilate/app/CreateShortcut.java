package com.goaffilate.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.adapter.HomeAdapter;
import com.goaffilate.app.model.PartnerModel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.drawable.Icon.createWithBitmap;
import static android.graphics.drawable.Icon.createWithResource;

public class CreateShortcut extends AppCompatActivity {

    private static final String TAG = "CreateShortcut";
    public static Bitmap bm;
    private String shortcutId, shortcutlink;
    String shortcutimage,userid,id;
    SharedPreferences sharedPreferences;
    private void finishActivity(String result) {
        Intent i = new Intent();
        i.putExtra("result", result);
        setResult(Activity.RESULT_OK, i);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CreateShortcut.this.finishAndRemoveTask();
        } else {
            CreateShortcut.this.finish();
        }
    }

    class WaitFor extends AsyncTask<Void, Void, Void> {
        final int waitPeriod;

        private WaitFor(int N) {
            this.waitPeriod = N * 1000;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(waitPeriod);
                Intent bi = new Intent(shortcutId);
                bi.putExtra("msg", "deny");
                sendBroadcast(bi);
            } catch (InterruptedException ignore) {
            }
            return null;
        }
    }

    private void postApi26CreateShortcut(Context c, Class scClass) {
        if (Build.VERSION.SDK_INT >= 26) {
            ShortcutManager sm = getSystemService(ShortcutManager.class);
            if (sm != null && sm.isRequestPinShortcutSupported()) {
                boolean shortcutExists = false;
                // We create the shortcut multiple times if given the
                // opportunity.  If the shortcut exists, put up
                // a toast message and exit.
                List<ShortcutInfo> shortcuts = sm.getPinnedShortcuts();
                for (int i = 0; i < shortcuts.size() && !shortcutExists; i++)
                    shortcutExists = shortcuts.get(i).getId().equals(shortcutId);
                if (shortcutExists) {
                    Toast.makeText(c, String.format(
                            "Shortcut %s already exists.", shortcutId
                            )
                            , Toast.LENGTH_LONG
                    ).show();
                    finishActivity("shortcutExists");
                } else {
                    // this intent is used to wake up the broadcast receiver.
                    // I couldn't get createShortcutResultIntent to work but
                    // just a simple intent as used for a normal broadcast
                    // intent works fine.
                    Intent broadcastIntent
                            = new Intent(shortcutId);
                    broadcastIntent.putExtra("msg", "approve");
                    // wait up to N seconds for user input, then continue
                    // on assuming user's choice was deny.
                    final AsyncTask<Void, Void, Void> waitFor
                            = new WaitFor(10).execute();
                    // create an anonymous broadcaster.  Unregister when done.
                    registerReceiver(new BroadcastReceiver() {
                                         @Override
                                         public void onReceive(Context c, Intent intent) {
                                             @SuppressWarnings("unused")
                                             String msg = intent.getStringExtra("msg");
                                             if (msg == null) msg = "NULL";
                                             unregisterReceiver(this);
                                             waitFor.cancel(true);
                                             Log.d(TAG, String.format(
                                                     "ShortcutReceiver activity = \"$1%s\" : msg = %s"
                                                     , intent.getAction()
                                                     , msg)
                                             );
                                             finishActivity(msg);
                                         }
                                     }
                            , new IntentFilter(shortcutId)
                    );

                    // this is the intent that actually creates the shortcut.
                    Intent shortcutIntent
                            = new Intent(c, scClass);
                    shortcutIntent.putExtra("category_name", shortcutId);
                    shortcutIntent.setAction(shortcutId);


                    ShortcutInfo shortcutInfo = new ShortcutInfo
                            .Builder(c, shortcutId)
                            .setShortLabel(shortcutId)
                            .setIcon(createWithBitmap(bm))
                            .setIntent(shortcutIntent)
                            .build();
                    PendingIntent successCallback = PendingIntent.getBroadcast(
                            c, 99
                            , broadcastIntent, 0);
                    // Shortcut gets created here.
                    sm.requestPinShortcut(shortcutInfo
                            , successCallback.getIntentSender());
                }
            }
        }
    }

    private void preApi26CreateShortcut(Activity activity, Class scClass) {
        Intent shortcutIntent = new Intent(getApplicationContext(), scClass);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutId);
        addIntent.putExtra("category_name", shortcutId);
        Intent.ShortcutIconResource icon =
                Intent.ShortcutIconResource.fromContext(activity, R.drawable.shopicon);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);
        activity.sendBroadcast(addIntent);
        Toast.makeText(activity
                , "Created pre-api26 shortcut"
                , Toast.LENGTH_LONG)
                .show();
        finishActivity("allow");
    }

    private void createShortcut(Activity activity, Class scClass) {
        if (Build.VERSION.SDK_INT >= 26)
            postApi26CreateShortcut(activity, scClass);
        else
            preApi26CreateShortcut(activity, scClass);
    }

    private void promptForShortcut(Activity activity, Class scClass) {
        String promptMess = getString(R.string.promptForShortcut, shortcutId);
        new AlertDialog.Builder(activity)
                .setTitle(R.string.promptForShortcutTitle)
                .setMessage(promptMess)
                .setNegativeButton(R.string.NO, (dialog, which) -> finishActivity("deny"))
                .setPositiveButton(R.string.YES
                        , (dialog, which) -> createShortcut(activity, scClass))
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_shortcut);
        sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");

        id=getIntent().getStringExtra("id");

        Intent i = getIntent();

        String scid = i.getStringExtra("catname");

        shortcutimage = getIntent().getStringExtra("cat_image");

        shortcutId = ((scid == null) ? "NULL" : scid);

        setIntent(null);

        MyAsync obj = new MyAsync(){
            @Override
            protected void onPostExecute(Bitmap bmp) {
                super.onPostExecute(bmp);

                 bm = bmp;

            }
        };

        obj.execute();

addpoints();

        promptForShortcut(CreateShortcut.this, ShoppingActivity.class);

    }


    public class MyAsync extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... params) {

            try {
                URL url = new URL(shortcutimage);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }
    private void addpoints() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("id",id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Add_app, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");

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
                    Toast.makeText(CreateShortcut.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
