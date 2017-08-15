package com.boredream.bdcodehelper.net;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class SimpleDisObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
