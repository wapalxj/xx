package com.vero.c4_custom_ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.vero.c4_custom_ui.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ColorTrackTextView extends AppCompatTextView {


    // 绘制默认颜色的paint
    Paint mOriginPaint;

    // 绘制变色的颜色paint
    Paint mChangePaint;

    // 当前变色进度
    private float mCurrentProgress = 0.3f;


    private Direction mDirection = Direction.LEFT_TO_RIGHT;

    // 不同方向
    public enum Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }


    public ColorTrackTextView(@NonNull Context context) {
        this(context, null);
    }

    public ColorTrackTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView);

        int originColor = typedArray.getColor(R.styleable.ColorTrackTextView_originColor,
                getTextColors().getDefaultColor());

        int changeColor = typedArray.getColor(R.styleable.ColorTrackTextView_changeColor,
                getTextColors().getDefaultColor());

        //回收
        typedArray.recycle();


        mOriginPaint = getPaintByColor(originColor);

        mChangePaint = getPaintByColor(changeColor);


    }


    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        //防抖动
        paint.setDither(true);
        paint.setTextSize(getTextSize());

        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 计算变色分界点
        int currentPoint = (int) (mCurrentProgress * getWidth());

        // 从左到右变色
        if (mDirection == Direction.LEFT_TO_RIGHT) {
            drawText(canvas, mChangePaint, 0, currentPoint);
            drawText(canvas, mOriginPaint, currentPoint, getWidth());
        } else {
            drawText(canvas, mChangePaint, getWidth() - currentPoint, getWidth());
            drawText(canvas, mOriginPaint, 0, getWidth() - currentPoint);
        }
    }

    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        // 因为下面clip画布了，
        // 需要存储clip之前的状态,
        // 否则第二次绘制不出来
        canvas.save();
        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);


        String text = getText().toString();

        if (TextUtils.isEmpty(text)) return;

        // 获取文字区域
        Rect bounds = new Rect();

        paint.getTextBounds(text, 0, text.length(), bounds);

        // 获取x坐标
        int dx = getWidth() / 2 - bounds.width() / 2;


        // 获取基线 baseline
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;

        //baseLine 等价于 baseLine=getHeight()-fontMetricsInt.bottom


        //绘制文字
        canvas.drawText(text, dx, baseLine, paint);
//        canvas.drawText(text, dx, getHeight()-fontMetricsInt.bottom, mOriginPaint);

        canvas.restore();
    }

    public float getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }
}

