package com.boredream.bdcodehelper.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 用户信息保存工具
 */
public class UserInfoKeeper<T> {

    private static volatile UserInfoKeeper instance = null;

    public static UserInfoKeeper getInstance() {
        if (instance == null) {
            synchronized (UserInfoKeeper.class) {
                if (instance == null) {
                    instance = new UserInfoKeeper();
                }
            }
        }
        return instance;
    }

    private UserInfoKeeper() {
        // private
    }


    private T currentUser;
    private String token;

    public T getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(T user) {
        currentUser = user;
    }

    public void clearCurrentUser() {
        currentUser = null;
    }

    public void saveSessionToken(String token) {
        this.token = token;
    }

    public String getSessionToken() {
        return token;
    }

    /**
     * 登出,同时清空用户信息和登录信息
     */
    public void logout() {
        currentUser = null;
        token = null;
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
