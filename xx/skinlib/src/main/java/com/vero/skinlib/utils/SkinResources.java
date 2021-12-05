package com.vero.skinlib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

public class SkinResources {

    private String mSkinPkgName;

    private boolean isDefaultSkin = true;

    //app的原始的resource
    private Resources mAppResources;

    //皮肤包的resource
    private Resources mSkinResources;


    public SkinResources(Context context) {
        this.mAppResources = context.getResources();
    }

    //singleton
    private volatile static SkinResources instance;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(context);
                }
            }
        }
    }


    public static SkinResources getInstance() {
        return instance;
    }

    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }


    public void applySkin(Resources resources, String pkgName) {
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 1.通过原始app中的resId(R.xxx.xxx)获取到key
     * <p>
     * 2.根据上面的key和类型获取皮肤包中的resid
     */

    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinId = mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }


    /**
     * 通过主app的resId,得到skin resId
     *
     * @param resId
     * @return
     */
    public int getColor(int resId) {
        Log.e("getColor=====",resId+""+mAppResources.getResourceEntryName(resId));
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }

        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(skinId);
        }

        Log.e("getColor222=====",resId+""+mSkinResources.getResourceEntryName(resId));
        return mSkinResources.getColor(skinId);
    }


    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if ("color".equals(resourceTypeName)) {
            return getColor(resId);
        } else {
            // drawable
            return getDrawable(resId);
        }
    }

    public String getString(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getString(resId);
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getString(resId);
        }
        return mSkinResources.getString(skinId);
    }

}
