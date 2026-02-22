package com.group_13;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class AuthService
{
    private static String getRootUsername()
    {
        return "root";
    }
    private static String getRootPassword()
    {
        return "root";
    }

    public static Token getAuthTokenForServer(String serverAddress)
    {
        try {
            String fullUrl = "http://" + serverAddress + "/api/auth/token";
            System.out.println("täysurl: "+fullUrl);
            JSONObject obj = new JSONObject();
            obj.put("user", getRootUsername());
            obj.put("password", getRootPassword());


            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .POST(HttpRequest.BodyPublishers.ofString(obj.toString()))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            JSONObject tokenJson = new JSONObject(response.body());

            if (tokenJson.has("token") && tokenJson.has("expiration")) {
                return new Token(tokenJson);
            }
            return null;
        } catch (IOException | InterruptedException | JSONException e) {
            System.out.println("Failed to get auth token for server " + serverAddress);
            System.out.println(e.getMessage());
        }
        return null;
    }
}