package com.vero.skinlib;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.vero.skinlib.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 用来接管系统的view的生产过程
 */
public class SkinLayoutInflateFactory implements LayoutInflater.Factory2, Observer {

    //前缀
    private static final String[] mCLassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view.",
    };

    //记录对应View的构造函数
    static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> mConstructorMap =
            new HashMap<String, Constructor<? extends View>>();


    //当选择新皮肤后需要医患View与之对应的属性
    //页面属性管理器
    private SkinAttribute skinAttribute;


    //用于获取窗口的状态框信息
    private Activity activity;


    public SkinLayoutInflateFactory(Activity activity) {
        this.skinAttribute = new SkinAttribute();
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        //换肤就是在需要的时候替换View的属性(src.background)等
        //所以这里创建view,从而修改View属性
        View view = createSDKView(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }
        if (view != null) {
            //记录属性
            skinAttribute.look(view, attrs);
        }
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    private View createSDKView(String name, Context context, AttributeSet attrs) {
        if (-1 != name.indexOf('.')) {
            //不是SDK中的View，不拼接前缀
            return null;
        }

        for (int i = 0; i < mCLassPrefixList.length; i++) {
            //拼接一个对的前缀，生产view
            View view = createView(mCLassPrefixList[i] + name, context, attrs);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (constructor == null) {
            try {
                //反射获取2个参数的构造方法
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                mConstructorMap.put(name, constructor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return constructor;
    }

    //java.util.Observer
    //接收到通知后，全部换肤
    @Override
    public void update(Observable o, Object arg) {
        SkinThemeUtils.updateStatusBarColor(activity);
        skinAttribute.applySkin();
    }
}
