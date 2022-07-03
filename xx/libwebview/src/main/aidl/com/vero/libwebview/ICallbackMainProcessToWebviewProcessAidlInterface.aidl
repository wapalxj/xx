// IMainProcessToWebbiewProcessAidlInterface.aidl
package com.vero.libwebview;

// 回调到webview进程
interface ICallbackMainProcessToWebviewProcessAidlInterface {

    void onResult(String callbackName,String response);
}