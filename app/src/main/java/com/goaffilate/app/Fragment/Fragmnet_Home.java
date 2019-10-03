package com.goaffilate.app.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.goaffilate.app.BannerActivity;
import com.goaffilate.app.CategoryWebview;
import com.goaffilate.app.MainActivity;
import com.goaffilate.app.NavbarActivity;

import com.goaffilate.app.NewsActivity;
import com.goaffilate.app.R;
import com.goaffilate.app.Scratch;
import com.goaffilate.app.adapter.CatAdapter;
import com.goaffilate.app.adapter.CategoryAdapter;
import com.goaffilate.app.adapter.HomeAdapter;
import com.goaffilate.app.adapter.NewsfeedAdapter;
import com.goaffilate.app.adapter.ScratchAdapter;
import com.goaffilate.app.adapter.ShopingAdpater;
import com.goaffilate.app.adapter.SilderAdapter;
import com.goaffilate.app.adapter.TopAdapter;
import com.goaffilate.app.adapter.TopbannerAdapter;
import com.goaffilate.app.model.CategoryModel;
import com.goaffilate.app.model.PartnerModel;
import com.goaffilate.app.model.ScratchModel;
import com.goaffilate.app.model.Sildermodel;
import com.goaffilate.app.model.TopModels;
import com.goaffilate.app.model.TopbannerModel;
import com.goaffilate.app.model.newsfeedmodel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.ConnectivityReceiver;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.goaffilate.app.utils.RecyclerTouchListener;
import com.goaffilate.app.utils.Session_management;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.lang.reflect.Type;
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
public class Fragmnet_Home extends Fragment {

    SharedPreferences sharedPreferences;
    AlertDialog.Builder alertDialog;
    SharedPreferences.Editor editor;
    String versionName = "";
    int versionCode = -1;
    ActionBar ab;
    String value;
    AdView adView;
    RelativeLayout l1_newsfeed;
    private static final String TAG = "MainActivity";
    ProgressDialog progressDialog;
    Session_management sessionManagement;

    public static RelativeLayout l1_scratch, l1_partner;

    EditText editText;

    String appid, intersitialid, bannerid;

    public static Activity activity;
    public static String userid;

    SearchView searchView;


    int padding = 0;


    String bannerlink;


    TextView showpoint;

    String status;

    private ViewPager bannerslider;

    private List<Sildermodel> sildermodelslist = new ArrayList<>();

    private List<TopbannerModel> topbannermodellist = new ArrayList<>();

    public static List<ScratchModel> scratchModels = new ArrayList<>();

    private static int currentPage = 0;

    private static int NUM_PAGES = 0;

    CirclePageIndicator indicator;

    RecyclerView partnerrc, toprc, categoryrc, topbannerrc, newsfeedrc;

    RecyclerView shoppingrc, hotelrc, travelrc, socialrc;

    public static RecyclerView rcscratch;
    NavigationView navigationView;
    //    CircleImageView iv;
    String logoimage, logoname;

    TextView nameuser, title, unique;

    ImageView logo;

    public static ScratchAdapter scratchAdapter;

    DrawerLayout drawer;

    private List<PartnerModel> category_modelList = new ArrayList<>();

    List<TopModels> topModelsList, socialmodelist, hotelmodelist, shoppingmodelist, travelmodellist;

    private List<CategoryModel> cat_list = new ArrayList<>();
    private List<newsfeedmodel> newsmodellist = new ArrayList<>();


    String username;

    private HomeAdapter adapter;

    private CategoryAdapter categoryAdapter;
    NewsfeedAdapter newsfeedAdapter;
    TopbannerAdapter topbannerAdapter;

    TopAdapter topAdapter, hoteladapter, traveladapter;

    ShopingAdpater shoppingadapter;
    CatAdapter socialadapter;
    public static Menu nav_menu;

    InterstitialAd mInterstitialAd;


    public Fragmnet_Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmnet__home, container, false);


        MobileAds.initialize(getActivity(), getString(R.string.admob_app_id));

        alertDialog = new AlertDialog.Builder(getContext());

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.show();

        progressDialog.setMessage("Please Wait");

        sharedPreferences = getContext().getSharedPreferences("userid", MODE_PRIVATE);

        userid = sharedPreferences.getString("user", "");

        l1_newsfeed = view.findViewById(R.id.l1_newsfeed);

        username = sharedPreferences.getString("username", "");

        newsfeedrc = view.findViewById(R.id.newsfeedrc);

        MainActivity.toolbar.setVisibility(View.VISIBLE);

        l1_scratch = view.findViewById(R.id.l1_scratch);

        l1_partner = view.findViewById(R.id.l1_partner);

        topbannerrc = view.findViewById(R.id.topbannerrc);

        WebView web = (WebView) view.findViewById(R.id.webview);
        web.loadUrl("https://m.publicvibe.com/#/list");
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setDomStorageEnabled(true);

        web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web.setWebChromeClient(new WebChromeClient());

        web.setWebViewClient(new myWebClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            web.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        web.canGoBack();
        web.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && web.canGoBack()) {
                    web.goBack();
                    return true;
                }
                return false;
            }
        });




//      / myWebView = new WebView(getContext());
//        getActivity().setContentView(myWebView);
//        myWebView.loadUrl("https://m.publicvibe.com/#/list");



        admob();

        adView = view.findViewById(R.id.adView);
        adView=new AdView(getActivity());
//        adView.setAdSize(AdSize.BANNER);


        adView.setAdUnitId(getActivity().getResources().getString(R.string.admob_app_id));

        adView.setAdSize(AdSize.BANNER);

        AdRequest adRequest = new AdRequest.Builder()
                .build();



        adView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getActivity());


        topModelsList = new ArrayList<>();

        socialmodelist = new ArrayList<>();

        sessionManagement = new Session_management(getContext());

        shoppingmodelist = new ArrayList<>();

        hotelmodelist = new ArrayList<>();

        travelmodellist = new ArrayList<>();

        rcscratch = view.findViewById(R.id.rc_scratch);

        bannerslider = view.findViewById(R.id.viewpager);

        sildermodelslist = new ArrayList<Sildermodel>();

        indicator = (CirclePageIndicator) view.findViewById(R.id.view);

        bannerslider.setClipToPadding(false);

        bannerslider.setPageMargin(20);



        bannerslider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int postion) {
                Intent intent = new Intent(getContext(), BannerActivity.class);
                intent.putExtra("bannerlink", sildermodelslist.get(postion).getBannerlink());

                startActivity(intent);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        partnerrc = view.findViewById(R.id.partnerrc);

        categoryrc = view.findViewById(R.id.categoryrc);

        toprc = view.findViewById(R.id.toprc);

        shoppingrc = view.findViewById(R.id.shoppingrc);

        hotelrc = view.findViewById(R.id.tvrc);

        travelrc = view.findViewById(R.id.travelrc);

        socialrc = view.findViewById(R.id.socialrc);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        partnerrc.setLayoutManager(gridLayoutManager);

        partnerrc.setItemAnimator(new DefaultItemAnimator());

        partnerrc.setHasFixedSize(true);

        partnerrc.setNestedScrollingEnabled(false);

        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categoryrc.setLayoutManager(gridLayoutManagercat);

        categoryrc.setHasFixedSize(true);

        categoryrc.setItemAnimator(new DefaultItemAnimator());

        categoryrc.setHasFixedSize(true);

        categoryrc.setNestedScrollingEnabled(false);

        LinearLayoutManager gridLayoutManagertop = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        toprc.setLayoutManager(gridLayoutManagertop);

        toprc.setHasFixedSize(true);

        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        toprc.setItemAnimator(new DefaultItemAnimator());

        toprc.setNestedScrollingEnabled(false);

        toprc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), toprc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(getContext(), CategoryWebview.class);
                intent.putExtra("categorylink", topModelsList.get(position).getApp_link());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        LinearLayoutManager gridLayoutManagertsc = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        socialrc.setLayoutManager(gridLayoutManagertsc);

        socialrc.setHasFixedSize(true);
        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        socialrc.setItemAnimator(new DefaultItemAnimator());

        socialrc.setNestedScrollingEnabled(false);

        socialrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), socialrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getContext(), CategoryWebview.class);
                intent.putExtra("categorylink", socialmodelist.get(position).getApp_link());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        LinearLayoutManager gridLayoutManagertv = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        hotelrc.setLayoutManager(gridLayoutManagertv);
        hotelrc.setHasFixedSize(true);
        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        hotelrc.setItemAnimator(new DefaultItemAnimator());
        hotelrc.setNestedScrollingEnabled(false);

        hotelrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), hotelrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getContext(), CategoryWebview.class);

                intent.putExtra("categorylink", hotelmodelist.get(position).getApp_link());

                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        LinearLayoutManager gridLayoutManagersh = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        shoppingrc.setLayoutManager(gridLayoutManagersh);
        shoppingrc.setHasFixedSize(true);
        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        shoppingrc.setItemAnimator(new DefaultItemAnimator());
        shoppingrc.setNestedScrollingEnabled(false);
        shoppingrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), shoppingrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), CategoryWebview.class);
                intent.putExtra("categorylink", shoppingmodelist.get(position).getApp_link());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        LinearLayoutManager gridLayoutManagertrav = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        travelrc.setLayoutManager(gridLayoutManagertrav);
        travelrc.setHasFixedSize(true);
        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        travelrc.setItemAnimator(new DefaultItemAnimator());
        travelrc.setNestedScrollingEnabled(false);
        travelrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), travelrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getContext(), CategoryWebview.class);
                intent.putExtra("categorylink", travelmodellist.get(position).getApp_link());
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        LinearLayoutManager gridLayoutManagerscratch = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rcscratch.setLayoutManager(gridLayoutManagerscratch);

        rcscratch.setHasFixedSize(true);

        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        rcscratch.setItemAnimator(new DefaultItemAnimator());

        rcscratch.setNestedScrollingEnabled(false);

        rcscratch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rcscratch, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                Intent intent = new Intent(getContext(), Scratch.class);

                intent.putExtra("scratchimage", scratchModels.get(position).getScratch_card_image());
                intent.putExtra("scratchtype",scratchModels.get(position).getScratch_type());
                intent.putExtra("scratchid",scratchModels.get(position).getScratch_id());

                intent.putExtra("scratchamount", scratchModels.get(position).getEarning());

                startActivity(intent);

//
//
                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                mInterstitialAd.setAdListener(new AdListener() {

                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        topbannerrc.setLayoutManager(new GridLayoutManager( getContext(),3));

        topbannerrc.setHasFixedSize(true);

        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        topbannerrc.setItemAnimator(new DefaultItemAnimator());

        topbannerrc.setNestedScrollingEnabled(false);

        topbannerrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), topbannerrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override

            public void onItemClick(View view, int position) {



                Intent intent = new Intent(getContext(), NavbarActivity.class);


                intent.putExtra("category_name", topbannermodellist.get(position).getNavbar_app_name());


                startActivity(intent);



            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        LinearLayoutManager gridLayoutManagernews = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        newsfeedrc.setLayoutManager(gridLayoutManagernews);

        newsfeedrc.setHasFixedSize(true);

        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        newsfeedrc.setItemAnimator(new DefaultItemAnimator());

        newsfeedrc.setNestedScrollingEnabled(false);

        newsfeedrc.addOnItemTouchListener(new RecyclerTouchListener(getContext(), newsfeedrc, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("newsheading", newsmodellist.get(position).getFeed_heading());
                intent.putExtra("newsdesi",newsmodellist.get(position).getFeed_description());
                startActivity(intent);
//
//                Intent intent = new Intent(getContext(), CategoryWebview.class);
//                intent.putExtra("categorylink", newsfeedmodel.get(position).getApp_link());
//                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        if (ConnectivityReceiver.isConnected()) {
            progressDialog.dismiss();

            makepartner();
            topapps();
            banner();
            updateapp();
            categoryapps();
            socialapps();
            travelapps();
            scratchapps();
            hotelapps();
            shoppingapps();
            dailylogiun();
            newsfeedapp();
            topbannerapp();

        } else {
            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Check Your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        //do things

                    });
            AlertDialog alert = builder.create();
            alert.show();

        }


        return view;

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


//    @Override
//    public void onSearchStateChanged(boolean enabled) {
//    }
//
//    @Override
//    public void onSearchConfirmed(CharSequence text) {
//
//    }
//
//    @Override
//    public void onButtonClicked(int buttonCode) {
//        switch (buttonCode) {
//            case MaterialSearchBar.BUTTON_NAVIGATION:
//                drawer.openDrawer(Gravity.LEFT);
//                break;
//            case MaterialSearchBar.BUTTON_SPEECH:
//                break;
//            case MaterialSearchBar.BUTTON_BACK:
//                searchBar.disableSearch();
//                break;
//        }
//    }

    private void makepartner() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Group_app, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<PartnerModel>>() {
                                }.getType();
                                category_modelList = gson.fromJson(response.getString("data"), listType);
                                adapter = new HomeAdapter(category_modelList);
                                partnerrc.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void topapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Top_APP, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TopModels>>() {
                                }.getType();
                                topModelsList = gson.fromJson(response.getString("data"), listType);
                                topAdapter = new TopAdapter(topModelsList);
                                toprc.setAdapter(topAdapter);
                                topAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void socialapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Social_APP, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response != null && response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TopModels>>() {
                                }.getType();
                                socialmodelist = gson.fromJson(response.getString("data"), listType);
                                socialadapter = new CatAdapter(socialmodelist);
                                socialrc.setAdapter(socialadapter);
                                socialadapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void updateapp() {
        getVersionInfo();
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Update, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {

                                JSONObject jsonObject = response.getJSONObject("data");

                                value = jsonObject.getString("value");

                                if (value.contains(versionName)) {

                                } else {

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Update found...");

                                    // Setting Dialog Message
                                    alertDialog.setMessage("New Version Available");

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.drawable.shopicon);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
                                            startActivity(i);
                                            // Write your code here to invoke YES event
                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();
                                }
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void shoppingapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Shopping_APP, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TopModels>>() {
                                }.getType();
                                shoppingmodelist = gson.fromJson(response.getString("data"), listType);
                                shoppingadapter = new ShopingAdpater(shoppingmodelist);
                                shoppingrc.setAdapter(shoppingadapter);
                                shoppingadapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void travelapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.travel_APP, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TopModels>>() {
                                }.getType();
                                travelmodellist = gson.fromJson(response.getString("data"), listType);
                                traveladapter = new TopAdapter(travelmodellist);
                                travelrc.setAdapter(traveladapter);
                                traveladapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void hotelapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Hotel_APP, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response != null && response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TopModels>>() {
                                }.getType();
                                hotelmodelist = gson.fromJson(response.getString("data"), listType);
                                hoteladapter = new TopAdapter(hotelmodelist);
                                hotelrc.setAdapter(hoteladapter);
                                hoteladapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static void scratchapps() {

        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Scratch_APP, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response != null && response.length() > 0) {
                            String status = response.getString("status");

                            if (status.contains("1")) {

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<ScratchModel>>() {
                                }.getType();
                                scratchModels = gson.fromJson(response.getString("data"), listType);
                                scratchAdapter = new ScratchAdapter(scratchModels);
                                rcscratch.setAdapter(scratchAdapter);
                                scratchAdapter.notifyDataSetChanged();
                            }

                            else {
                                l1_scratch.setVisibility(View.GONE);

                            }

                        }
                        else {
                            Toast.makeText(activity, "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(activity, "Connection time out", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void categoryapps() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.GET_CATEGORY_URL, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<CategoryModel>>() {
                                }.getType();
                                cat_list = gson.fromJson(response.getString("data"), listType);
                                categoryAdapter = new CategoryAdapter(cat_list);
                                categoryrc.setAdapter(categoryAdapter);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void topbannerapp() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Nav_app, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response != null && response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<TopbannerModel>>() {
                                }.getType();
                                topbannermodellist = gson.fromJson(response.getString("data"), listType);
                                topbannerAdapter = new TopbannerAdapter(topbannermodellist);
                                topbannerrc.setAdapter(topbannerAdapter);
                                topbannerAdapter.notifyDataSetChanged();
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void newsfeedapp() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.socity_news, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<newsfeedmodel>>() {
                                }.getType();

                                newsmodellist = gson.fromJson(response.getString("data"), listType);

                                newsfeedAdapter = new NewsfeedAdapter(newsmodellist);

                                newsfeedrc.setAdapter(newsfeedAdapter);

                                newsfeedAdapter.notifyDataSetChanged();
                            }
                            else {
                                l1_newsfeed.setVisibility(View.GONE);
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void banner() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("params", "");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Banner, params, response -> {
                    Log.d(TAG, response.toString());

                    try {
                        if (response.length() > 0) {
                            String status = response.getString("status");
                            if (status.contains("1")) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject objJson = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    objJson = jsonArray.getJSONObject(i);

                                    String name = objJson.getString("banner_name");

                                    bannerlink = objJson.getString("banner_link");

                                    String image = objJson.getString("banner_image");

                                    sildermodelslist.add(new Sildermodel(image, bannerlink));

                                    NUM_PAGES = sildermodelslist.size();


                                }

                            }
                            if (getActivity()!=null){
                                final float density = getResources().getDisplayMetrics().density;
    //
                                indicator.setRadius(5 * density);

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
                                final Runnable Update = () -> {
                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    bannerslider.setCurrentItem(currentPage++, true);
                                };
                                Timer swipeTimer = new Timer();
                                swipeTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, 3000, 3000);


                                SilderAdapter silderAdapter1 = new SilderAdapter(getContext(), sildermodelslist);

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
                }, error -> {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

//    private class banner extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            sildermodelslist.clear();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                String json = JsonUtils.getJSONString(strings[0]);
//                JSONObject mainJson = new JSONObject(json);
//                status = mainJson.getString("status");
//                JSONArray jsonArray = mainJson.getJSONArray("data");
//                JSONObject objJson = null;
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    objJson = jsonArray.getJSONObject(i);
//
//                    String name = objJson.getString("banner_name");
//
//                    bannerlink = objJson.getString("banner_link");
//
//                    String image = objJson.getString("banner_image");
//
//                    sildermodelslist.add(new Sildermodel(image, bannerlink));
//
//                    NUM_PAGES = sildermodelslist.size();
//
//
//                }
//                final float density = getResources().getDisplayMetrics().density;
//
//                indicator.setRadius(5 * density);
//
//                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        currentPage = position;
//
//                    }
//
//                    @Override
//                    public void onPageScrolled(int pos, float arg1, int arg2) {
//
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int pos) {
//
//                    }
//                });
//
//
//                // Auto start of viewpager
//                final Handler handler = new Handler();
//                final Runnable Update = new Runnable() {
//                    public void run() {
//                        if (currentPage == NUM_PAGES) {
//                            currentPage = 0;
//                        }
//                        bannerslider.setCurrentItem(currentPage++, true);
//                    }
//                };
//                Timer swipeTimer = new Timer();
//                swipeTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        handler.post(Update);
//                    }
//                }, 3000, 3000);
//
//
//                SilderAdapter silderAdapter1 = new SilderAdapter(getContext(), sildermodelslist);
//
//                bannerslider.setAdapter(silderAdapter1);
//
//                indicator.setViewPager(bannerslider);
//
//
//                return "1";
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return "0";
//            } catch (Exception ee) {
//                ee.printStackTrace();
//                return "0";
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if (getContext() != null) {
//                final float density = getResources().getDisplayMetrics().density;
//
//                indicator.setRadius(5 * density);
//
//                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        currentPage = position;
//
//                    }
//
//                    @Override
//                    public void onPageScrolled(int pos, float arg1, int arg2) {
//
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int pos) {
//
//                    }
//                });
//
//
//                // Auto start of viewpager
//                final Handler handler = new Handler();
//                final Runnable Update = new Runnable() {
//                    public void run() {
//                        if (currentPage == NUM_PAGES) {
//                            currentPage = 0;
//                        }
//                        bannerslider.setCurrentItem(currentPage++, true);
//                    }
//                };
//                Timer swipeTimer = new Timer();
//                swipeTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        handler.post(Update);
//                    }
//                }, 3000, 3000);
//
//
//                SilderAdapter silderAdapter1 = new SilderAdapter(getContext(), sildermodelslist);
//
//                bannerslider.setAdapter(silderAdapter1);
//
//                indicator.setViewPager(bannerslider);
//
//
//            }
//            super.onPostExecute(s);
//        }
//    }



    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    private void admob() {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        ;

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.Admob_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    if (status.contains("1")) {

                        String msg = response.getString("message");

                        JSONObject obj = response.getJSONObject("data");

                        appid = obj.getString("app_id");

                        intersitialid = obj.getString("interstitial_unit_id");

                        bannerid = obj.getString("banner_unit_id");

                        mInterstitialAd.setAdUnitId(intersitialid);








//                        mInterstitialAd.setAdUnitId(intersitialid);
//
//
//                        // Load ads into Interstitial Ads
//                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//                        mInterstitialAd.setAdListener(new AdListener() {
//
//                            public void onAdLoaded() {
//                                showInterstitial();
//                            }
//                        });

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

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
    private void dailylogiun() {
        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Daily_Login, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        String status=response.getString("status");

                        String message = response.getString("message");
//                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

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
    public void getVersionInfo() {
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            Log.d("version", String.valueOf(versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }
    public class myWebClient extends WebViewClient {

        @Override

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);

        }

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            return true;

        }

        @Override

        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

//            progressDialog.dismiss();

        }
    }
}


