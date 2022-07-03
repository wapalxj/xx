package com.vero.libwebview.command;

import com.vero.libwebview.ICallbackMainProcessToWebviewProcessAidlInterface;

import java.util.Map;

public interface Command {
    String name();

    void execute(Map params, ICallbackMainProcessToWebviewProcessAidlInterface callback);
}
