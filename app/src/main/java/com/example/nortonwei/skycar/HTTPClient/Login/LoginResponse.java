package com.example.nortonwei.skycar.HTTPClient.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResponse {
    @SerializedName("status")
    @Expose
    int status;

    @SerializedName("msg")
    @Expose
    String msg;

    @SerializedName("data")
    @Expose
    LoginData data;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public LoginData getData() {
        return data;
    }
}