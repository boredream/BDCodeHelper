package com.boredream.bdcodehelper.net;

import android.util.Log;

import com.boredream.bdcodehelper.base.BaseView;

import rx.Subscriber;

public class SimpleSubscriber<T> extends Subscriber<T> {

    private BaseView view;

    public SimpleSubscriber(BaseView view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        if(view.isActive()) {
            view.showProgress();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if(view.isActive()) {
            view.dismissProgress();

            String error = ErrorConstants.parseHttpErrorInfo(e);
            view.showTip(error);
        }
    }

    @Override
    public void onNext(T t) {
        if(view.isActive()) {
            view.dismissProgress();
        }
    }
}
