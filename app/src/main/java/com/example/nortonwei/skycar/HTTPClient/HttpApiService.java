package com.example.nortonwei.skycar.HTTPClient;

import com.example.nortonwei.skycar.HTTPClient.Login.LoginRequest;
import com.example.nortonwei.skycar.HTTPClient.Login.LoginResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface HttpApiService {
    String BASE_URL = "http://mobile_api.dddyp.cn";
    String WECHAT_INFO_URL = "https://api.weixin.qq.com";
    int STATUS_OK = 1;
    int STATUS_LOGOUT = -90;

    //user login
    @POST("/account/login-phone")
    Call<JsonObject> createLogin(@Query("mobile") String mobile, @Query("password") String password);

    //wechat login
    @POST("/account/bind")
    Call<JsonObject> createWechatLogin(@Query("unionId") String unionId, @Query("nickname") String nickname, @Query("headimgurl") String headImg, @Query("sex") int sex);

    //obtain wechat access token
    @GET
    Call<JsonObject> obtainWechatAccessToken(@Url String url);

    //obtain wechat user info
    @GET
    Call<JsonObject> obtainWechatUserInfo(@Url String url);

    //get country list
    @GET("/index/country")
    Call<JsonObject> getCountryList();

    //send verification code
    @POST("/system/send-code")
    Call<JsonObject> sendVerificationCode(@Query("areaCode") String areaCode, @Query("mobile") String mobile, @Query("type") int type);

    //verify code
    @POST("/account/register")
    Call<JsonObject> verifyCode(@Query("areaCode") String areaCode, @Query("mobile") String mobile, @Query("code") String code);

    //set profile
    @POST("/user/register-info")
    Call<JsonObject> setProfile(@Header("token") String token, @Query("nickname") String nickname, @Query("sex") int sex, @Query("email") String email);

    //set password
    @POST("/user/register-pwd")
    Call<JsonObject> setPassword(@Header("token") String token, @Query("password") String password, @Query("password_confirmation") String confirmPassword);

    //index data
    @GET("/index")
    Call<JsonObject> getIndexData();

    //user comment
    @GET("index/comment")
    Call<JsonObject> getUserComment(@Query("token") String token);
}
