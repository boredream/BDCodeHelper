package com.boredream.bdcodehelper.lean.net;

import com.boredream.bdcodehelper.lean.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.lean.entity.CloudResponse;
import com.boredream.bdcodehelper.lean.entity.FileUploadResponse;
import com.boredream.bdcodehelper.lean.entity.LeanCloudObject;
import com.boredream.bdcodehelper.lean.entity.ListResponse;
import com.boredream.bdcodehelper.lean.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.lean.entity.LcUser;
import com.boredream.bdcodehelper.lean.entity.UserRegisterByMobilePhone;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * <pre>
 *     author : lichunyang
 *     time   : 2017/08/10
 *     desc   :
 * </pre>
 */
public interface LcApiService {

    // 登录用户
    @GET("/1.1/login")
    Observable<LcUser> login(
            @Query("username") String username,
            @Query("password") String password);

    // 注册
    @POST("/1.1/users")
    Observable<LcUser> userRegist(
            @Body LcUser user);

    // 利用token获取登陆用户信息
    @GET("/1.1/users/me")
    Observable<LcUser> login();

    // 手机获取验证码
    @POST("/1/requestSmsCode")
    Observable<Object> requestSmsCode(
            @Body Map<String, Object> mobilePhoneNumber);

    // 手机号注册
    @POST("1.1/usersByMobilePhone")
    Observable<LcUser> usersByMobilePhone(
            @Body UserRegisterByMobilePhone user);

    // 忘记密码重置
    @PUT("/1/resetPasswordBySmsCode/{smsCode}")
    Observable<Object> resetPasswordBySmsCode(
            @Path("smsCode") String smsCode,
            @Body Map<String, Object> password);

    // 旧密码修改新密码
    @POST(" /1/updateUserPassword/{objectId}")
    Observable<LcUser> updateUserPassword(
            @Path("smsCode") String smsCode,
            @Body UpdatePswRequest updatePswRequest);

    // 获取用户详情
    @GET("/1/users/{objectId}")
    Observable<LcUser> getUserById(
            @Path("objectId") String objectId);

    // TODO: 2017/7/13 两个方法重复

    // 根据条件获取用户集合
    @GET("1.1/users")
    Observable<ListResponse<LcUser>> getUsersByWhere(
            @Query("limit") int perPageCount,
            @Query("skip") int page,
            @Query("where") String where);

    // 根据条件获取用户集合
    @GET("1.1/users")
    Observable<ListResponse<LcUser>> getUsersByWhere(
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
    Observable<CloudResponse<LcUser>> imLogin(
            @Body Map<String, String> request);

    @POST("/1.1/call/friendRequest")
    Observable<CloudResponse<LcUser>> friendRequest(
            @Body Map<String, String> request);

    @POST("/1.1/call/getFriends")
    Observable<CloudResponse<ArrayList<LcUser>>> getFriends();

    @POST("/1.1/call/get_friend_requests")
    Observable<CloudResponse<ArrayList<LcUser>>> getFriendRequests();

    @POST("/1.1/call/apply_friend_request")
    Observable<CloudResponse<LeanCloudObject>> applyFriendRequest(
            @Body Map<String, String> request);
}
