package com.vero.skinlib;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vero.skinlib.utils.SkinResources;
import com.vero.skinlib.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.view.ViewCompat;


/**
 * 这里面
 */
public class SkinAttribute {

    //需要换肤的属性
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    //需要换肤的属性
    private List<SkinView> mSkinViews = new ArrayList<>();


    //记录下一个view身上哪几个属性需要换肤
    public void look(View view, AttributeSet attrs) {
        List<SkinPair> mSkinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //获得属性名，textColor/background
            //如果是需要换肤的，再获取resId
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);

                //以#开头:是写死的，不能换肤
                //以?开头:表示使用系统私有的属性,从主题中去找resId
                //正常以@开头:R文件中的

                //以#开头的是写死的，不能换肤
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                //以?开头的表示使用系统私有的属性,从主题中去找resId
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    //正常以@开头:R文件中的
                    resId = Integer.parseInt(attributeValue.substring(1));
                }

                SkinPair skinPair = new SkinPair(attributeName, resId);
                mSkinPairs.add(skinPair);
            }
        }
        if (!mSkinPairs.isEmpty() || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(view, mSkinPairs);
            //如果选择过皮肤，调用applySkin加载皮肤资源
            skinView.applySkin();
            mSkinViews.add(skinView);
        }

    }

    public void applySkin() {
        for (SkinView skinView : mSkinViews) {
            skinView.applySkin();
        }
    }

    static class SkinView {
        View view;
        //这个View 的属性集合
        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public List<SkinPair> getSkinPairs() {
            return skinPairs;
        }

        public void setSkinPairs(List<SkinPair> skinPairs) {
            this.skinPairs = skinPairs;
        }

        public void applySkin() {
            applySkinSupport();
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background": {
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        //背景可能是color，也可能是drawable
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    }

                    case "src": {
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);

                        }
                        break;
                    }

                    case "textColor": {
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                        break;
                    }
                    case "drawableLeft": {
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    }
                    case "drawableTop": {
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    }
                    case "drawableRight": {
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    }
                    case "drawableBottom": {
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    }
                    //替换更多的值
                    default:
                        break;
                }


                if (left != null || top != null || right != null || bottom != null) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

                }
            }

        }

        private void applySkinSupport() {
            //自定义属性的换肤实现
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }

    }

    static class SkinPair {
        //view的属性名
        String attributeName;
        //对应的res id
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }


}
