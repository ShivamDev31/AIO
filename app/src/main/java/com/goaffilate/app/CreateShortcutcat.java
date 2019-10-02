package com.goaffilate.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.graphics.drawable.Icon.createWithBitmap;
import static android.graphics.drawable.Icon.createWithResource;

public class CreateShortcutcat extends AppCompatActivity {

    private static final String TAG = "CreateShortcutcat";

    Bitmap bm;

    private String shortcutId,shortcutlink,shortcutimage;

    private void finishActivity (String result) {
        Intent i = new Intent();
        i.putExtra("result", result);
        setResult(Activity.RESULT_OK, i);

        if(android.os.Build.VERSION.SDK_INT >= 21) {

            this.finishAndRemoveTask();

        }

        else {
            this.finish();
        }
    }

    class WaitFor extends AsyncTask<Void,Void,Void> {

        final int waitPeriod;

        private WaitFor (int N) {
            this.waitPeriod = N * 1000;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(waitPeriod);
                Intent bi = new Intent(shortcutId);
                bi.putExtra("msg", "deny");
                sendBroadcast(bi);
            }
            catch (InterruptedException ignore) {
            }
            return null;
        }
    }
    private void postApi26CreateShortcutcat(Context c, Class scClass) {
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
                    Toast.makeText(c , String.format(
                            "Shortcut %s already exists.", shortcutId
                            )
                            , Toast.LENGTH_LONG
                    ).show();
                    finishActivity("shortcutExists");
                }
                else {
                    // this intent is used to wake up the broadcast receiver.
                    // I couldn't get CreateShortcutcatResultIntent to work but
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
                    shortcutIntent.putExtra("category_name",shortcutId);
                    shortcutIntent.putExtra("categorylink",shortcutlink);
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
    private void preApi26CreateShortcutcat (Activity activity, Class scClass) {

        Intent shortcutIntent = new Intent(getApplicationContext(), scClass);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutId);
        addIntent.putExtra("category_name",shortcutId);
        addIntent.putExtra("categorylink",shortcutlink);
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

    private void CreateShortcutcat(Activity activity, Class scClass) {
        if (Build.VERSION.SDK_INT >= 26)
            postApi26CreateShortcutcat(activity, scClass);
        else
            preApi26CreateShortcutcat(activity, scClass);
    }

    private void promptForShortcut(Activity activity, Class scClass) {

        String promptMess = getString(R.string.promptForShortcut, shortcutId);

        new AlertDialog.Builder(activity)

                .setTitle(R.string.promptForShortcutTitle)

                .setMessage(promptMess)

                .setNegativeButton(R.string.NO,(dialog, which) -> finishActivity("deny"))

                .setPositiveButton(R.string.YES

                        , (dialog, which) -> CreateShortcutcat(activity, scClass))

                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_shortcut);

        Intent i = getIntent();

        String scid = i.getStringExtra("catname");


        shortcutlink=i.getStringExtra("categorylink");

        shortcutId = ((scid == null) ? "NULL" : scid);

      shortcutimage=getIntent().getStringExtra("cat_image");

        setIntent(null);
        MyAsync obj = new MyAsync(){

            @Override
            protected void onPostExecute(Bitmap bmp) {
                super.onPostExecute(bmp);

                bm = bmp;


            }
        };
        obj.execute();
        promptForShortcut(CreateShortcutcat.this, CategoryWebview.class);
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
}