package com.vero.c4_custom_ui.dispatch

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ListView
import androidx.viewpager.widget.ViewPager
import kotlin.math.absoluteValue


/**
 * 内部拦截法
 */
class MyListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ListView(context, attrs, defStyleAttr) {

    var mLastX = 0
    var mLastY = 0

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x.toInt()
        val y = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 不允许parent拦截，强制接收到DOWN
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                if (deltaX.absoluteValue > deltaY.absoluteValue) {
                    // 横向滑动，允许parent拦截
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }

        mLastX = x
        mLastY = y

        return super.dispatchTouchEvent(ev)
    }
}