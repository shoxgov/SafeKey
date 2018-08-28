package com.qingwing.safekey.okhttp3.response;


/*
token	令牌
refreshentoken
  */
public class LoginResponse extends BaseResponse {
    private String token;
    private String refreshentoken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshentoken() {
        return refreshentoken;
    }

    public void setRefreshentoken(String refreshentoken) {
        this.refreshentoken = refreshentoken;
    }
}
