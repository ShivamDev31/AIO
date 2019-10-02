package com.goaffilate.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.goaffilate.app.R;


public class TestFragment extends Fragment {
    WebView web;

    ProgressDialog progressDialog;

    Activity activity;

    String weblink, appname;

    public static TestFragment newInstance(String link, String appname) {
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("welink", link);
        bundle.putString("appname", appname);
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        weblink = getArguments().getString("welink");
        appname = getArguments().getString("appname");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.testfragmnet, container, false);

        activity = getActivity();

        web = (WebView) view.findViewById(R.id.webview01);

        web.loadUrl(weblink);

        web.getSettings().setJavaScriptEnabled(true);

        web.getSettings().setLoadWithOverviewMode(true);

        web.getSettings().setUseWideViewPort(true);

        web.getSettings().setDomStorageEnabled(true);

        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        web.getSettings().setLoadsImagesAutomatically(true);

        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        web.setVerticalScrollBarEnabled(true);

        web.setHorizontalScrollBarEnabled(true);

        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        web.setWebChromeClient(new WebChromeClient());

        web.setOverScrollMode(WebView.OVER_SCROLL_ALWAYS);

        web.setHorizontalScrollBarEnabled(true);

        web.setWebViewClient(new myWebClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            web.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        } else {

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
        return view;
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




