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

    // 从左到右
    fun left_to_right(view: View) {
        val colorTrackTextView = findViewById<ColorTrackTextView>(R.id.tv_color)

        colorTrackTextView.direction = ColorTrackTextView.Direction.LEFT_TO_RIGHT

        ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                colorTrackTextView.currentProgress = it.animatedFraction
            }
            duration = 1500
            start()
        }
    }

    // 从右到左
    fun right_to_left(view: View) {
        val colorTrackTextView = findViewById<ColorTrackTextView>(R.id.tv_color)
        colorTrackTextView.direction = ColorTrackTextView.Direction.RIGHT_TO_LEFT
        ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                colorTrackTextView.currentProgress = it.animatedFraction
            }
            duration = 1500
            start()
        }
    }

    fun dispatchActivity(view: View) {
        val intent=Intent(this,DispatchActivity::class.java)
        startActivity(intent)
    }
}