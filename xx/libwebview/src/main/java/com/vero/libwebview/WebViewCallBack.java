package com.vero.libwebview;

public interface WebViewCallBack {
    void pageStarted(String url);

    void pageFinished(String url);

    void pageError();
}
