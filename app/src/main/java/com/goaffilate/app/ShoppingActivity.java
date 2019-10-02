package com.goaffilate.app;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import com.goaffilate.app.utils.CustomViewPager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShoppingActivity extends AppCompatActivity {
String appname,applink,categoryname;

     SparseArray<TestFragment> mTestFragments,searchfragments;
     PagerAdapter mPagerAdapter;

     Searchtheadapter searchadapter;
    private int key=0;

    AutoCompleteTextView autoCompleteTextView;

    String search_item;

    ArrayList<String> imagelist=new ArrayList<>();

    SearchView searchView;
    Toolbar toolbar;
    private boolean doNotifyDataSetChangedOnce = false;

    ArrayList<String> mylist=new ArrayList<>();
    LinearLayout l1;
    public  String appcolour;
    TabLayout tabLayout;
    String applogo;
    CustomViewPager viewPager;
    int padding;
    int mCurPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);


//        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView=findViewById(R.id.search_view_store);

        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                searchView.setFocusable(true);
                searchView.setIconified(false);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search_item = s.replace(" ", "%20");

                searchdata();

                doNotifyDataSetChangedOnce=true;


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {

                return false;
            }

        });




        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        viewPager = (CustomViewPager) findViewById(R.id.pager);
                 /* limit is a fixed integer*/

        viewPager.setOffscreenPageLimit(key);
          categoryname=getIntent().getStringExtra("category_name");
        if (categoryname.contains("Shopping")){
            toolbar.setVisibility(View.VISIBLE);
        }
        else {
            toolbar.setVisibility(View.GONE);
        }


        tabLayout.setupWithViewPager(viewPager);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override

            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                if (categoryname.contains("Shopping")){
                    viewPager.setPagingEnabled(false);

                    if (tab.getPosition()==0){

                        tabLayout.setBackgroundColor(Color.parseColor("#232f3e"));
                        toolbar.setBackgroundColor(Color.parseColor("#232f3e"));
                    }
                    else if (tab.getPosition()==1){
                        tabLayout.setBackgroundColor(Color.parseColor("#2874f0"));
                        toolbar.setBackgroundColor(Color.parseColor("#2874f0"));
                    }
                    else if (tab.getPosition()==2){
                        tabLayout.setBackgroundColor(Color.parseColor("#e40046"));
                        toolbar.setBackgroundColor(Color.parseColor("#e40046"));
                    }

                    else {
                        tabLayout.setBackgroundColor(Color.parseColor("#FF0E709C"));
                        toolbar.setBackgroundColor(Color.parseColor("#FF0E709C"));

                    }
                }
//
                else {
                    viewPager.setPagingEnabled(true);

                    for (int i = 0; i < imagelist.size(); i++) {

                        tabLayout.getTabAt(i).setCustomView(createTabItemView(imagelist.get(i)));

//                    }
                    }

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

//                tabLayout.setBackgroundColor(Color.parseColor("#2874f0"));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//

        mTestFragments = new SparseArray<>();

        searchfragments=new SparseArray<>();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos=position;
                Log.d("sort:", "onPageSelected: "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        if (ConnectivityReceiver.isConnected()){

            getdata();


        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingActivity.this);
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
           imagelist.clear();
        String JSON_URL = BaseURL.Group_app;
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
for (int j=0;j<heroArray.length();j++) {


    JSONObject appobj = heroArray.getJSONObject(j);

    String cat_name=appobj.getString("group_name");

    if (categoryname.contains(cat_name)){
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

                    applogo=BaseURL.IMG_CATEGORY_URL+heroObject1.getString("app_logo");

                    applink = heroObject1.getString("app_link");


                    appcolour=heroObject1.getString("app_hex_code");

                    mylist.add(appname);

                    imagelist.add(applogo);


                }
            }

            mTestFragments.put(key++, TestFragment.newInstance(applink, appname));

        }

    }

}
if (categoryname.contains("Shopping")){
    mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mTestFragments);

    viewPager.setAdapter(mPagerAdapter);

}
else {
    imageadapter iaa=new imageadapter(getSupportFragmentManager(),mTestFragments);
    viewPager.setAdapter(iaa);
}

                            doNotifyDataSetChangedOnce=true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(ShoppingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void searchdata(){

      searchfragments.clear();

      String JSON_URL = BaseURL.Search_APP;
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
                            for (int j=0;j<heroArray.length();j++) {


                                JSONObject appobj = heroArray.getJSONObject(j);

                                String cat_name=appobj.getString("group_name");
                                if (categoryname.contains(cat_name)){

                                    JSONArray apparray = appobj.getJSONArray("app");


                                    //now looping through all the elements of the json array

                                    for (int i = 0; i < apparray.length(); i++) {
                                        if (i < apparray.length()) {

                                            JSONObject heroObject1 = apparray.getJSONObject(i);

                                            if (heroObject1 != null) {

                                                appname = heroObject1.getString("app_name");
                                                  applogo =heroObject1.getString("app_logo");
                                                applink = heroObject1.getString("app_link");
                                                appcolour=heroObject1.getString("app_hex_code");
                                                mylist.add(appname);
                                            }
                                        }

                                        searchfragments.put(key++, TestFragment.newInstance(applink+search_item, appname));

                                    }
                                }

                            }

                            searchadapter = new Searchtheadapter(getSupportFragmentManager(), searchfragments);

                            viewPager.setAdapter(searchadapter);







                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(ShoppingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }
    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                tabView.setMinimumWidth(0);
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
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

    public  class PagerAdapter extends FragmentPagerAdapter {


        SparseArray<TestFragment> mTestFragments;


        public PagerAdapter(FragmentManager fm, SparseArray<TestFragment> testFragments) {
            super(fm);
            this.mTestFragments = testFragments;
        }

        @Override

        public Fragment getItem(int position) {

            TestFragment testFragment=mTestFragments.valueAt(position);

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
    public  class imageadapter extends FragmentPagerAdapter {


        SparseArray<TestFragment> mTestFragments;


        public imageadapter(FragmentManager fm, SparseArray<TestFragment> testFragments) {
            super(fm);
            this.mTestFragments = testFragments;
        }

        @Override

        public Fragment getItem(int position) {

            TestFragment testFragment=mTestFragments.valueAt(position);

            return testFragment;
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


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return true;
//    }
    public  class Searchtheadapter extends FragmentPagerAdapter {


        SparseArray<TestFragment> mTestFragments;


        public Searchtheadapter(FragmentManager fm, SparseArray<TestFragment> testFragments) {
            super(fm);
            this.mTestFragments = testFragments;
        }

        @Override
        public Fragment getItem(int position) {

            TestFragment testFragment=mTestFragments.valueAt(position);
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

    private View createTabItemView(String imgUri) {
        ImageView imageView = new ImageView(this);
        TabLayout.LayoutParams params = new TabLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        Picasso.with(this).load(imgUri).into(imageView);

        return imageView;
    }
}
