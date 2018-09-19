package com.example.nortonwei.skycar.HTTPClient.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("token")
    @Expose
    String token;

    public String getToken() {
        return token;
    }
}
