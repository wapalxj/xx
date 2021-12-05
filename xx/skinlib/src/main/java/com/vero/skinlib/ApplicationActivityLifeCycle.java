package com.vero.skinlib;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import com.vero.skinlib.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.LayoutInflaterCompat;

public class ApplicationActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    private Observable mObservable;
    private Map<Activity, SkinLayoutInflateFactory> mLayoutInflateFactories = new HashMap<>();

    public ApplicationActivityLifeCycle(Observable observable) {
        mObservable = observable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        //更新状态栏
        SkinThemeUtils.updateStatusBarColor(activity);

        //更新布局视图
        LayoutInflater layoutInflater = activity.getLayoutInflater();

        SkinLayoutInflateFactory skinLayoutInflateFactory = new SkinLayoutInflateFactory(activity);


        //api 30  失败
        forceSetFactory2(layoutInflater,skinLayoutInflateFactory,activity);

        //api--------------
//        try {
////            LayoutInflater使用mFactorySet标记是否设置过Factory
////            如果设置过，则再次设置会抛出异常
////            所以每次换肤都把这个标记设置为false
//            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
//            field.setAccessible(true);
//            field.setBoolean(layoutInflater, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        设置factory2
//        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflateFactory);

        //api--------------

        mLayoutInflateFactories.put(activity, skinLayoutInflateFactory);
        mObservable.addObserver(skinLayoutInflateFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflateFactory observer=mLayoutInflateFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }

    /*

        Android Q不能反射
    作者：JavaNoober
    链接：https://juejin.cn/post/6844903921111023630
    来源：掘金
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private static void forceSetFactory2(LayoutInflater inflater,LayoutInflater.Factory2 factory,Activity activity) {
        Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
        Class<LayoutInflater> inflaterClass = LayoutInflater.class;
        try {
            Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(inflater, false);
            Field mFactory = inflaterClass.getDeclaredField("mFactory");
            mFactory.setAccessible(true);
            Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
//            BackgroundFactory factory = new BackgroundFactory();
//            if (inflater.getFactory2() != null) {
//                factory.setInterceptFactory2(inflater.getFactory2());
//            } else if (inflater.getFactory() != null) {
//                factory.setInterceptFactory(inflater.getFactory());
//            }
            mFactory2.set(inflater, factory);
            mFactory.set(inflater, factory);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
