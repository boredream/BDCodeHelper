package com.boredream.bdcodehelper.net;

import android.os.AsyncTask;

import com.boredream.bdcodehelper.base.LeanCloudObject;
import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.CloudResponse;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.entity.UserRegisterByMobilePhone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
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
                @Path("objectId") String objectId);

        // TODO: 2017/7/13 两个方法重复

        // 根据条件获取用户集合
        @GET("1.1/users")
        Observable<ListResponse<User>> getUsersByWhere(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 根据条件获取用户集合
        @GET("1.1/users")
        Observable<ListResponse<User>> getUsersByWhere(
                @Query("where") String where);

        // 修改用户详情(注意, 提交什么参数修改什么参数)
        @PUT("/1/users/{objectId}")
        Observable<LeanCloudObject> updateUserById(
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
        Observable<CloudResponse<User>> imLogin(
                @Body Map<String, String> request);

        @POST("/1.1/call/friendRequest")
        Observable<CloudResponse<User>> friendRequest(
                @Body Map<String, String> request);

        @POST("/1.1/call/getFriends")
        Observable<CloudResponse<ArrayList<User>>> getFriends();

        @POST("/1.1/call/get_friend_requests")
        Observable<CloudResponse<ArrayList<User>>> getFriendRequests();

        @POST("/1.1/call/apply_friend_request")
        Observable<CloudResponse<LeanCloudObject>> applyFriendRequest(
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
    public Observable<ListResponse<User>> getUsersByUsername(String username, int page, int pageSize) {
        String where = "{}";
        if (username != null) {
            where = "{\"username\":\"" + username + "\"}";
        }
        ApiService service = getApiService();
        return service.getUsersByWhere(pageSize, (page - 1) * pageSize, where);
    }

    /**
     * 根据用户id集合获取用户信息集合
     *
     * @param userIds 为null时查询全部好友
     */
    public Observable<ListResponse<User>> getUsersByUsernames(List<String> userIds) {
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
        ApiService service = getApiService();
        return service.getUsersByWhere(where);
    }

}

