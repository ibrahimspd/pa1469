package com.example.myapplication.entites;

public class FcmToken
{
    private final String token = "token";
    private String tokenValue;
    public FcmToken() {//Intentionally empty
         };
    public FcmToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getToken() {
        return token;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
