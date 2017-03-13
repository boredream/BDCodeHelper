package com.boredream.bdcodehelper.net;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;

import com.boredream.bdcodehelper.base.BoreBaseEntity;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.IUser;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

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

        // log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

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
        Observable<IUser> login(
                @Query("username") String username,
                @Query("password") String password);

        // 手机获取验证码
        @POST("/1/requestSmsCode")
        Observable<Object> requestSmsCode(
                @Body Map<String, Object> mobilePhoneNumber);

        // 手机验证注册
        @POST("/1/users")
        Observable<IUser> userRegist(
                @Body IUser user);

        // 忘记密码重置
        @PUT("/1/resetPasswordBySmsCode/{smsCode}")
        Observable<Object> resetPasswordBySmsCode(
                @Path("smsCode") String smsCode,
                @Body Map<String, Object> password);

        // 旧密码修改新密码
        @POST(" /1/updateUserPassword/{objectId}")
        Observable<IUser> updateUserPassword(
                @Path("smsCode") String smsCode,
                @Body UpdatePswRequest updatePswRequest);

        // 根据昵称搜索用户
        @GET("/1/classes/_User")
        Observable<ListResponse<IUser>> getUserByName(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<IUser> getUserById(
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
    public static Observable<IUser> login(String username, String password) {
        BaseService service = getBaseApiService();
        return service.login(username, password)
                .doOnNext(new Action1<IUser>() {
                    @Override
                    public void call(IUser user) {
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
    public static Observable<IUser> loginByToken(final String[] loginData) {
        BaseService service = getBaseApiService();
        // 这种自动登录方法其实是使用token去再次获取当前账号数据
        return service.getUserById(loginData[0])
                .doOnNext(new Action1<IUser>() {
                    @Override
                    public void call(IUser user) {
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
     * 根据昵称模糊搜索用户,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param searchKey 搜索昵称
     * @param page      页数,从1开始
     */
    public static Observable<ListResponse<IUser>> getUserByName(String searchKey, int page) {
        BaseService service = getBaseApiService();
        String where = "{\"username\":{\"$regex\":\"" + searchKey + ".*\"}}";
        return service.getUserByName(COUNT_OF_PAGE,
                (page - 1) * COUNT_OF_PAGE, where);
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
    public static void fileUpload(final Context context, Uri uri, int reqW, int reqH, final Subscriber<FileUploadResponse> call) {
        final BaseService service = getBaseApiService();
        final String filename = "avatar_" + System.currentTimeMillis() + ".jpg";

        // 先从本地获取图片,利用Glide压缩图片后获取byte[]
        Glide.with(context).load(uri).asBitmap().toBytes().into(
                new SimpleTarget<byte[]>(reqW, reqH) {
                    @Override
                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        // 上传图片
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), resource);

                        Observable<FileUploadResponse> observable = service.fileUpload(filename, requestBody);
                        ObservableDecorator.decorate(observable)
                                .subscribe(call);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        call.onError(new Throwable("图片解析失败"));
                    }
                });
    }

}

