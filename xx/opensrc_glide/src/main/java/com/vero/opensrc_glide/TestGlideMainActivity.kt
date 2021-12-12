package com.vero.opensrc_glide

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget

class TestGlideMainActivity : AppCompatActivity() {


    var url = "http://guolin.tech/book.png"

    lateinit var requestManager: RequestManager

    private val imageView: ImageView by lazy {
        findViewById(R.id.image_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_glide)
        requestManager = Glide.with(this)
    }

    fun loadImage(view: View) {
        //Glide 高版本可能在低版本的模拟器上加载不出来
        //SocketException: socket failed: EPERM (Operation not permitted)
//        Glide.with(this)
//            .load(url)
//            .listener(object : RequestListener<Drawable> {
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    Log.e("loadImage", "111111111111111111")
//
//                    return false
//                }
//
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//            })
//            .into(imageView)

        //三大流程

        //with
        val requestManager: RequestManager = Glide.with(this)

        //load
        val requestBuilder: RequestBuilder<Drawable> = requestManager.load(url)

        //into
        val viewTarget :ViewTarget<ImageView,Drawable> = requestBuilder.into(imageView)


    }

    override fun onDestroy() {
        super.onDestroy()
//        loadImage(imageView)

        //非必须，内部已经做了
        //application作为参数的时候需要？会内存泄漏
        Glide.with(this.application).clear(imageView)
    }
}