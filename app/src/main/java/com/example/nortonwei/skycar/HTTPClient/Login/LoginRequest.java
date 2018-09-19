package com.example.nortonwei.skycar.HTTPClient.Login;

public class LoginRequest {
    private String mobile = "";
    private String password = "";

    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }
}
