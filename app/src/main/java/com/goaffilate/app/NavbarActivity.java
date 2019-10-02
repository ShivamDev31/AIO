package com.goaffilate.app;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goaffilate.app.adapter.TestFragment;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NavbarActivity extends AppCompatActivity {
    String appname, applink, categoryname;

    SparseArray<TestFragment> mTestFragments, searchfragments;
    PagerAdapter mPagerAdapter;
    private int key = 0;
    private boolean doNotifyDataSetChangedOnce = false;

    ArrayList<String> mylist = new ArrayList<>();
    LinearLayout l1;
    public String appcolour;
    TabLayout tabLayout;
    ViewPager viewPager;
    int padding;
    int mCurPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

                setContentView(R.layout.activity_navbar);


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setBackgroundColor(Color.parseColor("#232f3e"));

        viewPager = (ViewPager) findViewById(R.id.pager);

        categoryname = getIntent().getStringExtra("category_name");
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());


                    if (tab.getPosition() == 0) {

                        tabLayout.setBackgroundColor(Color.parseColor("#232f3e"));
                    } else if (tab.getPosition() == 1) {
                        tabLayout.setBackgroundColor(Color.parseColor("#2874f0"));
                    } else if (tab.getPosition() == 2) {
                        tabLayout.setBackgroundColor(Color.parseColor("#e40046"));
                    } else {
                        tabLayout.setBackgroundColor(Color.parseColor("#FF0E709C"));

                    }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//
//
        mTestFragments = new SparseArray<>();
        searchfragments = new SparseArray<>();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos = position;

                Log.d("sort:", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (ConnectivityReceiver.isConnected()) {

            getdata();


        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(NavbarActivity.this);
            builder.setMessage("Check Your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }


    }

    private void getdata() {

        String JSON_URL = BaseURL.Nav_app;
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("data");
                            for (int j = 0; j < heroArray.length(); j++) {


                                JSONObject appobj = heroArray.getJSONObject(j);

                                String cat_name = appobj.getString("navbar_app_name");
                                if (categoryname.contains(cat_name)) {
                                    JSONArray apparray = appobj.getJSONArray("app");


                                    //now looping through all the elements of the json array

                                    for (int i = 0; i < apparray.length(); i++) {
                                        if (i < apparray.length()) {
//                if (cat_name.contains("Social")){
////                    l1.setVisibility(View.GONE);
//                }
                                            JSONObject heroObject1 = apparray.getJSONObject(i);

                                            if (heroObject1 != null) {

                                                appname = heroObject1.getString("app_name");

                                                applink = heroObject1.getString("app_link");
                                                appcolour = heroObject1.getString("app_hex_code");
                                                mylist.add(appname);
                                            }
                                        }

                                        mTestFragments.put(key++, TestFragment.newInstance(applink, appname));

                                    }
                                }

                            }
                            mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTestFragments);

                            viewPager.setAdapter(mPagerAdapter);

                            doNotifyDataSetChangedOnce = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(NavbarActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void setMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {


        SparseArray<TestFragment> mTestFragments;


        public PagerAdapter(FragmentManager fm, SparseArray<TestFragment> testFragments) {
            super(fm);
            this.mTestFragments = testFragments;
        }

        @Override

        public Fragment getItem(int position) {

            TestFragment testFragment = mTestFragments.valueAt(position);

            return testFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mylist.get(position);

        }


        @Override
        public int getCount() {

            if (doNotifyDataSetChangedOnce) {
                doNotifyDataSetChangedOnce = false;
                notifyDataSetChanged();
            }

            return mTestFragments.size();

        }


        @Override
        public long getItemId(int position) {

            return mTestFragments.keyAt(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;


        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }
    }
}