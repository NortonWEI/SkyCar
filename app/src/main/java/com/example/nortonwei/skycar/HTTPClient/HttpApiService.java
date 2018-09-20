package com.example.nortonwei.skycar.HTTPClient;

import com.example.nortonwei.skycar.HTTPClient.Login.LoginRequest;
import com.example.nortonwei.skycar.HTTPClient.Login.LoginResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpApiService {
    String BASE_URL = "http://mobile_api.dddyp.cn";
    int STATUS_OK = 1;
    int STATUS_LOGOUT = -90;

    //user login
    @POST("/account/login-phone")
    Call<JsonObject> createLogin(@Query("mobile") String mobile, @Query("password") String password);

    //get country list
    @GET("/index/country")
    Call<JsonObject> getCountryList();

    //send verification code
    @POST("/system/send-code")
    Call<JsonObject> sendVerificationCode(@Query("areaCode") String areaCode, @Query("mobile") String mobile, @Query("type") int type);

    //verify code
    @POST("/account/register")
    Call<JsonObject> verifyCode(@Query("areaCode") String areaCode, @Query("mobile") String mobile, @Query("code") String code);

    //index data
    @GET("/index")
    Call<JsonObject> getIndexData();

    //user comment
    @GET("index/comment")
    Call<JsonObject> getUserComment(@Query("token") String token);
}
