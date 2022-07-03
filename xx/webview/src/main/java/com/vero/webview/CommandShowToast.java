package com.vero.webview;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.auto.service.AutoService;
import com.vero.base.BaseApplication;
import com.vero.libwebview.command.Command;

import java.util.Map;

@AutoService({Command.class})
public class CommandShowToast implements Command {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void execute(Map params) {
        String value = String.valueOf(params.get("message"));
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.sApplication, value, Toast.LENGTH_LONG).show();
            }
        });
    }
}
