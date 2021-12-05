package com.vero.skinlib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.vero.skinlib.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

//被观察者，发通知给SkinLayoutInflateFactory
public class SkinManager extends Observable {

    public static SkinManager instance;
    //Activity生命周期
    private ApplicationActivityLifeCycle mSkinActivityLifeCycle;
    private Application mContext;


    public static SkinManager getInstance() {
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public SkinManager(Application application) {
        mContext = application;

        //sp
        SkinPreference.init(application);

        //Resources
        SkinResources.init(application);

        //ActivityLifeCycle
        mSkinActivityLifeCycle = new ApplicationActivityLifeCycle(this);
        application.registerActivityLifecycleCallbacks(mSkinActivityLifeCycle);
        //加载上次使用的皮肤
        loadSkin(SkinPreference.getInstance().getSkin());

    }

    public void loadSkin(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            //还原默认皮肤
            SkinPreference.getInstance().reset();
            SkinResources.getInstance().reset();
        } else {
            try {

                //宿主app的 Resources
                Resources appResources = mContext.getResources();

                //反射创建
                AssetManager assetManager = AssetManager.class.newInstance();
                //资源路径设置
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

                addAssetPath.invoke(assetManager, skinPath);


                //根据当前的设备显示器信息与配置(横竖屏.语言等) 创建Resources

                Resources skinResources = new Resources(assetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());

                //获取皮肤包apk的包名
                PackageManager pm = mContext.getPackageManager();
                PackageInfo info = pm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;

                //设置皮肤resources
                SkinResources.getInstance().applySkin(skinResources, packageName);

                //记录
                SkinPreference.getInstance().setSkin(skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //通知观察者，SkinLayoutInflateFactory
        //换肤
        setChanged();
        notifyObservers();
    }

}
