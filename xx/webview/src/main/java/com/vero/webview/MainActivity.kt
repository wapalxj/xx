package com.vero.webview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.vero.base.autoservice.VeroServiceLoader
import com.vero.common.autoservice.IWebViewService
import com.vero.libwebview.WebViewActivity
import com.vero.webview.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.btn.setOnClickListener {


            //startActivity(Intent(this, WebViewActivity::class.java))

//            val iWebViewService = ServiceLoader.load(IWebViewService::class.java).iterator().next()
            val iWebViewService = VeroServiceLoader.load(IWebViewService::class.java)
            if (iWebViewService != null) {
//                iWebViewService.startWebViewActivity(this, "http://www.baidu.com", "百度",true)
                iWebViewService.startDemoHtml(this)
            }


        }

    }
}