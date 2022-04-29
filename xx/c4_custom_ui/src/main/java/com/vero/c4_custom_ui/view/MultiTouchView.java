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

                break;

            case MotionEvent.ACTION_POINTER_UP:
                //第n个手指触发
                break;
        }
        return true;
    }
}

