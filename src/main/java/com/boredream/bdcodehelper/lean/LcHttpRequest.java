package com.boredream.bdcodehelper.lean;

import com.boredream.bdcodehelper.lean.net.LcTokenKeeper;
import com.boredream.bdcodehelper.net.OkHttpClientFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
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
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(API_KEY_NAME, API_KEY_VALUE)
                        .addHeader(SESSION_TOKEN_KEY, LcTokenKeeper.getInstance().getSessionToken())
                        .build();
                return chain.proceed(request);
            }
        });
        interceptors.add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url();
                Set<String> names = url.queryParameterNames();
                String urlStr = url.url().toString();

                if(names.contains("page")) {
                    // 如果包含page，则自动转换为limit和skip的leancloud规则
                    int page = Integer.parseInt(url.queryParameterValues("page").get(0));
                    int skip = LcUtils.page2skip(page);

                    url = url.newBuilder()
                            .removeAllQueryParameters("page")
                            .addQueryParameter("limit", String.valueOf(LcUtils.PAGE_SIZE))
                            .addQueryParameter("skip", String.valueOf(skip))
                            .build();
                }

                if(urlStr.contains("Goods")) {
                    // 如果是Goods接口，则按时间倒序
                    url = url.newBuilder()
                            .addQueryParameter("order", "-createdAt")
                            .addQueryParameter("include", "user")
                            .build();
                }

                request = chain.request().newBuilder()
                        .url(url)
                        .build();
                return chain.proceed(request);
            }
        });

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                .client(OkHttpClientFactory.get(interceptors))
                .build();
    }

}

