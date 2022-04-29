package com.vero.libwebview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.vero.libwebview.databinding.ActivityWebViewBinding
import com.vero.libwebview.utils.Constants

class WebViewActivity : AppCompatActivity() {


    lateinit var mBinding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        mBinding.webview.settings.javaScriptEnabled = true
        //开启DOM
        mBinding.webview.settings.domStorageEnabled = true
        val url = intent.getStringExtra(Constants.URL) ?: ""
        val showActionBar = intent.getBooleanExtra(Constants.IS_SHOW_ACTION_BAR, true)

        mBinding.actionBar.visibility = if (showActionBar) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mBinding.webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
                mBinding.webview.loadUrl(url)
            }

        }


        mBinding.webview.loadUrl(url)
    }
}