package com.group_13.service;


public class AuthService {
    private static AuthService INSTANCE;
    private String token;

    private AuthService() {
        this.token = null;
    }

    public static AuthService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AuthService();
        }
        return INSTANCE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
