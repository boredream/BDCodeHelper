package com.boredream.bdcodehelper.net;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.boredream.bdcodehelper.base.BoreBaseEntity;
import com.boredream.bdcodehelper.base.UserInfoKeeper;
import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.CloudResponse;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.entity.UserRegisterByMobilePhone;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class HttpRequest {

    public static final String HOST = "https://api.leancloud.cn";

    protected Retrofit retrofit;
    private static volatile HttpRequest singleton = null;

    public static HttpRequest getSingleton() {
        if (singleton == null) {
            synchronized (HttpRequest.class) {
                if (singleton == null) {
                    singleton = new HttpRequest();
                }
            }
        }
        return singleton;
    }

    protected HttpRequest() {
        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                .client(HttpClientFactory.getOkHttpClient())
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public interface ApiService {
        // 登录用户
        @GET("/1.1/login")
        Observable<User> login(
                @Query("username") String username,
                @Query("password") String password);

        // 利用token获取登陆用户信息
        @GET("/1.1/users/me")
        Observable<User> login();

        // 手机获取验证码
        @POST("/1/requestSmsCode")
        Observable<Object> requestSmsCode(
                @Body Map<String, Object> mobilePhoneNumber);

        // 手机号注册
        @POST("1.1/usersByMobilePhone")
        Observable<User> usersByMobilePhone(
                @Body UserRegisterByMobilePhone user);

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

        // cloud
        @POST("/1.1/call/imlogin")
        Observable<CloudResponse<User>> imlogin(
                @Body Map<String, String> request);
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

    //////////////////////////////


    private Consumer<User> loginConsumer = new Consumer<User>() {
        @Override
        public void accept(@NonNull User user) throws Exception {
            // 保存登录用户数据以及token信息
            UserInfoKeeper.getInstance().setCurrentUser(user);
            // 保存自动登录使用的信息
            UserInfoKeeper.getInstance().saveSessionToken(user.getSessionToken());
        }
    };

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public Observable<User> login(String username, String password) {
        ApiService service = getApiService();
        return service.login(username, password)
                .doOnNext(loginConsumer);
    }

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public Observable<User> loginWithIm(String username, String password) {
        ApiService service = getApiService();
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);
        return service.imlogin(request)
                .map(new Function<CloudResponse<User>, User>() {
                    @Override
                    public User apply(@NonNull CloudResponse<User> response) throws Exception {
                        // TODO: 2017/6/30 error
                        return response.getResult();
                    }
                })
                .doOnNext(loginConsumer);
    }

    /**
     * 使用token自动登录
     */
    public Observable<User> loginByToken() {
        ApiService service = getApiService();
        return service.login()
                .doOnNext(loginConsumer);
    }

    /**
     * 使用token自动登录
     */
    public Observable<User> loginByTokenWithIm(String sessionToken) {
        ApiService service = getApiService();
        Map<String, String> request = new HashMap<>();
        request.put("sessionToken", sessionToken);
        return service.imlogin(request)
                .map(new Function<CloudResponse<User>, User>() {
                    @Override
                    public User apply(@NonNull CloudResponse<User> response) throws Exception {
                        // TODO: 2017/6/30 error
                        return response.getResult();
                    }
                })
                .doOnNext(loginConsumer);
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
    public void fileUpload(final Context context, Uri uri, int reqW, int reqH, final DefaultDisposableObserver<FileUploadResponse> call) {
        final ApiService service = getApiService();
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

