package com.boredream.bdcodehelper.net;

import android.support.annotation.NonNull;

import com.boredream.bdcodehelper.base.UserInfoKeeper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpClientFactory {

    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";
    private static final String SESSION_TOKEN_KEY = "X-LC-Session";

    private static final String APP_ID_VALUE = "kCb4hozHVvHQIYbL9xf2rdRi-gzGzoHsz";
    private static final String API_KEY_VALUE = "teykeivIf7fbObEo1TEvredO";

    private static volatile OkHttpClient httpClient;

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static OkHttpClient getOkHttpClient() {
        if (httpClient == null) {
            synchronized (HttpClientFactory.class) {
                if (httpClient == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    // OkHttpClient
                    httpClient = new OkHttpClient.Builder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader("Content-Type", "application/json")
                                            .addHeader(APP_ID_NAME, APP_ID_VALUE)
                                            .addHeader(API_KEY_NAME, API_KEY_VALUE)
                                            .addHeader(SESSION_TOKEN_KEY, UserInfoKeeper.getInstance().getSessionToken())
                                            .build();
                                    return chain.proceed(request);
                                }})
                            .addInterceptor(loggingInterceptor)
                            .build();
                }
            }
        }
        return httpClient;
    }

}
