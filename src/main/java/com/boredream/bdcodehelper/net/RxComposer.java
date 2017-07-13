package com.boredream.bdcodehelper.net;

import com.boredream.bdcodehelper.entity.BaseResponse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx組件
 */
public class RxComposer {

    public static <T> ObservableTransformer<BaseResponse<T>, T> handleCloudResponse() {
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BaseResponse<T> response) throws Exception {
                        if (response.success()) {
                            return Observable.just(response.getResult());
                        } else {
                            // 云引擎error会走200成功回调，要特殊处理下
                            return Observable.error(new LeanCloudError(response));
                        }
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<BaseResponse<T>, ArrayList<T>> handleListResponse() {
        return new ObservableTransformer<BaseResponse<T>, ArrayList<T>>() {
            @Override
            public ObservableSource<ArrayList<T>> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.flatMap(new Function<BaseResponse<T>, ObservableSource<ArrayList<T>>>() {
                    @Override
                    public ObservableSource<ArrayList<T>> apply(@NonNull BaseResponse<T> response) throws Exception {
                        ArrayList<T> results = response.getResults();
                        return Observable.just(results == null ? new ArrayList<T>() : results);
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> schedulers() {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
