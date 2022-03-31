package com.vero.c4_custom_ui.dispatch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import kotlin.math.absoluteValue

// parent View
class BadViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    var mLastX = 0
    var mLastY = 0

//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//
//        // 配合内部拦截法使用
//        if (ev.action == MotionEvent.ACTION_DOWN) {
//            super.onInterceptTouchEvent(ev)
//            return false
//        }
//        return true
//    }


    // 外部拦截法使用
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x
                mLastY = y
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                if (deltaX.absoluteValue > deltaY.absoluteValue) {
                    // 横向滑动，允许parent拦截
                    return true
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}