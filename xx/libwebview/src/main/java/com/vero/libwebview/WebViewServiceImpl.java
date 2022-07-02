package com.vero.libwebview;

import android.content.Context;
import android.content.Intent;

import com.google.auto.service.AutoService;
import com.vero.common.autoservice.IWebViewService;
import com.vero.libwebview.utils.Constants;

import androidx.fragment.app.Fragment;

import static com.vero.libwebview.utils.Constants.ANDROID_ASSETS_URI;

@AutoService(IWebViewService.class)
public class WebViewServiceImpl implements IWebViewService {

    @Override
    public void startWebViewActivity(Context context, String url, String title, boolean isShowActionBar) {
        if (context != null) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, title);
            intent.putExtra(Constants.URL, url);
            intent.putExtra(Constants.IS_SHOW_ACTION_BAR, isShowActionBar);
            context.startActivity(intent);
        }
    }

    @Override
    public Fragment getWebViewFragment(String url, boolean canNativeRefresh) {
        return WebViewFragment.Companion.newInstance(url, canNativeRefresh);
    }

    @Override
    public void startDemoHtml(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, "本地测试demo");
            intent.putExtra(Constants.URL, ANDROID_ASSETS_URI + "demo.html");
            context.startActivity(intent);
        }
    }
}
