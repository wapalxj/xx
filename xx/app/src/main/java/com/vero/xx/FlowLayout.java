package com.vero.xx;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "Flowlayout";
    private int mHorizontalSpacing = dp2px(16);//item横向间距
    private int mVerticalSpacing = dp2px(16);//item纵向间距

    //list需要再onMeasure()中第一行再次初始化(或者clear),
    //因为很多viewGroup要进行多次的measure
    //可参考FrameLayout源码
    private List<List<View>> allLines = new ArrayList<>();//记录所有的行，一行一行的存储，用于layout
    private List<Integer> lineHeights = new ArrayList<>();//记录每一行的行高，用于layout


    public FlowLayout(Context context) {
        super(context);
    }

    //反射
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //主题style
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //自定义属性
//    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void clearMeasureParams() {
        //每次都new 会造成内存抖动
//        allLines = new ArrayList<>();
//        lineHeights = new ArrayList<>();
        allLines.clear();
        lineHeights.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //
        clearMeasureParams();
        //先度量孩子
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        //parent解析可以给我的宽度
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);
        List<View> lineViews = new ArrayList<>();
        int lineWidthUsed = 0;//记录这行已经使用了的宽度size
        int lineHeight = 0;//一行行高


        int parentNeededHeight = 0;
        int parentNeededWidth = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            LayoutParams childLp = childView.getLayoutParams();

            //将LayoutParams转变成measureSpec
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    paddingLeft + paddingRight, childLp.width);

            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    paddingTop + paddingBottom, childLp.height);

            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            //获取子view宽高
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            //如果需要换行
            if (childMeasuredWidth + lineWidthUsed + mHorizontalSpacing > selfWidth) {
                //一旦换行,我们就可以判断当前行需要的宽和高了，所以此时要记录下来
                allLines.add(lineViews);
                lineHeights.add(lineHeight);

                //记录现在总高度
                parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
                //记录下目前所有行里，最宽的那一行
                parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);

                lineViews = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }


            //view是分行layout的,要记录每一行的view
            lineViews.add(childView);
            //每一行的宽高
            lineWidthUsed = lineWidthUsed + childMeasuredWidth + mHorizontalSpacing;
            lineHeight = Math.max(lineHeight, childMeasuredHeight);

            //处理最后一行
            if (i == childCount - 1) {
                allLines.add(lineViews);
                lineHeights.add(lineHeight);
                parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
                parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);
            }
        }

        //再度量自己
        //根据子view的度量结果+自己的mode，来重新度量viewGroup自己
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parentNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parentNeededHeight;
        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //待完善：这里需要考虑Gravity
        int lineCounts = allLines.size();
        int curL = getPaddingLeft();
        int curT = getPaddingTop();

        for (int i = 0; i < lineCounts; i++) {
            List<View> lineViews = allLines.get(i);
            int lineHeight = lineHeights.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View view = lineViews.get(j);
                int left = curL;
                int top = curT;
                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                curL = right + mHorizontalSpacing;
            }
            //下一行
            curL = getPaddingLeft();
            curT = curT + lineHeight + mVerticalSpacing;
        }
    }


    private int dp2px(int dp) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return (int) (dp * dm.density + 0.5f);
    }
}
