package com.group_13.model;

import java.util.List;
import java.util.Map;

/**
 * API response wrapper. Contains the status code, raw body and headers of the response.
 * Intended to be produced by API classes, and consumed by Service classes.
 */
public class ApiResponse {
    private int statusCode; // HTTP status code
    private String body;  // JSON string, to be parsed by Service classes
    private Map<String, List<String>> headers; // HTTP headers

    public ApiResponse(int statusCode, String body, Map<String, List<String>> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }
}
