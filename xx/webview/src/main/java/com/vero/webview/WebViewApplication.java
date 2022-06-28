package com.vero.webview;

import android.app.Application;

import com.kingja.loadsir.core.LoadSir;
import com.vero.base.BaseApplication;
import com.vero.base.loadsir.CustomCallback;
import com.vero.base.loadsir.EmptyCallback;
import com.vero.base.loadsir.ErrorCallback;
import com.vero.base.loadsir.LoadingCallback;
import com.vero.base.loadsir.TimeoutCallback;

public class WebViewApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }
}
