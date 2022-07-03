package com.vero.libwebview.webviewprocess;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vero.libwebview.WebViewCallBack;
import com.vero.libwebview.bean.JsParam;
import com.vero.libwebview.webviewprocess.settings.WebViewDefaultSettings;
import com.vero.libwebview.webviewprocess.webchromeclient.XXWebChromeClient;
import com.vero.libwebview.webviewprocess.webviewclient.XXWebViewClient;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BaseWebView extends WebView {

    public static final String TAG = "BaseWebView";

    public BaseWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // 连接主进程
        WebviewProcessCommandDispatcher.getInstance().initAidlConnection();

        WebViewDefaultSettings.getInstance().setSettings(this);
        // 将xiangxuewebview对象注入到window
        addJavascriptInterface(this, "xiangxuewebview");
    }

    public void registerWebViewCallBack(WebViewCallBack webViewCallBack) {
        setWebViewClient(new XXWebViewClient(webViewCallBack));
        setWebChromeClient(new XXWebChromeClient(webViewCallBack));
    }

    // h5 通过 xiangxuewebview 对象调用原生的方法
    @JavascriptInterface
    public void takeNativeAction(final String jsParams) {
        Log.e(TAG, "takeNativeAction()===" + jsParams);

        if (!TextUtils.isEmpty(jsParams)) {
            final JsParam jsParamObj = new Gson().fromJson(jsParams, JsParam.class);
            if (jsParamObj != null) {
                String json = new Gson().toJson(jsParamObj.param);
                WebviewProcessCommandDispatcher.getInstance().executeCommand(jsParamObj.name, json);
            }

        }
    }
}
