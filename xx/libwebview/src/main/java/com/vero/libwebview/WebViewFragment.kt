package com.vero.libwebview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.vero.base.loadsir.ErrorCallback
import com.vero.base.loadsir.LoadingCallback
import com.vero.libwebview.databinding.FragmentWebviewBinding
import com.vero.libwebview.utils.Constants
import com.vero.libwebview.webchromeclient.XXWebChromeClient
import com.vero.libwebview.webviewclient.XXWebViewClient

class WebViewFragment : Fragment(), WebViewCallBack, OnRefreshListener {

    private var mUrl: String = ""
    private var mCanNativeRefresh = true
    private var mIsError = false

    private val mBinding: FragmentWebviewBinding by lazy {
        FragmentWebviewBinding.inflate(layoutInflater,activity?.window?.decorView as ViewGroup,false)
    }

    private val mLoadService: LoadService<*> by lazy {
        LoadSir.getDefault().register(mBinding.root, object : Callback.OnReloadListener {
            override fun onReload(v: View?) {
                // 重新加载
                mLoadService.showCallback(LoadingCallback::class.java)
                mBinding.webview.reload()
            }
        })
    }

    companion object {
        private const val TAG = "WebViewFragment"
        fun newInstance(url: String, canNativeRefresh: Boolean = true): Fragment {
            val fragment = WebViewFragment()
            val bundle = Bundle().apply {
                putString(Constants.URL, url)
                putBoolean(Constants.CAN_NATIVE_REFRESH, canNativeRefresh)
            }
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUrl = arguments?.getString(Constants.URL) ?: ""
        mCanNativeRefresh = arguments?.getBoolean(Constants.CAN_NATIVE_REFRESH) ?: true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding.webview.settings.javaScriptEnabled = true
        //开启DOM
        mBinding.webview.settings.domStorageEnabled = true

        mBinding.webview.loadUrl(mUrl)

        mBinding.webview.webViewClient = object : XXWebViewClient(this) {

//            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                mBinding.webview.loadUrl(mUrl)
//                return super.shouldOverrideUrlLoading(view, request)
//                mBinding.webview.loadUrl(mUrl)
//            }

        }

        mBinding.webview.webChromeClient = XXWebChromeClient(this)


        mBinding.smartRefresh.setRefreshHeader(ClassicsHeader(activity))
        mBinding.smartRefresh.setOnRefreshListener(this)
        mBinding.smartRefresh.setEnableLoadMore(false)
        mBinding.smartRefresh.setEnableRefresh(mCanNativeRefresh)


//        return mBinding.root
        return mLoadService.loadLayout
    }

    override fun pageStarted(url: String?) {
        mLoadService.showCallback(LoadingCallback::class.java)

    }

    override fun pageFinished(url: String?) {
        if (mIsError) {
            // 错误 一定让刷新
            mIsError = false
            mLoadService.showCallback(ErrorCallback::class.java)
            mBinding.smartRefresh.setEnableRefresh(true)
        } else {
            mLoadService.showSuccess()
            mBinding.smartRefresh.setEnableRefresh(mCanNativeRefresh)
        }
        mBinding.smartRefresh.finishRefresh()
        Log.e(TAG, "pageFinished()")
    }

    override fun pageError() {
        // 注意 ：error后会继续调用pageFinished
        Log.e(TAG, "pageError()")
        mIsError = true
        mBinding.smartRefresh.finishRefresh()
    }

    override fun updateTitle(title: String) {
        (activity as? WebViewActivity)?.updateTitle(title)
    }

    // 下拉刷新
    override fun onRefresh(refreshLayout: RefreshLayout) {
        mBinding.webview.reload()
    }


}