package com.boredream.bdcodehelper.base;

public interface BaseView {

    boolean isActive();

    void showTip(String msg);

    void showProgress();

    void dismissProgress();

}
