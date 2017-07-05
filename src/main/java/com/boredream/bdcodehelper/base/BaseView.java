package com.boredream.bdcodehelper.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseView {

    void showTip(String msg);

    void showProgress();

    void dismissProgress();

    <T> LifecycleTransformer<T> getLifeCycleTransformer();

}
