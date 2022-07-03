package com.vero.c4_custom_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vero.c4_custom_ui.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MultiTouchView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;

    // 手指滑动偏移值
    private float offsetX;
    private float offsetY;

    //按下时候的x,y坐标
    private float downX;
    private float downY;


    // 上1次的偏移值
    private float lastOffsetX;
    private float lastOffsetY;

    // 当前按下的pointId
    private int currentPointId;

    public MultiTouchView(@NonNull Context context) {
        this(context, null);
    }

    public MultiTouchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTouchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.photo);
        mPaint = new Paint();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, offsetX, offsetY, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        switch (event.getAction()) {
        // 多指
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 只会触发一次，第1次按下
                downX = event.getX();
                downY = event.getY();
                currentPointId = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                // 所有手指都触发

                //根据id获取Index (id是不变的，index会变化，两者不一定相等)
                int index = event.findPointerIndex(currentPointId);

                // 偏移量 = 当前手指的位置 - 手指down的位置
                offsetX = (event.getX(index) - downX) + lastOffsetX;
                offsetY = (event.getY(index) - downY) + lastOffsetY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 只会触发一次，最后1个抬起
                // 抬手记录当前偏移值
                lastOffsetX = offsetX;
                lastOffsetY = offsetY;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                //第n个手指触发

                int actionIndex = event.getActionIndex();
                currentPointId = event.getPointerId(actionIndex);


                downX = event.getX(actionIndex);
                downY = event.getY(actionIndex);
                lastOffsetX = offsetX;
                lastOffsetY = offsetY;

                break;

            case MotionEvent.ACTION_POINTER_UP:
                //第n个手指触发

                //获取抬起手指的后的index和id
                int upIndex = event.getActionIndex();

                int pointerId = event.getPointerId(upIndex);

                // 下面逻辑：获取当前手指抬起后找到处理的手指nextUp
                int nextUp = upIndex;

                // 有可能第2个手指按下以后，抬起第1个手指
                // 所以要判断相等
                if (pointerId == currentPointId) {
                    // 最后1根id抬起,判断是不是最后1个Index
                    if (upIndex == event.getPointerCount() - 1) {
                        // 最后一根抬起，则currentPointId为倒数第2根
                        nextUp = event.getPointerCount() - 2;
                    } else {
                        // 抬起中间的手指，则currentPointId为下一根
                        nextUp++;
                    }

                    // 找到后面处理的手指
                    currentPointId = event.getPointerId(nextUp);


                    downX = event.getX(nextUp);
                    downY = event.getY(nextUp);
                    lastOffsetX = offsetX;
                    lastOffsetY = offsetY;

                }


                break;
        }
        return true;
    }
}

