package com.boredream.bdcodehelper.base;

import android.app.Application;

public class AppKeeper {

    private static Application app;

    public static void init(Application app) {
        AppKeeper.app = app;
    }

    public static Application getApp() {
        if(AppKeeper.app == null) {
            throw new RuntimeException("you must call AppKeeper.init(application) in your Application class first");
        }
        return app;
    }

}
