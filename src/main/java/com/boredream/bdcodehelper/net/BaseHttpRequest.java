package com.boredream.bdcodehelper.net;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.boredream.bdcodehelper.base.BoreBaseEntity;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.entity.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class BaseHttpRequest {

    public static final int COUNT_OF_PAGE = 20;

    // LeanCloud
    public static final String HOST = "https://api.leancloud.cn";

    public static final String FILE_HOST = "";
    private static final String APP_ID_NAME = "X-LC-Id";
    private static final String API_KEY_NAME = "X-LC-Key";

    public static final String SESSION_TOKEN_KEY = "X-LC-Session";
    private static final String APP_ID_VALUE = "YeWYXUgKCPByvToJj3u1mUUw-gzGzoHsz";

    private static final String API_KEY_VALUE = "Ypx2LgIxvScVwgzPI0UcNSgM";

    protected static Retrofit retrofit;
    protected static OkHttpClient httpClient;

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    static {
        // OkHttpClient
        httpClient = new OkHttpClient();

        // 统一添加的Header
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(API_KEY_NAME, API_KEY_VALUE)
                        .addHeader(SESSION_TOKEN_KEY, UserInfoKeeper.getInstance().getToken())
                        .build();
                return chain.proceed(request);
            }
        });

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }


    public interface BaseService {
        // 登录用户
        @GET("/1/login")
        Observable<User> login(
                @Query("username") String username,
                @Query("password") String password);

        // 手机获取验证码
        @POST("/1/requestSmsCode")
        Observable<Object> requestSmsCode(
                @Body Map<String, Object> mobilePhoneNumber);

        // 手机验证注册
        @POST("/1/users")
        Observable<User> userRegist(
                @Body User user);

        // 忘记密码重置
        @PUT("/1/resetPasswordBySmsCode/{smsCode}")
        Observable<Object> resetPasswordBySmsCode(
                @Path("smsCode") String smsCode,
                @Body Map<String, Object> password);

        // 旧密码修改新密码
        @POST(" /1/updateUserPassword/{objectId}")
        Observable<User> updateUserPassword(
                @Path("smsCode") String smsCode,
                @Body UpdatePswRequest updatePswRequest);

        // 根据昵称搜索用户
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getUserByName(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getUserById(
                @Path("objectId") String userId);

        // 修改用户详情(注意, 提交什么参数修改什么参数)
        @PUT("/1/users/{objectId}")
        Observable<BoreBaseEntity> updateUserById(
                @Path("objectId") String userId,
                @Body Map<String, Object> updateInfo);

        // 上传图片接口
        @POST("/1/files/{fileName}")
        Observable<FileUploadResponse> fileUpload(
                @Path("fileName") String fileName,
                @Body RequestBody image);

        // 查询app更新信息
        @GET("/1/classes/AppUpdateInfo")
        Observable<ListResponse<AppUpdateInfo>> getAppUpdateInfo();

        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String fileUrl);
    }

    public static BaseService getBaseApiService() {
        return retrofit.create(BaseService.class);
    }

    //////////////////////////////

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public static Observable<User> login(String username, String password) {
        BaseService service = getBaseApiService();
        return service.login(username, password)
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.getInstance().setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.getInstance().saveLoginData(user.getUserId(), user.getSessionToken());
                    }
                });
    }

    /**
     * 使用token自动登录
     *
     * @param loginData size为2的数组, 第一个为当前用户id, 第二个为当前用户token
     */
    public static Observable<User> loginByToken(final String[] loginData) {
        BaseService service = getBaseApiService();
        // 这种自动登录方法其实是使用token去再次获取当前账号数据
        return service.getUserById(loginData[0])
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        // 获取用户信息接口不会返回token
                        UserInfoKeeper.getInstance().setToken(loginData[1]);
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.getInstance().setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.getInstance().saveLoginData(user.getUserId(), user.getSessionToken());
                    }
                });
    }

    /**
     * 上传图片
     *
     * @param call    上传成功回调
     * @param context
     * @param uri     图片uri
     * @param reqW    上传图片需要压缩的宽度
     * @param reqH    上传图片需要压缩的高度
     * @param call
     */
    public static void fileUpload(final Context context, Uri uri, int reqW, int reqH, final SimpleSubscriber<FileUploadResponse> call) {
        final BaseService service = getBaseApiService();
        final String filename = "avatar_" + System.currentTimeMillis() + ".jpg";

        // 先从本地获取图片,利用Glide压缩图片后获取byte[]
        Glide.with(context).asFile().load(uri).into(new SimpleTarget<File>(reqW, reqH) {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                // 上传图片
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), resource);

                Observable<FileUploadResponse> observable = service.fileUpload(filename, requestBody);
                ObservableDecorator.decorate(observable)
                        .subscribe(call);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);

                call.onError(new Throwable("图片解析失败"));
            }
        });
    }

}

