package com.vero.libwebview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vero.libwebview.databinding.FragmentWebviewBinding
import com.vero.libwebview.utils.Constants

class WebViewFragment : Fragment() {

    private var mUrl: String = ""
    private lateinit var mBinding: FragmentWebviewBinding

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
        mBinding = FragmentWebviewBinding.inflate(layoutInflater, container, true)
        return mBinding.root
    }









}