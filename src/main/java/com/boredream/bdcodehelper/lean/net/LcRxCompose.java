package com.boredream.bdcodehelper.lean.net;

import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.lean.entity.CloudResponse;
import com.boredream.bdcodehelper.lean.entity.LcErrorResponse;
import com.boredream.bdcodehelper.lean.entity.ListResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LcRxCompose {

    public static <T> ObservableTransformer<T, T> defaultFailed(final BaseView view) {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull final Observable<T> upstream) {
                return upstream.doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        String error = LcErrorConstants.parseHttpErrorInfo(throwable);
                        view.showTip(error);
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<CloudResponse<T>, T> handleCloudResponse() {
        return new ObservableTransformer<CloudResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<CloudResponse<T>> upstream) {
                return upstream.flatMap(new Function<CloudResponse<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull CloudResponse<T> response) throws Exception {
                        if (response.success()) {
                            return Observable.just(response.getResult());
                        } else {
                            // 云引擎error会走200成功回调，要特殊处理下
                            return Observable.error(new LcErrorResponse(response));
                        }
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<ListResponse<T>, ArrayList<T>> handleListResponse() {
        return new ObservableTransformer<ListResponse<T>, ArrayList<T>>() {
            @Override
            public ObservableSource<ArrayList<T>> apply(@NonNull Observable<ListResponse<T>> upstream) {
                return upstream.flatMap(new Function<ListResponse<T>, ObservableSource<ArrayList<T>>>() {
                    @Override
                    public ObservableSource<ArrayList<T>> apply(@NonNull ListResponse<T> response) throws Exception {
                        ArrayList<T> results = response.getResults();
                        return Observable.just(results == null ? new ArrayList<T>() : results);
                    }
                });
            }
        };
    }

}
