package com.goaffilate.app;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.goaffilate.app.R;

public class CategoryWebview extends AppCompatActivity {
    WebView web;
    String applink;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        applink=getIntent().getStringExtra("categorylink");

        web = (WebView) findViewById(R.id.webview01);

        web.loadUrl(applink);

        web.getSettings().setJavaScriptEnabled(true);

        web.getSettings().setDomStorageEnabled(true);

        web.getSettings().setLoadWithOverviewMode(true);

        web.getSettings().setUseWideViewPort(true);

        web.setWebChromeClient(new WebChromeClient());

        web.getSettings().setPluginState(WebSettings.PluginState.ON);

        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        web.setWebViewClient(new myWebClient());

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
