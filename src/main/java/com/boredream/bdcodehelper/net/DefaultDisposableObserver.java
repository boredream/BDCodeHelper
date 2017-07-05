package com.boredream.bdcodehelper.net;

import com.boredream.bdcodehelper.base.BaseView;

import io.reactivex.observers.DisposableObserver;

public class DefaultDisposableObserver<T> extends DisposableObserver<T> {

    private BaseView view;

    public DefaultDisposableObserver(BaseView view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        view.showProgress();
    }

    @Override
    public void onError(Throwable e) {
        view.dismissProgress();

        String error = ErrorConstants.parseHttpErrorInfo(e);
        view.showTip(error);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        view.dismissProgress();
    }
}
