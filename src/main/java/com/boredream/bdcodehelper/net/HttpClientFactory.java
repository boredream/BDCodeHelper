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

    private static volatile OkHttpClient httpClient;

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static OkHttpClient getLeanCloudHttpClient() {
        final String appId = "kCb4hozHVvHQIYbL9xf2rdRi-gzGzoHsz";
        final String apiKey = "teykeivIf7fbObEo1TEvredO";
        final String session = UserInfoKeeper.getInstance().getSessionToken();
        
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
                                            .addHeader("X-LC-Id", appId)
                                            .addHeader("X-LC-Key", apiKey)
                                            .addHeader("X-LC-Session", session)
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
