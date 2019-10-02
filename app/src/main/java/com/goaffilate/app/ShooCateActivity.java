package com.goaffilate.app;

import android.content.res.Resources;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.goaffilate.app.adapter.Cat_subadapter;
import com.goaffilate.app.model.cat_submodel;
import com.goaffilate.app.utils.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShooCateActivity extends AppCompatActivity {
    RecyclerView cat_rc;

    String categoryname, appname, applink, applogo;
    Toolbar toolbar;
    int padding;
    Cat_subadapter cat_subadapter;

    private List<cat_submodel> cat_submodellist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        cat_rc = findViewById(R.id.cat_rc);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(ShooCateActivity.this, 3);
        cat_rc.setLayoutManager(gridLayoutManager1);
        cat_rc.setItemAnimator(new DefaultItemAnimator());
        cat_rc.setNestedScrollingEnabled(false);
        cat_rc.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));

        categoryname = getIntent().getStringExtra("category");
        Log.d("cat_name", categoryname);
        searchdata();

    }

    private void searchdata() {

        cat_submodellist.clear();

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


                                        JSONObject heroObject1 = apparray.getJSONObject(i);


                                        appname = heroObject1.getString("app_name");

                                        applink = heroObject1.getString("app_link");
                                        applogo = heroObject1.getString("app_logo");

                                        cat_submodel cat_submodel = new cat_submodel();

                                        cat_submodel.setApp_name(appname);

                                        cat_submodel.setApp_logo(applogo);

                                        cat_submodel.setApp_link(applink);

                                        cat_submodellist.add(cat_submodel);

                                    }
                                }
                            }
                            cat_subadapter = new Cat_subadapter(cat_submodellist);

                            cat_rc.setAdapter(cat_subadapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(ShooCateActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {

        Resources r = getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return true;
    }

}
