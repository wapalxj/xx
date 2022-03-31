package com.vero.c4_custom_ui

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vero.c4_custom_ui.view.ColorTrackTextView

class ColorTrackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_track)
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

}