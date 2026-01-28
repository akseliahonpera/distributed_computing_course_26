package com.group_13.api;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.group_13.model.ApiResponse;

public class AuthAPI extends BaseAPI {
    private static final String AUTH_ENDPOINT = BASE_URL + "/auth/token";

    public AuthAPI() {
        super();
    }

    public ApiResponse login(String username, String password) throws Exception {
        
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        String jsonBody = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(AUTH_ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());

        // TODO: Set the token:
        // setToken(extractedToken);
    }

    public ApiResponse logout(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(AUTH_ENDPOINT))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        setToken(null);
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }
}