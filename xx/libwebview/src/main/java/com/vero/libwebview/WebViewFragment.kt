package com.vero.libwebview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.vero.base.loadsir.LoadingCallback
import com.vero.libwebview.databinding.FragmentWebviewBinding
import com.vero.libwebview.utils.Constants
import com.vero.libwebview.webviewclient.XXWebViewClient
import java.util.*

class WebViewFragment : Fragment(), WebViewCallBack {

    private var mUrl: String = ""
    private lateinit var mBinding: FragmentWebviewBinding
    private lateinit var mLoadService: LoadService<*>

    companion object {
        fun newInstance(url: String): Fragment {
            val fragment = WebViewFragment()
            val bundle = Bundle().apply {
                putString(Constants.URL, url)
            }
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUrl = arguments?.getString(Constants.URL) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentWebviewBinding.inflate(layoutInflater, container, false)

        mBinding.webview.settings.javaScriptEnabled = true
        //开启DOM
        mBinding.webview.settings.domStorageEnabled = true



        mBinding.webview.loadUrl(mUrl)

        mLoadService = LoadSir.getDefault().register(mBinding.webview, object : Callback.OnReloadListener {
            override fun onReload(v: View?) {
                // 重新加载
                mLoadService.showCallback(LoadingCallback::class.java)
                mBinding.webview.reload()
            }
        })

        mBinding.webview.webViewClient = object : XXWebViewClient(this) {

//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                mBinding.webview.loadUrl(mUrl)
//                return super.shouldOverrideUrlLoading(view, request)
//                mBinding.webview.loadUrl(mUrl)
//            }

        }


//        return mBinding.root
        return mLoadService.loadLayout
    }

    override fun pageStarted(url: String?) {
        mLoadService.showCallback(LoadingCallback::class.java)

    }

    override fun pageFinished(url: String?) {
        mLoadService.showSuccess()
    }


}