package com.boredream.bdcodehelper.base;

import android.app.Application;

import com.boredream.bdcodehelper.net.HttpClientFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;

public class AppKeeper {

    public static Application app;

    public static void init(Application app) {
        AppKeeper.app = app;
        initGlide(app);
    }

    public static void checkInit() {
        if(AppKeeper.app == null) {
            throw new RuntimeException("you must call AppKeeper.init(application) in your Application class first");
        }
    }

    /**
     * 图片加载框架Glide,使用OkHttp处理网络请求
     */
    private static void initGlide(Application app) {
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(HttpClientFactory.getOkHttpClient());
        Glide.get(app).getRegistry().append(GlideUrl.class, InputStream.class, factory);
    }

}
