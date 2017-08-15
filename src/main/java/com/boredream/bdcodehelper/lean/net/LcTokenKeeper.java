package com.boredream.bdcodehelper.lean.net;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.boredream.bdcodehelper.base.AppKeeper;

public class LcTokenKeeper {

    private static final String SP_KEY_TOKEN = "token";
    private static volatile LcTokenKeeper instance = null;
    private final SharedPreferences sp;

    private String token;

    public static LcTokenKeeper getInstance() {
        if (instance == null) {
            synchronized (LcTokenKeeper.class) {
                if (instance == null) {
                    instance = new LcTokenKeeper();
                }
            }
        }
        return instance;
    }

    private LcTokenKeeper() {
        Application app = AppKeeper.getApp();
        sp = app.getSharedPreferences("token_config", Context.MODE_PRIVATE);
    }

    public void saveSessionToken(String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        // 保存在sp中,不像是账号密码敏感信息,无需加密
        this.token = token;
        sp.edit().putString(SP_KEY_TOKEN, token).apply();
    }

    public String getSessionToken() {
        if (token == null) {
            token = sp.getString(SP_KEY_TOKEN, "");
        }
        return token;
    }

    public void clear() {
        sp.edit().remove(SP_KEY_TOKEN).apply();
    }

}
