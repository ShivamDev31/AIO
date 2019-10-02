package com.goaffilate.app.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goaffilate.app.LoginActivity;
import com.goaffilate.app.MainActivity;
import com.goaffilate.app.MyRewardActivity;
import com.goaffilate.app.R;
import com.goaffilate.app.RedeemActivity;
import com.goaffilate.app.adapter.RewardsAdapter;
import com.goaffilate.app.model.RewardsModel;
import com.goaffilate.app.utils.AppController;
import com.goaffilate.app.utils.BaseURL;
import com.goaffilate.app.utils.ConnectivityReceiver;
import com.goaffilate.app.utils.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyRewards extends Fragment {
    SharedPreferences sharedPreferences;
    String userid;
    RecyclerView rewrdsrc;
    TextView rewardstext,currentbalance;
    Button redeem;
    ProgressDialog progressDialog;

    public  static String TAG="MyReward";
    String totalamount;

    RewardsAdapter rewardsAdapter;
    String currentbal;
    int balance;
    List<RewardsModel> rewardsModels=new ArrayList<>();

    public FragmentMyRewards() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_my_reward, container, false);

        MainActivity.toolbar.setVisibility(View.GONE);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
//
// ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        rewrdsrc=view.findViewById(R.id.rewrdsrc);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 2);
        rewrdsrc.setLayoutManager(gridLayoutManager1);
        rewrdsrc.setItemAnimator(new DefaultItemAnimator());
        rewrdsrc.setNestedScrollingEnabled(false);
        rewrdsrc.hasFixedSize();
        rewrdsrc.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
        rewardstext=view.findViewById(R.id.totalamount);
        sharedPreferences=getActivity().getSharedPreferences("userid",MODE_PRIVATE);
        userid=sharedPreferences.getString("user","");

        currentbalance=view.findViewById(R.id.currentamount);

redeem=view.findViewById(R.id.redeem);
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (balance<1000){
                    Toast.makeText(getActivity(), "Minimum Amount to Redeem Rs.1000", Toast.LENGTH_SHORT).show();

                }else {
                    redeem.setEnabled(false);
                    Intent intent=new Intent(getContext(), RedeemActivity.class);
                    intent.putExtra("user_id",userid);
                    startActivity(intent);
                    Animatoo.animateSlideLeft(getActivity());

                }


            }
        });

        if (ConnectivityReceiver.isConnected()){
            userreward();
        }
        else {
            Toast.makeText(getContext(), "Not Connected", Toast.LENGTH_SHORT).show();
        }




        if (balance<1000){
            redeem.setAlpha((float) 0.5);

        }

        return view;


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
    private void userreward() {
        progressDialog.dismiss();
        rewardsModels.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Rewards_Scratch, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("status");
                    totalamount=response.getString("total_amount");
                     currentbal=response.getString("current_amount");balance= Integer.parseInt(currentbal);

                    rewardstext.setText(totalamount);
                    currentbalance.setText(currentbal);
                    if (status.contains("1")) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RewardsModel>>() {
                        }.getType();
                        rewardsModels = gson.fromJson(response.getString("data"), listType);
                        rewardsAdapter = new RewardsAdapter(rewardsModels);
                        rewrdsrc.setAdapter(rewardsAdapter);
                        rewardsAdapter.notifyDataSetChanged();

                    }
                    else {


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
    }

}
