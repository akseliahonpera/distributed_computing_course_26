package com.group_13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.*;

import java.util.*;
import java.net.URI;
import java.net.URLDecoder;

public class ServerUtility {
    public static Map<String, String> parseQuery(HttpExchange t) {
        URI uri = t.getRequestURI();
        String query = uri.getQuery();

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

    static String GetBodyText(HttpExchange t)  throws IOException
    {
        InputStreamReader stream = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(stream);

        int c;
        StringBuilder buf = new StringBuilder();
        while ((c = reader.read()) != -1) {
            buf.append((char) c);
        }

        String text = buf.toString();

        stream.close();

        return text;
    }
}