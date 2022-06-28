package com.vero.base;

import android.app.Application;

public class BaseApplication extends Application {

    public static Application sApplication;

    @Override
    public void onCreate() {
        sApplication = this;
        super.onCreate();
    }
}
