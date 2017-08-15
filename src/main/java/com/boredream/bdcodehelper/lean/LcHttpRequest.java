package com.boredream.bdcodehelper.lean;

import android.support.annotation.NonNull;

import com.boredream.bdcodehelper.lean.net.LcTokenKeeper;

import java.io.IOException;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LcHttpRequest {

    private static final String HOST = "https://api.leancloud.cn";
    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";
    private static final String SESSION_TOKEN_KEY = "X-LC-Session";

    private static final String APP_ID_VALUE = "fMdvuP11j3fRLxco6VQ9Aj69-gzGzoHsz";
    private static final String API_KEY_VALUE = "VuP45bVSUIpo4fLGEmJgfuba";

    protected Retrofit retrofit;

    protected LcHttpRequest() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader(APP_ID_NAME, APP_ID_VALUE)
                                .addHeader(API_KEY_NAME, API_KEY_VALUE)
                                .addHeader(SESSION_TOKEN_KEY, LcTokenKeeper.getInstance().getSessionToken()) // FIXME: 2017/8/15 不应该有context的引用
                                .build();
                        return chain.proceed(request);
                    }})
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        HttpUrl url = request.url();
                        Set<String> names = url.queryParameterNames();
                        if(names.contains("page")) {
                            // 如果包含page，则自动转换为limit和skip的leancloud规则
                            int page = Integer.parseInt(url.queryParameterValues("page").get(0));
                            int skip = LcUtils.page2skip(page);

                            HttpUrl newUrl = url.newBuilder()
                                    .removeAllQueryParameters("page")
                                    .addQueryParameter("limit", String.valueOf(LcUtils.PAGE_SIZE))
                                    .addQueryParameter("skip", String.valueOf(skip))
                                    .build();

                            request = chain.request().newBuilder()
                                    .url(newUrl)
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .build();
    }

}

