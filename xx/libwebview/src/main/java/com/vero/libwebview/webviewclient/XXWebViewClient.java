package com.vero.libwebview.webviewclient;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vero.libwebview.WebViewCallBack;

public class XXWebViewClient extends WebViewClient {

    private WebViewCallBack mWebViewCallBack;
    private static final String TAG = "XXWebViewClient";


    public XXWebViewClient(WebViewCallBack callBack) {
        mWebViewCallBack = callBack;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.pageStarted(url);
        } else {
            Log.e(TAG, "WebViewCallBack is null");
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.pageFinished(url);
        } else {
            Log.e(TAG, "WebViewCallBack is null");
        }
    }
}
