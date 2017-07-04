package com.boredream.bdcodehelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.base.BoreBaseActivity;
import com.boredream.bdcodehelper.view.TitleBarView;

public class WebViewActivity extends BoreBaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";

    private WebView webview;
    private TitleBarView titlebar;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        title = getIntent().getStringExtra(EXTRA_TITLE);
        String url = getIntent().getStringExtra(EXTRA_URL);

        initView();

        showProgress();
        webview.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        titlebar = (TitleBarView) findViewById(R.id.title);
        if (!TextUtils.isEmpty(title)) {
            titlebar.setTitleText(title);
        }
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebClient());

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            titlebar.setTitleText(view.getTitle());
            dismissProgress();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webview.loadUrl(url);
            return true;
        }
    }
}
