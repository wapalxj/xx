package com.vero.opensrc_glide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vero.opensrc_glide.gtlide.TestGlideMainActivity
import com.vero.opensrc_glide.rxjava.TestRxjavaMainActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun jump2Glide(view: View) {
        startActivity(Intent(this, TestGlideMainActivity::class.java))
    }

    fun jump2Rxjava(view: View) {
        startActivity(Intent(this,TestRxjavaMainActivity::class.java))
    }
}