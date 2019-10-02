package com.goaffilate.app.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.R;
import com.goaffilate.app.StoreshowActvity;
import com.goaffilate.app.adapter.StoreAdapter;
import com.goaffilate.app.adapter.StoreBanner;
import com.goaffilate.app.model.StoreModel;
import com.goaffilate.app.model.StoreModelrc;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.RecyclerTouchListener;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStore extends Fragment  {


    ViewPager  bannerslider;
    String bannerlink;
    SharedPreferences locpref;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String userid;
    String latitude,longitude;
    RecyclerView show_storepostrc;
    SearchView searchView;
    List<StoreModel> sildermodelslist = new ArrayList<>();
    List<StoreModelrc> storeModelrcs = new ArrayList<>();
    String search;
    String measuringdis;
    private static int NUM_PAGES = 0;
    String storename,storeimage,storephone,location,storesociety;
    private static int currentPage = 0;
    CirclePageIndicator indicator;

    StoreAdapter storeAdapter;

    public FragmentStore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =   inflater.inflate(R.layout.fragment_fragment_store, container, false);


//        latitude=locpref.getString("lat","");
//        longitude=locpref.getString("lng","");
        searchView=view.findViewById(R.id.search_view_store);

        bannerslider=view.findViewById(R.id.viewpager_banner);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Please Wait");
        indicator = (CirclePageIndicator) view.findViewById(R.id.view);
        show_storepostrc=view.findViewById(R.id.show_storepost);
        show_storepostrc.setLayoutManager(new GridLayoutManager( getContext(),1));
        show_storepostrc.setHasFixedSize(true);
        sharedPreferences=getActivity().getSharedPreferences("userid",MODE_PRIVATE);

        userid=sharedPreferences.getString("user","");

        bannerslider.setClipToPadding(false);

        bannerslider.setPageMargin(20);

        storeshow();









         show_storepostrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), show_storepostrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(getContext(), StoreshowActvity.class);
                intent.putExtra("name", storeModelrcs.get(position).getStore_name());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


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

                search=s.replace(" ", "%20");;
             progressDialog.show();
                searchapi();

                storeModelrcs.clear();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {

                return false;
            }

        });




        bannerslider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int postion) {
//                Intent intent = new Intent(getContext(), BannerActivity.class);
//                intent.putExtra("bannerlink", sildermodelslist.get(postion).getBanner_link());
//
//                startActivity(intent);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        banner();
        return view;

    }

    private void banner() {

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("params", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.StoreBanner, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {

                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject objJson = null;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                objJson = jsonArray.getJSONObject(i);

                                String name = objJson.getString("banner_name");

                                bannerlink = objJson.getString("banner_link");

                                String image = objJson.getString("banner_image");

                                sildermodelslist.add(new StoreModel(image, bannerlink));

                                NUM_PAGES = sildermodelslist.size();


                            }

                        }
                        else {
                        }
                        if (getActivity()!=null){
                            final float density = getResources().getDisplayMetrics().density;
//
//                            indicator.setRadius(5 * density);

                            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage = position;

                                }

                                @Override
                                public void onPageScrolled(int pos, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int pos) {

                                }
                            });


                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    bannerslider.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 3000, 3000);


                            StoreBanner silderAdapter1 = new StoreBanner(sildermodelslist,getContext());

                            bannerslider.setAdapter(silderAdapter1);

                            indicator.setViewPager(bannerslider);

                        }
                    }

                    else {
                        Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void searchapi(){

        String tag_json_obj = "json_category_req";

        storeModelrcs.clear();

        Map<String, String> params = new HashMap<String, String>();
        params.put("keyword", search);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Search_Store, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            progressDialog.dismiss();
                          JSONArray jsonArray=response.getJSONArray("data");
                          for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                storename=jsonObject.getString("store_name");
                                storeimage=jsonObject.getString("store_images");
                                storephone=jsonObject.getString("store_phone");
                                storesociety=jsonObject.getString("store_society");
                                location=jsonObject.getString("location");

                                JSONArray storeimarr=new JSONArray(storeimage);

                                String arraystring= String.valueOf(storeimarr.get(0));

                              StoreModelrc storeModeldata=new StoreModelrc();

                             storeModeldata.setStore_society(storesociety);
                            storeModeldata.setLocation(location);
                             storeModeldata.setStore_name(storename);

//                             storeModeldata.setLat(lat);
//
//                             storeModeldata.setLon(lon);

                             storeModeldata.setStore_phone(storephone);

                             storeModeldata.setStore_images(arraystring);

                              storeModelrcs.add(storeModeldata);

                          }
                            storeAdapter = new StoreAdapter(storeModelrcs);
                            show_storepostrc.setAdapter(storeAdapter);
                            storeAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void storeshow(){

        String tag_json_obj = "json_category_req";

        storeModelrcs.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Store_url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status = response.getString("status");
                        if (status.contains("1")) {
                            progressDialog.dismiss();
                            JSONArray jsonArray=response.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                storename=jsonObject.getString("store_name");
                                storeimage=jsonObject.getString("store_images");
                                storephone=jsonObject.getString("store_phone");
                                storesociety=jsonObject.getString("store_society");
                                location=jsonObject.getString("location");

                                JSONArray storeimarr=new JSONArray(storeimage);

                                String arraystring= String.valueOf(storeimarr.get(0));

                                StoreModelrc storeModeldata=new StoreModelrc();
                                storeModeldata.setLocation(location);
                                storeModeldata.setStore_society(storesociety);
                                storeModeldata.setStore_name(storename);
                                storeModeldata.setStore_phone(storephone);
                                storeModeldata.setStore_images(arraystring);

                                storeModelrcs.add(storeModeldata);

                            }
                            storeAdapter = new StoreAdapter(storeModelrcs);
                            show_storepostrc.setAdapter(storeAdapter);
                            storeAdapter.notifyDataSetChanged();
                        }
                        else{
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }



//    private double distance(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//         dist = Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        return (dist);
//    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
