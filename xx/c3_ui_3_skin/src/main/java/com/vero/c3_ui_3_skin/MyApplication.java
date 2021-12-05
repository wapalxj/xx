package com.vero.c3_ui_3_skin;

import android.app.Application;

import com.vero.skinlib.SkinManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
