package com.group_13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;

public class ServerUtility {

    public enum HttpStatus {

        OK(200, "OK"),
        CREATED(201, "Created"),
        NO_CONTENT(204, "No Content"),

        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),

        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private final int code;
        private final String reason;

        HttpStatus(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int code() {
            return code;
        }

        public String reason() {
            return reason;
        }
    }

    public static String encodeParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        return params.entrySet()
            .stream()
            .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
    }

    public static Map<String, String> parseQuery(HttpExchange t) {
        URI uri = t.getRequestURI();
        String query = uri.getRawQuery();

        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;

        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1
                    ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                    : "";

            params.put(key, value);
        }
        return params;
    }

    static void sendResponse(HttpExchange t, String text, int statusCode) throws UnsupportedEncodingException, IOException
    {
        byte [] rawData = text.getBytes("UTF-8");
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //     // Ignore
        // }

        try (OutputStream stream = t.getResponseBody()) {
            t.sendResponseHeaders(statusCode, rawData.length);
            stream.write(rawData);
            stream.flush();
        }
    }

    static void sendResponse(HttpExchange t, String text, HttpStatus status) throws UnsupportedEncodingException, IOException
    {
        // try {
        // Thread.sleep(1000);
        // } catch (InterruptedException e) {
        //     // Ignore
        // }
        sendResponse(t, text, status.code);
    }

    static String GetBodyText(HttpExchange t)  throws IOException
    {
        String text;
        try (InputStreamReader stream = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)) {
            BufferedReader reader = new BufferedReader(stream);
            int c;
            StringBuilder buf = new StringBuilder();
            while ((c = reader.read()) != -1) {
                buf.append((char) c);
            }   text = buf.toString();
        }

        return text;
    }

    public static String extractBearerToken(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7).strip(); // after "Bearer "
    }
}