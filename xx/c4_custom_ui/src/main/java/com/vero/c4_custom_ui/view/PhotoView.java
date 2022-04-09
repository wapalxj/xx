package com.vero.c4_custom_ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import com.vero.c4_custom_ui.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PhotoView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;

    // 居中
    private float originalOffsetX;
    private float originalOffsetY;

    // 小放大：一边全屏，一边留白
    // 大放大：一边全屏，一边超出屏幕
    private float smallScale;//默认 为 小放大
    private float bigScale;

    //大放大系数
    private float OVER_SCALE_FACTOR = 1.5f;

    //当前缩放值
    private float currentScale = 1.0f;

    //是否放大状态
    private boolean isEnlarge;

    // 滑动Offset
    private float offsetX;
    private float offsetY;

    //惯性滑动
    private OverScroller mOverScroller;
    private FlingRunnable mFlingRunnable;


    private GestureDetector mPhotoGestureDetector = new GestureDetector(new PhotoGestureListener());

    private ScaleGestureDetector mPhotoScaleGestureListener;


    //是否处于双指缩放
    private boolean isScale;

    public PhotoView(@NonNull Context context) {
        this(context, null);
    }

    public PhotoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.photo);
        mPaint = new Paint();

        mOverScroller = new OverScroller(context);
        mFlingRunnable = new FlingRunnable();
        mPhotoScaleGestureListener = new ScaleGestureDetector(context, new PhotoScaleGestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 滑动 平移 (放在最前面，放在后面的话，坐标系被缩放会导致滑动距离翻倍)
        float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
        // scaleFraction缩放系数,防止图片跑出屏幕留白
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);
//        canvas.translate(offsetX, offsetY);


        //放大
        canvas.scale(currentScale, currentScale, getWidth() / 2, getHeight() / 2);


        // 居中
        canvas.drawBitmap(mBitmap, originalOffsetX, originalOffsetY, mPaint);
    }


    // onMeasure() ---> onSizeChanged
    // 每次改变尺寸也会调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float width = getWidth();
        float height = getHeight();
        float bitmapWidth = mBitmap.getWidth();
        float bitmapHeight = mBitmap.getHeight();

        originalOffsetX = (width - bitmapWidth) / 2;
        originalOffsetY = (height - bitmapHeight) / 2;

        //缩放一边为全屏大小
        if (bitmapWidth / bitmapHeight > width / height) {
            //图片是横向
            smallScale = width / bitmapWidth;
            bigScale = height / bitmapHeight * OVER_SCALE_FACTOR;
        } else {
            smallScale = height / bitmapHeight;
            bigScale = width / bitmapWidth * OVER_SCALE_FACTOR;

        }

        currentScale = smallScale;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //双指操作优先
        boolean result = mPhotoScaleGestureListener.onTouchEvent(event);
        if (!mPhotoScaleGestureListener.isInProgress()) {
            result = mPhotoGestureDetector.onTouchEvent(event);
        }

        return result;
    }

    //手势监听
    class PhotoGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // up时触发,双击第二次抬起才触发
            Log.e("PhotoGestureDetector===", "onSingleTapUp====");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // 长按 -- 300 ms
            Log.e("PhotoGestureDetector===", "onLongPress====");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 类似Move
            Log.e("PhotoGestureDetector===", "onScroll====");

            //distanceX : x轴滑过的距离(单位时间) == 旧位置-新位置
            //distanceY : y轴滑过的距离

            // 只有在放大时才能拖动
            if (isEnlarge) {
                offsetX -= distanceX;
                offsetY -= distanceY;
                //拖动矫正
                //不能拖到出现空白
                fixOffset();
                invalidate();
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 抛掷，惯性
            Log.e("PhotoGestureDetector===", "onFling====");

            if (isEnlarge) {
                //这里只会执行1次，所有要在下面的FlingRunnable 中继续改变offset
                int minX = (int) -(mBitmap.getWidth() * bigScale - getWidth()) / 2;
                int maxX = (int) (mBitmap.getWidth() * bigScale - getWidth()) / 2;
                int minY = (int) -(mBitmap.getHeight() * bigScale - getHeight()) / 2;
                int maxY = (int) (mBitmap.getHeight() * bigScale - getHeight()) / 2;

                // 回弹幅度为300
                mOverScroller.fling(
                        (int) offsetX, (int) offsetY,
                        (int) velocityX, (int) velocityY,
                        minX, maxX, minY, maxY,
                        300, 300

                );

                postOnAnimation(mFlingRunnable);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // down延时触发 100ms --->pressed效果
            Log.e("PhotoGestureDetector===", "onShowPress====");

        }

        @Override
        public boolean onDown(MotionEvent e) {
            // false 则其他方法不起作用
            Log.e("PhotoGestureDetector===", "onDown====");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // 双击 第2次点击down按下的时候 40ms---300ms
            // 小于40ms表示抖动,不认为是double
            // 双击放大/缩小
            Log.e("PhotoGestureDetector===", "onDoubleTap====" + e.getAction());

            isEnlarge = !isEnlarge;

            if (isEnlarge) {
                // 双击放大后，需要平移到点击的点
                offsetX = (e.getX() - getWidth() / 2) - (e.getX() - getWidth() / 2) * bigScale / smallScale;
                offsetY = (e.getY() - getHeight() / 2) - (e.getY() - getHeight() / 2) * bigScale / smallScale;
                fixOffset();

                //放大
                getScaleAnimator().start();
            } else {
                //缩小
                getScaleAnimator().reverse();
            }

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // 双击 第2次 down/move/up都会触发
            Log.e("PhotoGestureDetector===", "onDoubleTapEvent====");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // 单击按下时触发,双击时不触发
            // 延迟300ms判断，300ms内双击则不触发
            Log.e("PhotoGestureDetector===", "onSingleTapConfirmed====");
            return super.onSingleTapConfirmed(e);
        }
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }

    private ObjectAnimator mScaleAnimator;

    private ObjectAnimator getScaleAnimator() {
        if (mScaleAnimator == null) {
            mScaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0);
        }

        // 范围 从小变到大
        // 防止双击
        if (isScale) {
            // 防止双指缩放后，再双击，造成闪烁
            // 如果双指放大了，则双击先缩小
            isScale = false;
            mScaleAnimator.setFloatValues(smallScale, currentScale);
        } else {
            mScaleAnimator.setFloatValues(smallScale, bigScale);
        }

//        //范围 从小变到大
//        mScaleAnimator.setFloatValues(smallScale, bigScale);

        return mScaleAnimator;
    }

    //拖动矫正
    private void fixOffset() {
        offsetX = Math.min(offsetX, (mBitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetX = Math.max(offsetX, -(mBitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetY = Math.min(offsetY, (mBitmap.getHeight() * bigScale - getHeight()) / 2);
        offsetY = Math.max(offsetY, -(mBitmap.getHeight() * bigScale - getHeight()) / 2);

    }

    // FlingRunnable
    class FlingRunnable implements Runnable {

        @Override
        public void run() {
            //滚动还在进行
            if (mOverScroller.computeScrollOffset()) {
                offsetX = mOverScroller.getCurrX();
                offsetY = mOverScroller.getCurrY();
                invalidate();
                // 每帧动画执行1次，比post性能更好
                postOnAnimation(this);
            }
        }
    }

    // 双指操作
    class PhotoScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        float initialScale;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //缩放

            if ((currentScale > smallScale && !isEnlarge)
                    || (currentScale == smallScale && isEnlarge)) {
                //双指放大
                isEnlarge = !isEnlarge;
            }

            //拿到缩放因子
            currentScale = currentScale * detector.getScaleFactor();
            isScale = true;
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //缩放前,返回true,消费事件
            initialScale = currentScale;
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //缩放后

        }
    }
}

