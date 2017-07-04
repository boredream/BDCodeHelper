package com.boredream.bdcodehelper.net;

import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.base.BoreBaseActivity;

import java.lang.ref.WeakReference;

import io.reactivex.observers.DisposableObserver;

public class DefaultDisposableObserver<T> extends DisposableObserver<T> {

    private BaseView view;

    public DefaultDisposableObserver(BaseView view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        if(view.isActive()) {
            view.showProgress();
        }
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
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        if(view.isActive()) {
            view.dismissProgress();
        }
    }
}
