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

//        findViewById<Button>(R.id.btn_click)?.let {
//            it.setOnTouchListener { v, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        Log.e(TAG, "ACTION_DOWN =====${it.z}-->${it.translationZ}->${it.elevation}")
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        Log.e(TAG, "ACTION_MOVE =====${it.z}-->${it.translationZ}->${it.elevation}")
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        Log.e(TAG, "ACTION_UP =====${it.z}-->${it.translationZ}->${it.elevation}")
//                    }
//                    else -> {
//
//                    }
//                }
//                return@setOnTouchListener false
//            }
//        }
//
//
//
//        findViewById<FrameLayout>(R.id.fl_root).let {
//            it.children.first().let {
//                Log.e(TAG, "初始=====${it.z}-->${it.translationZ}->${it.elevation}")
//            }
//
//            it.postDelayed({
//                it.children.first().let {
//                    Log.e(TAG, "延迟=====${it.z}-->${it.translationZ}->${it.elevation}")
//                }
//            }, 2000)
//        }
    }
}