package com.boredream.bdcodehelper.net;

import com.boredream.bdcodehelper.entity.BaseResponse;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Rx組件
 */
public class RxComposer {

    public static <T> ObservableTransformer<BaseResponse<T>, T> handleBaseResponse() {
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


    public static <T> ObservableTransformer<T, T> schedulers() {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
