package com.vero.libwebview.webviewprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.vero.base.BaseApplication;
import com.vero.libwebview.IWebviewProcessToMainProcessAidlInterface;
import com.vero.libwebview.mainprocess.MainProcessCommandManager;
import com.vero.libwebview.mainprocess.MainProcessCommandService;

import java.util.Map;

public class WebviewProcessCommandDispatcher implements ServiceConnection {

    public static WebviewProcessCommandDispatcher sDispatcher;
    private IWebviewProcessToMainProcessAidlInterface mIWebviewProcessToMainProcessAidlInterface;

    private WebviewProcessCommandDispatcher() {

    }

    public static WebviewProcessCommandDispatcher getInstance() {
        if (sDispatcher == null) {
            synchronized (WebviewProcessCommandDispatcher.class) {
                if (sDispatcher == null) {
                    sDispatcher = new WebviewProcessCommandDispatcher();
                }
            }
        }
        return sDispatcher;
    }


    public void initAidlConnection() {
        // 连接主进程Service
        Intent intent = new Intent(BaseApplication.sApplication, MainProcessCommandService.class);
        BaseApplication.sApplication.bindService(intent, this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mIWebviewProcessToMainProcessAidlInterface = IWebviewProcessToMainProcessAidlInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mIWebviewProcessToMainProcessAidlInterface = null;
        initAidlConnection();
    }

    @Override
    public void onBindingDied(ComponentName name) {
        mIWebviewProcessToMainProcessAidlInterface = null;
        initAidlConnection();
    }

    public void executeCommand(String commandName, String params) {
        if (mIWebviewProcessToMainProcessAidlInterface != null) {
            try {
                // 调用服务端方法
                mIWebviewProcessToMainProcessAidlInterface.handleWebCommand(commandName, params);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
