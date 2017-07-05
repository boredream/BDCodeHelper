package com.boredream.bdcodehelper.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.User;
import com.google.gson.Gson;

/**
 * 用户信息保存工具
 */
public class UserInfoKeeper {

    private final SharedPreferences sp;

    public static UserInfoKeeper getInstance() {
        if(instance == null) {
            instance = new UserInfoKeeper();
        }
        return instance;
    }

    private static UserInfoKeeper instance;

    private UserInfoKeeper() {
        AppKeeper.checkInit();
        sp = AppKeeper.app.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

    private static final String SP_KEY_CURRENT_USER = "current_user";
    private static final String SP_KEY_TOKEN = "token";

    private User currentUser;

    /**
     * 获取当前登录用户,先从缓存中获取,获取不到时从sp中获取
     */
    public User getCurrentUser() {
        String userJson = sp.getString(SP_KEY_CURRENT_USER, null);
        if (currentUser == null && !TextUtils.isEmpty(userJson)) {
            currentUser = new Gson().fromJson(userJson, User.class);
        }
        return currentUser;
    }

    /**
     * 保存设置当前登录用户,缓存和sp中都进行保存
     */
    public void setCurrentUser(User user) {
        if (user != null) {
            String userJson = new Gson().toJson(user);
            sp.edit().putString(SP_KEY_CURRENT_USER, userJson).apply();
        }
        currentUser = user;
    }

    /**
     * 清空当前登录用户,同时清空缓存和sp中信息
     */
    public void clearCurrentUser() {
        currentUser = null;
        sp.edit().remove(SP_KEY_CURRENT_USER).remove(SP_KEY_TOKEN).apply();
    }

    public void saveSessionToken(String token) {
        if (TextUtils.isEmpty(token)) {
            return;
        }

        // 保存在sp中,不像是账号密码敏感信息,无需加密
        sp.edit().putString(SP_KEY_TOKEN, token).apply();
    }

    public String getSessionToken() {
        // 统一Header配置时用的token,没有的话要用空字符串,不能为null
        String token;
        if (currentUser != null && currentUser.getSessionToken() != null) {
            token = currentUser.getSessionToken();
        } else {
            token = sp.getString(SP_KEY_TOKEN, "");
        }
        return token;
    }

    /**
     * 登出,同时清空用户信息和登录信息
     */
    public void logout() {
        clearCurrentUser();
    }

    /**
     * 检测登录状态
     *
     * @return true-已登录 false-未登录,会自动跳转至登录页
     */
    public static boolean checkLogin(Context context, Class<? extends Activity> loginActivityClass) {
        if (UserInfoKeeper.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(context, loginActivityClass);
            intent.putExtra("checkLogin", true);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

}
