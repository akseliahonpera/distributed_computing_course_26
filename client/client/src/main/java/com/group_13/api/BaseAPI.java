package com.group_13.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_13.service.AuthService;

/**
 * BaseAPI class to be extended by specific API classes.
 * Contains common configurations BASE_URL and HttpClient instance.
 */




public abstract class BaseAPI {
    protected static String BASE_URL = ""; // SET URL HERE
    


    public static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10))
            .sslContext(getSslContext())
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


    private static SSLContext getSslContext(){
    KeyStore trustStore = null;
    try {
        trustStore = KeyStore.getInstance("PKCS12");
    } catch (KeyStoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
        try (FileInputStream in = new FileInputStream("certs/ds26-truststore.p12")) {
            trustStore.load(in, "Gambiinakiuas522".toCharArray());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            tmf.init(trustStore);
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            return sslContext;
        }

}
