package com.boredream.bdcodehelper.base;

import android.app.Application;

public class AppKeeper {

    public static Application app;

    public static void init(Application app) {
        AppKeeper.app = app;
    }

    public static void checkInit() {
        if(AppKeeper.app == null) {
            throw new RuntimeException("you must call AppKeeper.init(application) in your Application class first");
        }
    }

}
