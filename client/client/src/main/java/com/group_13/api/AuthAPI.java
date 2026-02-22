package com.group_13.api;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.group_13.model.ApiResponse;

/**
 * Handles API requests related to authentication.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods for user login and logout and token handling.
 */

public class AuthAPI extends BaseAPI {
    private static final AuthAPI INSTANCE = new AuthAPI();
    private static final String AUTH_ENDPOINT = BASE_URL + "auth/token";

    private AuthAPI() {
        super();
    }

    public static AuthAPI getInstance() {
        return INSTANCE;
    }

    public ApiResponse login(String username, String password) throws Exception {
        
        Map<String, String> body = new HashMap<>();
        body.put("user", username);
        body.put("password", password);

        String jsonBody = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(AUTH_ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        System.out.println("Sending login request with body: " + jsonBody);
        System.out.println("Request URI: " + request.uri());
        System.out.println("kilientti: "+ httpClient.toString());
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Received json response: " + response.body());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public ApiResponse logout(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(AUTH_ENDPOINT))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }
}