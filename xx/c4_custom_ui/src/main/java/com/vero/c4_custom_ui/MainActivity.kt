package com.vero.c4_custom_ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.animation.addListener
import com.vero.c4_custom_ui.dispatch.DispatchActivity
import com.vero.c4_custom_ui.view.ColorTrackTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun photoViewActivityActivity(view: View) {
        val intent=Intent(this,PhotoViewActivity::class.java)
        startActivity(intent)
    }

    fun dispatchActivity(view: View) {
        val intent=Intent(this,DispatchActivity::class.java)
        startActivity(intent)
    }

    fun colorTrackActivity(view: View) {
        val intent=Intent(this,ColorTrackActivity::class.java)
        startActivity(intent)
    }
}