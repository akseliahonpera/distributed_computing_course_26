package com.group_13.api;

import java.net.http.HttpClient;
import java.time.Duration;

public abstract class BaseAPI {
    protected static final String BASE_URL = "http://localhost:9999/api"; // SET URL HERE
    protected static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
}
