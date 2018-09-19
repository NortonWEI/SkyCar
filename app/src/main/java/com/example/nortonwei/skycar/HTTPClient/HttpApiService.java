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

    //send phone
    @POST("/user/register-phone")
    Call<JsonObject> createRegister(@Query("areaCode") String areaCode, @Query("mobile") String mobile, @Query("type") int type);

    //index data
    @GET("/index")
    Call<JsonObject> getIndexData();
}
