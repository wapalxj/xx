package com.vero.webview;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;

import com.google.auto.service.AutoService;
import com.vero.base.BaseApplication;
import com.vero.libwebview.ICallbackMainProcessToWebviewProcessAidlInterface;
import com.vero.libwebview.command.Command;

import java.util.Map;

@AutoService({Command.class})
public class CommandOpenPage implements Command {
    @Override
    public String name() {
        return "openPage";
    }

    @Override
    public void execute(Map params, ICallbackMainProcessToWebviewProcessAidlInterface callback) {
        String targetClass = String.valueOf(params.get("target_class"));
        if (!TextUtils.isEmpty(targetClass)) {
            Intent intent = new Intent(targetClass);
            intent.setComponent(new ComponentName(BaseApplication.sApplication, targetClass));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.sApplication.startActivity(intent);
        }
    }
}
