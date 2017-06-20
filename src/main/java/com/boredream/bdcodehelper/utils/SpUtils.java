package com.boredream.bdcodehelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(AppUtils.getSimplePackageName(context), Context.MODE_PRIVATE);
    }

}
