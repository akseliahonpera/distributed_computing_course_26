package com.group_13.api;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.group_13.model.Record;
import com.group_13.model.ApiResponse;
import com.group_13.model.Patient;

/**
 * Handles API requests related to Records.
 * Extends BaseAPI to inherit common configurations.
 * Implements methods to perform CRUD operations on Record resources.
 */

public class RecordAPI extends BaseAPI {
    private static final RecordAPI INSTANCE = new RecordAPI();
    private static final String RECORDS_ENDPOINT = BASE_URL + "/records";

    private RecordAPI() {
        super();
    }

    public static RecordAPI getInstance() {
        return INSTANCE;
    }

    public CompletableFuture<ApiResponse> getAllRecordsAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        return futureResponse.thenApply(
                response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));
    }

    public ApiResponse getAllRecords() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public CompletableFuture<ApiResponse> getRecordAsync(Record record) {
        try {
            String queryString = recordToQueryString(record);
            System.out.println("Query string: " + queryString); // Debugging line
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RECORDS_ENDPOINT + queryString))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString());
            return futureResponse.thenApply(
                    response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public ApiResponse getRecord(Record record) throws Exception {

        String queryString = recordToQueryString(record);
        System.out.println("Query string: " + queryString); // Debugging line
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + queryString))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public CompletableFuture<ApiResponse> getRecordByIdAsync(Record record) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RECORDS_ENDPOINT + "?id="
                            + URLEncoder.encode(record.getId(), "UTF-8")))
                    .header("Authorization", "Bearer " + getToken())
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString());
            return futureResponse.thenApply(
                    response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public ApiResponse getRecordById(Record record) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?id="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public CompletableFuture<ApiResponse> getRecordsByPatientIdAsync(Patient patient) {
        try {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?patientid="
                        + URLEncoder.encode(patient.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        return futureResponse.thenApply(
                response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public ApiResponse getRecordsByPatientId(Patient patient) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?patientid="
                        + URLEncoder.encode(patient.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public CompletableFuture<ApiResponse> createRecordAsync(Record record) {
        try {
        String jsonBody = objectMapper.writeValueAsString(record);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        return futureResponse.thenApply(
                response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public ApiResponse createRecord(Record record) throws Exception {

        String jsonBody = objectMapper.writeValueAsString(record);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public CompletableFuture<ApiResponse> updateRecordAsync(Record record) {
        try {
        String jsonBody = objectMapper.writeValueAsString(record);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?id="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        return futureResponse.thenApply(
                response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public ApiResponse updateRecord(Record record) throws Exception {

        String jsonBody = objectMapper.writeValueAsString(record);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?id="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    public CompletableFuture<ApiResponse> deleteRecordAsync(Record record) {
        try {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?id="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .DELETE()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        return futureResponse.thenApply(
                response -> new ApiResponse(response.statusCode(), response.body(), response.headers().map()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public ApiResponse deleteRecord(Record record) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RECORDS_ENDPOINT + "?id="
                        + URLEncoder.encode(record.getId(), "UTF-8")))
                .header("Authorization", "Bearer " + getToken())
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body(), response.headers().map());
    }

    private String recordToQueryString(Record record) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();

        if (record.getId() != null && !record.getId().isEmpty()) {
            params.put("id", record.getId());
        }
        if (record.getPatientid() != null && !record.getPatientid().isEmpty()) {
            params.put("patientid", record.getPatientid());
        }
        if (record.getOperation() != null && !record.getOperation().isEmpty()) {
            params.put("operation", record.getOperation());
        }
        if (record.getResponsible() != null && !record.getResponsible().isEmpty()) {
            params.put("responsible", record.getResponsible());
        }
        if (record.getFollowUp() != null && !record.getFollowUp().isEmpty()) {
            params.put("followup", record.getFollowUp());
        }

        StringBuilder query = new StringBuilder("?");
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first)
                query.append("&");
            query.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            first = false;
        }

        return query.toString();
    }
}
