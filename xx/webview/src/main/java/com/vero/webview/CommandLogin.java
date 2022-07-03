package com.vero.webview;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.auto.service.AutoService;
import com.vero.base.BaseApplication;
import com.vero.libwebview.ICallbackMainProcessToWebviewProcessAidlInterface;
import com.vero.libwebview.command.Command;

import java.util.Map;

@AutoService({Command.class})
public class CommandLogin implements Command {
    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(Map params, ICallbackMainProcessToWebviewProcessAidlInterface callback) {
        Log.e("CommandLogin", params.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String callbackName = (String) params.get("callbackname");
                    Thread.sleep(2000);

                    // 回调webview进程的方法
                    callback.onResult(callbackName, "{\"accountName\":\"login success\"}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
