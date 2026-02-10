package com.group_13.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.group_13.api.AuthAPI;
import com.group_13.model.ApiResponse;
import org.json.JSONObject;

import com.group_13.model.Result;

public class AuthService {
    private static final AuthService INSTANCE = new AuthService();
    private final AuthAPI authAPI = AuthAPI.getInstance();
    private String token;
    private long expiration;

    private AuthService() {
        this.token = null;
    }

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public boolean isTokenValid() {
        return token != null && System.currentTimeMillis() < expiration;
    }

    public Result<Void> login(String username, String password) {
        boolean success = false;
        try {
            ApiResponse response = authAPI.login(username, password);
            if (response.getStatusCode() == 200) {
                JSONObject obj = new JSONObject(response.getbody());
                String token = obj.getString("token");
                String expiration = obj.getString("expiration");
                setToken(token);
                setExpiration(Long.parseLong(expiration) * 1000L); // Convert to milliseconds for comparison
                success = true;
                return new Result<Void>(success, null, "Login successful");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<Void>(false, null, "Login failed");
    }

    public Result<Void> logout() {
        boolean success = false;
        try {
            ApiResponse response = authAPI.logout(getToken());
            if (response.getStatusCode() == 200) {
                setToken(null);
                setExpiration(0);
                success = true;
                return new Result<Void>(success, null, "Logout successful");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<Void>(false, null, "Logout failed");
    }
}
