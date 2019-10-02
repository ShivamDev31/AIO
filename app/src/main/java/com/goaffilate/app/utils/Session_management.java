package com.goaffilate.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.goaffilate.app.LoginActivity;
import com.goaffilate.app.MainActivity;

import java.util.HashMap;


import static com.goaffilate.app.utils.BaseURL.IS_LOGIN;
import static com.goaffilate.app.utils.BaseURL.KEY_Id;
import static com.goaffilate.app.utils.BaseURL.KEY_MOBILE;
import static com.goaffilate.app.utils.BaseURL.KEY_NAME;
import static com.goaffilate.app.utils.BaseURL.PREFS_NAME;
import static com.goaffilate.app.utils.BaseURL.PREFS_NAME2;

/**
 * Created by Rajesh Dabhi on 28/6/2017.
 */

public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {

        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();

    }

    public void createLoginSession( String name
            , String mobile,String id) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE,mobile);
        editor.putString(KEY_Id,id);
        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));
        user.put(KEY_Id,null);
        // return user
        return user;
    }

    public void updateData(String name, String mobile,String id) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_Id,id);
        editor.apply();
    }


    public void logoutSession() {
        editor.clear();
        editor.commit();



        Intent logout = new Intent(context, MainActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();


        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }


    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
