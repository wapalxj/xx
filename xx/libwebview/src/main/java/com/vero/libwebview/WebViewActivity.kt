package com.vero.libwebview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.vero.libwebview.databinding.ActivityWebviewBinding
import com.vero.libwebview.utils.Constants

class WebViewActivity : AppCompatActivity() {


    lateinit var mBinding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview)


        val url = intent.getStringExtra(Constants.URL) ?: ""
        val title = intent.getStringExtra(Constants.TITLE) ?: ""
        val showActionBar = intent.getBooleanExtra(Constants.IS_SHOW_ACTION_BAR, true)

        mBinding.title.text = title
        mBinding.actionBar.visibility = if (showActionBar) {
            View.VISIBLE
        } else {
            View.GONE
        }

        mBinding.back.setOnClickListener {
            finish()
        }


        val fragment = WebViewFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commitAllowingStateLoss()


// 下面的放到了fragment中

//        mBinding.webview.settings.javaScriptEnabled = true
//        //开启DOM
//        mBinding.webview.settings.domStorageEnabled = true

//        mBinding.webview.webViewClient = object : WebViewClient() {
//
//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                return super.shouldOverrideUrlLoading(view, request)
//                mBinding.webview.loadUrl(url)
//            }
//
//        }
//
//
//        mBinding.webview.loadUrl(url)
    }
}