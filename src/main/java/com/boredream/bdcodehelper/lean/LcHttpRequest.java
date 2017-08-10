package com.boredream.bdcodehelper.lean;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.lean.entity.LcUser;
import com.boredream.bdcodehelper.lean.entity.ListResponse;
import com.boredream.bdcodehelper.lean.net.LcApiService;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
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

    private static final String APP_ID_VALUE = "kCb4hozHVvHQIYbL9xf2rdRi-gzGzoHsz";
    private static final String API_KEY_VALUE = "teykeivIf7fbObEo1TEvredO";

    private Retrofit retrofit;
    private static volatile LcHttpRequest singleton = null;

    public static LcHttpRequest getSingleton() {
        if (singleton == null) {
            synchronized (LcHttpRequest.class) {
                if (singleton == null) {
                    singleton = new LcHttpRequest();
                }
            }
        }
        return singleton;
    }

    private LcHttpRequest() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient
        OkHttpClient httpClient = new OkHttpClient.Builder()
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

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public LcApiService getApiService() {
        return retrofit.create(LcApiService.class);
    }

    //////////////////////////////

    /**
     * 根据用户名获取用户信息集合
     *
     */
    public Observable<ListResponse<LcUser>> getUsersByUsername(String username, int page, int pageSize) {
        String where = "{}";
        if (username != null) {
            where = "{\"username\":\"" + username + "\"}";
        }
        LcApiService service = getApiService();
        return service.getUsersByWhere(pageSize, (page - 1) * pageSize, where);
    }

    /**
     * 根据用户id集合获取用户信息集合
     *
     * @param userIds 为null时查询全部好友
     */
    public Observable<ListResponse<LcUser>> getUsersByUsernames(List<String> userIds) {
        String where = "{}";
        if (userIds != null) {
            StringBuilder sb = new StringBuilder();
            for (String id : userIds) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append("\"").append(id).append("\"");
            }
            where = "{\"objectId\":{\"$in\":[" + sb.toString() + "]}}";
        }
        LcApiService service = getApiService();
        return service.getUsersByWhere(where);
    }

}

