package com.group_13;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient
{
    private HttpClient client = null;
    private String authToken = null;
    private String host = null;


    public static String encodeParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        return params.entrySet()
            .stream()
            .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
    }

    private SSLContext getSSLContext(String trustoreFile, char[] password) throws Exception
    {
        KeyStore trustStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream trustStorefile = new FileInputStream(trustoreFile)) {
            trustStore.load(trustStorefile, password);
        } catch (Exception e) {
            System.out.println("Cannot read keystores. Check that cert folder is contains correct keystore files!");
            return null;
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }

    private String getAuthToken(String username, String password) throws Exception
    {
        JSONObject authJson = new JSONObject();

        authJson.put("user", username);
        authJson.put("password", password);

        String fullUrl = host + "/api/auth/token";

        System.out.println(fullUrl);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl))
                                                      .POST(HttpRequest.BodyPublishers.ofString(authJson.toString()))
                                                      .build();

        System.out.println(authJson.toString());

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println(response.body());

        JSONObject result = new JSONObject(response.body());

        return result.getString("token");
    }

    public ApiClient(String host, String username, String password)
    {
        try {
            this.host = "https://" + host;

            System.out.println("Creating SSL context");

            SSLContext sslContext = getSSLContext("ds26-truststore.p12", "Gambiinakiuas522".toCharArray());

            System.out.println("Creating httpclient");

            client = HttpClient.newBuilder()
                                .sslContext(sslContext)
                                .build();
            
            System.out.println("Get auth token from server");

            authToken = getAuthToken(username, password);

        } catch (Exception e) {
            System.out.println("ApiClient creation failed!");
            System.out.println(e.getMessage());
        }
    }

    public JSONObject insert(JSONObject o, String table) throws Exception {
        String fullUrl = host + "/api/" + table;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl))
                                                      .header("Authorization", "Bearer " + authToken)
                                                      .POST(HttpRequest.BodyPublishers.ofString(o.toString()))
                                                      .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        JSONArray result = new JSONArray(response.body());

        return result.getJSONObject(0);
    }

    public JSONArray query(String table, Map<String,String> params) throws Exception
    {
        String fullUrl = host + "/api/" + table;

        if (params != null && !params.keySet().isEmpty()) {
            fullUrl = fullUrl + "?" + encodeParams(params);
        }

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl))
                                                      .header("Authorization", "Bearer " + authToken)
                                                      .GET()
                                                      .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return new JSONArray(response.body());
    }

    public void delete(long id, String table) throws Exception {
        String fullUrl = host + "/api/" + table + "?id=" + Long.toString(id);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl))
                                                      .header("Authorization", "Bearer " + authToken)
                                                      .DELETE()
                                                      .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            System.out.println("Delete failed!");
        }
    }

    public JSONObject update(JSONObject o, long id, String table) throws Exception {
        String fullUrl = host + "/api/" + table + "?id=" + Long.toString(id);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl))
                                                      .header("Authorization", "Bearer " + authToken)
                                                      .PUT(HttpRequest.BodyPublishers.ofString(o.toString()))
                                                      .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        JSONArray result = new JSONArray(response.body());

        return result.getJSONObject(0);
    }
}