package com.boredream.bdcodehelper.utils;

import android.util.Log;

import com.boredream.bdcodehelper.constants.CommonConstants;

public class LogUtils {

    public static void showLog(String log) {
        showLog("DDD", log);
    }

    public static void showLog(String tag, String log) {
        showLog(Log.INFO, tag, log);
    }

    public static void showLog(int level, String tag, String log) {
        if (!CommonConstants.isShowLog) {
            return;
        }

        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, log);
                break;
            case Log.DEBUG:
                Log.d(tag, log);
                break;
            case Log.INFO:
                Log.i(tag, log);
                break;
            case Log.WARN:
                Log.w(tag, log);
                break;
            case Log.ERROR:
                Log.e(tag, log);
                break;
            default:
                Log.i(tag, log);
                break;
        }
    }
}
