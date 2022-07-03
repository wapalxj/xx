package com.vero.libwebview.mainprocess;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.vero.libwebview.ICallbackMainProcessToWebviewProcessAidlInterface;
import com.vero.libwebview.IWebviewProcessToMainProcessAidlInterface;
import com.vero.libwebview.command.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class MainProcessCommandManager extends IWebviewProcessToMainProcessAidlInterface.Stub {


    public static MainProcessCommandManager sCommandManager;

    private HashMap<String, Command> commands = new HashMap<>();

    private MainProcessCommandManager() {
        ServiceLoader<Command> serviceLoader = ServiceLoader.load(Command.class);
        for (Command command : serviceLoader) {
            // 自动注册命令到集合中
            if (!commands.containsKey(command.name())) {
                commands.put(command.name(), command);
            }
        }
    }

    public static MainProcessCommandManager getInstance() {
        if (sCommandManager == null) {
            synchronized (MainProcessCommandManager.class) {
                if (sCommandManager == null) {
                    sCommandManager = new MainProcessCommandManager();
                }
            }
        }
        return sCommandManager;
    }


    @SuppressLint("LongLogTag")
    public void executeCommand(String commandName, Map params, ICallbackMainProcessToWebviewProcessAidlInterface callback) {
        // 这里运行在binder子线程
        Log.e("MainProcessCommandManager",""+Thread.currentThread());
        commands.get(commandName).execute(params,callback);
    }

    @Override
    public void handleWebCommand(String commandName, String params, ICallbackMainProcessToWebviewProcessAidlInterface callback) throws RemoteException {
        if (TextUtils.isEmpty(commandName)) {
            return;
        }
        getInstance().executeCommand(commandName, new Gson().fromJson(params, Map.class),callback);


    }
}
