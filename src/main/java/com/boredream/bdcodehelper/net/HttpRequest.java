package com.boredream.bdcodehelper.net;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.boredream.bdcodehelper.base.BoreBaseEntity;
import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.BaseResponse;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.entity.UserRegisterByMobilePhone;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
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

        // 注册
        @POST("/1.1/users")
        Observable<User> userRegist(
                @Body User user);

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

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getUserById(
                @Path("objectId") String userId);

        // TODO: 2017/7/13 两个方法重复

        // 根据条件获取用户集合
        @GET("1.1/users")
        Observable<BaseResponse<User>> getUsersByWhere(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 根据条件获取用户集合
        @GET("1.1/users")
        Observable<BaseResponse<User>> getUsersByWhere(
                @Query("where") String where);

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
        Observable<AppUpdateInfo> getAppUpdateInfo();

        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String fileUrl);

        // cloud
        @POST("/1.1/call/imlogin")
        Observable<BaseResponse<User>> imLogin(
                @Body Map<String, String> request);
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

    //////////////////////////////

    /**
     * 根据用户名获取用户信息集合
     *
     */
    public Observable<BaseResponse<User>> getUsersByUsername(String username, int page, int pageSize) {
        String where = "{}";
        if (username != null) {
            where = "{\"username\":\"" + username + "\"}";
        }
        ApiService service = getApiService();
        return service.getUsersByWhere(pageSize, (page - 1) * pageSize, where);
    }

    /**
     * 根据用户名集合获取用户信息集合
     *
     * @param usernames 为null时查询全部好友
     */
    public Observable<BaseResponse<User>> getUsersByUsernames(List<String> usernames) {
        String where = "{}";
        if (usernames != null) {
            StringBuilder sb = new StringBuilder();
            for (String id : usernames) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append("\"").append(id).append("\"");
            }
            where = "{\"username\":{\"$in\":[" + sb.toString() + "]}}";
        }
        ApiService service = getApiService();
        return service.getUsersByWhere(where);
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

                service.fileUpload(filename, requestBody)
                    .compose(RxComposer.<FileUploadResponse>schedulers())
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

