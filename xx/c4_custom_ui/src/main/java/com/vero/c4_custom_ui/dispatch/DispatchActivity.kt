package com.vero.c4_custom_ui.dispatch

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.children
import com.vero.c4_custom_ui.R

class DispatchActivity : AppCompatActivity() {
    val TAG = "DispatchActivity"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch)

    }
}