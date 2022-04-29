package com.vero.base.autoservice;

import java.util.ServiceLoader;

/**
 * 通用加载器
 */
public class VeroServiceLoader {

    private VeroServiceLoader() {

    }

    public static <S> S load(Class<S> service) {
        try {
            return ServiceLoader.load(service).iterator().next();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
