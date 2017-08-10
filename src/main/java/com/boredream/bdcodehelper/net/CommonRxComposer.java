package com.boredream.bdcodehelper.net;

import com.boredream.bdcodehelper.base.BaseView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx組件
 */
public class CommonRxComposer {

    public static <T> ObservableTransformer<T, T> schedulers() {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> doProgress(final BaseView view) {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull final Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                view.showProgress();
                            }
                        })
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                view.dismissProgress();
                            }
                        });
            }
        };
    }
}
