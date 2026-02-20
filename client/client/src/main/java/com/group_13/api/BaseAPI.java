package com.group_13.api;

import java.net.http.HttpClient;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_13.service.AuthService;

/**
 * BaseAPI class to be extended by specific API classes.
 * Contains common configurations BASE_URL and HttpClient instance.
 */




public abstract class BaseAPI {
    protected static String BASE_URL = "http://127.0.0.1:8001/api"; // SET URL HERE
    protected static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    
    // Getters and Setters for token management
    protected String getToken() {
        return AuthService.getInstance().getToken();
    }

    protected void setToken(String token) {
        AuthService.getInstance().setToken(token);
    }

    public static void setBaseURL(String url) {
        BaseAPI.BASE_URL = url;
    }

}
