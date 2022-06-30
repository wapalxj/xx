package com.vero.libwebview.webchromeclient;

import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.vero.libwebview.WebViewCallBack;

public class XXWebChromeClient  extends WebChromeClient {

    private WebViewCallBack mWebViewCallBack;
    private static final String TAG = "XXWebChromeClient";

    public XXWebChromeClient(WebViewCallBack webViewCallBack) {
        mWebViewCallBack = webViewCallBack;
    }

    // 读取到title

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.updateTitle(title);
        } else {
            Log.e(TAG, "WebViewCallBack is null");
        }
    }
}
