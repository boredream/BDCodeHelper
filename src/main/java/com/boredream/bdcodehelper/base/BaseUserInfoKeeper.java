package com.boredream.bdcodehelper.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 用户信息保存工具
 */
public abstract class BaseUserInfoKeeper<T, E extends Activity> {

    private T currentUser;

    public T getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(T user) {
        currentUser = user;
    }

    public void clearCurrentUser() {
        currentUser = null;
    }

    /**
     * 登出,同时清空用户信息和登录信息
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * 检测登录状态
     *
     * @return true-已登录 false-未登录,会自动跳转至登录页
     */
    public boolean checkLogin(Context context, Class<E> loginActivityClass) {
        if (getCurrentUser() == null) {
            Intent intent = new Intent(context, loginActivityClass);
            intent.putExtra("checkLogin", true);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

}
