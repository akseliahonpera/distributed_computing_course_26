package com.group_13.model;

import java.util.List;
import java.util.Map;

/**
 * API response wrapper. Contains the status code, raw body and headers of the response.
 * Intended to be produced by API classes, and consumed by Service classes.
 */
public class ApiResponse {
    private int statusCode;
    private String body;
    private Map<String, List<String>> headers;

    public ApiResponse(int statusCode, String body) {
        this(statusCode, body, null);
    }

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

    public String getbody() {
        return body;
    }

    public void setbody(String body) {
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
